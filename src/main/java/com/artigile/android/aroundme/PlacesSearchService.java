package com.artigile.android.aroundme;

import android.os.AsyncTask;
import com.artigile.android.aroundme.api.model.Place;
import com.artigile.android.aroundme.api.model.PlacesApiResponseEntity;
import com.artigile.android.aroundme.api.service.GooglePlacesApiImpl;
import com.artigile.android.aroundme.api.service.RankByType;
import com.artigile.android.aroundme.app.LocationProvider;
import com.artigile.android.aroundme.app.PlacesSearchListener;
import com.artigile.android.aroundme.app.event.PlacesSearchResultsAvailableEvent;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import roboguice.inject.InjectResource;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * @author IoaN, 11/17/12 10:40 PM
 */
@Singleton
public class PlacesSearchService {

    public static final int MAX_ALLOWED_RADIUS = 5000;
    @Inject
    protected AppState appState;
    @Inject
    private GooglePlacesApiImpl googlePlacesApi;
    @InjectResource(R.string.api_key)
    private String apiKey;
    @Inject
    private EventBus eventBus;

    public void loadPlaceDetails(Place place, final PlacesSearchListener placesSearchListener) {
        new AsyncTask<Place, Void, PlacesApiResponseEntity>() {

            @Override
            protected PlacesApiResponseEntity doInBackground(Place... params) {
                try {
                    return googlePlacesApi.getPlaceDetails(apiKey, params[0].getReference(), true, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(PlacesApiResponseEntity result) {
                super.onPostExecute(result);
                if (appState.getLastSearchResult() != null & appState.getLastSearchResult().getPlaceList() != null
                        && !appState.getLastSearchResult().getPlaceList().isEmpty()) {
                    appState.setSinglePlaceToDisplayOnMap(result);
                    placesSearchListener.onResultReadyAndAppStateUpdated(result);
                }
            }
        }.execute(place);
    }

    public void searchPlaces(final LocationProvider locationProvider, final String searchQuery, final PlacesSearchListener placesSearchListener) {
        appState.setFoundPlacesList(null);
        new AsyncTask<String, Void, PlacesApiResponseEntity>() {
            @Override
            protected PlacesApiResponseEntity doInBackground(String... params) {
                try {
                    RankByType rankByType = Strings.isNullOrEmpty(searchQuery) ? RankByType.PROMINENCE : RankByType.DISTANCE;
                    PlacesApiResponseEntity placesApiResponseEntity = googlePlacesApi.searchNearBy(apiKey,
                            locationProvider.getLocation().getLongitude(), locationProvider.getLocation().getLatitude(), MAX_ALLOWED_RADIUS, rankByType, true, searchQuery, null, null, null, null);
                    return placesApiResponseEntity;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(PlacesApiResponseEntity place) {
                super.onPostExecute(place);
                appState.setLastSearchResult(place);
                appState.setFoundPlacesList(place);
                eventBus.post(new PlacesSearchResultsAvailableEvent());
                placesSearchListener.onResultReadyAndAppStateUpdated(place);
            }
        }.execute();
    }

    public void loadMorePlaces(final PlacesSearchListener placesSearchListener) {
        if (appState.getLastSearchResult().getNextPageToken() != null) {
            new AsyncTask<String, Void, PlacesApiResponseEntity>() {
                @Override
                protected PlacesApiResponseEntity doInBackground(String... params) {
                    try {
                        return googlePlacesApi.searchNearBy(apiKey, -1, -1, null, null, true, null
                                , null, null, null, appState.getLastSearchResult().getNextPageToken());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(PlacesApiResponseEntity places) {
                    super.onPostExecute(places);
                    appState.setLastSearchResult(places);
                    if (appState.getFoundPlacesList() == null) {
                        appState.setFoundPlacesList(places);
                    } else {
                        appendSearchResultAndMapWithNextPageResult(appState.getFoundPlacesList(), places);
                    }
                    placesSearchListener.onResultReadyAndAppStateUpdated(places);
                }
            }.execute();
        } else {
            placesSearchListener.onResultReadyAndAppStateUpdated(null);
        }
    }

    private void appendSearchResultAndMapWithNextPageResult(PlacesApiResponseEntity appStatePlacesEntity, PlacesApiResponseEntity places) {
        if (appStatePlacesEntity == null || places.getPlaceList() == null) {
            return;
        }
        appStatePlacesEntity.getPlaceList().addAll(places.getPlaceList());
        appStatePlacesEntity.setNextPageToken(places.getNextPageToken());
        appStatePlacesEntity.setHtmlAttribution(places.getHtmlAttribution());
        appStatePlacesEntity.setStatus(places.getStatus());
    }
}
