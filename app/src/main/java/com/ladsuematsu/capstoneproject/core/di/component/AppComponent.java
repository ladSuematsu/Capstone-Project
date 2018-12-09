package com.ladsuematsu.capstoneproject.core.di.component;

import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.di.module.AuthModule;
import com.ladsuematsu.capstoneproject.core.di.module.PlaceModule;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;

import java.lang.ref.WeakReference;

public class AppComponent {
    private static AppComponent appComponent;

    private WeakReference<AuthModule> authModule;
    private WeakReference<PlaceModule> placeModule;

    public static AppComponent getInstance() {
        return appComponent;
    }

    public AppComponent() {
        appComponent = this;
    }

    public AuthModule getAuthRepository() {
        if (authModule == null || authModule.get() == null) {
            authModule = new WeakReference<>(new AuthModule());
        }

        return authModule.get();
    }

    public DataProvider<PlaceEntry, String> getPlaceRepository() {
        if (placeModule == null || placeModule.get() != null) {
            placeModule = new WeakReference<>(new PlaceModule());
        }

        return placeModule.get();
    }

}
