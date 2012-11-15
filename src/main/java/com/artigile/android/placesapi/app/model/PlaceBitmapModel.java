package com.artigile.android.placesapi.app.model;

import android.graphics.Bitmap;
import com.artigile.android.placesapi.api.model.Place;

/**
 * @author IoaN, 11/14/12 9:14 PM
 */
public class PlaceBitmapModel {

    private Bitmap bitmap;

    private Place place;

    public PlaceBitmapModel() {
    }

    public PlaceBitmapModel(Bitmap bitmap, Place place) {
        this.bitmap = bitmap;
        this.place = place;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
