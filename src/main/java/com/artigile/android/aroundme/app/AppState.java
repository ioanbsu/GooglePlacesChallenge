package com.artigile.android.aroundme.app;

import com.artigile.android.aroundme.app.event.PendingSearchEvent;
import com.artigile.android.aroundme.placesapi.model.Place;
import com.artigile.android.aroundme.placesapi.model.PlacesApiResponseEntity;

import javax.inject.Singleton;
import java.util.Map;

/**
 * @author IoaN, 11/10/12 3:56 PM
 */
@Singleton
public class AppState {

    private PlacesApiResponseEntity lastSearchResult;

    private Place singlePlaceToDisplayOnMap;

    private PlacesApiResponseEntity foundPlacesList;

    private Place lastSelectedPlaceDetails;

    private boolean requestIsInProgress;

    private boolean startSearchButtonShow=true;

    private PendingSearchEvent pendingSearchEvent;

    public PlacesApiResponseEntity getLastSearchResult() {
        return lastSearchResult;
    }

    public void setLastSearchResult(PlacesApiResponseEntity lastSearchResult) {
        this.lastSearchResult = lastSearchResult;
    }

    public Place getSinglePlaceToDisplayOnMap() {
        return singlePlaceToDisplayOnMap;
    }

    public void setSinglePlaceToDisplayOnMap(Place singlePlaceToDisplayOnMap) {
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

    public PendingSearchEvent getPendingSearchEvent() {
        return pendingSearchEvent;
    }

    public void setPendingSearchEvent(PendingSearchEvent pendingSearchEvent) {
        this.pendingSearchEvent = pendingSearchEvent;
    }
}
