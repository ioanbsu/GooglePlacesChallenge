package com.artigile.android.aroundme.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.Surface;
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


    public  void disableRotation(Activity activity)
    {
        final int orientation = activity.getResources().getConfiguration().orientation;
        final int rotation = activity.getWindowManager().getDefaultDisplay().getOrientation();

        // Copied from Android docs, since we don't have these values in Froyo 2.2
        int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8;
        int SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO)
        {
            SCREEN_ORIENTATION_REVERSE_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            SCREEN_ORIENTATION_REVERSE_PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }

        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)
        {
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270)
        {
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                activity.setRequestedOrientation(SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        }
    }

    public void enableRotation(Activity activity)
    {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

}
