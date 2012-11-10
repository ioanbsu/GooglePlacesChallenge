package com.artigile.android.placesapi.api.service;

import com.artigile.android.placesapi.api.exception.InvalidParametersException;
import com.artigile.android.placesapi.api.model.PlacesApiResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 1:38 PM
 */
public interface GooglePlacesApi {

    PlacesApiResponseEntity searchNearBy(String key, double longitude, double latitude, Integer radius, RankByType rankBy, Boolean sensor,
                                         String keyword, String language, String name, Set<String> types, String pageToken) throws IOException, InvalidParametersException;

    PlacesApiResponseEntity textSearch(String key, String query, Boolean sensor, String location, String radius,
                                  String language, List<String> types) throws IOException, InvalidParametersException;
}

