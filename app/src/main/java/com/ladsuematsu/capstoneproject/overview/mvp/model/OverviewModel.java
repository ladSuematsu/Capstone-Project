package com.ladsuematsu.capstoneproject.overview.mvp.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.di.module.GeoModule;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.overview.mvp.OverviewMvp;

public class OverviewModel implements OverviewMvp.Model {
    private static String TAG =  OverviewModel.class.getSimpleName();

    private final DataProvider<PlaceEntry, String> placeProvider;
    private final GeoModule geoModule;
    private final LocationHelper locationHelper;
    private Callback callback;

    public OverviewModel(Context context) {
        geoModule = AppComponent.getInstance().getGeoRepository();
        placeProvider = AppComponent.getInstance().getPlaceRepository();

        locationHelper = new LocationHelper(context);
    }

    @Override
    public void refreshPlaces() {
        locationHelper.startListening(locationListener);
    }

    private GeoModule.GeoObserver geoObserver = new GeoModule.GeoObserver() {
        @Override
        public void onChange(PlaceEntry placeEntry) {
            if (callback == null) {
                return;
            }

            callback.onRefreshedPlace(placeEntry);
        }

        @Override
        public void onExit(String key) {
            if (callback == null) {
                return;
            }

            callback.onPlaceExit(key);
        }

        @Override
        public void onMoved(String key, double latitude, double longitude) {
            if (callback == null) {
                return;
            }

            callback.onPlaceMoved(key, latitude, longitude);
        }

        @Override
        public void onError(int code, String errorLog) {
            Log.e(TAG, errorLog);
        }
    };

    private final LocationHelper.LocationListener locationListener = new LocationHelper.LocationListener() {
        private final double DEFAULT_SEARCH_RADIUS = 10.0D;

        @Override
        public void onLocationResult(LocationHelper.LocationInfo locationInfo) {
            locationHelper.stopListening();

            if (callback != null) {
                double latitude = locationInfo.getLatitude();
                double longitude = locationInfo.getLongitude();
                callback.onCurrentLocation(latitude, longitude);

                recoverPlaces(locationInfo);
            }
        }

        private void recoverPlaces(LocationHelper.LocationInfo locationInfo) {
            geoModule.setSearch(locationInfo.getLatitude(), locationInfo.getLongitude(), DEFAULT_SEARCH_RADIUS);;

            geoModule.startListening(geoObserver);
        }
    };

    @Override
    public void attachCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void detachCallback() {
        geoModule.unlisten();
        locationHelper.stopListening();

        this.callback = null;
    }

    public static class LocationHelper {

        private final FusedLocationProviderClient fusedLocationProvider;
        private final Context context;

        public LocationHelper(Context context) {
            this.context = context.getApplicationContext();
            this.fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this.context);
        }

        public void getLastLocation() {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                fusedLocationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                    }
                });
            }

        }

        public interface LocationListener {

            void onLocationResult(LocationInfo locationInfo);
        }

        public interface LocationInfo {
            double getLatitude();
            double getLongitude();
        }

        private static class DefaultLocationInfo implements LocationInfo {

            private final Location location;

            public DefaultLocationInfo(Location location) {
                this.location = location;
            }

            @Override
            public double getLatitude() {
                return location.getLatitude();
            }

            @Override
            public double getLongitude() {
                return location.getLongitude();
            }
        }

        LocationListener locationListener;

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationListener != null) {
                    final Location location = locationResult.getLastLocation();
                    locationListener.onLocationResult(new DefaultLocationInfo(location));
                }
            }
        };

        public void startListening(LocationListener listener) {

            locationListener = listener;

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                LocationRequest locationRequest = LocationRequest.create()
                                                .setNumUpdates(1);

                fusedLocationProvider.requestLocationUpdates(locationRequest, locationCallback, null);

            }
        }

        public void stopListening() {
            locationListener = null;
            fusedLocationProvider.removeLocationUpdates(locationCallback);
        }

    }
}
