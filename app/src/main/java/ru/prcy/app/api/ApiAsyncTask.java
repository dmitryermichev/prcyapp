package ru.prcy.app.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.prcy.app.data.ApiKey;

/**
 * Created by dmitry on 26.10.17.
 */

public abstract class ApiAsyncTask  extends AsyncTask<String, Void, AnalizeResult> {

    private static final String API_BASE_PATH = "https://a.pr-cy.ru/api/v1.1.0/analysis/";
    protected static final int CHECK_STATUS_INTERVAL = 3000;
    protected static final int CHECK_STATUS_MAX_TIMES = 20;

    Retrofit retrofit;
    ApiService service;
    ApiKey key;
    Gson gson;
    Context context;

//    String currentDomain;
    ResultListener listener;

    public interface ResultListener {
        public void onResult(AnalizeResult result);
    }

    public ApiAsyncTask(Context context) {
        this.context = context;
        this.key = ApiKey.load(context);

        this.gson = new Gson();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_PATH)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.service = retrofit.create(ApiService.class);
    }

    public ApiAsyncTask(Context context, ResultListener listener) {
        this(context);
        this.listener = listener;

    }

    public void setResultListener(ResultListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onPostExecute(AnalizeResult analizeResult) {
        if(this.listener != null)
            listener.onResult(analizeResult);
        else
            Log.d(this.getClass().getName(), "result listener is null");
    }


    protected Context getContext() {
        return this.context;
    }

    protected Response<JsonObject> advancedStatus(String domain) throws IOException {
        Call<JsonObject> call = service.advancedAnalizeStatus(domain, this.key.getKey());
        return call.execute();
    }

    protected Response<JsonObject> baseStatus(String domain) throws IOException {
        Call<JsonObject> call = service.baseAnalizeStatus(domain, this.key.getKey());
        return call.execute();
    }

    protected Response<JsonObject> baseAnalize(String domain) throws IOException {
        Call<JsonObject> call = service.baseAnalize(domain, this.key.getKey());
        return call.execute();
    }

    protected Response<JsonObject> advancedAnalize(String domain) throws IOException {
        Call<JsonObject> call = service.advancedAnalize(domain, this.key.getKey());
        return call.execute();
    }

    protected Response<JsonObject> updateBase(String domain) throws IOException {
        Call<JsonObject> call = service.updateBaseAnalize(domain, this.key.getKey());
        return call.execute();
    }

    protected Response<JsonObject> updateAdvanced(String domain) throws IOException {
        Call<JsonObject> call = service.updateAdvancedAnalize(domain, this.key.getKey());
        return call.execute();
    }
}
