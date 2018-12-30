package com.ladsuematsu.capstoneproject.core.di.component;

import android.content.Context;

import com.ladsuematsu.capstoneproject.AuthWatcher;
import com.ladsuematsu.capstoneproject.core.data.persistence.WidgetDataProvider;
import com.ladsuematsu.capstoneproject.core.di.module.WidgetDataProviderModule;
import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.di.module.AuthModule;
import com.ladsuematsu.capstoneproject.core.di.module.FirebaseAuthWatcher;
import com.ladsuematsu.capstoneproject.core.di.module.GeoModule;
import com.ladsuematsu.capstoneproject.core.di.module.PlaceModule;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;

import java.lang.ref.WeakReference;

public class AppComponent {
    private static AppComponent appComponent;
    private final Context applicationContext;

    private WeakReference<AuthModule> authModule;
    private WeakReference<PlaceModule> placeModule;
    private WeakReference<GeoModule> geoModule;
    private WeakReference<WidgetDataProviderModule> appWidgetDataModule;

    public static AppComponent getInstance() {
        return appComponent;
    }

    public AppComponent(Context context) {
        applicationContext = context;
        appComponent = this;
    }

    public AuthModule getAuthRepository() {
        if (authModule == null || authModule.get() == null) {
            authModule = new WeakReference<>(new AuthModule());
        }

        return authModule.get();
    }

    public DataProvider<PlaceEntry, String> getPlaceRepository() {
        if (placeModule == null || placeModule.get() == null) {
            placeModule = new WeakReference<>(new PlaceModule());
        }

        return placeModule.get();
    }

    public GeoModule getGeoRepository() {
        if (geoModule == null || geoModule.get() == null) {
            geoModule = new WeakReference<>(new GeoModule());
        }

        return geoModule.get();
    }

    public AuthWatcher getAuthWatcher() {
        return new FirebaseAuthWatcher();
    }

    public WidgetDataProvider getAppWidgetDataModule() {
        if (appWidgetDataModule == null || appWidgetDataModule.get() == null) {
            appWidgetDataModule = new WeakReference<>(new WidgetDataProviderModule(applicationContext));
        }

        return appWidgetDataModule.get();
    }
}
