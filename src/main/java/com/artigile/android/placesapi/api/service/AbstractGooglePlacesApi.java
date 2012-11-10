package com.artigile.android.placesapi.api.service;

import android.util.Log;
import com.artigile.android.placesapi.api.exception.InvalidParametersException;
import com.artigile.android.placesapi.api.model.PlacesApiResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 3:23 PM
 */
public abstract class AbstractGooglePlacesApi implements GooglePlacesApi {

    public static final String KEY = "AIzaSyAiM8su2DOeNr3Ii2sNW6sdm2ZUDIugHak";

    protected abstract PlacesUrlsBuilder getUrlBuilder();

    public abstract PlacesApiResponseEntity parseStreamResponse(InputStream is) throws IOException;


    protected HttpURLConnection buildConnection(String newUrl) throws IOException {
        URL url = new URL(newUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        return conn;
    }


    @Override
    public PlacesApiResponseEntity searchNearBy(String key, double longitude, double latitude, Integer radius,
                                                RankByType rankBy, Boolean sensor, String keyword, String language,
                                                String name, Set<String> types, String pageToken) throws IOException, InvalidParametersException {
        String newUrl = getUrlBuilder().buildSearchNearBy(KEY, longitude, latitude, radius, rankBy, sensor, keyword, language, name,
                types, pageToken);
        return doRequestPlaceApi(newUrl);
    }

    @Override
    public PlacesApiResponseEntity textSearch(String key, String query, Boolean sensor, String location, String radius, String language, List<String> types) throws IOException, InvalidParametersException {
        String newUrl = getUrlBuilder().buildTextSearch(KEY, query, sensor, location, radius, language, types);
        return doRequestPlaceApi(newUrl);
    }


    private PlacesApiResponseEntity doRequestPlaceApi(String newUrl) throws IOException {
        InputStream is = null;
        try {
            HttpURLConnection conn = buildConnection(newUrl);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG_TAG", "The response is: " + response);
            is = conn.getInputStream();
            return parseStreamResponse(is);
        } catch (Exception e) {
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


}
