package com.ladsuematsu.capstoneproject.overview.mvp.presenter;

import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.core.mvp.Mvp;
import com.ladsuematsu.capstoneproject.core.mvp.presenter.MvpPresenter;
import com.ladsuematsu.capstoneproject.overview.mvp.OverviewMvp;

import java.util.HashMap;
import java.util.Map;


public class OverviewPresenter implements Mvp.Presenter<OverviewMvp.View>  {
    private MvpPresenter<OverviewMvp.View> presenterHelper = new MvpPresenter<>();
    private final OverviewMvp.Model model;
    private Map<String, PlaceEntry> placesMap = new HashMap<>();

    private OverviewMvp.Model.Callback modelListener = new OverviewMvp.Model.Callback() {
        @Override
        public void onCurrentLocation(double latitude, double longitude) {
            presenterHelper.getView().addMarkerForCurrentLocation(latitude, longitude);
        }

        @Override
        public void onRefreshedPlace(PlaceEntry place) {
            boolean previouslyMapped = placesMap.containsKey(place.getUid());

            placesMap.put(place.getUid(), place);

            if (!previouslyMapped) {
                presenterHelper.getView().addMarker(place.getUid(), place.getLatitude(), place.getLongitude());
            } else {
                presenterHelper.getView().refreshMarker(place.getUid(), place.getLatitude(), place.getLongitude());
            }
        }

        @Override
        public void onPlaceExit(String placeKey) {
            presenterHelper.getView().removeMarker(placeKey);

            placesMap.remove(placeKey);
        }

        @Override
        public void onPlaceMoved(String key, double latitude, double longitude) {
        }
    };

    public OverviewPresenter(OverviewMvp.Model model) {
        this.model = model;
    }

    @Override
    public void attachView(OverviewMvp.View view) {
        model.attachCallback(modelListener);
        presenterHelper.attachView(view);
    }

    @Override
    public void detachView() {
        model.detachCallback();
        presenterHelper.detachView();
    }

    public void requestPlacesInVicinity() {
        model.refreshPlaces();
    }

    public void selectPlace(String placeKey) {
        if (!presenterHelper.isViewAttached()) {  return; }

        if (placesMap.containsKey(placeKey)) {
            PlaceEntry place = placesMap.get(placeKey);
            presenterHelper.getView().onSelectedPlace(place);
        }
    }
}