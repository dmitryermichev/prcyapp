package ru.prcy.app.gui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import ru.prcy.app.R;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.db.FavouriteDomainData;
import ru.prcy.app.gui.fragments.AnalizeFragment2;
import ru.prcy.app.gui.fragments.AnalizeInProgressFragment;
import ru.prcy.app.gui.fragments.AnalizeFragment;
import ru.prcy.app.gui.fragments.AnalizeResultsFragment;
import ru.prcy.app.gui.fragments.FavouritesFragment;
import ru.prcy.app.gui.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.OnFragmentInteractionListener,
        AnalizeFragment.OnFragmentInteractionListener,
        AnalizeInProgressFragment.OnFragmentInteractionListener,
        AnalizeResultsFragment.OnFragmentInteractionListener,
        AnalizeFragment2.OnFragmentInteractionListener,
        FavouritesFragment.OnListFragmentInteractionListener
{

    private ViewGroup rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        openAnalizeFragment(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.nav_login) {
            openLoginFragment(false);
        } else if (id == R.id.nav_favourites) {

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_login) {
            openLoginFragment(false);
        } else if(id == R.id.nav_analize) {
            openAnalizeFragment(true);
        } else if (id == R.id.nav_favourites) {
            openFavourites();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLogin() {

    }

    private void openLoginFragment(boolean closeOnSave) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, LoginFragment.newInstance(closeOnSave)).addToBackStack(null).commit();
    }

    private void openAnalizeFragment(boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentContainer, AnalizeFragment2.newInstance());

        if(addToBackStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }

    private void openAnalizeInProgress(boolean offline, boolean forceUpdate, String domain) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, AnalizeInProgressFragment.newInstance(domain, offline, forceUpdate)).
                addToBackStack(null).
                commit();
    }

    private void openFavourites() {
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, FavouritesFragment.newInstance(1, true)).
                addToBackStack(null).
                commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        openAnalizeFragment(true);
    }

    private void openAnalizeResults(long id) {
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, AnalizeResultsFragment.newInstance(id))
                //.addToBackStack(null)
                .commit();
    }

    @Override
    public void onAnalizeRequested(boolean offline, String site) {
        openAnalizeInProgress(offline, false, site);
    }

    @Override
    public void onApiKeyIsEmpty() {
        openLoginFragment(true);
    }

    @Override
    public void onAnalizeFinished(long domainId) {
        openAnalizeResults(domainId);
    }

    @Override
    public void onAnalizeUpdateRequested(String site) {
        openAnalizeInProgress(false, true, site);
    }

    @Override
    public void onDomainSelected(FavouriteDomainData data) {
        openAnalizeInProgress(false, false, data.getDomain());
    }

    @Override
    public void onDomainDeleted(String domain) {

    }
}
