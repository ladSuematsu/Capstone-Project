package com.ladsuematsu.capstoneproject.newplace.mvp.presenter;

import com.ladsuematsu.capstoneproject.core.data.adapter.ApiPlaceAdapter;
import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.entity.Place;
import com.ladsuematsu.capstoneproject.core.mvp.Mvp;
import com.ladsuematsu.capstoneproject.core.mvp.presenter.MvpPresenter;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;


public class NewPlacePresenter implements Mvp.Presenter<NewPlaceMvp.View> {
    private MvpPresenter<NewPlaceMvp.View> presenterHelper = new MvpPresenter<>();
    private DataProvider<Place, String> placeProvider = AppComponent.getInstance().getPlaceRepository();
    private ApiPlaceAdapter selectedPlaceAdapter;

    public NewPlacePresenter() {

    }

    @Override
    public void attachView(NewPlaceMvp.View view) {
        presenterHelper.attachView(view);
    }

    @Override
    public void detachView() {
        presenterHelper.detachView();
    }

    public void onSelectedPlace(ApiPlaceAdapter placeAdapter) {
        if(!presenterHelper.isViewAttached()) { return; }

        this.selectedPlaceAdapter = placeAdapter;

        presenterHelper.getView().onSelectedName(placeAdapter.getName());
        presenterHelper.getView().onSelectedAddress(placeAdapter.getAddress());
    }

    public void savePlace(String name) {
        if(!presenterHelper.isViewAttached()) { return; }

        com.ladsuematsu.capstoneproject.core.entity.Place place = new com.ladsuematsu.capstoneproject.core.entity.Place(selectedPlaceAdapter.getId(),
                name,
                selectedPlaceAdapter.getAddress(),
                selectedPlaceAdapter.getLatitude(),
                selectedPlaceAdapter.getLongitude());

        placeProvider.create(place);

        presenterHelper.getView().onPlaceSavedSuccess();
    }
}