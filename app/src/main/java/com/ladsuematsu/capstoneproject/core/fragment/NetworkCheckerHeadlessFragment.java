package com.ladsuematsu.capstoneproject.core.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Partial implementation reference:
 * https://stackoverflow.com/questions/15698790/broadcast-receiver-for-checking-internet-connection-in-android-app
 */
public class NetworkCheckerHeadlessFragment extends Fragment  {

    public static final String DEFAULT_TAG = NetworkCheckerHeadlessFragment.class.getSimpleName();

    private final static String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    private NetworkCheckerCallback callback;

    BroadcastReceiver networkBroadcastReceiver =  new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)
                    && action.equals(ACTION_CONNECTIVITY_CHANGE)) {
                checkNetworkStatus();
            }

        }
    };

    private ConnectivityManager connectivityManager;

    public static NetworkCheckerHeadlessFragment getInstance() {
        return new NetworkCheckerHeadlessFragment();
    }

    public NetworkCheckerHeadlessFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NetworkCheckerCallback) {
            callback = (NetworkCheckerCallback) context;
        } else {
            throw new IllegalArgumentException("Activity implement NetworkCheckerHeadlessFragment.NetworkCheckerCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    @Override
    public void onResume() {
        super.onResume();

        registerReceivers();
    }

    @Override
    public void onStop() {
        unregisterReceivers();

        super.onStop();
    }

    @Override
    public void onDestroy() {
        callback = null;

        super.onDestroy();
    }

    private void registerReceivers() {
        getContext().registerReceiver(networkBroadcastReceiver, new IntentFilter(ACTION_CONNECTIVITY_CHANGE));
    }

    public void unregisterReceivers() {
        getContext().unregisterReceiver(networkBroadcastReceiver);
    }

    public void checkNetworkStatus() {

        if (hasConnection()) {
            callback.onNetworkActive();
        } else {
            callback.onNoNetwork();
        }

    }

    public boolean  hasConnection() {

        final NetworkInfo wifi = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final NetworkInfo mobile = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifi != null && wifi.isAvailable() && wifi.isConnected())
                 || (mobile != null &&  mobile.isAvailable() && mobile.isConnected());
    }

    public interface NetworkCheckerCallback {

        void onNetworkActive();
        void onNoNetwork();

    }

}
