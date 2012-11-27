package com.artigile.android.aroundme.placesapi.model;

import java.util.List;

/**
 * @author IoaN, 11/10/12 1:42 PM
 */
public class AddressComponent {

    private List<String> types;

    private String longName;

    private String shortName;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
