package com.artigile.android.aroundme;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import com.artigile.android.aroundme.app.LocationProvider;
import com.artigile.android.aroundme.app.fragment.PlaceDetailsFragment;
import com.artigile.android.aroundme.app.fragment.SearchResultFragment;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

@Singleton
public class GooglePlaces extends RoboFragmentActivity implements SearchView.OnQueryTextListener {

    @Inject
    private LocationProvider locationProvider;
    @Inject
    private AppState appState;
    @InjectFragment(R.id.searchResultsFragment)
    private SearchResultFragment searchResultFragment;
    @InjectView(R.id.doSearchMainButton)
    private ImageView doSearchMainButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_page);
        if (locationProvider.getLocation().getLatitude() == 0 && locationProvider.getLocation().getLongitude() == 0) {
            Toast.makeText(getBaseContext(), R.string.searching_gps_satellites_toast, 2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doSearchMainButton.setVisibility(appState.isStartSearchButtonShow() ? VISIBLE : INVISIBLE);
        ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationProvider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).removeUpdates(locationProvider);
    }

    public void showAllOnMap(MenuItem v) {
        appState.setSinglePlaceToDisplayOnMap(appState.getFoundPlacesList());
        showMap();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return false;
    }

    public void doFirstSearch(View v) {
        doSearch("");
    }

    public void doSearchAll(MenuItem v) {
        doSearch("");
    }

    private void doSearch(String query) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        if (!searchResultFragment.doSearch(query)) {
            Toast.makeText(getBaseContext(), R.string.search_please_wait_for_better_gps_signal, 10).show();
        } else {
            doSearchMainButton.setVisibility(INVISIBLE);
        }
    }

    private void showMap() {
        Intent intent = new Intent(getBaseContext(), MapResultsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }
}

