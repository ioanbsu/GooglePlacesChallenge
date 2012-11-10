package com.artigile.android.placesapi.service;

import android.net.Uri;
import android.util.Log;
import android.util.Xml;
import com.artigile.android.placesapi.exception.InvalidParametersException;
import com.artigile.android.placesapi.model.PlacesApiResponseEntity;
import com.artigile.android.placesapi.parser.PlaceXmlToEntityParserImpl;
import com.google.common.base.Joiner;
import org.xmlpull.v1.XmlPullParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 1:38 PM
 */
@Singleton
public class GooglePlacesApiImpl extends AbstractGooglePlacesApi {

    @Inject
    private PlaceXmlToEntityParserImpl placeXmlParser;


    public PlacesApiResponseEntity getPlaces(String myurl) throws IOException {
        InputStream is = null;
        try {
            String newUrl = buildTextSearch(KEY, "Emeryville CA", false, null, null, null, null);
            URL url = new URL(newUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG_TAG", "The response is: " + response);
            is = conn.getInputStream();
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(is, null);
                parser.nextTag();
                return placeXmlParser.parse(parser, "PlaceSearchResponse");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                is.close();
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }


    public String buildSearchNearBy(String key, String location, Integer radius, RankByType rankBy, Boolean sensor,
                                    String keyword, String language, String name, Set<String> types, String pageToken)
            throws InvalidParametersException {

        if (rankBy != null && radius != null) {
            throw new InvalidParametersException("you should specify eithe radius or rankby but now both parameters.");
        }
        if (key == null) {
            throw new InvalidParametersException("Key parameter is required");
        }
        if (location == null) {
            throw new InvalidParametersException("Location parameter is required");
        }
        if (sensor == null) {
            throw new InvalidParametersException("Sensor parameter is required");
        }
        String radiusOrRankByValue = radius == null ? rankBy.getValue() : radius + "";
        String radiusOrRankBy = radius == null ? "rankby" : "radius";
        StringBuilder urlBuilder = new StringBuilder(MessageFormat.format(NEARBY_SEARCH_URL, key, location, radiusOrRankBy, radiusOrRankByValue, sensor));

        if (keyword != null) {
            urlBuilder.append("&keyword=").append(keyword);
        }
        if (language != null) {
            urlBuilder.append("&language=").append(language);
        }
        if (name != null) {
            urlBuilder.append("&name=").append(name);
        }
        if (types != null) {
            urlBuilder.append("&types=").append(Joiner.on("|").skipNulls().join(types));
        }
        if (pageToken != null) {
            urlBuilder.append("&pagetoken=").append(pageToken);
        }
        return urlBuilder.toString();
    }

    public String buildTextSearch(String key, String query, Boolean sensor, String location, String radius,
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
        Uri.Builder builder = Uri.parse(MessageFormat.format(TEXT_SEARCH_URL, key, Uri.encode(query), sensor)).buildUpon();
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


