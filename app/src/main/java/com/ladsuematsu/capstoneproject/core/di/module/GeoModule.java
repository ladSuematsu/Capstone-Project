package com.ladsuematsu.capstoneproject.core.di.module;

import android.support.annotation.NonNull;
import android.util.Log;

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

        public interface GeoObserver {
            void onChange(PlaceEntry placeEntry);
            void onExit(String key);
            void onMoved(String key, double latitude, double longitude);
            void onError(int code, String errorLog);
        }

        public GeoObserver geoObserver;
        

        private final GeoQueryEventListener geoQueryEventListener = new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                placeDetailsReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                if (geoObserver != null) {
                    geoObserver.onExit(key);
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                if (geoObserver != null) {
                    geoObserver.onMoved(key, location.latitude, location.longitude);
                }
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

                geoObserver.onError(error.getCode(), errorLog);

            }
        };

        public GeoModule() {
            DatabaseReference placesRoot = FirebaseDatabase.getInstance().getReference().child(PLACE_PATH);
            placeDetailsReference = placesRoot.child(DETAIL_PATH);
            geoReference = placeDetailsReference.child(GEO_PATH);
            geoFire = new GeoFire(geoReference);
        }


        GeoQuery geoQuery;
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
            geoQuery.addGeoQueryEventListener(geoQueryEventListener);
        }

        public void unlisten() {
            if (geoQuery != null) {
                geoQuery.removeAllListeners();
            }

            this.geoObserver = null;
        }
    }
