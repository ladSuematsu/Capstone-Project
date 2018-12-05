package com.ladsuematsu.capstoneproject.core.mvp.presenter;

import java.lang.ref.WeakReference;

public class MvpPresenter<V> {

    private WeakReference<V> view;

    public void attachView(V view) {
        this.view = new WeakReference<>(view);
    }

    public void detachView() {
        this.view = null;
    }

    public boolean isViewAttached() {
        return this.view != null && this.view.get()!= null;
    }

    public V getView() {
        return view.get();
    }

}
