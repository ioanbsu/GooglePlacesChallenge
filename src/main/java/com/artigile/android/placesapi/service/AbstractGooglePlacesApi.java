package com.artigile.android.placesapi.service;

import android.util.Log;
import android.util.Xml;
import com.artigile.android.placesapi.model.PlacesApiResponseEntity;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 3:23 PM
 */
public abstract class AbstractGooglePlacesApi implements GooglePlacesApi {

    public static final String KEY="AIzaSyAiM8su2DOeNr3Ii2sNW6sdm2ZUDIugHak";
    public static final String NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/xml?key={0}&location={1}&{2}={3}&sensor={4}";
    public static final String TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/xml?key={0}&query={1}&sensor={2}";



}
