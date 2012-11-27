package com.artigile.android.aroundme.sfparkingapi;

import com.artigile.android.aroundme.sfparkingapi.model.ParkingPlacesResult;
import com.google.inject.ImplementedBy;

/**
 * User: ioanbsu
 * Date: 11/27/12
 * Time: 8:55 AM
 */
@ImplementedBy(SfParkingResponseParserImpl.class)
public interface SfParkingResponseParser {

    ParkingPlacesResult parse(String response) throws ParkingResultNotSuccessException;
}
