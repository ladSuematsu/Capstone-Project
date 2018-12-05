package com.ladsuematsu.capstoneproject.login.mvp.model;

import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.di.module.AuthModule;
import com.ladsuematsu.capstoneproject.login.mvp.LoginMvp;

public class LoginModel     implements LoginMvp.Model {

    private final AuthModule authRepository;
    private Callback callback;

    public LoginModel() {
        this.authRepository = AppComponent.getInstance().getAuthRepository();
    }

    @Override
    public void attachCallback(LoginMvp.Model.Callback callback) {
        this.callback = callback;
    }

    @Override
    public void detach() {
        this.callback = null;
    }

    @Override
    public void doLogin(String login, String password) {
        authRepository.login(login, password, new AuthModule.LoginListener() {
            @Override
            public void onLoginsuccess() {
                if (callback != null) {
                    callback.onLoginSuccess();
                }
            }

            @Override
            public void onLoginFailure() {
                if (callback != null) {
                    callback.onLoginFailure();
                }
            }
        });
    }

    @Override
    public void doSignUp(String login, String password) {
        authRepository.signUp(login, password, new AuthModule.SignUpListener() {
            @Override
            public void onSignUpSuccess() {
                if (callback != null) {
                    callback.onSignUpSuccess();
                }
            }

            @Override
            public void onSignUpFailure() {
                if (callback != null) {
                    callback.onSignUpFailure();
                }
            }
        });
    }

    @Override
    public void refreshSession() {
        if (callback != null && authRepository.isSessionValid()) {
            callback.onLoginSuccess();
        }
    }
}
