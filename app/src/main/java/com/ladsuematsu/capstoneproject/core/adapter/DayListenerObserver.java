package com.ladsuematsu.capstoneproject.core.adapter;

public interface DayListenerObserver {

    interface HolderObserver {

        void fillWeekDay(String weekDay);

        void fillHours(String hourStart, String hourEnd);

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

        void onWeekEdit(int itemPosition);
        int HOME_DELIVERY_CHECKBOX = 1;
        int ANIMAL_FRIENDLY_CHECKBOX = 2;
        int DISABLED_PEOPLE_FACILITIES_CHECKBOX = 3;

        void setOnCheckToggle(int checkCode, boolean checked);
    }

}
