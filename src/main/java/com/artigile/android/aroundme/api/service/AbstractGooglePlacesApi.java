package com.artigile.android.aroundme.api.service;

import android.util.Log;
import com.artigile.android.aroundme.api.model.PlacesApiResponseEntity;

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

    protected abstract PlacesUrlsBuilder getUrlBuilder();

    protected abstract PlacesApiResponseEntity parseStreamResponse(InputStream is,String mainTag) throws IOException;


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
                                                RankByType rankBy, boolean sensor, String keyword, String language,
                                                String name, Set<String> types, String pageToken) throws IOException {
        String requestUrl = getUrlBuilder().buildSearchNearBy(key, longitude, latitude, radius, rankBy, sensor, keyword, language, name,
                types, pageToken);
        return doRequestPlaceApi(requestUrl,"PlaceSearchResponse");
    }

    @Override
    public PlacesApiResponseEntity textSearch(String key, String query, boolean sensor, String location, String radius, String language, List<String> types) throws IOException {
        String requestUrl = getUrlBuilder().buildTextSearch(key, query, sensor, location, radius, language, types);
        return doRequestPlaceApi(requestUrl,"PlaceSearchResponse");
    }

    @Override
    public PlacesApiResponseEntity getPlaceDetails(String key, String reference, boolean sensor, String language) throws IOException {
        String requestUrl = getUrlBuilder().buildPlaceDetails(key, reference, sensor, language);
        return doRequestPlaceApi(requestUrl,"PlaceDetailsResponse");
    }


    private PlacesApiResponseEntity doRequestPlaceApi(String newUrl,String mainTag) throws IOException {
        InputStream is = null;
        try {
            HttpURLConnection conn = buildConnection(newUrl);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG_TAG", "The response is: " + response);
            is = conn.getInputStream();
            return parseStreamResponse(is,mainTag);
        } catch (Exception e) {
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


}
