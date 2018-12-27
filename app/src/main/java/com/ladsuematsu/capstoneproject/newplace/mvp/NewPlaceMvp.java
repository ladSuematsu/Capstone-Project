package com.ladsuematsu.capstoneproject.newplace.mvp;


import com.ladsuematsu.capstoneproject.core.mvp.Mvp;

public interface NewPlaceMvp {
    interface View {
        void onPlaceSavedSuccess();
        void onSearchAddress();
        void refreshFields();
        void refreshFields(int position);
        void onEditWeek(int hourOfDay, int minutes);
    }

    interface Model extends Mvp.Model<Model.Callback> {
        void refreshSession();

        interface Callback {
            void lol();
        }
    }

}
