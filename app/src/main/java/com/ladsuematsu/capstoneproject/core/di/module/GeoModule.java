package com.ladsuematsu.capstoneproject.core.di.module;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;

public class GeoModule {
    private static final String TAG = GeoModule.class.getSimpleName();

    private static final String PLACE_PATH = PlaceModule.PLACE_PATH;
    private static final String GEO_PATH = PlaceModule.GEO_PATH;
    private static final String DETAIL_PATH = PlaceModule.DETAIL_PATH;

    private final DatabaseReference placeDetailsReference;
        private final DatabaseReference geoReference;
        private final GeoFire geoFire;
        private GeoQuery geoQuery;
        private GeoObserver geoObserver;


    public interface GeoObserver {
        void onChange(PlaceEntry placeEntry);
        void onExit(String key);
        void onMoved(String key, double latitude, double longitude);
        void onError(int code, String errorLog);
    }

        private final GeoQueryEventListener geoQueryEventListener = new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.d(TAG, "onKeyEntered, key: " + key + ", " + location.longitude + "|" + location.longitude);

                placeDetailsReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "Recovered detail data for place with key: " + dataSnapshot.getKey());

                        PlaceEntry placeEntry = dataSnapshot.getValue(PlaceEntry.class);

                        if (geoObserver != null) {
                            geoObserver.onChange(placeEntry);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        String errorLog = new StringBuilder()
                                .append("Error code: ")
                                .append(databaseError.getCode())
                                .append(" | ")
                                .append("Human-readable message: ")
                                .append(databaseError.getMessage())
                                .append(" | ")
                                .append("Details: ")
                                .append(databaseError.getDetails()).toString();

                        geoObserver.onError(databaseError.getCode(), errorLog);
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
                Log.d(TAG, "onKeyExited, key: " + key);

                if (geoObserver == null) { return; }

                geoObserver.onExit(key);
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d(TAG, "onKeyMoved, key: " + key + ", " + location.longitude + "|" + location.longitude);

                if (geoObserver == null) { return; }

                geoObserver.onMoved(key, location.latitude, location.longitude);
            }

            @Override
            public void onGeoQueryReady() {
                Log.d(TAG, "GeoFire GeoQuery ready");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                if (geoObserver == null) { return; }

                String errorLog = new StringBuilder()
                        .append("Error code: ")
                        .append(error.getCode())
                        .append(" | ")
                        .append("Human-readable message: ")
                        .append(error.getMessage())
                        .append(" | ")
                        .append("Details: ")
                        .append(error.getDetails()).toString();

                Log.e(TAG, errorLog, error.toException());

                geoObserver.onError(error.getCode(), errorLog);

            }
        };

        public GeoModule() {
            DatabaseReference placesRoot = FirebaseDatabase.getInstance().getReference().child(PLACE_PATH);
            placeDetailsReference = placesRoot.child(DETAIL_PATH);
            geoReference = placesRoot.child(GEO_PATH);
            geoFire = new GeoFire(geoReference);
        }


        public void setSearch( double latitude, double longitude, double radius){

            GeoLocation center = new GeoLocation(latitude, longitude);
            if (geoQuery == null) {

                geoQuery = geoFire.queryAtLocation(center, radius);

            } else {

                geoQuery.setCenter(center);
                geoQuery.setRadius(radius);

            }
        }

        public void startListening(GeoObserver geoObserver) {
            this.geoObserver = geoObserver;

            try {
                geoQuery.addGeoQueryEventListener(geoQueryEventListener);
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Listener already mapped.", e);
            }
        }

        public void unlisten() {
            if (geoQuery != null) {
                geoQuery.removeAllListeners();
            }

            this.geoObserver = null;
        }
    }
