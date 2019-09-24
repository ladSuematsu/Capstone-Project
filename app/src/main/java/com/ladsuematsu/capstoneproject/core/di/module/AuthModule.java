package com.ladsuematsu.capstoneproject.core.di.module;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ladsuematsu.capstoneproject.core.entity.UserAuth;

public class AuthModule {
    public interface SignUpListener {

        void onSignUpSuccess();
        void onSignUpFailure();
    }
    public interface LoginListener {

        void onLoginsuccess();
        void onLoginFailure();
    }

    private final FirebaseAuth firebaseAuth;

    public AuthModule() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public boolean isSessionValid() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public void signUp(String email, String password, final SignUpListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (listener != null) {
                    if (task.isSuccessful()) {
                        listener.onSignUpSuccess();
                    } else {
                        listener.onSignUpFailure();
                    }
                }
            }
        });
    }

    public void login(String email, String password, final LoginListener listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (listener != null) {
                    if (task.isSuccessful()) {
                        listener.onLoginsuccess();
                    } else {
                        listener.onLoginFailure();
                    }
                }
            }
        });
    }

    public UserAuth getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        UserAuth    userAuth = firebaseUser != null
                    ? new UserAuth(firebaseUser.getDisplayName(), firebaseUser.getEmail())
                    : new UserAuth("", "");

        return userAuth;
    }

}
