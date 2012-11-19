package com.artigile.android.placesapi;

import com.artigile.android.placesapi.api.model.PlacesApiResponseEntity;

import javax.inject.Singleton;

/**
 * @author IoaN, 11/10/12 3:56 PM
 */
@Singleton
public class AppState {

    private PlacesApiResponseEntity lastSearchResult;

    private PlacesApiResponseEntity singlePlaceToDisplayOnMap;

    private PlacesApiResponseEntity foundPlacesList;

    public PlacesApiResponseEntity getLastSearchResult() {
        return lastSearchResult;
    }

    public void setLastSearchResult(PlacesApiResponseEntity lastSearchResult) {
        this.lastSearchResult = lastSearchResult;
    }

    public PlacesApiResponseEntity getSinglePlaceToDisplayOnMap() {
        return singlePlaceToDisplayOnMap;
    }

    public void setSinglePlaceToDisplayOnMap(PlacesApiResponseEntity singlePlaceToDisplayOnMap) {
        this.singlePlaceToDisplayOnMap = singlePlaceToDisplayOnMap;
    }

    public PlacesApiResponseEntity getFoundPlacesList() {
        return foundPlacesList;
    }

    public void setFoundPlacesList(PlacesApiResponseEntity foundPlacesList) {
        this.foundPlacesList = foundPlacesList;
    }

}
