package com.artigile.android.aroundme;

import android.os.Bundle;
import com.artigile.android.aroundme.app.AppState;
import com.artigile.android.aroundme.app.fragment.PlaceDetailsFragment;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author IoaN, 11/25/12 3:39 PM
 */
@Singleton
public class PlaceDetailsActivity extends RoboFragmentActivity {

    @InjectFragment(R.id.selectedPlaceDetailsFragment)
    private PlaceDetailsFragment placeDetailsFragment;
    @Inject
    private AppState appState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_details_activity);
    }


    @Override
    protected void onResume() {
        super.onResume();
        placeDetailsFragment.loadPlaceDetails(appState.getLastSelectedPlaceDetails());
    }
}
