package com.artigile.android.placesapi;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.*;
import android.widget.*;
import com.artigile.android.placesapi.api.model.Place;
import com.artigile.android.placesapi.api.model.PlacesApiResponseEntity;
import com.artigile.android.placesapi.api.service.GooglePlacesApiImpl;
import com.artigile.android.placesapi.api.service.RankByType;
import com.artigile.android.placesapi.app.LocationProvider;
import com.artigile.android.placesapi.app.PlaceEfficientAdapter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;

import java.io.IOException;

@Singleton
public class GooglePlaces extends RoboListActivity implements SearchView.OnQueryTextListener {

    @Inject
    private GooglePlacesApiImpl googlePlacesApi;

    @Inject
    private AppState appState;

    @Inject
    private LocationProvider locationProvider;

    @InjectView(android.R.id.list)
    private ListView listView;

    @InjectView(R.id.placesSearchLoadingBar)
    private ProgressBar progressBar;

    @InjectView(R.id.searchResultsView)
    private LinearLayout searchResultsView;

    private LocationManager mlocManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new PlacesDetailsDownloader().execute((Place) listView.getItemAtPosition(position));
            }
        });
        listView.setLayoutAnimation(getListAnimation());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapSearchResultsToUi();
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationProvider);
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
        mlocManager.removeUpdates(locationProvider);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void showAllOnMap(View view) {
        appState.setSelectedPlaceForViewDetails(appState.getLastSearchResult());
        showMap();
    }


    private void doSearch(String searchQuery) {
    /*    if (locationProvider.getLatitude() == 0 && locationProvider.getLongitude() == 0) {
            Toast.makeText(getBaseContext(),"Please wait for the better GPS signal",10).show();
        } else {*/
        progressBar.setVisibility(View.VISIBLE);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new PlacesDownloader().execute(searchQuery);
//            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void doSearchAll(MenuItem v) {
        doSearch("");
    }


    private void mapSearchResultsToUi() {
        if (appState.getLastSearchResult() != null && appState.getLastSearchResult().getPlaceList() != null) {
            listView.setAdapter(new PlaceEfficientAdapter(getBaseContext(), appState.getLastSearchResult().getPlaceList()));
        }
    }


    private void showMap() {
        Intent intent = new Intent(getBaseContext(), ShowAllPlacesOnMapActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    private class PlacesDetailsDownloader extends AsyncTask<Place, Void, String> {

        @Override
        protected String doInBackground(Place... params) {
            try {
                PlacesApiResponseEntity placesApiResponseEntity = googlePlacesApi.getPlaceDetails(getBaseContext().getString(R.string.api_key), params[0].getReference(), true, null);
                appState.setSelectedPlaceForViewDetails(placesApiResponseEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (appState.getLastSearchResult() != null & appState.getLastSearchResult().getPlaceList() != null
                    && !appState.getLastSearchResult().getPlaceList().isEmpty()) {
                showMap();
            }
        }
    }


    private class PlacesDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String searchQuery = params == null ? null : params[0];
                PlacesApiResponseEntity places = googlePlacesApi.searchNearBy(getBaseContext().getString(R.string.api_key),
                        locationProvider.getLongitude(), locationProvider.getLatitude(), 1000, RankByType.PROMINENCE, true, searchQuery, null, null, null, null);
                appState.setLastSearchResult(places);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            mapSearchResultsToUi();
            progressBar.setVisibility(View.GONE);
        }
    }


    private LayoutAnimationController getListAnimation(){
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        return controller;
    }

}

