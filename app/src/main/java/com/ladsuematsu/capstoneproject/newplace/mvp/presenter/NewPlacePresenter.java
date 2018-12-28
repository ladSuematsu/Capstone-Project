package com.ladsuematsu.capstoneproject.newplace.mvp.presenter;

import android.util.Log;

import com.ladsuematsu.capstoneproject.core.adapter.DayListenerObserver;
import com.ladsuematsu.capstoneproject.core.data.adapter.ApiPlaceAdapter;
import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.core.entity.WeekTime;
import com.ladsuematsu.capstoneproject.core.mvp.Mvp;
import com.ladsuematsu.capstoneproject.core.mvp.presenter.MvpPresenter;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class NewPlacePresenter implements Mvp.Presenter<NewPlaceMvp.View>, DayListenerObserver.HolderListener {
    private static final int IDLE_TIME_PICKER_CODE = 0;
    private static final int START_TIME_PICKER_CODE = 1;
    private static final int END_TIME_PICKER_CODE = 2;

    private MvpPresenter<NewPlaceMvp.View> presenterHelper = new MvpPresenter<>();
    private DataProvider<PlaceEntry, String> placeProvider = AppComponent.getInstance().getPlaceRepository();
    private ApiPlaceAdapter selectedPlaceAdapter;

    private String placeName = "";
    private String placePhoneNumber ="";
    private final HashMap<Integer, Boolean> serviceCheck = new HashMap<>();
    private final HashMap<Integer, WeekTime> openWeekdayHours = new HashMap<>();


    private int editItemPosition;
    private int pickerCode = IDLE_TIME_PICKER_CODE;
    private WeekTime weekDay;

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

        PlaceEntry placeEntry = new PlaceEntry(selectedPlaceAdapter.getId(),
                placeName,
                placePhoneNumber,
                selectedPlaceAdapter.getAddress(),
                selectedPlaceAdapter.getLatitude(),
                selectedPlaceAdapter.getLongitude(),
                serviceCheck.containsKey(HOME_DELIVERY_CHECKBOX) ? serviceCheck.get(HOME_DELIVERY_CHECKBOX) : false,
                serviceCheck.containsKey(ANIMAL_FRIENDLY_CHECKBOX) ? serviceCheck.get(ANIMAL_FRIENDLY_CHECKBOX) : false,
                serviceCheck.containsKey(DISABLED_PEOPLE_FACILITIES_CHECKBOX) ? serviceCheck.get(DISABLED_PEOPLE_FACILITIES_CHECKBOX) : false
        );

        placeEntry.setWeekTimes(new ArrayList<>(openWeekdayHours.values()));

        placeProvider.create(placeEntry);
        presenterHelper.getView().onPlaceSavedSuccess();
    }

    @Override
    public void bindHolder(int itemPosition, DayListenerObserver.TextfieldObserver observer) {
        observer.setPlaceName(placeName);
        observer.setPhoneNumber(placePhoneNumber);

        observer.setAddress(selectedPlaceAdapter != null ? selectedPlaceAdapter.getAddress() : "");
    }

    @Override
    public void bindHolder(int itemPosition, DayListenerObserver.HolderObserver observer) {

        int weekCode;
        switch(itemPosition) {
            case DayListenerObserver.SUNDAY:
                weekCode = WeekTime.SUNDAY_CODE;
                break;

            case DayListenerObserver.MONDAY:
                weekCode = WeekTime.MONDAY_CODE;
                break;

            case DayListenerObserver.THURSDAY:
                weekCode = WeekTime.THURSDAY_CODE;
                break;

            case DayListenerObserver.WEDNESDAY:
                weekCode = WeekTime.WEDNESDAY_CODE;
                break;

            case DayListenerObserver.TUESDAY:
                weekCode = WeekTime.TUESDAY_CODE;
                break;

            case DayListenerObserver.FRIDAY:
                weekCode = WeekTime.FRIDAY_CODE;
                break;

            case DayListenerObserver.SATURDAY:
                weekCode = WeekTime.SATURDAY_CODE;
                break;

            default:
                weekCode = 0;
        }

        String hourStart = "";
        String hourEnd = "";
        if (weekCode != 0 && openWeekdayHours.containsKey(weekCode)) {
            WeekTime weekTime = openWeekdayHours.get(weekCode);

            assert weekTime != null;
            hourStart = weekTime.getStartTime();
            hourEnd = weekTime.getEndTime();
        }

        observer.fillHours(weekCode, hourStart, hourEnd);
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
    public void onWeekEdit(int itemPosition, int weekCode) {
        if (!presenterHelper.isViewAttached()) { return; }

        int hour = 0;
        int minute = 0;

        if (openWeekdayHours.containsKey(weekCode)) {
            weekDay = new WeekTime(openWeekdayHours.get(weekCode));
            String startTime = weekDay.getStartTime();
            String[] hourPart = startTime.split(WeekTime.TIME_SEPARATOR);

            if (hourPart.length == 2) {
                hour = Integer.valueOf(hourPart[0]);
                minute = Integer.valueOf(hourPart[1]);
            }

        } else {
            weekDay = new WeekTime(weekCode);
        }

        pickerCode = START_TIME_PICKER_CODE;
        editItemPosition = itemPosition;
        presenterHelper.getView().onEditWeek(hour, minute);

    }

    @Override
    public void setOnCheckToggle(int checkCode, boolean checked) {
        serviceCheck.put(checkCode, checked);
    }

    public void onTimeSet(int hourOfDay, int minute) {
        String time = String.format(Locale.ROOT, "%02d" + WeekTime.TIME_SEPARATOR + "%02d", hourOfDay, minute);

        switch (pickerCode) {

            case START_TIME_PICKER_CODE:
                weekDay.setStartTime(time);

                Log.d("WEEK_TIME", "Time START: " + weekDay.toString());


                pickerCode = END_TIME_PICKER_CODE;
                presenterHelper.getView().onEditWeek(hourOfDay, minute);

                return;

            case END_TIME_PICKER_CODE:
                weekDay.setEndTime(time);
                Log.d("WEEK_TIME", "Time END: " + weekDay.toString());

                openWeekdayHours.put(weekDay.getWeekDayCode(), weekDay);

                pickerCode = IDLE_TIME_PICKER_CODE;

                presenterHelper.getView().refreshFields(editItemPosition);

                return;
        }

    }
}