package com.artigile.android.aroundme.app.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.artigile.android.aroundme.R;
import com.artigile.android.aroundme.app.*;
import com.artigile.android.aroundme.app.event.PlaceSelectedEvent;
import com.artigile.android.aroundme.app.util.AnimationUtil;
import com.artigile.android.aroundme.app.util.UiUtil;
import com.artigile.android.aroundme.placesapi.model.Place;
import com.artigile.android.aroundme.placesapi.model.PlacesApiResponseEntity;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
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
    private final String IS_LOAD_ING_SHOWING_KEY = "LOADING_SHOWING_KEY";
    @InjectView(R.id.searchResultsView)
    private LinearLayout searchResultsView;
    @Inject
    private PlacesSearchService placesSearchService;
    @Inject
    private AnimationUtil animationUtil;
    @Inject
    private AppState appState;
    @Inject
    private AppLocationProvider appLocationProvider;
    @Inject
    private Context context;
    @Inject
    private EventBus eventBus;
    @Inject
    private UiUtil uiUtil;
    private PlaceEfficientAdapter placeEfficientAdapter;
    private ProgressDialog loadingDialog;

    //    public static final String IS_LOADING_SHOWING_KEY="LOADING_SHIWONG_KEY";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_list_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initOnResume();
    }

  /*  @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_LOADING_SHOWING_KEY,loadingDialog.isShowing());
        loadingDialog.dismiss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createLoadingDialog();
        if(savedInstanceState!=null&&Objects.firstNonNull(savedInstanceState.getBoolean(IS_LOADING_SHOWING_KEY),false)){
            showLoading(getActivity().getString(R.string.place_details_loading_label));
        }
    }*/

    private void initOnResume() {
        createListView();
        initPlacesEfficientAdapter();
        if (appLocationProvider.getLocation().getLatitude() == 0 && appLocationProvider.getLocation().getLongitude() == 0) {
            Toast.makeText(getActivity().getBaseContext(), R.string.searching_gps_satellites_toast, 2);
        }
        if (appState.getFoundPlacesList() != null && appState.getFoundPlacesList().getPlaceList() != null) {
            placeEfficientAdapter.clear();
            placeEfficientAdapter.addAll(appState.getFoundPlacesList().getPlaceList());
            for (Place place : appState.getFoundPlacesList().getPlaceList()) {
                if (appState.getLastSelectedPlaceDetails() != null && Objects.equal(place.getId(), appState.getLastSelectedPlaceDetails().getId())) {
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
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        View selectedPlaceDetailsLandscapeView = getActivity().findViewById(R.id.selectedPlaceDetailsFragment);
        if (selectedPlaceDetailsLandscapeView != null && selectedPlaceDetailsLandscapeView.getVisibility() == View.VISIBLE) {
            getListView().setItemChecked(position, true);
            eventBus.post(new PlaceSelectedEvent((Place) getListView().getItemAtPosition(position)));
        } else {
            uiUtil.showPlaceOnMap(SearchResultFragment.this);
            appState.setSinglePlaceToDisplayOnMap((Place) getListView().getItemAtPosition(position));
        }
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

    public boolean doSearch(final String searchQuery) {
        if (appLocationProvider.getLocation().getLatitude() == 0 && appLocationProvider.getLocation().getLongitude() == 0) {
            return false;
        }
        initPlacesEfficientAdapter();
        appState.setStartSearchButtonShow(false);
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            showLoading(context.getString(R.string.search_places_loading_window));
            appState.setRequestIsInProgress(true);
            searchResultsView.scrollTo(0, 0);
            appState.setRequestIsInProgress(false);
            placeEfficientAdapter.clear();
            placesSearchService.searchPlaces(appLocationProvider, searchQuery, new PlacesSearchListener() {
                @Override
                public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                    populateSearchResultsDataOnUi(placesApiResponseEntity, searchQuery);
                }
            });
        } else {
            hideLoading();
        }
        return true;
    }

    private void populateSearchResultsDataOnUi(PlacesApiResponseEntity placesApiResponseEntity, String searchQuery) {
        if (placesApiResponseEntity.getPlaceList() != null) {
            ((PlaceEfficientAdapter) getListView().getAdapter()).addAll(placesApiResponseEntity.getPlaceList());
        } else {
            String noResultsText;
            if (Strings.isNullOrEmpty(searchQuery)) {
                noResultsText = context.getString(R.string.search_places_nothing_found_in_area);
            } else {
                noResultsText = context.getString(R.string.search_places_nothing_found_by_query) + " \"" + searchQuery + "\"";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(noResultsText).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
        hideLoading();
    }

    private void loadMorePlaces() {
        String loadingMessageToDisplay = context.getString(R.string.search_places_loading_window);
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

                    } else {
                        if (placesApiResponseEntity.getPlaceList() != null) {
                            placeEfficientAdapter.addAll(placesApiResponseEntity.getPlaceList());
                        }
                    }
                    appState.setRequestIsInProgress(false);
                    hideLoading();
                }
            });
        } else {
            appState.setRequestIsInProgress(false);
            hideLoading();
        }
    }

    private void showLoading(final String message) {
        uiUtil.disableRotation(getActivity());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog = ProgressDialog.show(getActivity(), "", message);
                if (!loadingDialog.isShowing()) {
                    loadingDialog.setMessage(message);
                    loadingDialog.show();
                }
            }
        });
    }

    private void hideLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.hide();
                    loadingDialog.dismiss();
                }
            }
        });
        uiUtil.enableRotation(getActivity());
    }

    private void initPlacesEfficientAdapter() {
        if (placeEfficientAdapter == null && appLocationProvider.getLocation().getLatitude() != 0 && appLocationProvider.getLocation().getLongitude() != 0) {
            placeEfficientAdapter = new PlaceEfficientAdapter(context, appLocationProvider.getLocation());
            getListView().setAdapter(placeEfficientAdapter);
        }
    }


}
