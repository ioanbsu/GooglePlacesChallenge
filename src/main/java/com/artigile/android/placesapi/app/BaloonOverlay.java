package com.artigile.android.placesapi.app;

import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

/**
 * @author IoaN, 11/14/12 7:21 PM
 */
 public class BaloonOverlay<T> extends ItemizedOverlay<OverlayItem> {

    private ContextWrapper contextWrapper;

    private ArrayList<OverlayItem> myOverlays ;

    private T object;

    private BaloonTapListener<T> baloonClickListener;

    public BaloonOverlay(Drawable defaultMarker,T object, ContextWrapper contextWrapper) {
        super(boundCenterBottom(defaultMarker));
        this.contextWrapper=contextWrapper;
        myOverlays = new ArrayList<OverlayItem>();
        this.object=object;
        populate();
    }

    public void addOverlay(OverlayItem overlay){
        myOverlays.add(overlay);
        populate();
    }

    @Override
    protected OverlayItem createItem(int i) {
        return myOverlays.get(i);
    }

    // Removes overlay item i
    public void removeItem(int i){
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

    public void setBaloonClickListener(BaloonTapListener<T> baloonClickListener){
       this.baloonClickListener=baloonClickListener;
    }

    @Override
    protected boolean onTap(int index) {
        if(baloonClickListener!=null){
            baloonClickListener.onBaloonTapped(object);
        }
        String title = myOverlays.get(index).getTitle();
        Toast.makeText(contextWrapper.getBaseContext(), title, Toast.LENGTH_LONG).show();
        return super.onTap(index);
    }
}