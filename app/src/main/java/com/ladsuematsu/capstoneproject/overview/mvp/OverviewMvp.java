package com.ladsuematsu.capstoneproject.overview.mvp;


import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.core.mvp.Mvp;

import java.util.List;

public interface OverviewMvp {
    interface View {
        void onSelectedPlace();
    }

    interface Model extends Mvp.Model<Model.Callback> {
        void refreshPlaces();

        interface Callback {
            void onRefreshedPlaces(List<PlaceEntry> places);
        }
    }

}
