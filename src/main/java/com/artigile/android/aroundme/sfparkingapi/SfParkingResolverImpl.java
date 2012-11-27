package com.artigile.android.aroundme.sfparkingapi;

import android.os.AsyncTask;
import android.util.Log;
import com.artigile.android.aroundme.app.event.ParkingInfoReadyEvent;
import com.artigile.android.aroundme.placesapi.model.Place;
import com.artigile.android.aroundme.sfparkingapi.model.ParkingPlacesResult;
import com.google.common.eventbus.EventBus;
import org.apache.http.client.ClientProtocolException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author IoaN, 11/26/12 8:47 PM
 */
@Singleton
public class SfParkingResolverImpl implements SfParkingResolver {

    public static final String testUrl = "http://api.sfpark.org/sfpark/rest/availabilityservice?lat=37.792275&long=-122.397089&radius=0.25&uom=mile&response=json";
    @Inject
    private SfParkingResponseParser sfParkingResponseParser;
    @Inject
    private EventBus eventBus;

    @Override
    public void getParkingSpacesList(Place place) {
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                try {
                    URLConnection conn = new URL(testUrl).openConnection();
                    is = conn.getInputStream();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is), 1000);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    if (is != null) {
                        is.close();
                    }
                    return sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String response) {
                ParkingPlacesResult parkingPlacesResult = null;
                try {
                    parkingPlacesResult = sfParkingResponseParser.parse(response);
                    if (parkingPlacesResult != null) {
                        Log.i("parking place result:", parkingPlacesResult.toString());
                        eventBus.post(new ParkingInfoReadyEvent(parkingPlacesResult));
                    }
                } catch (ParkingResultNotSuccessException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
