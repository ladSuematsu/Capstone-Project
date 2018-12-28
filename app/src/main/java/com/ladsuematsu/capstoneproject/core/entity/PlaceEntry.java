package com.ladsuematsu.capstoneproject.core.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceEntry implements Parcelable {

    private String uid;
    private String name;
    private String address;
    private String phoneNumber;
    private double latitude;
    private double longitude;
    private boolean doesDoorDelivery;
    private boolean isAnimalFriendly;
    private boolean hasFacilitiesForDisabledPeople;

    @Exclude private List<WeekTime> weekTimes = new ArrayList<>();

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

    public PlaceEntry(String uid, String name, String phoneNumber, String address, double latitude, double longitude, boolean doesDoorDelivery, boolean isAnimalFriendly, boolean hasFacilitiesForDisabledPeople) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.doesDoorDelivery = doesDoorDelivery;
        this.isAnimalFriendly = isAnimalFriendly;
        this.hasFacilitiesForDisabledPeople = hasFacilitiesForDisabledPeople;
    }

    protected PlaceEntry(Parcel in) {
        uid = in.readString();
        name = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        doesDoorDelivery = in.readByte() != 0;
        isAnimalFriendly = in.readByte() != 0;
        hasFacilitiesForDisabledPeople = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (doesDoorDelivery ? 1 : 0));
        dest.writeByte((byte) (isAnimalFriendly ? 1 : 0));
        dest.writeByte((byte) (hasFacilitiesForDisabledPeople ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    @Exclude public void setWeekTimes(List<WeekTime> weekTimes) {
        this.weekTimes = weekTimes;
    }

    @Exclude  public List<WeekTime> getWeekTimes() {
        return weekTimes;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("address", address);
        result.put("phoneNumber", phoneNumber);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("doesDoorDelivery", doesDoorDelivery);
        result.put("isAnimalFriendly", isAnimalFriendly);
        result.put("hasFacilitiesForDisabledPeople", hasFacilitiesForDisabledPeople);
        return result;
    }

    @Override
    public String toString() {
        return "PlaceEntry{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", doesDoorDelivery=" + doesDoorDelivery +
                ", isAnimalFriendly=" + isAnimalFriendly +
                ", hasFacilitiesForDisabledPeople=" + hasFacilitiesForDisabledPeople +
                '}';
    }
}
