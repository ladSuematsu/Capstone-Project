package com.ladsuematsu.capstoneproject.application;

import android.app.Application;

import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;

public class CapstoneApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new AppComponent();
    }
}
