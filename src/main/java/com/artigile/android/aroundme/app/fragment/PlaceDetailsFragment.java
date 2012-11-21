package com.artigile.android.aroundme.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.artigile.android.aroundme.AppState;
import com.artigile.android.aroundme.PlacesSearchService;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.api.model.Place;
import com.artigile.android.aroundme.api.model.PlacesApiResponseEntity;
import com.artigile.android.aroundme.app.PlacesSearchListener;
import com.artigile.android.aroundme.app.event.PlaceSelectedEvent;
import com.artigile.android.aroundme.app.event.PlacesSearchResultsAvailableEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import javax.inject.Singleton;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * @author IoaN, 11/20/12 11:08 PM
 */
@Singleton
public class PlaceDetailsFragment extends RoboFragment {
    @InjectView(R.id.placeDetailsName)
    private TextView placeDetailsName;
    @InjectView(R.id.placeDetailsAddress)
    private TextView placeDetailsAddress;
    @InjectView(R.id.placeDetailsWebAddress)
    private TextView placeDetailsWebAddress;
    @InjectView(R.id.placeDetailsFragmentMainPanel)
    private LinearLayout placeDetailsFragmentMainPanel;
    @InjectView(R.id.placeDetailsFragmentProgressBar)
    private ProgressBar placeDetailsFragmentProgressBar;
    @InjectView(R.id.placeDetailsFragmentSelectPlaceLabel)
    private TextView placeDetailsFragmentSelectPlaceLabel;
    @Inject
    private EventBus eventBus;
    @Inject
    private AppState appState;
    @Inject
    private PlacesSearchService placesSearchService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_details_fragment, container, false);
        eventBus.register(new PlaceSelectedRecorder());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkForSelectPlaceLabel();
        showPlaceDetails();
    }

    private void loadPlaceDetails(PlaceSelectedEvent e) {
        placeDetailsFragmentProgressBar.setVisibility(VISIBLE);
        placeDetailsFragmentMainPanel.setVisibility(INVISIBLE);
        placeDetailsFragmentSelectPlaceLabel.setVisibility(INVISIBLE);
        placesSearchService.loadPlaceDetails(e.getPlace(), new PlacesSearchListener() {
            @Override
            public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                Place place = placesApiResponseEntity.getPlaceList().get(0);
                appState.setLastSelectedPlaceDetails(place);
                showPlaceDetails();
                placeDetailsFragmentProgressBar.setVisibility(INVISIBLE);
                placeDetailsFragmentMainPanel.setVisibility(VISIBLE);
            }
        });
    }

    private void showPlaceDetails() {
        Place place = appState.getLastSelectedPlaceDetails();
        if (place != null) {
            placeDetailsFragmentMainPanel.setVisibility(VISIBLE);
            placeDetailsName.setText(place.getName());
            placeDetailsAddress.setText(place.getFormattedAddress());
            placeDetailsWebAddress.setText(place.getWebsite());
            placeDetailsFragmentSelectPlaceLabel.setVisibility(INVISIBLE);
        }
    }

    private void checkForSelectPlaceLabel() {
        if (appState.getLastSelectedPlaceDetails() == null && appState.getFoundPlacesList() != null && !appState.getFoundPlacesList().getPlaceList().isEmpty()) {
            placeDetailsFragmentSelectPlaceLabel.setVisibility(VISIBLE);
        }else{
            placeDetailsFragmentSelectPlaceLabel.setVisibility(INVISIBLE);
        }
    }

    class PlaceSelectedRecorder {
        @Subscribe
        public void recordPlaceSelected(PlaceSelectedEvent e) {
            loadPlaceDetails(e);
        }

        @Subscribe
        public void recordPlacesSearchResultReady(PlacesSearchResultsAvailableEvent e) {
            checkForSelectPlaceLabel();
        }

    }
}
