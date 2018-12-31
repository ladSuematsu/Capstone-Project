package com.ladsuematsu.capstoneproject.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressLocationFetchService extends IntentService {

    public static final String TAG = AddressLocationFetchService.class.getSimpleName();

    public static final int CODE_SUCCESS = 1;
    public static final int CODE_FAILURE = -1;

    public static final int MAX_GEOCODER_RESULTS = 10;

    public static final String RESULT_DATA = "result_data";

    public static final String LOCATION_DATA_RECEIVER = "com.ladsuematsu.capstoneproject.service.action.LOCATION_RECEIVER";
    public static final String LOCATION_DATA_EXTRA = "com.ladsuematsu.capstoneproject.service.action.LOCATION_INFO";

    private ResultReceiver resultReceiver;

    public AddressLocationFetchService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {

            ArrayList<Address> resultData = getAddresses(intent);
            deliverResult(resultData);

        } catch (Exception e) {

            Log.e(TAG,"Something went wrong", e);

            deliverErrorResult();
        }

    }

    private ArrayList<Address> getAddresses(Intent intent) throws IOException {

        if (!intent.hasExtra(LOCATION_DATA_RECEIVER) || !intent.hasExtra(LOCATION_DATA_EXTRA)) {
            return null;
        }

        resultReceiver = intent.getParcelableExtra(LOCATION_DATA_RECEIVER);
        String addressPart = intent.getStringExtra(LOCATION_DATA_EXTRA);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = geocoder.getFromLocationName(addressPart, MAX_GEOCODER_RESULTS);

        if (addresses == null) {
            throw new IllegalStateException("Could not get response from Geocoder API.");
        }

        return addresses.isEmpty() ? new ArrayList<Address>() : new ArrayList(addresses);
    }

    private void deliverResult(ArrayList<Address> resultData) {
        Bundle resultBundle = new Bundle();
        resultBundle.putParcelableArrayList(RESULT_DATA, resultData);

        resultReceiver.send(CODE_SUCCESS, resultBundle);
    }
    private void deliverErrorResult() {
        Bundle resultBundle = new Bundle();
        resultBundle.putParcelableArrayList(RESULT_DATA, new ArrayList<Address>());

        resultReceiver.send(CODE_FAILURE, resultBundle);
    }

}
