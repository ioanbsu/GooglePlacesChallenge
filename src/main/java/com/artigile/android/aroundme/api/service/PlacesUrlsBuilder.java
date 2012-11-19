package com.artigile.android.aroundme.api.service;


import java.util.List;
import java.util.Set;

/**
 * @author IoaN, 11/10/12 9:01 AM
 */
public interface PlacesUrlsBuilder {

    String buildSearchNearBy(String key, double longitude, double latitude, Integer radius, RankByType rankBy, boolean sensor,
                             String keyword, String language, String name, Set<String> types, String pageToken);

    String buildTextSearch(String key, String query, boolean sensor, String location, String radius,
                           String language, List<String> types);

    String buildPlaceDetails(String key, String reference, boolean sensor, String language);
}
