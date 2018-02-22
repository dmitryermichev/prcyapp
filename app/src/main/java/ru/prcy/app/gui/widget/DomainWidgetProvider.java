package ru.prcy.app.gui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.Date;
import java.util.List;

import ru.prcy.app.R;
import ru.prcy.app.api.AnalizeResult;
import ru.prcy.app.api.ApiAsyncTask;
import ru.prcy.app.api.BasicAnalizeAsyncTask;
import ru.prcy.app.data.AnalizeFieldBoolean;
import ru.prcy.app.data.AnalizeFieldInt;
import ru.prcy.app.data.AnalizeFieldString;
import ru.prcy.app.data.Common;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.db.DomainDataHelper;

/**
 * Created by dmitry on 06.11.17.
 */

public class DomainWidgetProvider extends AppWidgetProvider {

    public DomainWidgetProvider() {

    }

    public static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager, final int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences("widget" + widgetId + "Prefs", Context.MODE_PRIVATE);
        final String domain = prefs.getString("domain", null);
        if(domain == null)
            return;

        Log.d(DomainWidgetProvider.class.getName(), "loading data for domain " + domain);
//        DomainDataHelper helper = new DomainDataHelper(context);
//        List<DomainData> searchResults= helper.searchDomainsFromHistory(domain, 1);
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        remoteViews.setViewVisibility(R.id.allInfo, View.GONE);
        remoteViews.setViewVisibility(R.id.errorLayout, View.GONE);
        remoteViews.setViewVisibility(R.id.loading, View.VISIBLE);
        appWidgetManager.updateAppWidget(widgetId, remoteViews);

        final Context finalContext = context;
        final BasicAnalizeAsyncTask task = new BasicAnalizeAsyncTask(context, true);

