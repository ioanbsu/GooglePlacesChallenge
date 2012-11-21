package com.artigile.android.aroundme.app.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.artigile.android.aroundme.AppState;
import com.artigile.android.aroundme.MapResultsActivity;
import com.artigile.android.aroundme.PlacesSearchService;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.api.model.Place;
import com.artigile.android.aroundme.api.model.PlacesApiResponseEntity;
import com.artigile.android.aroundme.app.AnimationUtil;
import com.artigile.android.aroundme.app.LocationProvider;
import com.artigile.android.aroundme.app.PlaceEfficientAdapter;
import com.artigile.android.aroundme.app.PlacesSearchListener;
import com.artigile.android.aroundme.app.event.PlaceSelectedEvent;
import com.google.common.base.Objects;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import roboguice.fragment.RoboListFragment;
import roboguice.inject.InjectView;

import javax.inject.Singleton;

/**
 * @author IoaN, 11/20/12 6:49 PM
 */
@Singleton
public class SearchResultFragment extends RoboListFragment {
    @InjectView(R.id.searchResultsView)
    private LinearLayout searchResultsView;
    @Inject
    private PlacesSearchService placesSearchService;
    @Inject
    private AnimationUtil animationUtil;
    @Inject
    private AppState appState;
    @Inject
    private LocationProvider locationProvider;
    @Inject
    private Context context;
    @Inject
    private EventBus eventBus;
    private PlaceEfficientAdapter placeEfficientAdapter;
    private ProgressDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_list_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        createListView();
        createLoadingDialog();
        initPlacesEfficientAdapter();
        if (locationProvider.getLocation().getLatitude() == 0 && locationProvider.getLocation().getLongitude() == 0) {
            Toast.makeText(getActivity().getBaseContext(), R.string.searching_gps_satellites_toast, 2);
        }
        if (appState.getFoundPlacesList() != null && appState.getFoundPlacesList().getPlaceList() != null) {
            placeEfficientAdapter.clear();
            placeEfficientAdapter.addAll(appState.getFoundPlacesList().getPlaceList());
            for (Place place : appState.getFoundPlacesList().getPlaceList()) {
                if (Objects.equal(place.getId(),appState.getLastSelectedPlaceDetails().getId())) {
                    getListView().setItemChecked(appState.getFoundPlacesList().getPlaceList().indexOf(place), true);
                }
            }
        }
    }

    private void createListView() {
        getListView().setLayoutAnimation(animationUtil.getListAnimation());
        getListView().setOnScrollListener(initScrollListener());
        View selectedPlaceDetailsLandscapeView = getActivity().findViewById(R.id.selectedPlaceDetailsFragment);
        if (selectedPlaceDetailsLandscapeView != null && selectedPlaceDetailsLandscapeView.getVisibility() == View.VISIBLE) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        } else {

        }
    }

    @Override
    public void onListItemClick(ListView l, View v,final int position, long id) {
        View selectedPlaceDetailsLandscapeView = getActivity().findViewById(R.id.selectedPlaceDetailsFragment);
        if (selectedPlaceDetailsLandscapeView != null && selectedPlaceDetailsLandscapeView.getVisibility() == View.VISIBLE) {
            getListView().setItemChecked(position, true);
            eventBus.post(new PlaceSelectedEvent((Place) getListView().getItemAtPosition(position)));
        } else {
            placesSearchService.loadPlaceDetails((Place) getListView().getItemAtPosition(position), new PlacesSearchListener() {
                @Override
                public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                    showMap();
                }
            });
        }
    }

    private void showMap() {
        Intent intent = new Intent(getActivity().getBaseContext(), MapResultsActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    private AbsListView.OnScrollListener initScrollListener() {
        return new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0 && (visibleItemCount + firstVisibleItem) == totalItemCount) {
                    if (!appState.isRequestIsInProgress()) {
                        appState.setRequestIsInProgress(true);
                        loadMorePlaces();
                    }
                }
            }
        };
    }

    public boolean doSearch(String searchQuery) {
        if (locationProvider.getLocation().getLatitude() == 0 && locationProvider.getLocation().getLongitude() == 0) {
            return false;
        }
        initPlacesEfficientAdapter();
        appState.setStartSearchButtonShow(false);
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            showLoading(getBaseContext().getString(R.string.search_places_loading_window));
            appState.setRequestIsInProgress(true);
            searchResultsView.scrollTo(0, 0);
            appState.setRequestIsInProgress(false);
            placeEfficientAdapter.clear();
            placesSearchService.searchPlaces(locationProvider, searchQuery, new PlacesSearchListener() {
                @Override
                public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                    if (placesApiResponseEntity.getPlaceList() != null) {
                        placeEfficientAdapter.addAll(placesApiResponseEntity.getPlaceList());
                        searchResultsView.animate();
                    }
                    loadingDialog.hide();
                }
            });
        } else {
            loadingDialog.hide();
        }
        return true;
    }

    private void loadMorePlaces() {
        String loadingMessageToDisplay = getBaseContext().getString(R.string.search_places_loading_window);
        if (placeEfficientAdapter.getCount() < 40) {
            loadingMessageToDisplay = getString(R.string.search_places_loading_window_loading_more);
        } else if (placeEfficientAdapter.getCount() < 60) {
            loadingMessageToDisplay = getString(R.string.search_places_loading_window_loading_even_more);
        }
        showLoading(loadingMessageToDisplay);
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            placesSearchService.loadMorePlaces(new PlacesSearchListener() {
                @Override
                public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                    if (placesApiResponseEntity == null) {
                        //  Toast.makeText(getBaseContext(), R.string.no_more_results_to_display_toast, 1).show();
                    } else {
                        if (placesApiResponseEntity.getPlaceList() != null) {
                            placeEfficientAdapter.addAll(placesApiResponseEntity.getPlaceList());
                        }
                    }
                    loadingDialog.hide();
                    appState.setRequestIsInProgress(false);
                }
            });
        } else {
            appState.setRequestIsInProgress(false);
            loadingDialog.hide();
        }
    }

    private void createLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = new ProgressDialog(getActivity());
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCancelable(false);
        }
    }

    private void showLoading(String message) {
        loadingDialog.setMessage(message);
        loadingDialog.show();
    }

    private void initPlacesEfficientAdapter() {
        if (placeEfficientAdapter == null && locationProvider.getLocation().getLatitude() != 0 && locationProvider.getLocation().getLongitude() != 0) {
            placeEfficientAdapter = new PlaceEfficientAdapter(getBaseContext(), locationProvider.getLocation());
            getListView().setAdapter(placeEfficientAdapter);
        }
    }

    private Context getBaseContext() {
        return context;
    }


}
