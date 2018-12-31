package com.ladsuematsu.capstoneproject.core.data.adapter;

public interface AuthWatcher {

    interface AuthListener {

        void onValidated();
        void onInvalidated();

        void onRefreshInvalidated();

    }
    void attach(AuthListener listener);

    void detach();

    void doLogout();
    void refreshSession();

}
