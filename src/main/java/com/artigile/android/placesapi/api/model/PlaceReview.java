package com.artigile.android.placesapi.api.model;

import java.util.List;

/**
 * @author IoaN, 11/10/12 2:36 PM
 */
public class PlaceReview {

    private List<AspectRating> aspectRatings;

    private String authorName;

    private String authorUrl;

    private String text;

    private String time;

    public List<AspectRating> getAspectRatings() {
        return aspectRatings;
    }

    public void setAspectRatings(List<AspectRating> aspectRatings) {
        this.aspectRatings = aspectRatings;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
