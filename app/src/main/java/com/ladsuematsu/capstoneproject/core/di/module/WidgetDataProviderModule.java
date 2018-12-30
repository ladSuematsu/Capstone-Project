package com.ladsuematsu.capstoneproject.core.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.ladsuematsu.capstoneproject.BuildConfig;
import com.ladsuematsu.capstoneproject.core.data.persistence.WidgetDataProvider;

public class WidgetDataProviderModule implements WidgetDataProvider {

    private static final String LAST_PLACE_UID = "last_place_uid";
    private static final String LAST_PLACE_NAME = "last_place_name";
    private static final String LAST_PLACE_ADDRESS = "last_place_address";
    private static final String LAST_PLACE_PHONE_NUMBER = "last_place_phone_number";

    private final SharedPreferences sharedPreferences;

    public WidgetDataProviderModule(Context context) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
    }


    @Override
    public String getLastPlaceUid() {
        return sharedPreferences.getString(LAST_PLACE_UID, "");
    }

    @Override
    public String getlastPlaceName() {
        return sharedPreferences.getString(LAST_PLACE_NAME, "");
    }

    @Override
    public String getLastPlaceAddress() {
        return sharedPreferences.getString(LAST_PLACE_ADDRESS, "");
    }

    @Override
    public String getLastPlacePhoneNumber() {
        return sharedPreferences.getString(LAST_PLACE_PHONE_NUMBER, "");
    }

    @Override
    public void setLastPlaceEntry(String uid, String name, String address, String phoneNumber) {
        sharedPreferences.edit()
                .putString(LAST_PLACE_UID, uid)
                .putString(LAST_PLACE_NAME, name)
                .putString(LAST_PLACE_ADDRESS, address)
                .putString(LAST_PLACE_PHONE_NUMBER, phoneNumber)
                .apply();
    }

}
