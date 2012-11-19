package com.artigile.android.aroundme.api.service;

import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

/**
 * @author IoaN, 11/10/12 9:03 AM
 */
@Singleton
public class PlacesUrlsXmlBuilderImpl extends AbstractPlacesUrlsBuilder {

    public static final String XML_SEARCH = "xml";

    @Override
    public String buildSearchNearBy(String key, double longitude, double latitude, Integer radius, RankByType rankBy,
                                    boolean sensor, String keyword, String language, String name, Set<String> types,
                                    String pageToken) {
        return buildSearchNearBy(XML_SEARCH, key, longitude, latitude, radius, rankBy, sensor, keyword, language, name, types, pageToken);
    }

    @Override
    public String buildTextSearch(String key, String query, boolean sensor, String location, String radius,
                                  String language, List<String> types) {
        return buildTextSearch(XML_SEARCH, key, query, sensor, location, radius, language, types);
    }

    @Override
    public String buildPlaceDetails(String key, String reference, boolean sensor, String language) {
        return buildPlaceDetails(XML_SEARCH,key,  reference,  sensor,  language);
    }
}
