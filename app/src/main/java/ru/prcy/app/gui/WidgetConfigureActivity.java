package ru.prcy.app.gui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import ru.prcy.app.R;
import ru.prcy.app.db.FavouriteDomainData;
import ru.prcy.app.gui.fragments.FavouritesFragment;
import ru.prcy.app.gui.widget.DomainWidgetProvider;

public class WidgetConfigureActivity extends AppCompatActivity implements FavouritesFragment.OnListFragmentInteractionListener {

    int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.widgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        openFavourites();

    }

    private void openFavourites() {
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, FavouritesFragment.newInstance(1, false)).
                addToBackStack(null).
                commit();
    }

    @Override
    public void onDomainSelected(FavouriteDomainData data) {
        Log.d(this.getClass().getName(), "domain selected " + data.getDomain() + " updating widget " + widgetId);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("widget" + widgetId + "Prefs", MODE_PRIVATE);
        prefs.edit().putString("domain", data.getDomain()).commit();

        DomainWidgetProvider.updateAppWidget(getApplicationContext(), appWidgetManager, widgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

    @Override
    public void onDomainDeleted(String domain) {

    }
}
