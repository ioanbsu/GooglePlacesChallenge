package com.artigile.android.aroundme.app.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import com.artigile.android.aroundme.MapResultsActivity;
import com.artigile.android.aroundme.api.model.Place;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author IoaN, 11/25/12 12:56 PM
 */
@Singleton
public class UiUtil {

    @Inject
    private Context context;

    public void showPlaceOnMap(Fragment fragment) {
        Intent intent = new Intent(context, MapResultsActivity.class);
        fragment.startActivity(intent);
    }

    public void navigateToPlace(Place place) {
        if (place != null) {
            Intent intent = null;
            if (Strings.isNullOrEmpty(place.getFormattedAddress())) {
                intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + place.getGeometry()
                        .getLocation().getLat() + "," + place.getGeometry().getLocation().getLng()));
            } else {
                intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + place.getFormattedAddress()));
            }
            context.startActivity(intent);
        }
    }

}
