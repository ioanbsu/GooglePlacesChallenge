package com.artigile.android.aroundme.app.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.app.AppState;
import com.artigile.android.aroundme.app.LocationProvider;
import com.artigile.android.aroundme.app.event.ParkingInfoReadyEvent;
import com.artigile.android.aroundme.app.util.UiUtil;
import com.artigile.android.aroundme.sfparkingapi.SfParkingResolver;
import com.artigile.android.aroundme.sfparkingapi.model.Ophrs;
import com.artigile.android.aroundme.sfparkingapi.model.ParkingSpace;
import com.artigile.android.aroundme.sfparkingapi.model.ParkingSpaceType;
import com.artigile.android.aroundme.sfparkingapi.model.Rate;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    @Inject
    private SfParkingResolver sfParkingResolver;
    @Inject
    private EventBus eventBus;
    @Inject
    private LocationProvider locationProvider;
    @Inject
    private AppState appState;
    @Inject
    private UiUtil uiUtil;
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

    private void navigateToParkingPlace(String placeLocation) {
        List<String> splittedCoords = extractCoordinates(placeLocation);
        uiUtil.navigateToPlace(splittedCoords.get(1) + "," + splittedCoords.get(0));
    }

    private List<String> extractCoordinates(String placeLocation) {
        List<String> splittedCoords = new ArrayList<String>();
        Iterable<String> coordsIterator = Splitter.on(",").trimResults().omitEmptyStrings().split(placeLocation);
        for (String coord : coordsIterator) {
            splittedCoords.add(coord);
        }
        return splittedCoords;
    }

    private void popupateParkingDataOnUi(ParkingInfoReadyEvent e) {
        if (getActivity() != null && e.getParkingPlacesResult() != null && e.getParkingPlacesResult().getNumRecords() > 0) {
            int ordinalNumber = 1;
            List<ParkingSpace> parkingSpaces = e.getParkingPlacesResult().getAvl();
            for (ParkingSpace parkingSpace : e.getParkingPlacesResult().getAvl()) {
                String distance = "";
                if (parkingSpace.getLoc() != null) {
                    Location parkingLocation = new Location("");
                    String locationStr = parkingSpace.getLoc();
                    Double longitude = Double.valueOf(locationStr.substring(0, locationStr.indexOf(",")));
                    Double latitude = Double.valueOf(locationStr.substring(locationStr.lastIndexOf(",") + 1));
                    parkingLocation.setLatitude(latitude);
                    parkingLocation.setLongitude(longitude);
                    distance = milesFormat.format(locationProvider.getLocation().distanceTo(parkingLocation) * 0.000621371) + "mi.";
                }
                LayoutInflater mInflater = LayoutInflater.from(getActivity());
                View view = mInflater.inflate(R.layout.parking_place_detail, null);
                TextView placeNameTextView = (TextView) view.findViewById(R.id.parkingPlaceName);
                TextView placeAddressTextView = (TextView) view.findViewById(R.id.parkingPlaceAddess);
                TextView placeDistanceTextView = (TextView) view.findViewById(R.id.parkingPlaceDistance);
                TextView placeAvailableSpacesTextView = (TextView) view.findViewById(R.id.parkingAvailableVsAllSpaces);
                TextView placeHoursOfOperationTextView = (TextView) view.findViewById(R.id.parkingPlaceHoursOfOperation);
                TextView parkingTypeTextView = (TextView) view.findViewById(R.id.parkingType);
                TextView parkingRatesTextView = (TextView) view.findViewById(R.id.parkingRates);


                placeNameTextView.setText(ordinalNumber + "." + parkingSpace.getName());
                if (parkingSpace.getType() == ParkingSpaceType.OFF) {
                    parkingTypeTextView.setText("Off street parking");
                }
                if (parkingSpace.getType() == ParkingSpaceType.ON) {
                    parkingTypeTextView.setText("On street parking");
                }
                placeAddressTextView.setText(parkingSpace.getDesc());
                placeDistanceTextView.setText("(" + distance + ")");
                placeAvailableSpacesTextView.setText((parkingSpace.getOper() - parkingSpace.getOcc()) + " available of " + parkingSpace.getOper());
                placeHoursOfOperationTextView.setText(buildHoursOfOperation(parkingSpace.getOphrs()));
                parkingRatesTextView.setText(buildRates(parkingSpace.getRates()));

                hideEmptyTextViews(placeAvailableSpacesTextView, placeAddressTextView, placeHoursOfOperationTextView);
                final String placeLocation = parkingSpace.getLoc();
                Button navigateToParkingPlaceButton = (Button) view.findViewById(R.id.navigateToParkingPlaceButton);
                navigateToParkingPlaceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToParkingPlace(placeLocation);
                    }
                });
                parkingPlaceFragment.addView(view);
                ordinalNumber++;
            }
        }
    }

    private String buildHoursOfOperation(List<Ophrs> ophrs) {
        List<String> workingHours = new ArrayList<String>();
        if (ophrs != null && !ophrs.isEmpty()) {
            for (Ophrs ophr : ophrs) {
                StringBuilder hoursOfOperationBuilder = new StringBuilder();
                hoursOfOperationBuilder.append(ophr.getFrom());
                if (!Strings.isNullOrEmpty(ophr.getTo())) {
                    hoursOfOperationBuilder.append("-").append(ophr.getTo());
                }
                hoursOfOperationBuilder.append(": ").append(ophr.getBeg()).append("-").append(ophr.getEnd());
                workingHours.add(hoursOfOperationBuilder.toString());
            }
            return Joiner.on("\n").join(workingHours);
        } else {
            return "";
        }
    }

    private String buildRates(List<Rate> rates) {
        List<String> ratesStrings = new ArrayList<String>();
        if (rates != null && !rates.isEmpty()) {
            for (Rate rate : rates) {
                StringBuilder ratesBuilder = new StringBuilder();
                ratesBuilder.append(rate.getDesc());
                ratesBuilder.append(rate.getBeg());
                if (rate.getEnd() != null) {
                    ratesBuilder.append("-").append(rate.getEnd());
                }
                ratesBuilder.append(" ").append(rate.getRr()).append(": ").append(rate.getRate());
                ratesStrings.add(ratesBuilder.toString());
            }
            return Joiner.on("\n").join(ratesStrings);
        } else {
            return "";
        }
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

    class ParingInfoRecorder {
        @Subscribe
        public void parkingInfoReady(ParkingInfoReadyEvent e) {
            popupateParkingDataOnUi(e);
        }
    }
}
