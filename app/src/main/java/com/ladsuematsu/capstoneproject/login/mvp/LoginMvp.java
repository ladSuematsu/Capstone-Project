package com.ladsuematsu.capstoneproject.login.mvp;


import com.ladsuematsu.capstoneproject.core.mvp.Mvp;

public interface LoginMvp {
    interface View {
        void onLoginSuccess();
        void onLoginFailure();
        void onSignupFailure();
        void onSignUpSuccess();
    }

    interface Model extends Mvp.Model<Model.Callback>{
        void doLogin(String login, String password);
        void doSignUp(String login, String password);
        void refreshSession();

        interface Callback {
            void onLoginSuccess();
            void onLoginFailure();
            void onSignUpSuccess();
            void onSignUpFailure();
        }
    }

}
