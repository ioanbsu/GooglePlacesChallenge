package com.artigile.android.aroundme.app;

import com.artigile.android.aroundme.api.model.PlacesApiResponseEntity;

/**
 * @author IoaN, 11/17/12 10:19 PM
 */
public interface PlacesSearchListener {
    void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity);
}
