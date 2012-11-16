package com.artigile.android.placesapi;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import com.artigile.android.placesapi.api.model.Place;
import com.artigile.android.placesapi.app.BaloonOverlay;
import com.artigile.android.placesapi.app.BaloonTapListener;
import com.artigile.android.placesapi.app.BitmapLoader;
import com.artigile.android.placesapi.app.model.PlaceBitmapModel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.common.base.Joiner;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;

/**
 * @author IoaN, 11/14/12 7:38 PM
 */
public class ShowAllPlacesOnMapActivity extends RoboMapActivity {


    @InjectView(R.id.mapview)
    private MapView mapView;

    @InjectView(R.id.placeName)
    private TextView placeName;

    @InjectView(R.id.placePhoneNumber)
    private TextView placePhoneNumber;

    @InjectView(R.id.placeType)
    private TextView placeType;

    @InjectView(R.id.placeRatingStars)
    private RatingBar placeRatingStars;

    @Inject
    private AppState appState;

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
        mapView.getOverlays().clear();
        if (appState.getSelectedPlaceForViewDetails() != null
                && appState.getSelectedPlaceForViewDetails().getPlaceList() != null
                && !appState.getSelectedPlaceForViewDetails().getPlaceList().isEmpty()) {
            for (final Place selectedPlace : appState.getSelectedPlaceForViewDetails().getPlaceList()) {

                new AsyncTask<String, Void, PlaceBitmapModel>() {
                    @Override
                    protected PlaceBitmapModel doInBackground(String... params) {
                        return new PlaceBitmapModel(BitmapLoader.loadBitmap(selectedPlace.getIcon()),selectedPlace);
                    }

                    @Override
                    protected void onPostExecute(PlaceBitmapModel result) {
                        super.onPostExecute(result);
                        if (result.getBitmap() != null) {
                            GeoPoint geoPoint = new GeoPoint((int) (1E6 * result.getPlace().getGeometry().getLocation().getLat()), (int) (1E6 * selectedPlace.getGeometry().getLocation().getLng()));
                            mapView.getController().animateTo(geoPoint);
                            mapView.getController().setZoom(15);

                            BaloonOverlay<Place> baloonOverlay = new BaloonOverlay<Place>(scaleBitmap(result.getBitmap()),result.getPlace(),ShowAllPlacesOnMapActivity.this);
                            OverlayItem overlayItem = new OverlayItem(geoPoint, result.getPlace().getName(), null);
                            baloonOverlay.addOverlay(overlayItem);
                            mapView.getOverlays().add(baloonOverlay);
                            baloonOverlay.setBaloonClickListener(createBaloonListener());
                        }
                    }
                }.execute();

            }

        }
    }

    private BaloonTapListener<Place> createBaloonListener() {
      return new BaloonTapListener<Place>() {
          @Override
          public void onBaloonTapped(Place tapedValue) {
              placeName.setText(tapedValue.getName());
              placePhoneNumber.setText(tapedValue.getFormattedPhoneNumber());
              if (tapedValue.getRating() != null) {
                  placeRatingStars.setVisibility(View.VISIBLE);
                  placeRatingStars.setRating(tapedValue.getRating());
              }else{
                  placeRatingStars.setVisibility(View.GONE);
              }
              placeType.setText(Joiner.on(", ").skipNulls().join(tapedValue.getTypes()));
          }
      };

    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private BitmapDrawable scaleBitmap(Bitmap bitmap) {
        Bitmap bitmapOrig = Bitmap.createScaledBitmap(bitmap, 60, 60, false);
        return new BitmapDrawable(getResources(), bitmapOrig);
    }
}