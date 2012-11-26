package com.artigile.android.aroundme;

import android.os.Bundle;
import roboguice.activity.RoboActivity;
import roboguice.activity.RoboFragmentActivity;

import javax.inject.Singleton;

/**
 * @author IoaN, 11/25/12 3:39 PM
 */
@Singleton
public class PlaceDetailsActivity extends RoboFragmentActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details_activity);
    }
}
