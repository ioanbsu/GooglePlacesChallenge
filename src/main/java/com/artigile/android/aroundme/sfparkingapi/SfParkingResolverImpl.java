package com.artigile.android.aroundme.sfparkingapi;

import android.os.AsyncTask;
import android.util.Log;
import com.artigile.android.aroundme.placesapi.model.Place;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Singleton;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author IoaN, 11/26/12 8:47 PM
 */
@Singleton
public class SfParkingResolverImpl implements SfParkingResolver {

    public static final String testUrl = "http://api.sfpark.org/sfpark/rest/availabilityservice?lat=37.792275&long=-122.397089&radius=0.25&uom=mile&response=json";
    static JSONObject jObj = null;

    @Override
    public List<ParkingPlace> getParkingSpacesList(Place place) {
        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;

                // Making HTTP request
                try {
                    // defaultHttpClient
                    URLConnection conn=new URL(testUrl).openConnection();
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
                        sb.append(line + "\n");
                    }
                    is.close();
                    return sb.toString();
                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(String response) {
                parseResponse(response);
            }
        }.execute();


        return new ArrayList<ParkingPlace>();
    }

    private  void parseResponse(String response) {

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(response);
            String a=jObj.getString("NUM_RECORDS");
            String b=jObj.getString("STATUS");
            System.out.println(jObj.getString("STATUS") +" "+jObj.getString("NUM_RECORDS") );
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
    }
}
