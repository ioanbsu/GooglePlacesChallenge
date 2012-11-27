package com.artigile.android.aroundme.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.artigile.android.aroundme.app.AppState;
import com.artigile.android.aroundme.PlacesSearchService;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.placesapi.model.Place;
import com.artigile.android.aroundme.placesapi.model.PlaceReview;
import com.artigile.android.aroundme.placesapi.model.PlacesApiResponseEntity;
import com.artigile.android.aroundme.app.PlacesSearchListener;
import com.artigile.android.aroundme.app.event.PlaceSelectedEvent;
import com.artigile.android.aroundme.app.event.PlacesSearchResultsAvailableEvent;
import com.artigile.android.aroundme.app.util.UiUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
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
    private TextView nameText;
    @InjectView(R.id.placeDetailsAddress)
    private TextView addressText;
    @InjectView(R.id.placeDetailsWebAddress)
    private TextView webAddressText;
    @InjectView(R.id.placeDetailsPhone)
    private TextView phoneText;
    @InjectView(R.id.placeDetailsFragmentMainPanel)
    private LinearLayout mainViewPanel;
    @InjectView(R.id.placeDetailsFragmentProgressBar)
    private ProgressBar placeDetailsAreLoadingProgressBar;
    @InjectView(R.id.placeDetailsFragmentSelectPlaceLabel)
    private TextView selectAPlacePromotionLabel;
    @InjectView(R.id.placeDetailsPlaceType)
    private TextView placeTypeText;
    @InjectView(R.id.placeDetaildReviews)
    private LinearLayout reviewsContainer;
    @InjectView(R.id.placeDetailsShowMap)
    private Button showMapButton;
    @InjectView(R.id.placeDetailsNavigateToPlace)
    private Button navigateToPlaceButton;
    @InjectView(R.id.placeDetailsRatingBar)
    private RatingBar placeDetailsRatingBar;
    @Inject
    private EventBus eventBus;
    @Inject
    private AppState appState;
    @Inject
    private PlacesSearchService placesSearchService;
    @Inject
    private Context context;
    @Inject
    private UiUtil uiUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.place_details_fragment, container, false);
        eventBus.register(new PlaceSelectedRecorder());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkForSelectAPlacePromotionLabel();
        showPlaceDetails(appState.getLastSelectedPlaceDetails(), context.getString(R.string.place_details_empty_label));
        initListeners();
    }

    public void loadPlaceDetails(Place place) {
        placeDetailsAreLoadingProgressBar.setVisibility(VISIBLE);
        selectAPlacePromotionLabel.setVisibility(INVISIBLE);
        showPlaceDetails(place, context.getString(R.string.place_details_loading_label));
        placesSearchService.loadPlaceDetails(place, new PlacesSearchListener() {
            @Override
            public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                Place place = placesApiResponseEntity.getPlaceList().get(0);
                appState.setLastSelectedPlaceDetails(place);
                showPlaceDetails(appState.getLastSelectedPlaceDetails(), context.getString(R.string.place_details_empty_label));
                placeDetailsAreLoadingProgressBar.setVisibility(INVISIBLE);
            }
        });
    }

    private void showPlaceDetails(Place place, String emptyText) {
        if (place != null) {
            nameText.setText(place.getName());
            addressText.setText(getEmptyOrValueText(place.getFormattedAddress(), emptyText));
            webAddressText.setText(getEmptyOrValueText(place.getWebsite(), emptyText));
            phoneText.setText(getEmptyOrValueText(place.getFormattedPhoneNumber(), emptyText));
            placeTypeText.setText(getEmptyOrValueText(Joiner.on(", ").skipNulls().join(place.getTypes()), emptyText));
            if (place.getRating() != null) {
                placeDetailsRatingBar.setRating(place.getRating());
                placeDetailsRatingBar.setVisibility(VISIBLE);
            } else {
                placeDetailsRatingBar.setVisibility(INVISIBLE);
            }
            reviewsContainer.removeAllViews();
            if (place.getPlaceReview() != null) {
                for (PlaceReview placeReview : place.getPlaceReview()) {
                    TextView ratingText = buildRatingText();
                    ratingText.setText(Html.fromHtml("<b>"+placeReview.getAuthorName() + "</b>: " + placeReview.getText()));
                    reviewsContainer.addView(ratingText);
                }
            } else {
                TextView ratingText = buildRatingText();
                ratingText.setText(R.string.place_details_no_reviews);
                reviewsContainer.addView(ratingText);
            }
        }
        checkViewsVisibility();
    }

    private TextView buildRatingText() {
        TextView ratingText = new TextView(context);
        ratingText.setPadding(0, 0, 0, 15);
        ratingText.setTextSize(15);
        return ratingText;
    }

    private String getEmptyOrValueText(String text, String emptyText) {
        if (Strings.isNullOrEmpty(text)) {
            return emptyText;
        }
        return text;
    }

    private void checkViewsVisibility() {
        if (appState.getLastSelectedPlaceDetails() != null) {
            selectAPlacePromotionLabel.setVisibility(INVISIBLE);
            mainViewPanel.setVisibility(VISIBLE);
        } else {
            checkForSelectAPlacePromotionLabel();
        }
    }

    private void checkForSelectAPlacePromotionLabel() {
        if (appState.getLastSelectedPlaceDetails() == null && appState.getFoundPlacesList() != null && !appState.getFoundPlacesList().getPlaceList().isEmpty()) {
            selectAPlacePromotionLabel.setVisibility(VISIBLE);
        } else {
            selectAPlacePromotionLabel.setVisibility(INVISIBLE);
        }
    }

    private void initListeners() {
        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiUtil.showPlaceOnMap(PlaceDetailsFragment.this);
            }
        });
        navigateToPlaceButton.setOnClickListener(new

                                                         View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 uiUtil.navigateToPlace(appState.getLastSelectedPlaceDetails());
                                                             }
                                                         });
    }

    class PlaceSelectedRecorder {
        @Subscribe
        public void recordPlaceSelected(PlaceSelectedEvent e) {
            loadPlaceDetails(e.getPlace());
        }

        @Subscribe
        public void recordPlacesSearchResultReady(PlacesSearchResultsAvailableEvent e) {
            checkForSelectAPlacePromotionLabel();
        }
    }
}
