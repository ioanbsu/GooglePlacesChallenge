package com.artigile.android.placesapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.artigile.android.placesapi.api.model.Place;
import com.artigile.android.placesapi.api.model.PlacesApiResponseEntity;
import com.artigile.android.placesapi.api.service.GooglePlacesApiImpl;
import com.artigile.android.placesapi.api.service.RankByType;
import com.artigile.android.placesapi.app.PlacesArrayAdapter;
import com.artigile.android.placesapi.app.SearchType;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.io.IOException;

@Singleton
public class GooglePlaces extends RoboActivity implements LocationListener {

    @Inject
    private SharedPreferences sharedPrefs;

    @Inject
    private GooglePlacesApiImpl googlePlacesApi;

    @Inject
    private AppState appState;

    @InjectView(R.id.listView)
    private ListView listView;


    @InjectView(R.id.searchText)
    private EditText searchText;

    private PlacesArrayAdapter placesAdapter;
    private double longitude;
    private double latitude;

    private LocationManager mlocManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        placesAdapter = new PlacesArrayAdapter(getBaseContext());
        listView.setAdapter(placesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new PlacesDetailsDownloader().execute((Place) listView.getItemAtPosition(position));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        restoreAppProperties();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapSearchResultsToUi();
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mlocManager.removeUpdates(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public void searchByCriteria(View view) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new PlacesDownloader().execute(SearchType.SEARCH_BY_CRITERIA);
        }
    }

    public void searchNearMe(View view) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new PlacesDownloader().execute(SearchType.SEARCH_NEAR_ME);
        }
    }

    private void restoreAppProperties() {

    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(getApplicationContext(), "Status changed" + status, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
    }


    private class PlacesDownloader extends AsyncTask<SearchType, Void, String> {

        @Override
        protected String doInBackground(SearchType... params) {
            try {
                PlacesApiResponseEntity places = null;

                if (params == null || params[0] == SearchType.SEARCH_NEAR_ME) {
                    places = googlePlacesApi.searchNearBy("AIzaSyAiM8su2DOeNr3Ii2sNW6sdm2ZUDIugHak",
                            longitude, latitude, 100, RankByType.PROMINENCE, true, null, null, null, null, null);
                } else if (params[0] == SearchType.SEARCH_BY_CRITERIA) {
                    places = googlePlacesApi.textSearch("AIzaSyAiM8su2DOeNr3Ii2sNW6sdm2ZUDIugHak", searchText.getText().toString(), false, null, null, null, null);
                }
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
        }
    }

    private void mapSearchResultsToUi() {
        placesAdapter.clear();
        if (appState.getLastSearchResult() != null) {
            placesAdapter.addAll(appState.getLastSearchResult().getPlaceList());
        }
    }

    private class PlacesDetailsDownloader extends AsyncTask<Place, Void, String> {

        @Override
        protected String doInBackground(Place... params) {
            try {
                PlacesApiResponseEntity placesApiResponseEntity = googlePlacesApi.getPlaceDetails("AIzaSyAiM8su2DOeNr3Ii2sNW6sdm2ZUDIugHak", params[0].getReference(), true, null);
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
                Intent intent = new Intent(getBaseContext(), PlaceInfo.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.fade, R.anim.hold);
            }

        }
    }


    //================== menu ==========================

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.adjustSensor:
                Intent intent = new Intent(this, ApplicationConfiguration.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade, R.anim.hold);
                return true;
            /*     case R.id.about:
           System.out.println("About!!!");
           return true;*/
            default:
                /*  // Don't toast text when a submenu is clicked
                if (!item.hasSubMenu()) {
                    Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }*/
                break;
        }
        return false;
    }

}

