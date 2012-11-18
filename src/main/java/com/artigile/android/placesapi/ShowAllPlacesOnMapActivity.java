package com.artigile.android.placesapi;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.artigile.android.placesapi.api.model.Place;
import com.artigile.android.placesapi.app.BaloonTapListener;
import com.artigile.android.placesapi.app.BitmapLoader;
import com.artigile.android.placesapi.app.PlaceOverlay;
import com.artigile.android.placesapi.app.PlacesSearchListener;
import com.artigile.android.placesapi.app.model.PlaceBitmapModel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;

/**
 * @author IoaN, 11/14/12 7:38 PM
 */
public class ShowAllPlacesOnMapActivity extends RoboMapActivity {


    @InjectView(R.id.mapview)
    private MapView mapView;

    @Inject
    private AppState appState;

    @Inject
    private PlacesSearchService placesSearchService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detailed_info);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.setBuiltInZoomControls(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayAllPlacesFromSavedAppState();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private BaloonTapListener<Place> createBaloonListener() {
        return new BaloonTapListener<Place>() {
            @Override
            public void onBaloonTapped(Place tapedValue) {
                showPlaceDetails(tapedValue);
            }
        };
    }

    private void showPlaceDetails(Place tapedValue) {
        System.out.println("do nothing here....yet");
    }

    public void loadMorePlaces(View v) {
        placesSearchService.loadMorePlaces(new PlacesSearchListener() {
            @Override
            public void onResultReadyAndAppStateUpdated() {
                displayAllPlacesFromSavedAppState();
            }
        });
    }

    private BitmapDrawable scaleBitmap(Bitmap bitmap) {
        Bitmap bitmapOrig = Bitmap.createScaledBitmap(bitmap, 30, 30, false);
        return new BitmapDrawable(getResources(), bitmapOrig);
    }

    private void displayAllPlacesFromSavedAppState() {
        mapView.getOverlays().clear();
        if (appState.getSelectedPlacesForViewDetails() != null
                && appState.getSelectedPlacesForViewDetails().getPlaceList() != null
                && !appState.getSelectedPlacesForViewDetails().getPlaceList().isEmpty()) {
            float minLeft = Float.MAX_VALUE;
            float maxRight = Float.MIN_VALUE;
            float minBottom = Float.MAX_VALUE;
            float maxTop = Float.MIN_VALUE;
            if (appState.getSelectedPlacesForViewDetails().getPlaceList().size() == 1) {
                showPlaceDetails(appState.getSelectedPlacesForViewDetails().getPlaceList().get(0));
            }
            for (final Place selectedPlace : appState.getSelectedPlacesForViewDetails().getPlaceList()) {
                minLeft = Math.min(minLeft, selectedPlace.getGeometry().getLocation().getLng());
                maxRight = Math.max(maxRight, selectedPlace.getGeometry().getLocation().getLng());
                minBottom = Math.min(minBottom, selectedPlace.getGeometry().getLocation().getLat());
                maxTop = Math.max(maxTop, selectedPlace.getGeometry().getLocation().getLat());
                new AsyncTask<String, Void, PlaceBitmapModel>() {
                    @Override
                    protected PlaceBitmapModel doInBackground(String... params) {
                        return new PlaceBitmapModel(BitmapLoader.loadBitmap(selectedPlace.getIcon()), selectedPlace);
                    }

                    @Override
                    protected void onPostExecute(PlaceBitmapModel result) {
                        super.onPostExecute(result);
                        if (result.getBitmap() != null) {
                            GeoPoint geoPoint = new GeoPoint((int) (1E6 * result.getPlace().getGeometry().getLocation().getLat()), (int) (1E6 * selectedPlace.getGeometry().getLocation().getLng()));
                            mapView.getController().animateTo(geoPoint);
                            PlaceOverlay baloonOverlay = new PlaceOverlay(scaleBitmap(result.getBitmap()), mapView, result.getPlace());
                            baloonOverlay.setBalloonBottomOffset(50);
                            baloonOverlay.setShowDisclosure(true);
                            OverlayItem overlayItem = new OverlayItem(geoPoint, result.getPlace().getName(), null);
                            baloonOverlay.addOverlay(overlayItem);
                            mapView.getOverlays().add(baloonOverlay);
                            baloonOverlay.setBaloonClickListener(createBaloonListener());
                        }
                    }
                }.execute();
            }
            Log.i("left", minLeft + "");
            Log.i("right", maxRight + "");
            Log.i("top", maxTop + "");
            Log.i("bottom", (maxRight - minLeft) + "");
            Log.i("Max Distance longitude", (maxRight - minLeft) + "");
            Log.i("Max Distance latitude", (maxTop - minBottom) + "");
            mapView.getController().setZoom(15);

        }
    }
}