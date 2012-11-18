package com.artigile.android.placesapi;

import android.os.AsyncTask;
import com.artigile.android.placesapi.api.model.Place;
import com.artigile.android.placesapi.api.model.PlacesApiResponseEntity;
import com.artigile.android.placesapi.api.service.GooglePlacesApiImpl;
import com.artigile.android.placesapi.api.service.RankByType;
import com.artigile.android.placesapi.app.LocationProvider;
import com.artigile.android.placesapi.app.PlacesSearchListener;
import com.google.inject.Inject;
import roboguice.inject.InjectResource;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * @author IoaN, 11/17/12 10:40 PM
 */
@Singleton
public class PlacesSearchService {

    @Inject
    private GooglePlacesApiImpl googlePlacesApi;

    @Inject
    protected AppState appState;

    @InjectResource(R.string.api_key)
    private String apiKey;


    public void loadPlaceDetails(Place place, final PlacesSearchListener placesSearchListener) {
        new AsyncTask<Place, Void, String>() {

            @Override
            protected String doInBackground(Place... params) {
                try {
                    PlacesApiResponseEntity placesApiResponseEntity = googlePlacesApi.getPlaceDetails(apiKey, params[0].getReference(), true, null);
                    appState.setSelectedPlacesForViewDetails(placesApiResponseEntity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (appState.getLastSearchResult() != null & appState.getLastSearchResult().getPlaceList() != null
                        && !appState.getLastSearchResult().getPlaceList().isEmpty()) {
                    placesSearchListener.onResultReadyAndAppStateUpdated();
                }
            }
        }.execute(place);
    }

    public void searchPlaces(final LocationProvider locationProvider, final String searchQuery, final PlacesSearchListener placesSearchListener) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    PlacesApiResponseEntity places = googlePlacesApi.searchNearBy(apiKey,
                            locationProvider.getLongitude(), locationProvider.getLatitude(), 1000, RankByType.PROMINENCE, true, searchQuery, null, null, null, null);
                    appState.setLastSearchResult(places);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                placesSearchListener.onResultReadyAndAppStateUpdated();
            }
        }.execute();
    }

    public void loadMorePlaces(final PlacesSearchListener placesSearchListener) {
        if (appState.getLastSearchResult().getNextPageToken() != null) {
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    try {
                        PlacesApiResponseEntity places = googlePlacesApi.searchNearBy(apiKey, -1, -1, null, null, true, null
                                , null, null, null, appState.getLastSearchResult().getNextPageToken());
                        appState.setLastSearchResult(places);
                        appState.getSelectedPlacesForViewDetails().getPlaceList().addAll(places.getPlaceList());
                        appState.getSelectedPlacesForViewDetails().setNextPageToken(places.getNextPageToken());
                        appState.getSelectedPlacesForViewDetails().setHtmlAttribution(places.getHtmlAttribution());
                        appState.getSelectedPlacesForViewDetails().setStatus(places.getStatus());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String aVoid) {
                    super.onPostExecute(aVoid);
                    placesSearchListener.onResultReadyAndAppStateUpdated();
                }
            }.execute();
        }
    }
}
