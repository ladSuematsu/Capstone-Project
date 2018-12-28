package com.ladsuematsu.capstoneproject.core.entity;

import com.google.firebase.database.Exclude;

public class WeekTime {
    @Exclude public static final int SUNDAY_CODE = 1;
    @Exclude public static final int MONDAY_CODE = 2;
    @Exclude public static final int THURSDAY_CODE = 3;
    @Exclude public static final int WEDNESDAY_CODE = 4;
    @Exclude public static final int TUESDAY_CODE = 5;
    @Exclude public static final int FRIDAY_CODE = 6;
    @Exclude public static final int SATURDAY_CODE = 7;
    @Exclude public static final String TIME_SEPARATOR = ":";

    private String uid;
    private int weekDayCode;
    private String startTime;
    private String endTime;

    public WeekTime() { }

    public WeekTime(int weekDayCode) {
        this.weekDayCode = weekDayCode;
    }

    public WeekTime(WeekTime weekTime) {
        this.weekDayCode = weekTime.getWeekDayCode();
        this.startTime = weekTime.getStartTime();
        this.endTime = weekTime.getEndTime();
    }

    public void setPlaceUid(String uid) {
        this.uid = uid;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUid() {
        return uid;
    }

    public int getWeekDayCode() {
        return weekDayCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "WeekTime{" +
                "weekDayCode=" + weekDayCode +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
