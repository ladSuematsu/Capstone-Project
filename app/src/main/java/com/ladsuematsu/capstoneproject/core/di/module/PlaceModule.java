package com.ladsuematsu.capstoneproject.core.di.module;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.core.entity.WeekTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceModule implements DataProvider<PlaceEntry, String> {
    static final String PLACE_PATH = "places";
    static final String GEO_PATH = "geo";
    static final String DETAIL_PATH = "details";
    static final String WEEKTIME_PATH = "weekday_times";
    private static final String TAG = PlaceModule.class.getSimpleName();
    private final GeoFire geoFireInstance;
    private final DatabaseReference placesReference;
    private final DatabaseReference weekTimeReference;
    private ValueEventListener valueEventListener;


    public PlaceModule() {
        DatabaseReference placesRoot = FirebaseDatabase.getInstance().getReference().child(PLACE_PATH);

        placesReference = placesRoot.child(DETAIL_PATH);
        weekTimeReference = placesRoot.child(WEEKTIME_PATH);

        DatabaseReference geoReference = placesRoot.child(GEO_PATH);
        geoFireInstance = new GeoFire(geoReference);
    }


    @Override
    public void fetch(String searchKey, final ProviderListener<PlaceEntry> listener) {
        if (searchKey == null || searchKey.isEmpty()) { return; }

        Query resultReference = placesReference.orderByChild("name").startAt(searchKey).limitToFirst(100);

        if (valueEventListener != null) {
            resultReference.removeEventListener(valueEventListener);
        }

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PlaceEntry> placeEntries = new ArrayList<>();

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                    PlaceEntry placeEntry = childDataSnapshot.getValue(PlaceEntry.class);


                    placeEntries.add(placeEntry);

                }

                if (listener != null) {
                    listener.onSuccess(placeEntries);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Place search failed", databaseError.toException());
                listener.onFailure();
            }
        };

        resultReference.addListenerForSingleValueEvent(valueEventListener);

    }

    @Override
    public void create(PlaceEntry entity) {

        // Write new Place entry
        String key = placesReference.push().getKey();

        entity.setUid(key);
        Map<String, Object> map = entity.toMap();

        Map<String, Object> updates = new HashMap<>();
        updates.put(key, map);

        // Write Geofire entry
        geoFireInstance.setLocation(key, new GeoLocation(entity.getLatitude(),
                                                            entity.getLongitude()));
        placesReference.updateChildren(updates);


        // Writet week times
        DatabaseReference placeWeekTimeReference = weekTimeReference.child(key);
        List<WeekTime> weekTimes = entity.getWeekTimes();
        for (WeekTime weekTime : weekTimes) {
            DatabaseReference keyReference = placeWeekTimeReference.push();

            weekTime.setPlaceUid(keyReference.getKey());
            keyReference.setValue(weekTime);
        }

    }

    @Override
    public void update(PlaceEntry entity) {
        Map<String, Object> map = entity.toMap();

        Map<String, Object> updates = new HashMap<>();
        updates.put("/" + PLACE_PATH + "/" + entity.getUid(), map);

        placesReference.updateChildren(updates);
    }

}
