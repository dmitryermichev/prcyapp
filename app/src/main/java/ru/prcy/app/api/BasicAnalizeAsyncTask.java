package ru.prcy.app.api;

import android.app.PendingIntent;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.prcy.app.R;
import ru.prcy.app.api.exceptions.AnalizeCommonException;
import ru.prcy.app.data.AnalizeField;
import ru.prcy.app.data.ApiKey;
import ru.prcy.app.data.Common;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.db.DomainDataHelper;

/**
 * Created by dmitry on 26.10.17.
 */

public class BasicAnalizeAsyncTask extends ApiAsyncTask{

    private boolean forceUpdate;

    public BasicAnalizeAsyncTask(Context context, boolean forceUpdate, ResultListener listener) {
        super(context, listener);
        this.forceUpdate = forceUpdate;
    }

    public BasicAnalizeAsyncTask(Context context, boolean forceUpdate) {
        super(context);
        this.forceUpdate = forceUpdate;
    }

    @Override
    protected AnalizeResult doInBackground(String... params) {

        String domain = params[0];

        Date updatedAt = new Date();

        try {

            if(forceUpdate) {
                return forceUpdate(domain);
            } else {

                Response<JsonObject> status =  this.baseStatus(domain);

                //Если уже есть сформированный анализ
                if(status.isSuccessful()) {

                    if(status.body().has("updated") && !status.body().get("updated").isJsonNull()) {
                        try {
                            updatedAt = Common.parseJSONDate(status.body().get("updated").getAsString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    //Если существующий анализ уже просрочен
                    if(status.body().get("isExpired") != null && status.body().get("isExpired").getAsBoolean() == true) {
                        return forceUpdate(domain);

                    } else {
                        Response<JsonObject> analize = baseAnalize(domain);

                        //Если запрос выполнился успешно
                        if(analize.isSuccessful()) {
                            return AnalizeResult.success(updatedAt, analize.body());
                        } else {
                            return AnalizeResult.error(new AnalizeCommonException(analize.errorBody().string()));
                        }
                    }

                } else {
                    return forceUpdate(domain);
                }

            }
        } catch (IOException e) {
            return AnalizeResult.error(e);
        } catch (InterruptedException e) {
            return AnalizeResult.error(e);
        } catch (AnalizeCommonException e) {
            return AnalizeResult.error(e);
        }

    }

    public AnalizeResult forceUpdate(String domain) throws IOException, AnalizeCommonException, InterruptedException {
        Date updatedAt = new Date();
        Response<JsonObject> status = updateAnalize(domain);

        if(status.isSuccessful() && status.body().has("updated")) {
            try {
                updatedAt = Common.parseJSONDate(status.body().get("updated").getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        Response<JsonObject> analize = baseAnalize(domain);

        //Если запрос выполнился успешно
        if(analize.isSuccessful()) {
            return AnalizeResult.success(updatedAt, analize.body());
        } else {
            return AnalizeResult.error(new AnalizeCommonException(analize.errorBody().string()));
        }
    }



    private Response<JsonObject> updateAnalize(String domain) throws IOException, InterruptedException, AnalizeCommonException {

        Response<JsonObject> update = updateBase(domain);
        if(update.isSuccessful() && !update.body().has("error")) {
            for(int i = 0; i < CHECK_STATUS_MAX_TIMES; i++) {
                if(isCancelled())
                    return null;
                Response<JsonObject> status = baseStatus(domain);

                if(status.isSuccessful()) {
                    JsonObject result = status.body();
                    if(!result.get("isUpdating").getAsBoolean()) {
                        return status;
                    } else {
                        Thread.sleep(CHECK_STATUS_INTERVAL);
                    }
                } else {
                    throw new AnalizeCommonException(status.errorBody().string());
                }
            }
            throw new AnalizeCommonException(getContext().getString(R.string.analize_in_progress_error_timeout));
        } else {
            throw new AnalizeCommonException(update.errorBody().string());
        }
    }

    public long saveDomainData(String domain, AnalizeResult result, DomainDataHelper helper) {
        if(result.isSucceed()) {
            String favicon = null;
            if (result.getBody().has("favicon")) {
                favicon = result.getBody().getAsJsonObject("favicon").get("faviconSrc").getAsString();
            }
            DomainData data = new DomainData(domain, new Date(), result.getUpdatedAt(), favicon, result.getBody());
            long domainId = helper.addDomainToHistory(data);
            return domainId;
        } else
            return 0;
    }


}
