package com.artigile.android.placesapi;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.util.List;

/**
 * @author IoaN, 11/14/12 7:38 PM
 */
public class ShowAllPlacesOnMapActivity extends RoboMapActivity {


    @InjectView(R.id.mapview)
    private MapView mapView;

    @InjectView(R.id.mapRelativeLayout)
    private RelativeLayout mapRelativeLayout;


    @Inject
    private AppState appState;

    @Inject
    private PlacesSearchService placesSearchService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detailed_info);
        mapView.getController().setZoom(15);
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
                displayMapOverlays(appState.getLastSearchResult().getPlaceList());
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
            if (appState.getSelectedPlacesForViewDetails().getPlaceList().size() == 1) {
                showPlaceDetails(appState.getSelectedPlacesForViewDetails().getPlaceList().get(0));
            }
            displayMapOverlays(appState.getSelectedPlacesForViewDetails().getPlaceList());
        }
    }

    private void displayMapOverlays(List<Place> placeList) {
        float minLon = Float.MAX_VALUE;
        float maxLon = -1*Float.MAX_VALUE;
        float minLat = Float.MAX_VALUE;
        float maxLat = -1*Float.MAX_VALUE;
        for (final Place selectedPlace : appState.getSelectedPlacesForViewDetails().getPlaceList()) {
            minLon = Math.min(minLon, selectedPlace.getGeometry().getLocation().getLng());
            maxLon = Math.max(maxLon, selectedPlace.getGeometry().getLocation().getLng());
            minLat = Math.min(minLat, selectedPlace.getGeometry().getLocation().getLat());
            maxLat = Math.max(maxLat, selectedPlace.getGeometry().getLocation().getLat());
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
                        final PlaceOverlay baloonOverlay = new PlaceOverlay(scaleBitmap(result.getBitmap()), mapView, result.getPlace());
                        baloonOverlay.setBalloonBottomOffset(50);
                        baloonOverlay.setShowDisclosure(true);
                        OverlayItem overlayItem = new OverlayItem(geoPoint, result.getPlace().getName(), null);
                        baloonOverlay.addOverlay(overlayItem);
                        baloonOverlay.setBaloonClickListener(createBaloonListener());


                        ImageView markerView = new ImageView(getBaseContext());
                        markerView.setImageDrawable(scaleBitmap(result.getBitmap()));
                        AnimationSet animation = new AnimationSet(true);
                        animation.setInterpolator(getBaseContext(), android.R.anim.bounce_interpolator);

                        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, -400.0f, 0.0f);
                        translateAnimation.setDuration(1000);
                        animation.addAnimation(translateAnimation);

                        markerView.startAnimation(animation);

                        mapView.addView(markerView,
                                new MapView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        geoPoint,
                                        MapView.LayoutParams.BOTTOM_CENTER));
                        mapView.invalidate();


                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(1500); /*
                                 * Making thread sleep for 1 sec so that animation can be
                                 * seen before updating the overlays
                                 */
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mapView.post(new Runnable() {
                                    public void run() {
                                        mapView.removeAllViews(); /* Removing all the image Views */
                                        mapView.getOverlays().add(baloonOverlay);
/*
                                               * Adding the overlay items on map overlay
                                               * list
                                               */
                                        mapView.invalidate(); /* Refreshing the map overlays */
                                    }
                                });

                            }
                        }).start();


                    }
                }
            }.execute();
        }
        Log.i("left", minLon + "");
        Log.i("right", maxLon + "");
        Log.i("top", maxLat + "");
        Log.i("bottom", (maxLon - minLon) + "");
        Log.i("Max Distance longitude", (maxLon - minLon) + "");
        Log.i("Max Distance latitude", (maxLat - minLat) + "");

        double fitFactor = 1.2;
        mapView.getController().zoomToSpan((int) (Math.abs(maxLat - minLat) * fitFactor*1E6), (int) (Math.abs(maxLon- minLon) * fitFactor*1E6));
        mapView.getController().animateTo(new GeoPoint( (int)(1E6*(maxLat + minLat))/2,
                (int)(1E6*(maxLon + minLon))/2 ));
    }


}