package com.artigile.android.aroundme;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.*;
import android.widget.*;
import com.artigile.android.aroundme.api.model.PlacesApiResponseEntity;
import com.artigile.android.aroundme.app.LocationProvider;
import com.artigile.android.aroundme.app.PlaceEfficientAdapter;
import com.artigile.android.aroundme.app.PlacesSearchListener;
import com.artigile.android.aroundme.app.fragment.PlaceDetailsFragment;
import com.artigile.android.aroundme.app.fragment.SearchResultFragment;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.AbsListView.OnScrollListener;

@Singleton
public class GooglePlaces extends RoboFragmentActivity implements SearchView.OnQueryTextListener {

    @Inject
    private LocationProvider locationProvider;
    @Inject
    private PlacesSearchService placesSearchService;
    @Inject
    private AppState appState;
    @InjectFragment(R.id.searchResultsFragment)
    private SearchResultFragment searchResultFragment;
    @InjectFragment(R.id.selectedPlaceDetailsFragment)
    private PlaceDetailsFragment selectedPlaceDetailsFragment;
    @InjectView(R.id.searchResultsView)
    private LinearLayout searchResultsView;
    @InjectView(R.id.doSearchMainButton)
    private ImageView doSearchMainButton;
    private LocationManager mlocManager;
    private PlaceEfficientAdapter placeEfficientAdapter;
    private ProgressDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results_page);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        createListView();
        createLoadingDialog();
        if (locationProvider.getLocation().getLatitude() == 0 && locationProvider.getLocation().getLongitude() == 0) {
            Toast.makeText(getBaseContext(), R.string.searching_gps_satellites_toast, 2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doSearchMainButton.setVisibility(appState.isStartSearchButtonShow() ? VISIBLE : INVISIBLE);
        initPlacesEfficientAdapter();
        if (appState.getFoundPlacesList() != null && appState.getFoundPlacesList().getPlaceList() != null) {
            placeEfficientAdapter.clear();
            placeEfficientAdapter.addAll(appState.getFoundPlacesList().getPlaceList());
        }
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationProvider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mlocManager.removeUpdates(locationProvider);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void showAllOnMap(MenuItem v) {
        appState.setSinglePlaceToDisplayOnMap(appState.getFoundPlacesList());
        showMap();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void loadMorePlaces() {
        String loadingMessageToDisplay = getBaseContext().getString(R.string.search_places_loading_window);
        if (placeEfficientAdapter.getCount() < 40) {
            loadingMessageToDisplay = getString(R.string.search_places_loading_window_loading_more);
        } else if (placeEfficientAdapter.getCount() < 60) {
            loadingMessageToDisplay = getString(R.string.search_places_loading_window_loading_even_more);
        }
        showLoading(loadingMessageToDisplay);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return false;
    }

    public void doFirstSearch(View v) {
        doSearch("");
    }

    public void doSearchAll(MenuItem v) {
        doSearch("");
    }

    private void doSearch(String query) {
        if (!searchResultFragment.doSearch(query)) {
            Toast.makeText(getBaseContext(), R.string.search_please_wait_for_better_gps_signal, 10).show();
        } else {
            doSearchMainButton.setVisibility(INVISIBLE);
        }
    }

    private void initPlacesEfficientAdapter() {
        if (placeEfficientAdapter == null && locationProvider.getLocation().getLatitude() != 0 && locationProvider.getLocation().getLongitude() != 0) {
            placeEfficientAdapter = new PlaceEfficientAdapter(getBaseContext(), locationProvider.getLocation());
            //listView.setAdapter(placeEfficientAdapter);
        }
    }

    private void createLoadingDialog() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCancelable(false);
    }

    private void createListView() {
      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                placesSearchService.loadPlaceDetails((Place) listView.getItemAtPosition(position), new PlacesSearchListener() {
                    @Override
                    public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                        showMap();
                    }
                });
            }
        });
        listView.setLayoutAnimation(getListAnimation());
        listView.setOnScrollListener(initScrollListener());*/
    }

    private void showMap() {
        Intent intent = new Intent(getBaseContext(), MapResultsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    private LayoutAnimationController getListAnimation() {
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(100);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(200);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        return controller;
    }

    private OnScrollListener initScrollListener() {
        return new OnScrollListener() {
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

    private void showLoading(String message) {
        loadingDialog.setMessage(message);
        loadingDialog.show();
    }


}

