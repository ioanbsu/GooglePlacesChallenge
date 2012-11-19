package com.artigile.android.placesapi.app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 11/16/12
 * Time: 3:28 PM
 */
@Singleton
public class LocationProvider implements LocationListener {

    private Location location=new Location("");

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


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
