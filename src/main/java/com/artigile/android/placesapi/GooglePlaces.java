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

import java.io.*;
import java.util.List;

@Singleton
public class GooglePlaces extends RoboActivity implements LocationListener {

    @Inject
    private SharedPreferences sharedPrefs;

    @Inject
    private GooglePlacesApiImpl googlePlacesApi;

    @InjectView(R.id.listView)
    private ListView listView;


    @InjectView(R.id.searchText)
    private EditText searchText;

    private PlacesArrayAdapter placesAdapter;
    private double longitude;
    private double latitude;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        restoreAppProperties();
        placesAdapter = new PlacesArrayAdapter(getBaseContext());
        listView.setAdapter(placesAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
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

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
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


    private class PlacesDownloader extends AsyncTask<SearchType, Void, List<Place>> {

        @Override
        protected List<Place> doInBackground(SearchType... params) {
            try {
                PlacesApiResponseEntity places = null;
                if (params == null || params[0] == SearchType.SEARCH_NEAR_ME) {
                    places = googlePlacesApi.searchNearBy("AIzaSyAiM8su2DOeNr3Ii2sNW6sdm2ZUDIugHak",
                            longitude, latitude, 100, RankByType.PROMINENCE, true, null, null, null, null, null);
                } else if (params[0] == SearchType.SEARCH_BY_CRITERIA) {
                    places = googlePlacesApi.textSearch("AIzaSyAiM8su2DOeNr3Ii2sNW6sdm2ZUDIugHak", searchText.getText().toString(), false, null, null, null, null);
                }
                if (places != null) {
                    return places.getPlaceList();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            placesAdapter.clear();
            placesAdapter.addAll(places);
        }
    }

}

