package com.ladsuematsu.capstoneproject.overview.mvp;


import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.core.mvp.Mvp;

public interface OverviewMvp {
    interface View {
        void onSelectedPlace(PlaceEntry placeEntry);
        void onPlaceLoaded(PlaceEntry placeEntry);
        void addMarker(String key, double latitude, double longitude);
        void addMarkerForCurrentLocation(double latitude, double longitude);

        void removeMarker(String placeKey);

        void refreshMarker(String uid, double latitude, double longitude);
    }

    interface Model extends Mvp.Model<Model.Callback> {
        void refreshPlaces();

        interface Callback {
            void onRefreshedPlace(PlaceEntry place);
            void onPlaceExit(String placeKey);

            void onPlaceMoved(String key, double latitude, double longitude);

            void onCurrentLocation(double latitude, double longitude);
        }
    }

}
