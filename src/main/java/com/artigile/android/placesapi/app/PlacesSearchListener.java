package com.artigile.android.placesapi.app;

import com.artigile.android.placesapi.api.model.PlacesApiResponseEntity;

/**
 * @author IoaN, 11/17/12 10:19 PM
 */
public interface PlacesSearchListener {
    void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity);
}
