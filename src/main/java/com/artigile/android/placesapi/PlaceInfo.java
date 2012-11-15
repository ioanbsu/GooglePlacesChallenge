package com.artigile.android.placesapi;

import android.os.Bundle;
import android.widget.TextView;
import com.artigile.android.placesapi.api.model.Place;
import com.google.android.maps.MapView;
import com.google.common.base.Joiner;
import roboguice.activity.RoboActivity;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author IoaN, 11/10/12 3:51 PM
 */
@Singleton
public class PlaceInfo extends RoboMapActivity {

    @InjectView(R.id.placeName)
    private TextView placeName;

    @InjectView(R.id.placeType)
    private TextView placeType;

    @InjectView(R.id.placePhoneNumber)
    private TextView placePhoneNumber;

    @InjectView(R.id.placeRating)
    private TextView placeRating;

    @InjectView(R.id.mapview)
    private MapView mapView;

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
        if (appState.getSelectedPlaceForViewDetails() != null
                && appState.getSelectedPlaceForViewDetails().getPlaceList() != null
                && !appState.getSelectedPlaceForViewDetails().getPlaceList().isEmpty()) {

            Place selectedPlace = appState.getSelectedPlaceForViewDetails().getPlaceList().get(0);
            placeName.setText(selectedPlace.getName());
            placeType.setText(Joiner.on(", ").skipNulls().join(selectedPlace.getTypes()));
            placePhoneNumber.setText(selectedPlace.getFormattedPhoneNumber());
            placeRating.setText(selectedPlace.getRating() + "");
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
