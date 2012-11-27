package com.artigile.android.aroundme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.artigile.android.aroundme.app.mapballoon.BaloonTapListener;
import com.artigile.android.aroundme.app.util.BitmapLoader;
import com.artigile.android.aroundme.placesapi.model.Place;
import com.artigile.android.aroundme.placesapi.model.PlacesApiResponseEntity;
import com.artigile.android.aroundme.app.*;
import com.artigile.android.aroundme.app.model.PlaceBitmapModel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @author IoaN, 11/14/12 7:38 PM
 */
@Singleton
public class MapResultsActivity extends RoboMapActivity {


    @InjectView(R.id.mapview)
    private MapView mapView;
    @InjectView(R.id.mapRelativeLayout)
    private RelativeLayout mapRelativeLayout;
    @InjectView(R.id.loadingMoreResults)
    private ProgressBar loadingMoreResults;
    @Inject
    private AppState appState;
    @Inject
    private LocationProvider locationProvider;
    @Inject
    private PlacesSearchService placesSearchService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        mapView.getController().setZoom(15);
    }

    public void centerOnMyLocation(View view) {
        mapView.getController().animateTo(new GeoPoint((int) (1E6 * locationProvider.getLocation().getLatitude()),
                (int) (1E6 * locationProvider.getLocation().getLongitude())));
    }

    public void loadMorePlaces(View v) {
        if (appState.getLastSearchResult().getNextPageToken() != null) {
            loadingMoreResults.setVisibility(View.VISIBLE);
            placesSearchService.loadMorePlaces(new PlacesSearchListener() {
                @Override
                public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                    if (placesApiResponseEntity.getPlaceList() != null) {
                        displayMapOverlays(appState.getLastSearchResult().getPlaceList());
                    }
                    loadingMoreResults.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(getBaseContext(), R.string.no_more_results_to_display_toast, 10).show();
            mapView.removeAllViews();
            mapView.getOverlays().clear();
            mapView.removeAllViewsInLayout();
            displayMapOverlays(appState.getFoundPlacesList().getPlaceList());
            displayMyLocationOnMap();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.setBuiltInZoomControls(false);
        mapView.setFilterTouchesWhenObscured(true);
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

    private BitmapDrawable scaleBitmap(Bitmap bitmap) {
        Bitmap bitmapOrig = Bitmap.createScaledBitmap(bitmap, 30, 30, false);
        return new BitmapDrawable(getResources(), bitmapOrig);
    }

    private void displayAllPlacesFromSavedAppState() {
        mapView.getOverlays().clear();
        if (appState.getSinglePlaceToDisplayOnMap() != null
                && appState.getSinglePlaceToDisplayOnMap().getPlaceList() != null
                && !appState.getSinglePlaceToDisplayOnMap().getPlaceList().isEmpty()) {
            showPlaceDetails(appState.getSinglePlaceToDisplayOnMap().getPlaceList().get(0));
            displayMapOverlays(appState.getSinglePlaceToDisplayOnMap().getPlaceList());
        } else if (appState.getFoundPlacesList() != null
                && appState.getFoundPlacesList().getPlaceList() != null
                && !appState.getFoundPlacesList().getPlaceList().isEmpty()) {
            displayMapOverlays(appState.getFoundPlacesList().getPlaceList());
        }
        displayMyLocationOnMap();
    }

    private void displayMyLocationOnMap() {
        GeoPoint geoPoint = new GeoPoint((int) (1E6 * locationProvider.getLocation().getLatitude()),
                (int) (1E6 * locationProvider.getLocation().getLongitude()));
        Place place = new Place();
        place.setName(getBaseContext().getString(R.string.my_location_on_map_overlay_name));
        final PlaceOverlay balloonOverlay = createMapOverlay(place, BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.my_location), geoPoint);
        mapView.getOverlays().add(balloonOverlay);
    }

    private void displayMapOverlays(List<Place> placeList) {
        float minLon = (float) locationProvider.getLocation().getLongitude();
        float maxLon = (float) locationProvider.getLocation().getLongitude();
        float minLat = (float) locationProvider.getLocation().getLatitude();
        float maxLat = (float) locationProvider.getLocation().getLatitude();
        for (final Place selectedPlace : placeList) {
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
                        final PlaceOverlay baloonOverlay = createMapOverlay(result.getPlace(), result.getBitmap(), geoPoint);


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
                                    Thread.sleep(1050); /*
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

        double fitFactor = 1.1;
        mapView.getController().zoomToSpan((int) (Math.abs(maxLat - minLat) * fitFactor * 1E6), (int) (Math.abs(maxLon - minLon) * fitFactor * 1E6));
        mapView.getController().animateTo(new GeoPoint((int) (1E6 * (maxLat + minLat)) / 2,
                (int) (1E6 * (maxLon + minLon)) / 2));
    }

    private PlaceOverlay createMapOverlay(Place place, Bitmap bitmap, GeoPoint geoPoint) {
        final PlaceOverlay baloonOverlay = new PlaceOverlay(scaleBitmap(bitmap), mapView, place);
        baloonOverlay.setBalloonBottomOffset(20);
        OverlayItem overlayItem = new OverlayItem(geoPoint, place.getName(), null);
        baloonOverlay.addOverlay(overlayItem);
        baloonOverlay.setBaloonClickListener(createBaloonListener());
        return baloonOverlay;
    }


}