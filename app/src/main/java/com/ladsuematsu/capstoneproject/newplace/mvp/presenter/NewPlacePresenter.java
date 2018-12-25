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

    private String placeName = "";
    private String placePhoneNumber ="";
    private final HashMap<Integer, Boolean> serviceCheck = new HashMap<>();
    private final HashMap<Integer, String> openWeekdayHours = new HashMap<>();

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
        placeName = this.selectedPlaceAdapter.getName().isEmpty() ? placeName : this.selectedPlaceAdapter.getName();
        placePhoneNumber = this.selectedPlaceAdapter.getPhoneNumber().isEmpty() ? placePhoneNumber : this.selectedPlaceAdapter.getPhoneNumber();

        presenterHelper.getView().refreshFields();
    }

    public void savePlace() {
        if(!presenterHelper.isViewAttached()) { return; }

//        PlaceEntry placeEntry = new PlaceEntry(selectedPlaceAdapter.getId(),
//                name,
//                phoneNumber,
//                selectedPlaceAdapter.getAddress(),
//                selectedPlaceAdapter.getLatitude(),
//                selectedPlaceAdapter.getLongitude(),
//                serviceCheck.containsKey(HOME_DELIVERY_CHECKBOX) ? serviceCheck.get(HOME_DELIVERY_CHECKBOX) : false,
//                serviceCheck.containsKey(ANIMAL_FRIENDLY_CHECKBOX) ? serviceCheck.get(ANIMAL_FRIENDLY_CHECKBOX) : false,
//                serviceCheck.containsKey(DISABLED_PEOPLE_FACILITIES_CHECKBOX) ? serviceCheck.get(DISABLED_PEOPLE_FACILITIES_CHECKBOX) : false
//        );

//        placeProvider.create(placeEntry);
//        presenterHelper.getView().onPlaceSavedSuccess();
    }

    Map<Integer, String[]> hoursMapping = new HashMap<>();

    @Override
    public void bindHolder(int itemPosition, DayListenerObserver.TextfieldObserver observer) {
        observer.setPlaceName(placeName);
        observer.setPhoneNumber(placePhoneNumber);

        observer.setAddress(selectedPlaceAdapter != null ? selectedPlaceAdapter.getAddress() : "");
    }

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
    public void bindHolder(int itemPosition, DayListenerObserver.CheckableObserver observer) {
        switch (itemPosition) {
            case HOME_DELIVERY_CHECKBOX:

                break;
            case ANIMAL_FRIENDLY_CHECKBOX:

                break;
            case DISABLED_PEOPLE_FACILITIES_CHECKBOX:

                break;
        }

        observer.setCheckable(itemPosition, serviceCheck.containsKey(itemPosition) ? serviceCheck.get(itemPosition) : false);

    }

    @Override
    public void onAddressSearch() {
        if (!presenterHelper.isViewAttached()) { return; }

        presenterHelper.getView().onSearchAddress();
    }

    @Override
    public void onEditName(String name) {
        placeName = name;
    }

    @Override
    public void onEditPhoneNumber(String phoneNumber) {
        placePhoneNumber = phoneNumber;
    }

    @Override
    public void onWeekEdit(int itemPosition) {

    }

    @Override
    public void setOnCheckToggle(int checkCode, boolean checked) {
        serviceCheck.put(checkCode, checked);
    }
}