package com.ladsuematsu.capstoneproject.core.data.adapter;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class PlacesAdapter implements ApiPlaceAdapter {

    private final Place place;

    public PlacesAdapter(Context context, Intent data) {
        place = PlaceAutocomplete.getPlace(context, data);
    }

    @Override
    public String getId() { return place.getId(); }

    @Override
    public String getName() {
        return place.getName().toString();
    }

    @Override
    public String getPhoneNumber() {
        return place.getPhoneNumber() != null ? place.getPhoneNumber().toString() : "";
    }

    @Override
    public String getAddress() {
        return place.getAddress().toString();
    }

    @Override
    public double getLatitude() {
        return place.getLatLng().latitude;
    }

    @Override
    public double getLongitude() {
        return place.getLatLng().longitude;
    }

}