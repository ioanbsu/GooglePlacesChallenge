package com.artigile.android.placesapi.api.model;

import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 3:15 PM
 */
public class PlacesApiResponseEntity {

    private String status;

    private List<Place> placeList;

    private String nextPageToken;

    private String htmlAttribution;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Place> getPlaceList() {
        return placeList;
    }

    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getHtmlAttribution() {
        return htmlAttribution;
    }

    public void setHtmlAttribution(String htmlAttribution) {
        this.htmlAttribution = htmlAttribution;
    }
}
