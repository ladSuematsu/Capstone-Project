package com.ladsuematsu.capstoneproject.core.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class PlaceEntry implements Parcelable {

    private String uid;
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public PlaceEntry() { }


    public PlaceEntry(String name) {
        this.name = name;
    }
    public PlaceEntry(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public PlaceEntry(String uid, String name, String address, double latitude, double longitude) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected PlaceEntry(Parcel in) {
        uid = in.readString();
        name = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<PlaceEntry> CREATOR = new Creator<PlaceEntry>() {
        @Override
        public PlaceEntry createFromParcel(Parcel in) {
            return new PlaceEntry(in);
        }

        @Override
        public PlaceEntry[] newArray(int size) {
            return new PlaceEntry[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
