package com.artigile.android.placesapi.service;

import com.artigile.android.placesapi.model.Place;
import com.artigile.android.placesapi.model.PlacesApiResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 1:38 PM
 */public interface GooglePlacesApi {

    PlacesApiResponseEntity getPlaces(String url) throws IOException;

 }

