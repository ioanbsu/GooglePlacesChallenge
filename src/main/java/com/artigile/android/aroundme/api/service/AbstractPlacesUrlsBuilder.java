package com.artigile.android.aroundme.api.service;

import android.net.Uri;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * @author IoaN, 11/10/12 9:02 AM
 */
public abstract class AbstractPlacesUrlsBuilder implements PlacesUrlsBuilder {

    public static final String NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/{0}?key={1}&location={2}&sensor={3}";
    public static final String TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/{0}?key={1}&query={2}&sensor={3}";
    public static final String PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/{0}?key={1}&reference={2}&sensor={3}";


    protected String buildSearchNearBy(String type, String key, double longitude, double latitude, Integer radius, RankByType rankBy, boolean sensor,
                                       String keyword, String language, String name, Set<String> types, String pageToken) {
        Preconditions.checkNotNull(key, "Key parameter is required");
/*
        Preconditions.checkArgument(!(rankBy != null && rankBy == RankByType.DISTANCE && radius != null)
                , "Radius option can not be specified when rankBy value is [distance].");
*/

        Uri.Builder builder = Uri.parse(MessageFormat.format(NEARBY_SEARCH_URL, type, key, latitude + "," + longitude, sensor)).buildUpon();
        if (radius != null) {
            builder.appendQueryParameter("radius", radius + "");
        }
        if (rankBy != null) {
            builder.appendQueryParameter("rankBy", rankBy.toString().toLowerCase() + "");
        }
        if (keyword != null) {
            builder.appendQueryParameter("keyword", keyword);
        }
        if (language != null) {
            builder.appendQueryParameter("language", language);
        }
        if (name != null) {
            builder.appendQueryParameter("name",name);
        }
        if (types != null) {
            builder.appendQueryParameter("types",Joiner.on("|").skipNulls().join(types));
        }
        if (pageToken != null) {
            builder.appendQueryParameter("pagetoken", Uri.encode(pageToken));
        }
        return builder.toString();
    }

    protected String buildTextSearch(String type, String key, String query, boolean sensor, String location, String radius,
                                     String language, List<String> types) {
        Preconditions.checkNotNull(key, "Key parameter is required");
        Preconditions.checkNotNull(query, "Query parameter is required");
        Uri.Builder builder = Uri.parse(MessageFormat.format(TEXT_SEARCH_URL, type, key, Uri.encode(query), sensor)).buildUpon();
        if (location != null) {
            builder.appendQueryParameter("location", location);
        }
        if (radius != null) {
            builder.appendQueryParameter("radius", radius);
        }
        if (language != null) {
            builder.appendQueryParameter("language", language);
        }
        if (types != null) {
            builder.appendQueryParameter("types",(Joiner.on("|").skipNulls().join(types)));
        }
        return builder.toString();
    }

    protected String buildPlaceDetails(String type, String key, String reference, boolean sensor, String language) {
        Preconditions.checkNotNull(key, "Key parameter is required");
        Preconditions.checkNotNull(reference, "Reference parameter is requied to build the places details URL");

        Uri.Builder builder = Uri.parse(MessageFormat.format(PLACE_DETAILS_URL, type, key, reference, sensor)).buildUpon();

        if (language != null) {
            builder.appendQueryParameter("language", language);
        }
        return builder.toString();
    }

}
