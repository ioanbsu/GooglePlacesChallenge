package com.artigile.android.aroundme.placesapi.model;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 4:02 PM
 */
public class Viewport {

    private Location southwest;

    private Location northeast;

    public Location getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Location southwest) {
        this.southwest = southwest;
    }

    public Location getNortheast() {
        return northeast;
    }

    public void setNortheast(Location northeast) {
        this.northeast = northeast;
    }
}
