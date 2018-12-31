package com.ladsuematsu.capstoneproject.core.di.module;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.ladsuematsu.capstoneproject.core.data.adapter.AuthWatcher;

public class FirebaseAuthWatcher implements AuthWatcher {


    FirebaseAuth.AuthStateListener firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            if (firebaseAuth.getCurrentUser() == null) {


                if (listener == null) { return; }

                listener.onRefreshInvalidated();

            }
        }
    };

    private final FirebaseAuth firebaseAuth;
    private AuthListener listener;
    private boolean redirectToLogin = false;

    public FirebaseAuthWatcher() {

        firebaseAuth = FirebaseAuth.getInstance();


    }


    @Override
    public void attach(AuthListener listener) {
        this.listener = listener;
        firebaseAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void detach() {
        if (this.listener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthStateListener);
            this.listener = null;
        }
    }

    @Override
    public void doLogout() {
        firebaseAuth.signOut();
    }

    @Override
    public void refreshSession() {
        if (firebaseAuth.getCurrentUser() != null) {

            if (listener != null) {
                listener.onValidated();
            }

        } else {

            if (listener != null) {
                listener.onRefreshInvalidated();
            }
        }

    }



}

