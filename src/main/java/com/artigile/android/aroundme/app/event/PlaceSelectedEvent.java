package com.artigile.android.aroundme.app.event;

import com.artigile.android.aroundme.placesapi.model.Place;

/**
 * @author IoaN, 11/20/12 11:22 PM
 */
public class PlaceSelectedEvent {

    private Place place;

    public PlaceSelectedEvent(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
