package com.artigile.android.placesapi.api.service;

import android.net.Uri;
import com.artigile.android.placesapi.api.exception.InvalidParametersException;
import com.google.common.base.Joiner;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * @author IoaN, 11/10/12 9:02 AM
 */
public abstract class AbstractPlacesUrlsBuilder implements PlacesUrlsBuilder {

    public static final String NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/{0}?key={1}&location={2}&sensor={3}";
    public static final String TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/{0}?key={1}&query={2}&sensor={3}";


    protected String buildSearchNearBy(String type,String key, double longitude, double latitude, Integer radius, RankByType rankBy, Boolean sensor,
                                       String keyword, String language, String name, Set<String> types, String pageToken)
            throws InvalidParametersException {

        if (rankBy != null && rankBy == RankByType.DISTANCE && radius != null) {
            throw new InvalidParametersException("Radius option can not be specified when rankBy value is [distance].");
        }
        if (key == null) {
            throw new InvalidParametersException("Key parameter is required");
        }
        if (sensor == null) {
            throw new InvalidParametersException("Sensor parameter is required");
        }
        Uri.Builder builder = Uri.parse(MessageFormat.format(NEARBY_SEARCH_URL,type, key, latitude + "," + longitude, sensor)).buildUpon();
        if (radius != null) {
            builder.appendQueryParameter("radius", radius + "");
        }
        if (rankBy != null) {
            builder.appendQueryParameter("rankBy", rankBy + "");
        }
        if (keyword != null) {
            builder.appendQueryParameter("keyword", Uri.encode(keyword));
        }
        if (language != null) {
            builder.appendQueryParameter("language", Uri.encode(language));
        }
        if (name != null) {
            builder.appendQueryParameter("name", Uri.encode(name));
        }
        if (types != null) {
            builder.appendQueryParameter("types", Uri.encode(Joiner.on("|").skipNulls().join(types)));
        }
        if (pageToken != null) {
            builder.appendQueryParameter("pagetoken", Uri.encode(pageToken));
        }
        return builder.toString();
    }

    protected String buildTextSearch(String type,String key, String query, Boolean sensor, String location, String radius,
                                     String language, List<String> types) throws InvalidParametersException {
        if (key == null) {
            throw new InvalidParametersException("Key parameter is required");
        }
        if (query == null) {
            throw new InvalidParametersException("Query parameter is required");
        }
        if (sensor == null) {
            throw new InvalidParametersException("Sensor parameter is required");
        }
        Uri.Builder builder = Uri.parse(MessageFormat.format(TEXT_SEARCH_URL,type, key, Uri.encode(query), sensor)).buildUpon();
        if (location != null) {
            builder.appendQueryParameter("location", Uri.encode(location));
        }
        if (radius != null) {
            builder.appendQueryParameter("radius", Uri.encode(radius));
        }
        if (language != null) {
            builder.appendQueryParameter("language", Uri.encode(language));
        }
        if (types != null) {
            builder.appendQueryParameter("types", Uri.encode((Joiner.on("|").skipNulls().join(types))));
        }
        return builder.toString();
    }
}
