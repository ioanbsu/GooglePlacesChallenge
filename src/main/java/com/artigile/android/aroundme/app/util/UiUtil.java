package com.artigile.android.aroundme.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import com.artigile.android.aroundme.GooglePlaces;
import com.artigile.android.aroundme.MapResultsActivity;
import com.artigile.android.aroundme.placesapi.model.Place;
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

    public void showSearchPage(Activity activity) {
        Intent intent = new Intent(context, GooglePlaces.class);
        activity.startActivity(intent);
    }

    public void navigateToPlace(Place place) {
        if (place != null) {
            String addressQuery = "";
            if (Strings.isNullOrEmpty(place.getFormattedAddress())) {
                addressQuery = place.getGeometry().getLocation().getLat() + "," + place.getGeometry().getLocation().getLng();
            } else {
                addressQuery = place.getFormattedAddress();
            }
            navigateToPlace(addressQuery);
        }
    }

    public void navigateToPlace(String queryAddress) {
        if (queryAddress != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + queryAddress));
            context.startActivity(intent);
        }
    }

}
