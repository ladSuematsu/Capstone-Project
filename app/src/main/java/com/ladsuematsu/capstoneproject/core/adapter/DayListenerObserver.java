package com.ladsuematsu.capstoneproject.core.adapter;

public interface DayListenerObserver {

    int ITEM_COUNT = 13;

    int TEXT_EDIT_FIELDS = 0;

    int HEADER_CHECK_ITEMS = 1;
    int HOME_DELIVERY_CHECKBOX = 2;
    int ANIMAL_FRIENDLY_CHECKBOX = 3;
    int DISABLED_PEOPLE_FACILITIES_CHECKBOX = 4;

    int HEADER_WEEKDAY_TIMES = 5;
    int SUNDAY = 6;
    int MONDAY = 7;
    int THURSDAY = 8;
    int WEDNESDAY = 9;
    int TUESDAY = 10;
    int FRIDAY = 11;

    int SATURDAY = 12;

    interface HolderObserver {

        void fillWeekDay(String weekDay);

        void fillHours(int weekCode, String hourStart, String hourEnd);

    }

    interface CheckableObserver {


        void setLabel(String labelText);

        void setCheckable(int code, boolean checked);

    }

    interface TextfieldObserver {

        void setPlaceName(String placeName);

        void setAddress(String address);

        void setPhoneNumber(String phoneNumber);

    }

    interface HolderListener {

        void bindHolder(int itemPosition, TextfieldObserver observer);
        void bindHolder(int itemPosition, HolderObserver observer);
        void bindHolder(int itemPosition, CheckableObserver observer);


        void onAddressSearch();

        void onEditName(String name);

        void onEditPhoneNumber(String phoneNumber);

        void onWeekEdit(int itemPosition, int weekCode);
        int HOME_DELIVERY_CHECKBOX = 1;
        int ANIMAL_FRIENDLY_CHECKBOX = 2;
        int DISABLED_PEOPLE_FACILITIES_CHECKBOX = 3;

        void setOnCheckToggle(int checkCode, boolean checked);
    }

}
