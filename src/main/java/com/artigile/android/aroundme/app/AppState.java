package com.artigile.android.aroundme.app;

import com.artigile.android.aroundme.placesapi.model.Place;
import com.artigile.android.aroundme.placesapi.model.PlacesApiResponseEntity;

import javax.inject.Singleton;

/**
 * @author IoaN, 11/10/12 3:56 PM
 */
@Singleton
public class AppState {

    private PlacesApiResponseEntity lastSearchResult;

    private PlacesApiResponseEntity singlePlaceToDisplayOnMap;

    private PlacesApiResponseEntity foundPlacesList;

    private Place lastSelectedPlaceDetails;

    private boolean requestIsInProgress;

    private boolean startSearchButtonShow=true;

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

    public boolean isRequestIsInProgress() {
        return requestIsInProgress;
    }

    public void setRequestIsInProgress(boolean requestIsInProgress) {
        this.requestIsInProgress = requestIsInProgress;
    }

    public boolean isStartSearchButtonShow() {
        return startSearchButtonShow;
    }

    public void setStartSearchButtonShow(boolean startSearchButtonShow) {
        this.startSearchButtonShow = startSearchButtonShow;
    }

    public Place getLastSelectedPlaceDetails() {
        return lastSelectedPlaceDetails;
    }

    public void setLastSelectedPlaceDetails(Place lastSelectedPlaceDetails) {
        this.lastSelectedPlaceDetails = lastSelectedPlaceDetails;
    }
}
