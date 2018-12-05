package com.ladsuematsu.capstoneproject.core.mvp;

public interface Mvp {

        interface Presenter<V> {
                void attachView(V view);

                void detachView();
        }

        interface Model<C> {
                void attachCallback(C callback);
                void detach();
        }

}
