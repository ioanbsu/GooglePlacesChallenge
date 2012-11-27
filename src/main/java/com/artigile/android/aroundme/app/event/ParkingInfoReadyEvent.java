package com.artigile.android.aroundme.app.event;

import com.artigile.android.aroundme.sfparkingapi.model.ParkingPlacesResult;

/**
 * User: ioanbsu
 * Date: 11/27/12
 * Time: 11:35 AM
 */
public class ParkingInfoReadyEvent {

    private ParkingPlacesResult parkingPlacesResult;

    public ParkingInfoReadyEvent(ParkingPlacesResult parkingPlacesResult) {
        this.parkingPlacesResult = parkingPlacesResult;
    }

    public ParkingPlacesResult getParkingPlacesResult() {
        return parkingPlacesResult;
    }
}
