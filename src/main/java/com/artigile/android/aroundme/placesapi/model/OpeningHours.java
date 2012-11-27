package com.artigile.android.aroundme.placesapi.model;

import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 1:36 PM
 */
public class OpeningHours {

    private boolean openNow;

    private List<PlaceOpenClosePeriod> placeOpenPeriodList;

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public List<PlaceOpenClosePeriod> getPlaceOpenPeriodList() {
        return placeOpenPeriodList;
    }

    public void setPlaceOpenPeriodList(List<PlaceOpenClosePeriod> placeOpenPeriodList) {
        this.placeOpenPeriodList = placeOpenPeriodList;
    }
}
