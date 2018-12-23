package com.ladsuematsu.capstoneproject.newplace.mvp.presenter;

import com.ladsuematsu.capstoneproject.core.adapter.DayListenerObserver;
import com.ladsuematsu.capstoneproject.core.data.adapter.ApiPlaceAdapter;
import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.core.mvp.Mvp;
import com.ladsuematsu.capstoneproject.core.mvp.presenter.MvpPresenter;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;

import java.util.HashMap;
import java.util.Map;


public class NewPlacePresenter implements Mvp.Presenter<NewPlaceMvp.View>, DayListenerObserver.HolderListener {
    private MvpPresenter<NewPlaceMvp.View> presenterHelper = new MvpPresenter<>();
    private DataProvider<PlaceEntry, String> placeProvider = AppComponent.getInstance().getPlaceRepository();
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

        PlaceEntry placeEntry = new PlaceEntry(selectedPlaceAdapter.getId(),
                name,
                selectedPlaceAdapter.getAddress(),
                selectedPlaceAdapter.getLatitude(),
                selectedPlaceAdapter.getLongitude());

        placeProvider.create(placeEntry);

        presenterHelper.getView().onPlaceSavedSuccess();
    }

    Map<Integer, String[]> hoursMapping = new HashMap<>();

    @Override
    public void bindHolder(int itemPosition, DayListenerObserver.HolderObserver observer) {
        String hourStart = "";
        String hourEnd = "";

        if (hoursMapping.containsKey(itemPosition)) {
            String[] hours = hoursMapping.get(itemPosition);

            assert hours != null;
            hourStart = hours[0];
            hourEnd = hours[1];
        }


        observer.fillHours(hourStart, hourEnd);
        observer.fillWeekDay(null);
    }

    @Override
    public void onWeekEdit(int itemPosition) {

    }

    @Override
    public void setOnCheckToggle(boolean checked) {

    }
}