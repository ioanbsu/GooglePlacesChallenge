package com.artigile.android.aroundme.app.util;

import android.location.Location;
import com.artigile.android.aroundme.sfparkingapi.model.Ophrs;
import com.artigile.android.aroundme.sfparkingapi.model.Rate;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: ioanbsu
 * Date: 11/28/12
 * Time: 10:51 AM
 */
@Singleton
public class UiStringUtil {
    private static final float MILES_IN_METER = 0.000621371F;
    private DecimalFormat milesFormat = new DecimalFormat("#,###,###,##0.00");

    public String buildHoursOfOperation(List<Ophrs> ophrs) {
        List<String> workingHours = new ArrayList<String>();
        if (ophrs != null && !ophrs.isEmpty()) {
            for (Ophrs ophr : ophrs) {
                StringBuilder hoursOfOperationBuilder = new StringBuilder();
                hoursOfOperationBuilder.append(ophr.getFrom());
                if (!Strings.isNullOrEmpty(ophr.getTo())) {
                    hoursOfOperationBuilder.append("-").append(ophr.getTo());
                }
                hoursOfOperationBuilder.append(": ").append(ophr.getBeg()).append("-").append(ophr.getEnd());
                workingHours.add(hoursOfOperationBuilder.toString());
            }
            return Joiner.on("\n").join(workingHours);
        } else {
            return "";
        }
    }

    public String buildRates(List<Rate> rates) {
        List<String> ratesStrings = new ArrayList<String>();
        if (rates != null && !rates.isEmpty()) {
            for (Rate rate : rates) {
                StringBuilder ratesBuilder = new StringBuilder();
                ratesBuilder.append(rate.getDesc());
                if (!Strings.isNullOrEmpty(rate.getBeg()) || !Strings.isNullOrEmpty(rate.getEnd())) {
                    ratesBuilder.append(rate.getBeg());
                    if (rate.getEnd() != null) {
                        ratesBuilder.append("-").append(rate.getEnd());
                    }
                }
                ratesBuilder.append(MessageFormat.format(": {0}$ ({1})", rate.getRate(), rate.getRq()));
                ratesStrings.add(ratesBuilder.toString());
            }
            return Joiner.on("\n").join(ratesStrings);
        } else {
            return "";
        }
    }

    public float getDistanceInMiles(String parkingLocation1, String parkingLocation2) {
        return getDistanceInMiles(getLocationFromString(parkingLocation1), getLocationFromString(parkingLocation2));

    }

    public float getDistanceInMiles(Location location, String parkingLocationData) {
        try {
            if (parkingLocationData == null) {
                return -1;
            }
            Location placeLocation = getLocationFromString(parkingLocationData);
            if (placeLocation == null) {
                return -1;
            }
            return getDistanceInMiles(location, placeLocation);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public float getDistanceInMiles(Location location1, Location location2) {
        return location1.distanceTo(location2) * MILES_IN_METER;
    }

    public String getDistanceInMilesStr(String parkingLocationData1, String parkingLocationData2) {
        float distance = getDistanceInMiles(parkingLocationData1, parkingLocationData2);
        if (distance == -1) {
            return "";
        }
        return milesFormat.format(distance) + "mi.";

    }

    public List<Double> extractCoordinates(String placeLocation) {

        List<Double> locationStringCoordinates = new ArrayList<Double>();
        StringTokenizer coordinatesTokinezer = new StringTokenizer(placeLocation, ",");
        while (coordinatesTokinezer.hasMoreTokens()) {
            locationStringCoordinates.add(Double.valueOf(coordinatesTokinezer.nextToken()));
        }
        return locationStringCoordinates;
    }

    private Location getLocationFromString(String parkingLocationData) {
        List<Double> locationStringCoordinates = extractCoordinates(parkingLocationData);
        if (locationStringCoordinates.size() < 2) {
            return null;
        }

        Location placeLocation = new Location("");
        placeLocation.setLatitude(locationStringCoordinates.get(1));
        placeLocation.setLongitude(locationStringCoordinates.get(0));
        return placeLocation;
    }
}
