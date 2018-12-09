package com.ladsuematsu.capstoneproject.core.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Place {

    private String uid;
    private String name;
    @Exclude private String address;
    @Exclude private double latitude;
    @Exclude private double longitude;

    public Place() { }


    public Place(String name) {
        this.name = name;
    }
    public Place(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public Place(String uid, String name, String address, double latitude, double longitude) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setUid(String uid) { this.uid = uid; }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("address", address);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        return result;
    }
}
