package com.ladsuematsu.capstoneproject.search.mvp.presenter;

import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.core.mvp.Mvp;
import com.ladsuematsu.capstoneproject.core.mvp.presenter.MvpPresenter;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;
import com.ladsuematsu.capstoneproject.search.mvp.SearchPlaceMvp;

import java.util.ArrayList;
import java.util.List;


public class SearchPlacePresenter implements Mvp.Presenter<SearchPlaceMvp.View> {
    private MvpPresenter<SearchPlaceMvp.View> presenterHelper = new MvpPresenter<>();
    private List<PlaceEntry> currentResult = new ArrayList<>();

    public SearchPlacePresenter() {

    }

    @Override
    public void attachView(SearchPlaceMvp.View view) {
        presenterHelper.attachView(view);
    }

    @Override
    public void detachView() {
        presenterHelper.detachView();
    }

    public void search(String searchKey) {
        if(!presenterHelper.isViewAttached()) { return; }

        searchPlaces(searchKey);
    }

    private DataProvider<PlaceEntry, String> placeProvider = AppComponent.getInstance().getPlaceRepository();
    private void searchPlaces(String searchKey) {
        placeProvider.fetch(searchKey, new DataProvider.ProviderListener<PlaceEntry>() {
            @Override
            public void onSuccess(List<PlaceEntry> result) {
                if (!presenterHelper.isViewAttached()) { return; }

                currentResult = result;
                presenterHelper.getView().onResultRefresh();
            }

            @Override
            public void onFailure() {

            }
        });
    }

//    public void onSelectedPlace(ApiPlaceAdapter placeAdapter) {
//        if(!presenterHelper.isViewAttached()) { return; }
//
//        this.selectedPlaceAdapter = placeAdapter;
//
//        presenterHelper.getView().onSelectedName(placeAdapter.getName());
//        presenterHelper.getView().onSelectedAddress(placeAdapter.getAddress());
//    }

}