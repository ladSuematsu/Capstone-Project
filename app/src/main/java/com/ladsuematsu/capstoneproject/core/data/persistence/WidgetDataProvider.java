package com.ladsuematsu.capstoneproject.core.data.persistence;

public interface WidgetDataProvider {
    String getLastPlaceUid();

    String getlastPlaceName();

    String getLastPlaceAddress();

    String getLastPlacePhoneNumber();

    void setLastPlaceUid(String uid, String name, String address, String phoneNumber);
}
