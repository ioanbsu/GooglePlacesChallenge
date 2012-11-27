package com.artigile.android.aroundme.sfparkingapi;

import com.artigile.android.aroundme.placesapi.model.Place;

import java.util.List;

/**
 * @author IoaN, 11/26/12 8:43 PM
 */
public interface SfParkingResolver {

    public List<ParkingPlace> getParkingSpacesList(Place place);
}
