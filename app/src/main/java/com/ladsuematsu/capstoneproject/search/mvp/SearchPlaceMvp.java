package com.ladsuematsu.capstoneproject.search.mvp;


import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.core.mvp.Mvp;

import java.util.List;

public interface SearchPlaceMvp {
    interface View {
        void onResultRefresh();
    }

    interface Model extends Mvp.Model<Model.Callback> {
        void searchPlaces(String searchKey);

        interface Callback {
            void onResult(List<PlaceEntry> result);
        }
    }

}
