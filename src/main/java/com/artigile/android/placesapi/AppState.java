package com.artigile.android.placesapi;

import com.artigile.android.placesapi.api.model.PlacesApiResponseEntity;

import javax.inject.Singleton;

/**
 * @author IoaN, 11/10/12 3:56 PM
 */
@Singleton
public class AppState {

    private PlacesApiResponseEntity lastSearchResult;

    private PlacesApiResponseEntity selectedPlacesForViewDetails;

    public PlacesApiResponseEntity getLastSearchResult() {
        return lastSearchResult;
    }

    public void setLastSearchResult(PlacesApiResponseEntity lastSearchResult) {
        this.lastSearchResult = lastSearchResult;
    }

    public PlacesApiResponseEntity getSelectedPlacesForViewDetails() {
        return selectedPlacesForViewDetails;
    }

    public void setSelectedPlacesForViewDetails(PlacesApiResponseEntity selectedPlacesForViewDetails) {
        this.selectedPlacesForViewDetails = selectedPlacesForViewDetails;
    }
}
