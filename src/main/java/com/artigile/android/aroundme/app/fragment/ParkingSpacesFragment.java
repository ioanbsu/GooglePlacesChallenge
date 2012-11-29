package com.artigile.android.aroundme.app.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.app.AppLocationProvider;
import com.artigile.android.aroundme.app.AppState;
import com.artigile.android.aroundme.app.event.ParkingInfoReadyEvent;
import com.artigile.android.aroundme.app.util.UiStringUtil;
import com.artigile.android.aroundme.app.util.UiUtil;
import com.artigile.android.aroundme.sfparkingapi.SfParkingResolver;
import com.artigile.android.aroundme.sfparkingapi.model.ParkingSpace;
import com.artigile.android.aroundme.sfparkingapi.model.ParkingSpaceType;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/27/12
 * Time: 11:21 AM
 */
@Singleton
public class ParkingSpacesFragment extends RoboFragment {
    @InjectView(R.id.parkingPlaceFragment)
    private LinearLayout parkingPlaceFragment;
    @InjectView(R.id.parkingLoadingProgressBarPanel)
    private RelativeLayout parkingLoadingProgressBarLayout;
    @InjectView(R.id.noParkingDataAvailableLabel)
    private TextView noParkingDataAvailableLabel;
    @Inject
    private SfParkingResolver sfParkingResolver;
    @Inject
    private EventBus eventBus;
    @Inject
    private AppLocationProvider appLocationProvider;
    @Inject
    private AppState appState;
    @Inject
    private UiUtil uiUtil;
    @Inject
    private UiStringUtil uiStringUtil;
    private LayoutInflater mInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parking_info_fragment, container, false);
        eventBus.register(new ParingInfoRecorder());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parkingLoadingProgressBarLayout.setVisibility(View.VISIBLE);
        sfParkingResolver.getParkingSpacesList(appState.getLastSelectedPlaceDetails());
        mInflater = LayoutInflater.from(getActivity());
    }

    private void navigateToParkingPlace(String placeLocation) {
        List<Double> splittedCoords = uiStringUtil.extractCoordinates(placeLocation);
        uiUtil.navigateToPlace(splittedCoords.get(1) + "," + splittedCoords.get(0));
    }

    private void popupateParkingDataOnUi(ParkingInfoReadyEvent e) {
        parkingPlaceFragment.removeAllViews();
        if (getActivity() != null && e.getParkingPlacesResult() != null && e.getParkingPlacesResult().getNumRecords() > 0) {
            noParkingDataAvailableLabel.setVisibility(View.GONE);
            int ordinalNumber = 1;
            List<ParkingSpace> parkingSpaces = e.getParkingPlacesResult().getAvl();
            Collections.sort(parkingSpaces, new Comparator<ParkingSpace>() {
                @Override
                public int compare(ParkingSpace lhs, ParkingSpace rhs) {
                    if (lhs.getLoc() == null || rhs.getLoc() == null) {
                        return 0;
                    }
                    float distance1 = uiStringUtil.getDistanceInMiles(lhs.getLoc(), appState.getLastSelectedPlaceDetails().getGeometry().getLocation().getLng() + ", " + appState.getLastSelectedPlaceDetails().getGeometry().getLocation().getLat());
                    float distance2 = uiStringUtil.getDistanceInMiles(rhs.getLoc(), appState.getLastSelectedPlaceDetails().getGeometry().getLocation().getLng() + ", " + appState.getLastSelectedPlaceDetails().getGeometry().getLocation().getLat());
                    if (distance1 > distance2) {
                        return 1;
                    } else if (distance1 < distance2) {
                        return -1;
                    }
                    return 0;
                }
            });
            for (ParkingSpace parkingSpace : parkingSpaces) {
                View view = mInflater.inflate(R.layout.parking_place_detail, null);
                new ParkingDetailsLazyLoader(view, ordinalNumber++, parkingSpace).execute();
            }
        } else {
            noParkingDataAvailableLabel.setVisibility(View.VISIBLE);
        }
        parkingLoadingProgressBarLayout.setVisibility(View.GONE);
    }

    private void hideEmptyTextViews(TextView... textViews) {
        for (TextView textView : textViews) {
            if (textView.getText().toString().isEmpty()) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    private class ParkingDetailsLazyLoader extends AsyncTask<View, View, View> {

        private View view;
        private int ordinalNumber;
        private ParkingSpace parkingSpace;

        private ParkingDetailsLazyLoader(View view, int ordinalNumber, ParkingSpace parkingSpace) {
            this.view = view;
            this.ordinalNumber = ordinalNumber;
            this.parkingSpace = parkingSpace;
        }

        @Override
        protected View doInBackground(View... params) {
            TextView placeNameTextView = (TextView) view.findViewById(R.id.parkingPlaceName);
            TextView placeAddressTextView = (TextView) view.findViewById(R.id.parkingPlaceAddess);
            TextView placeDistanceTextView = (TextView) view.findViewById(R.id.parkingPlaceDistance);
            TextView placeAvailableSpacesTextView = (TextView) view.findViewById(R.id.parkingAvailableVsAllSpaces);
            TextView placeHoursOfOperationTextView = (TextView) view.findViewById(R.id.parkingPlaceHoursOfOperation);
            TextView parkingTypeTextView = (TextView) view.findViewById(R.id.parkingType);
            TextView parkingRatesTextView = (TextView) view.findViewById(R.id.parkingRates);
            placeNameTextView.setText(ordinalNumber + "." + parkingSpace.getName());
            if (parkingSpace.getType() == ParkingSpaceType.OFF) {
                parkingTypeTextView.setText(getActivity().getString(R.string.parking_off_street_parking));
            }
            if (parkingSpace.getType() == ParkingSpaceType.ON) {
                parkingTypeTextView.setText(getActivity().getString(R.string.parking_on_street_parking));
            }
            placeAddressTextView.setText(parkingSpace.getDesc());
            String distance = uiStringUtil.getDistanceInMilesStr(parkingSpace.getLoc(),
                    appState.getLastSelectedPlaceDetails().getGeometry().getLocation().getLng() + ", " + appState.getLastSelectedPlaceDetails().getGeometry().getLocation().getLat());
            placeDistanceTextView.setText("(" + distance + ")");
            placeAvailableSpacesTextView.setText((parkingSpace.getOper() - parkingSpace.getOcc()) + " available of " + parkingSpace.getOper());
            placeHoursOfOperationTextView.setText(uiStringUtil.buildHoursOfOperation(parkingSpace.getOphrs()));
            parkingRatesTextView.setText(uiStringUtil.buildRates(parkingSpace.getRates()));

            hideEmptyTextViews(placeAvailableSpacesTextView, placeAddressTextView, placeHoursOfOperationTextView);
            final String placeLocation = parkingSpace.getLoc();
            ImageView navigateToParkingPlaceButton = (ImageView) view.findViewById(R.id.navigateToParkingPlaceButton);
            navigateToParkingPlaceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToParkingPlace(placeLocation);
                }
            });
            return view;
        }

        @Override
        protected void onPostExecute(View view) {
            super.onPostExecute(view);
            parkingPlaceFragment.addView(view);
        }
    }

    class ParingInfoRecorder {
        @Subscribe
        public void parkingInfoReady(ParkingInfoReadyEvent e) {
            popupateParkingDataOnUi(e);
        }
    }
}
