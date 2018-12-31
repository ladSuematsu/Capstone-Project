package com.ladsuematsu.capstoneproject.newplace.mvp;


import com.ladsuematsu.capstoneproject.core.mvp.Mvp;

public interface NewPlaceMvp {
    String EXTRA_PLACE_KEY = "extra_place_key";

    interface View {
        void onPlaceSavedSuccess();
        void onSearchAddress();
        void refreshFields();
        void refreshFields(int position);
        void onEditWeek(int hourOfDay, int minutes);

        void requestEdit(String key);

        void showProgress(boolean show);
    }

    interface Model extends Mvp.Model<Model.Callback> {
        void refreshSession();

        interface Callback {
            void lol();
        }
    }

}
