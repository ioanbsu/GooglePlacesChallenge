package com.artigile.android.aroundme.app;

import android.graphics.drawable.Drawable;
import com.artigile.android.aroundme.app.mapballoon.BalloonItemizedOverlay;
import com.artigile.android.aroundme.app.mapballoon.BaloonTapListener;
import com.artigile.android.aroundme.placesapi.model.Place;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

/**
 * @author IoaN, 11/14/12 7:21 PM
 */
public class PlaceOverlay extends BalloonItemizedOverlay<OverlayItem> {


    private ArrayList<OverlayItem> myOverlays;

    private Place object;

    private BaloonTapListener<Place> baloonClickListener;


    public PlaceOverlay(Drawable defaultMarker, MapView mapView, Place object) {
        super(boundCenterBottom(defaultMarker), mapView);
        myOverlays = new ArrayList<OverlayItem>();
        this.object = object;
        populate();
    }

    public void addOverlay(OverlayItem overlay) {
        myOverlays.add(overlay);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return myOverlays.get(i);
    }

    // Removes overlay item i
    public void removeItem(int i) {
        myOverlays.remove(i);
        populate();
    }

    // Returns present number of items in list
    @Override
    public int size() {
        return myOverlays.size();
    }


    public void addOverlayItem(OverlayItem overlayItem) {
        myOverlays.add(overlayItem);
        populate();
    }


    public void addOverlayItem(int lat, int lon, String title) {
        try {
            GeoPoint point = new GeoPoint(lat, lon);
            OverlayItem overlayItem = new OverlayItem(point, title, null);
            addOverlayItem(overlayItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBaloonClickListener(BaloonTapListener<Place> baloonClickListener) {
        this.baloonClickListener = baloonClickListener;
    }

    @Override
    protected Place getOverlayPlaceObject() {
        return object;
    }

    @Override
    protected boolean onTap(int index) {
        if (baloonClickListener != null) {
            baloonClickListener.onBaloonTapped(object);
        }
        return super.onTap(index);
    }
}