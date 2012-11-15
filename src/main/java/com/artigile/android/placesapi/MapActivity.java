package com.artigile.android.placesapi;

import android.os.Bundle;
import com.google.android.maps.MapView;
import roboguice.activity.RoboMapActivity;
import roboguice.inject.InjectView;

import javax.inject.Singleton;

/**
 * @author IoaN, 11/11/12 10:01 PM
 */
@Singleton
public class MapActivity extends RoboMapActivity {

    @InjectView(R.id.mapview)
    private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);
        mapView.setBuiltInZoomControls(true);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
