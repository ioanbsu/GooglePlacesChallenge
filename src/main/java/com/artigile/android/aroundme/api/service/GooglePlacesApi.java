package com.artigile.android.aroundme.api.service;

import com.artigile.android.aroundme.api.model.PlacesApiResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 1:38 PM
 */
public interface GooglePlacesApi {

    PlacesApiResponseEntity searchNearBy(String key, double longitude, double latitude, Integer radius, RankByType rankBy, boolean sensor,
                                         String keyword, String language, String name, Set<String> types, String pageToken) throws IOException;

    PlacesApiResponseEntity textSearch(String key, String query, boolean sensor, String location, String radius,
                                  String language, List<String> types) throws IOException;

    PlacesApiResponseEntity getPlaceDetails(String key,String reference, boolean sensor, String language) throws IOException;
}

