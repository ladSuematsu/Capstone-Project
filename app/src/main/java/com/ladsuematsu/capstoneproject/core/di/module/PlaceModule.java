package com.ladsuematsu.capstoneproject.core.di.module;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ladsuematsu.capstoneproject.core.data.persistence.DataProvider;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceModule implements DataProvider<PlaceEntry, String> {
    private static final String PLACE_PATH = "places";
    private final DatabaseReference databaseReference;


    public PlaceModule() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(PLACE_PATH);

    }

    @Override
    public void fetch(String searchKey, final ProviderListener<PlaceEntry> listener) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PlaceEntry> placeEntries = new ArrayList<>();

                for (DataSnapshot childDataSnapshot: dataSnapshot.getChildren()) {

                    PlaceEntry placeEntry = childDataSnapshot.getValue(PlaceEntry.class);

                    placeEntries.add(placeEntry);

                }

                if (listener != null) {
                    listener.onSuccess(placeEntries);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });

    }

    @Override
    public void create(PlaceEntry entity) {
        String key = databaseReference.push().getKey();

        entity.setUid(key);
        Map<String, Object> map = entity.toMap();

        Map<String, Object> updates = new HashMap<>();
        updates.put(key, map);

        databaseReference.updateChildren(updates);
    }

    @Override
    public void update(PlaceEntry entity) {
        Map<String, Object> map = entity.toMap();

        Map<String, Object> updates = new HashMap<>();
        updates.put("/" + PLACE_PATH + "/" + entity.getUid(), map);

        databaseReference.updateChildren(updates);
    }
}
