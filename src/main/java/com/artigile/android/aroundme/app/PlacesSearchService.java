package com.artigile.android.aroundme.app;

import android.os.AsyncTask;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.app.event.PlacesSearchResultsAvailableEvent;
import com.artigile.android.aroundme.placesapi.model.Place;
import com.artigile.android.aroundme.placesapi.model.PlacesApiResponseEntity;
import com.artigile.android.aroundme.placesapi.service.GooglePlacesApiImpl;
import com.artigile.android.aroundme.placesapi.service.RankByType;
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
    private boolean isQueued = false;

    public void loadPlaceDetails(Place place, final PlacesSearchListener placesSearchListener) {
        isQueued = true;
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
                isQueued = false;
                if (appState.getLastSearchResult() != null & appState.getLastSearchResult().getPlaceList() != null
                        && !appState.getLastSearchResult().getPlaceList().isEmpty()) {
                    if (result.getPlaceList() != null && result.getPlaceList().size() > 0)
                        appState.setSinglePlaceToDisplayOnMap(result.getPlaceList().get(0));
                    placesSearchListener.onResultReadyAndAppStateUpdated(result);
                }
            }
        }.execute(place);
    }

    public void searchPlaces(final AppLocationProvider appLocationProvider, final String searchQuery, final PlacesSearchListener placesSearchListener) {
        appState.setPendingSearchEvent(null);
        isQueued = true;
        appState.setFoundPlacesList(null);
        new AsyncTask<String, Void, PlacesApiResponseEntity>() {
            @Override
            protected PlacesApiResponseEntity doInBackground(String... params) {
                try {
                    RankByType rankByType = Strings.isNullOrEmpty(searchQuery) ? RankByType.PROMINENCE : RankByType.DISTANCE;
                    PlacesApiResponseEntity placesApiResponseEntity = googlePlacesApi.searchNearBy(apiKey,
                            appLocationProvider.getLocation().getLongitude(), appLocationProvider.getLocation().getLatitude(), MAX_ALLOWED_RADIUS, rankByType, true, searchQuery, null, null, null, null);
                    return placesApiResponseEntity;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(PlacesApiResponseEntity place) {
                isQueued = false;
                appState.setLastSearchResult(place);
                appState.setFoundPlacesList(place);
                eventBus.post(new PlacesSearchResultsAvailableEvent());
                placesSearchListener.onResultReadyAndAppStateUpdated(place);
            }
        }.execute();
    }

    public void loadMorePlaces(final PlacesSearchListener placesSearchListener) {
        isQueued = true;
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
                    isQueued = false;
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

    public boolean isQueued() {
        return isQueued;
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
