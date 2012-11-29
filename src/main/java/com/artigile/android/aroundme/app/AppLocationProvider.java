package com.artigile.android.aroundme.app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 11/16/12
 * Time: 3:28 PM
 */
@Singleton
public class AppLocationProvider implements LocationListener {

    @Inject
    private AppState appState;
    @Inject
    private EventBus eventBus;
    private Location location = new Location("");

    public AppLocationProvider() {
        location.setLongitude(-122.397089);
        location.setLatitude(37.792275);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (status == android.location.LocationProvider.AVAILABLE&&appState.getPendingSearchEvent()!=null) {
            eventBus.post(appState.getPendingSearchEvent());
            appState.setPendingSearchEvent(null);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
