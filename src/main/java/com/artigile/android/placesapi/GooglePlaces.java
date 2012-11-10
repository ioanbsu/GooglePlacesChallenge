package com.artigile.android.placesapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import com.artigile.android.placesapi.model.Place;
import com.artigile.android.placesapi.model.PlacesApiResponseEntity;
import com.artigile.android.placesapi.service.GooglePlacesApiImpl;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import roboguice.activity.RoboActivity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Singleton
public class GooglePlaces extends RoboActivity {

    @Inject
    private SharedPreferences sharedPrefs;

    @Inject
    private IoaNJsonReader ioaNJsonReader;

    @Inject
    private GooglePlacesApiImpl googlePlacesApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        restoreAppProperties();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

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

    public void callApi(View view) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new AsyncTask() {

                @Override
                protected Object doInBackground(Object... params) {
                    String downloadedString = null;
                    try {
                        PlacesApiResponseEntity places = googlePlacesApi.getPlaces("https://maps.googleapis.com/maps/api/place/search/xml?location=-33.8670522,151.1957362&rankby=distance&types=food&name=harbour&sensor=false&key=AIzaSyAiM8su2DOeNr3Ii2sNW6sdm2ZUDIugHak");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return downloadedString;
                }
            }.execute("some url here");
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


    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG_TAG", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string    \
            ioaNJsonReader.readJsonStream(is);
            String contentAsString = "asdfasdf";
            readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            System.out.println("sdfsdf");
            return "shit";
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private void restoreAppProperties() {

    }

}

