package com.artigile.android.aroundme.app.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.app.AppState;
import com.artigile.android.aroundme.app.LocationProvider;
import com.artigile.android.aroundme.app.event.ParkingInfoReadyEvent;
import com.artigile.android.aroundme.app.event.PlaceSelectedEvent;
import com.artigile.android.aroundme.sfparkingapi.SfParkingResolver;
import com.artigile.android.aroundme.sfparkingapi.model.ParkingSpace;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;

/**
 * User: ioanbsu
 * Date: 11/27/12
 * Time: 11:21 AM
 */
@Singleton
public class ParkingSpacesFragment extends RoboFragment {
    @InjectView(R.id.parkingPlaceFragment)
    private LinearLayout parkingPlaceFragment;
    @Inject
    private SfParkingResolver sfParkingResolver;
    @Inject
    private EventBus eventBus;
    @Inject
    private LocationProvider locationProvider;
    @Inject
    private AppState appState;
    private DecimalFormat milesFormat = new DecimalFormat("#,###,###,##0.00");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parking_info_fragment, container, false);
        eventBus.register(new ParingInfoRecorder());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parkingPlaceFragment.removeAllViews();
        sfParkingResolver.getParkingSpacesList(appState.getLastSelectedPlaceDetails());
    }

    class ParingInfoRecorder {
        @Subscribe
        public void paringInfoReady(ParkingInfoReadyEvent e) {
            if (getActivity() != null && e.getParkingPlacesResult() != null && e.getParkingPlacesResult().getNumRecords() > 0) {
                for (ParkingSpace parkingSpace : e.getParkingPlacesResult().getAvl()) {
                    TextView textView = new TextView(getActivity());
                    String distance = "";
                    if (parkingSpace.getLoc() != null) {
                        Location parkingLocation = new Location("");
                        String locationStr = parkingSpace.getLoc();
                        System.out.println(locationStr.substring(0, locationStr.indexOf(",")));
                        System.out.println(locationStr.substring(locationStr.lastIndexOf(",") + 1));
                        Double longitude = Double.valueOf(locationStr.substring(0, locationStr.indexOf(",")));
                        Double latitude = Double.valueOf(locationStr.substring(locationStr.lastIndexOf(",") + 1));
                        parkingLocation.setLatitude(latitude);
                        parkingLocation.setLongitude(longitude);
                        distance = milesFormat.format(locationProvider.getLocation().distanceTo(parkingLocation) * 0.000621371) + "mi.";
                    }
                    textView.setText(parkingSpace.getName() + " (" + distance + ")");

                    parkingPlaceFragment.addView(textView);
                }
            }
        }

        @Subscribe
        public void recordPlaceSelected(PlaceSelectedEvent e) {
            sfParkingResolver.getParkingSpacesList(e.getPlace());
        }
    }
}
