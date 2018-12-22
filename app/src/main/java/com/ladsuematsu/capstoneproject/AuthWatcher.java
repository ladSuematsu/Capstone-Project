package com.ladsuematsu.capstoneproject;

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
