package com.artigile.android.placesapi.api.model;

/**
 * @author IoaN, 11/10/12 2:33 PM
 */
public class PlaceOpenClosePeriod {

    private OpenClosePair open;

    private OpenClosePair close;

    public OpenClosePair getOpen() {
        return open;
    }

    public void setOpen(OpenClosePair open) {
        this.open = open;
    }

    public OpenClosePair getClose() {
        return close;
    }

    public void setClose(OpenClosePair close) {
        this.close = close;
    }
}
