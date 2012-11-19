package com.artigile.android.aroundme.api.model;

/**
 * @author IoaN, 11/10/12 2:29 PM
 */
public class PlaceEvent {

    private String eventId;

    private String startTime;

    private String summary;

    private String url;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
