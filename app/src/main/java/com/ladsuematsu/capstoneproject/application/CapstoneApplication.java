package com.ladsuematsu.capstoneproject.application;

import android.app.Application;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.widget.PlaceWidgetUpdateReceiver;

public class CapstoneApplication extends Application {

    private LocalBroadcastManager localBroadcastManager;
    private PlaceWidgetUpdateReceiver placeWidgetReceiver;


    @Override
    public void onCreate() {
        super.onCreate();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        new AppComponent(this);

        initializeReceivers();
    }

    private void initializeReceivers() {
        placeWidgetReceiver = new PlaceWidgetUpdateReceiver();
        localBroadcastManager.registerReceiver(placeWidgetReceiver, new IntentFilter(PlaceWidgetUpdateReceiver.DEFAULT_ACTION));
    }

    @Override
    public void onTerminate() {
        localBroadcastManager.unregisterReceiver(placeWidgetReceiver);

        super.onTerminate();
    }
}