        task.setResultListener(new ApiAsyncTask.ResultListener() {
            @Override
            public void onResult(AnalizeResult result) {
                if(result.isSucceed()) {

                    if(task != null)
                        task.saveDomainData(domain, result, new DomainDataHelper(finalContext));
                    Date updateTime = new Date();

                    remoteViews.setTextViewText(R.id.name, domain);

                    if(result.getBody().has("favicon")) {
                        String favicon = AnalizeFieldString.getValueFromAnalize(result.getBody(), "favicon", "faviconSrc").value;

                        Log.d(DomainWidgetProvider.class.getName(), "loading image " + favicon);
                        Glide.with(finalContext)
                                .asBitmap()
                                .load(favicon)
                                .into(new SimpleTarget<Bitmap>(100, 100) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        Log.d(this.getClass().getName(), "widget favicon loaded");
                                        remoteViews.setImageViewBitmap(R.id.icon, resource);
//                            remoteViews.setImageViewResource(R.id.icon, R.drawable.fb);
                                        appWidgetManager.updateAppWidget(widgetId, remoteViews);

                                    }
                                });

                    }

                    AnalizeFieldInt tic = AnalizeFieldInt.getValueFromAnalize(result.getBody(), "yandexCitation", "yandexCitation");
                    remoteViews.setTextViewText(R.id.tic_value, "" + tic.getValueString());
                    if(tic.getDelta() > 0) {
                        remoteViews.setTextColor(R.id.tic_delta, ContextCompat.getColor(finalContext, R.color.colorGreen));
                    } else if(tic.getDelta() < 0) {
                        remoteViews.setTextColor(R.id.tic_delta, ContextCompat.getColor(finalContext, R.color.colorRed));
                    } else {
                        remoteViews.setViewVisibility(R.id.tic_delta, View.GONE);
                    }
                    remoteViews.setTextViewText(R.id.tic_delta, "+" + tic.getDeltaString());


                    AnalizeFieldInt yaRank = AnalizeFieldInt.getValueFromAnalize(result.getBody(), "yandexRank", "yandexRank");
                    remoteViews.setTextViewText(R.id.yarank_value, yaRank.getValueString());

                    if(yaRank.getDelta()> 0) {
                        remoteViews.setTextColor(R.id.yarank_delta, ContextCompat.getColor(finalContext, R.color.colorGreen));
                    } else if(yaRank.getDelta() < 0) {
                        remoteViews.setTextColor(R.id.yarank_delta, ContextCompat.getColor(finalContext, R.color.colorRed));
                    } else {
                        remoteViews.setViewVisibility(R.id.yarank_delta, View.GONE);
                    }
                    remoteViews.setTextViewText(R.id.yarank_delta, yaRank.getDeltaString());

                    AnalizeFieldBoolean yac = AnalizeFieldBoolean.getValueFromAnalize(result.getBody(), "yandexCatalog", "yandexCatalog");
                    if(yac.value) {
                        remoteViews.setTextViewText(R.id.yac_value, finalContext.getString(R.string.yes));
                    } else {
                        remoteViews.setTextViewText(R.id.yac_value, finalContext.getString(R.string.no));
                    }

                    AnalizeFieldInt yaIndex = AnalizeFieldInt.getValueFromAnalize(result.getBody(), "yandexIndex", "yandexIndex");
                    remoteViews.setTextViewText(R.id.yaindex_value, yaIndex.getValueString());

                    if(yaIndex.getDelta() > 0) {
                        remoteViews.setTextColor(R.id.yaindex_delta, ContextCompat.getColor(finalContext, R.color.colorGreen));
                    } else if(yaIndex.getDelta() < 0) {
                        remoteViews.setTextColor(R.id.yaindex_delta, ContextCompat.getColor(finalContext, R.color.colorRed));
                    } else {
                        remoteViews.setViewVisibility(R.id.yaindex_delta, View.GONE);
                    }
                    remoteViews.setTextViewText(R.id.yaindex_delta, yaIndex.getDeltaString());

                    AnalizeFieldInt goIndex = AnalizeFieldInt.getValueFromAnalize(result.getBody(), "googleIndex", "googleIndex");
                    remoteViews.setTextViewText(R.id.goindex_value, goIndex.getValueString());

                    if(goIndex.getDelta() > 0) {
                        remoteViews.setTextColor(R.id.goindex_delta, ContextCompat.getColor(finalContext, R.color.colorGreen));
                    } else if(goIndex.getDelta() < 0) {
                        remoteViews.setTextColor(R.id.goindex_delta, ContextCompat.getColor(finalContext, R.color.colorRed));
                    } else {
                        remoteViews.setViewVisibility(R.id.goindex_delta, View.GONE);
                    }
                    remoteViews.setTextViewText(R.id.goindex_delta, goIndex.getDeltaString());

                    remoteViews.setTextViewText(R.id.updateTime, finalContext.getString(R.string.analize_results_update_time, Common.formatDate(updateTime)));
                    remoteViews.setViewVisibility(R.id.allInfo, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.loading, View.GONE);
                    remoteViews.setViewVisibility(R.id.errorLayout, View.GONE);

                } else {
                    remoteViews.setTextViewText(R.id.errorInfo, finalContext.getString(R.string.loading_error, result.getException().getLocalizedMessage()));
                    remoteViews.setViewVisibility(R.id.allInfo, View.GONE);
                    remoteViews.setViewVisibility(R.id.loading, View.GONE);
                    remoteViews.setViewVisibility(R.id.errorInfo, View.VISIBLE);
                }

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        });

        task.execute(domain);

        Intent intent = new Intent(context, DomainWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.updateImage, pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.errorUpdateButton, pendingIntent);

        appWidgetManager.updateAppWidget(widgetId, remoteViews);

    }

    @Override
    public void onUpdate(Context context, final AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(this.getClass().getName(), "adding widget");
        final int count = appWidgetIds.length;

        for(int i = 0; i < count; i++) {
            final int widgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, widgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] allIds = manager.getAppWidgetIds(new ComponentName(context, DomainWidgetProvider.class));
        for(int widgetId: allIds) {
            updateAppWidget(context, manager, widgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent != null &&
                (
                        intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE) ||
                        intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_ENABLED)
                ) &&
                intent.getExtras() != null &&
                intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID) != 0) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int appWidgetId = intent.getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
            int[] appWidgetIds = new int[] {appWidgetId};
            onUpdate(context, appWidgetManager, appWidgetIds);
        }

    }
}
