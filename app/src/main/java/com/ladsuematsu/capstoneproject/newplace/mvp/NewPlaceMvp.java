package com.ladsuematsu.capstoneproject.newplace.mvp;


import com.ladsuematsu.capstoneproject.core.mvp.Mvp;

public interface NewPlaceMvp {
    interface View {
        void onSelectedName(String name);
        void onSelectedAddress(String address);
        void onPlaceSavedSuccess();
    }

    interface Model extends Mvp.Model<Model.Callback> {
        void refreshSession();

        interface Callback {
            void lol();
        }
    }

}
