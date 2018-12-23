package com.ladsuematsu.capstoneproject.core.adapter;

public interface DayListenerObserver {

    interface HolderObserver {

        void fillWeekDay(String weekDay);

        void fillHours(String hourStart, String hourEnd);

    }

    interface HolderListener {
        void bindHolder(int itemPosition, HolderObserver observer);

        void onWeekEdit(int itemPosition);

        void setOnCheckToggle(boolean checked);
    }

}
