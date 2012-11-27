package com.artigile.android.aroundme.sfparkingapi;

import com.artigile.android.aroundme.placesapi.model.Place;
import com.google.inject.ImplementedBy;

import java.util.List;

/**
 * @author IoaN, 11/26/12 8:43 PM
 */
@ImplementedBy(SfParkingResolverImpl.class)
public interface SfParkingResolver {

    public void getParkingSpacesList(Place place);
}
