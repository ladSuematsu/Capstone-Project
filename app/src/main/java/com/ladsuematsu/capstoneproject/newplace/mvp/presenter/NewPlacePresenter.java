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
import java.util.List;
import java.util.Locale;


public class NewPlacePresenter implements Mvp.Presenter<NewPlaceMvp.View>, DayListenerObserver.HolderListener {
    private static final int IDLE_TIME_PICKER_CODE = 0;
    private static final int START_TIME_PICKER_CODE = 1;
    private static final int END_TIME_PICKER_CODE = 2;

    private final DataProvider.ProviderListener<PlaceEntry> listener = new DataProvider.ProviderListener<PlaceEntry>() {
        @Override
        public void onSuccess(List<PlaceEntry> result) {
            if (!presenterHelper.isViewAttached()) { return; }

            PlaceEntry placeEntry = result.get(0);

            loadPlaceEntry(placeEntry);

            presenterHelper.getView().refreshFields();
        }

        @Override
        public void onFailure() {

        }
    };

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
    private String key;

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

    public void setLoadParameters(String key) {
        this.key = key;
    }

    public void load() {
        if (this.key != null && !this.key.isEmpty()) {

            placeProvider.fetch(this.key, listener);

        }
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

        PlaceEntry placeEntry = new PlaceEntry(key,
                placeName,
                placePhoneNumber,
                selectedPlaceAdapter.getAddress(),
                selectedPlaceAdapter.getLatitude(),
                selectedPlaceAdapter.getLongitude(),
                serviceCheck.containsKey(DayListenerObserver.HOME_DELIVERY_CHECKBOX) ? serviceCheck.get(DayListenerObserver.HOME_DELIVERY_CHECKBOX) : false,
                serviceCheck.containsKey(DayListenerObserver.ANIMAL_FRIENDLY_CHECKBOX) ? serviceCheck.get(DayListenerObserver.ANIMAL_FRIENDLY_CHECKBOX) : false,
                serviceCheck.containsKey(DayListenerObserver.DISABLED_PEOPLE_FACILITIES_CHECKBOX) ? serviceCheck.get(DayListenerObserver.DISABLED_PEOPLE_FACILITIES_CHECKBOX) : false
        );

        placeEntry.setWeekTimes(openWeekdayHours);

        placeProvider.create(placeEntry);
        presenterHelper.getView().onPlaceSavedSuccess();
    }

    public void editPlace() {
        if(!presenterHelper.isViewAttached()) { return; }

        presenterHelper.getView().requestEdit(key);
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

    private void loadPlaceEntry(final PlaceEntry placeEntry) {
        placeName = placeEntry.getName();
        placePhoneNumber = placeEntry.getPhoneNumber();
        serviceCheck.put(DayListenerObserver.HOME_DELIVERY_CHECKBOX, placeEntry.getDoesDoorDelivery());
        serviceCheck.put(DayListenerObserver.ANIMAL_FRIENDLY_CHECKBOX, placeEntry.getIsAnimalFriendly());
        serviceCheck.put(DayListenerObserver.DISABLED_PEOPLE_FACILITIES_CHECKBOX, placeEntry.getHasFacilitiesForDisabledPeople());
        selectedPlaceAdapter = new ApiPlaceAdapter() {
            @Override
            public String getId() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getPhoneNumber() {
                return null;
            }

            @Override
            public String getAddress() {
                return placeEntry.getAddress();
            }

            @Override
            public double getLatitude() {
                return placeEntry.getLatitude();
            }

            @Override
            public double getLongitude() {
                return placeEntry.getLongitude();
            }
        };

        openWeekdayHours.putAll(placeEntry.getWeekTimes());

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