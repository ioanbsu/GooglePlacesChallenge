package com.artigile.android.placesapi.app;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 11/16/12
 * Time: 3:28 PM
 */
@Singleton
public class LocationProvider implements LocationListener {


    private double longitude;
    private double latitude;

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();

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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
