package com.artigile.android.placesapi.api.service;

import com.artigile.android.placesapi.api.exception.InvalidParametersException;

import java.util.List;
import java.util.Set;

/**
 * @author IoaN, 11/10/12 9:01 AM
 */
public interface PlacesUrlsBuilder {

    String buildSearchNearBy(String key, double longitude, double latitude, Integer radius, RankByType rankBy, Boolean sensor,
                             String keyword, String language, String name, Set<String> types, String pageToken) throws InvalidParametersException;

    String buildTextSearch(String key, String query, Boolean sensor, String location, String radius,
                           String language, List<String> types) throws InvalidParametersException;
}
