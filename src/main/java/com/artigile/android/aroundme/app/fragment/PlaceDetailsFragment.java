package com.artigile.android.aroundme.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.app.event.PlaceSelectedEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import javax.inject.Singleton;

/**
 * @author IoaN, 11/20/12 11:08 PM
 */
@Singleton
public class PlaceDetailsFragment extends RoboFragment {
    @Inject
    private EventBus eventBus;
    @InjectView(R.id.placeDetailsName)
    private TextView placeDetailsName;
    @InjectView(R.id.placeDetailsAddress)
    private TextView placeDetailsAddress;
    @InjectView(R.id.placeDetailsWebAddress)
    private TextView placeDetailsWebAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_details_fragment, container, false);
        eventBus.register(new PlaceSelectedRecorder());
        return view;
    }

    class PlaceSelectedRecorder {
        @Subscribe
        public void recordPlaceSelected(PlaceSelectedEvent e) {
            placeDetailsName.setText(e.getPlace().getName());
            placeDetailsAddress.setText(e.getPlace().getFormattedAddress());
            placeDetailsWebAddress.setText(e.getPlace().getWebsite());
        }
    }
}
