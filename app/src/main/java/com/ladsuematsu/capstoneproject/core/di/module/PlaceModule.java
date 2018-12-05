package com.ladsuematsu.capstoneproject.core.di.module;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.entity.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceModule implements DataProvider<Place, String> {
    private static final String PLACE_PATH = "places";
    private final DatabaseReference databaseReference;


    public PlaceModule() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(PLACE_PATH);

    }

    @Override
    public void fetch(String searchKey, final ProviderListener<Place> listener) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Place> places = new ArrayList<>();

                for (DataSnapshot childDataSnapshot: dataSnapshot.getChildren()) {

                    Place place = childDataSnapshot.getValue(Place.class);

                    places.add(place);

                }

                if (listener != null) {
                    listener.onSuccess(places);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });

    }

    @Override
    public void create(Place entity) {
        String key = databaseReference.push().getKey();

        entity.setUid(key);
        Map<String, Object> map = entity.toMap();

        Map<String, Object> updates = new HashMap<>();
        updates.put(key, map);

        databaseReference.updateChildren(updates);
    }

    @Override
    public void update(Place entity) {
        Map<String, Object> map = entity.toMap();

        Map<String, Object> updates = new HashMap<>();
        updates.put("/" + PLACE_PATH + "/" + entity.getUid(), map);

        databaseReference.updateChildren(updates);
    }
}
