package com.artigile.android.aroundme.app.mapballoon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.artigile.android.aroundme.PlaceDetailsActivity;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.app.AppState;
import com.artigile.android.aroundme.placesapi.model.Place;
import com.google.android.maps.OverlayItem;
import roboguice.RoboGuice;

/**
 * @author IoaN, 11/17/12 5:52 PM
 */
public class BalloonOverlayView<Item extends OverlayItem> extends FrameLayout {

    private LinearLayout layout;
    private TextView title;
    private TextView snippet;
    private RatingBar placeRating;
    private TextView navigate;
    private TextView balloonPlaceDetails;
    private Place baloonPlace;

    /**
     * Create a new BalloonOverlayView.
     *
     * @param context             - The activity context.
     * @param balloonBottomOffset - The bottom padding (in pixels) to be applied
     *                            when rendering this view.
     */
    public BalloonOverlayView(Context context, int balloonBottomOffset) {

        super(context);

        setPadding(10, 0, 10, balloonBottomOffset);

        layout = new LimitLinearLayout(context);
        layout.setVisibility(VISIBLE);

        setupView(context, layout);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.NO_GRAVITY;

        addView(layout, params);

    }

    /**
     * Inflate and initialize the BalloonOverlayView UI. Override this method
     * to provide a custom view/layout for the balloon.
     *
     * @param context - The activity context.
     * @param parent  - The root layout into which you must inflate your view.
     */
    protected void setupView(final Context context, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.map_balloon, parent);
        title = (TextView) v.findViewById(R.id.balloon_item_title);
        snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);
        navigate = (TextView) v.findViewById(R.id.navigateToPlace);
        placeRating = (RatingBar) v.findViewById(R.id.placeRating);
        balloonPlaceDetails=(TextView)v.findViewById(R.id.balloonPlaceDetails);
        navigate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + baloonPlace.getVicinity()));
                context.startActivity(intent);
            }
        });
        balloonPlaceDetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RoboGuice.getInjector(context).getInstance(AppState.class).setLastSelectedPlaceDetails(baloonPlace);
                Intent intent = new Intent(context, PlaceDetailsActivity.class);
                context.startActivity(intent);
            }
        });

    }

    /**
     * Sets the view data from a given overlay item.
     *
     * @param selectedPlace - The overlay item containing the relevant view data.
     */
    public void setData(Place selectedPlace) {
        this.baloonPlace = selectedPlace;
        layout.setVisibility(VISIBLE);
        setBalloonData(selectedPlace, layout);
    }

    /**
     * Sets the view data from a given overlay item. Override this method to create
     * your own data/view mappings.
     *
     * @param selectedPlace - The overlay item containing the relevant view data.
     * @param parent        - The parent layout for this BalloonOverlayView.
     */
    protected void setBalloonData(Place selectedPlace, ViewGroup parent) {
        if (selectedPlace.getName() != null) {
            title.setVisibility(VISIBLE);
            title.setText(selectedPlace.getName());
        } else {
            title.setText("");
            title.setVisibility(GONE);
        }
        if (selectedPlace.getVicinity() != null) {
            snippet.setVisibility(VISIBLE);
            snippet.setText(selectedPlace.getVicinity());
            navigate.setVisibility(VISIBLE);
            balloonPlaceDetails.setVisibility(VISIBLE);
        } else {
            snippet.setText("");
            snippet.setVisibility(GONE);
            balloonPlaceDetails.setVisibility(GONE);
            navigate.setVisibility(GONE);
        }
        if (selectedPlace.getRating() != null) {
            placeRating.setRating(selectedPlace.getRating());
        } else {
            placeRating.setVisibility(GONE);
        }
    }

    private class LimitLinearLayout extends LinearLayout {

        private static final int MAX_WIDTH_DP = 280;

        final float SCALE = getContext().getResources().getDisplayMetrics().density;

        public LimitLinearLayout(Context context) {
            super(context);
        }

        public LimitLinearLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int mode = MeasureSpec.getMode(widthMeasureSpec);
            int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
            int adjustedMaxWidth = (int) (MAX_WIDTH_DP * SCALE + 0.5f);
            int adjustedWidth = Math.min(measuredWidth, adjustedMaxWidth);
            int adjustedWidthMeasureSpec = MeasureSpec.makeMeasureSpec(adjustedWidth, mode);
            super.onMeasure(adjustedWidthMeasureSpec, heightMeasureSpec);
        }
    }

}
