package com.artigile.android.placesapi;

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
import com.artigile.android.placesapi.api.model.Place;
import com.artigile.android.placesapi.api.model.PlacesApiResponseEntity;
import com.artigile.android.placesapi.app.LocationProvider;
import com.artigile.android.placesapi.app.PlaceEfficientAdapter;
import com.artigile.android.placesapi.app.PlacesSearchListener;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import roboguice.activity.RoboListActivity;
import roboguice.inject.InjectView;

@Singleton
public class GooglePlaces extends RoboListActivity implements SearchView.OnQueryTextListener {

    @Inject
    private LocationProvider locationProvider;
    @Inject
    private PlacesSearchService placesSearchService;
    @Inject
    private AppState appState;
    @InjectView(android.R.id.list)
    private ListView listView;
    @InjectView(R.id.placesSearchLoadingBar)
    private ProgressBar progressBar;
    @InjectView(R.id.searchResultsView)
    private LinearLayout searchResultsView;
    private LocationManager mlocManager;
    private PlaceEfficientAdapter placeEfficientAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        listView.setOnScrollListener(new

                                             AbsListView.OnScrollListener() {
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
                                             });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private void loadMorePlaces() {
        progressBar.setVisibility(View.VISIBLE);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            placesSearchService.loadMorePlaces(new PlacesSearchListener() {
                @Override
                public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                    if (placesApiResponseEntity == null) {
                        Toast.makeText(getBaseContext(), R.string.no_more_results_to_display_toast, 2).show();
                    } else {
                        if (placesApiResponseEntity.getPlaceList() != null) {
                            placeEfficientAdapter.addAll(placesApiResponseEntity.getPlaceList());
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    appState.setRequestIsInProgress(false);
                }
            });
        } else {
            appState.setRequestIsInProgress(false);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void doSearch(String searchQuery) {
    /*    if (locationProvider.getLatitude() == 0 && locationProvider.getLongitude() == 0) {
            Toast.makeText(getBaseContext(),"Please wait for the better GPS signal",10).show();
        } else {*/
        initPlacesEfficientAdapter();
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            progressBar.setVisibility(View.VISIBLE);
            appState.setRequestIsInProgress(true);
            searchResultsView.scrollTo(0,0);
            appState.setRequestIsInProgress(false);
            placesSearchService.searchPlaces(locationProvider, searchQuery, new PlacesSearchListener() {
                @Override
                public void onResultReadyAndAppStateUpdated(PlacesApiResponseEntity placesApiResponseEntity) {
                    placeEfficientAdapter.clear();
                    if (placesApiResponseEntity.getPlaceList() != null) {
                        placeEfficientAdapter.addAll(placesApiResponseEntity.getPlaceList());
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void initPlacesEfficientAdapter() {
        if (placeEfficientAdapter == null) {
            placeEfficientAdapter = new PlaceEfficientAdapter(getBaseContext(), locationProvider.getLocation());
            listView.setAdapter(placeEfficientAdapter);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void doSearchAll(MenuItem v) {
        doSearch("");
    }

    private void showMap() {
        Intent intent = new Intent(getBaseContext(), ShowAllPlacesOnMapActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
    }

    private LayoutAnimationController getListAnimation() {
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        return controller;
    }

}

