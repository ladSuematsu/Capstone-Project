package com.ladsuematsu.capstoneproject.login.mvp.presenter;

import com.ladsuematsu.capstoneproject.core.mvp.Mvp;
import com.ladsuematsu.capstoneproject.core.mvp.presenter.MvpPresenter;
import com.ladsuematsu.capstoneproject.login.mvp.LoginMvp;

public class LoginPresenter implements Mvp.Presenter<LoginMvp.View> {
    private final MvpPresenter<LoginMvp.View> presenterHelper;
    private final LoginMvp.Model model;

    LoginMvp.Model.Callback modelCallback = new LoginMvp.Model.Callback() {
        @Override
        public void onLoginSuccess() {
            if (!presenterHelper.isViewAttached()) { return; }

            presenterHelper.getView().onLoginSuccess();
        }

        @Override
        public void onLoginFailure() {
            if (!presenterHelper.isViewAttached()) { return; }

            presenterHelper.getView().onLoginFailure();
        }

        @Override
        public void onSignUpSuccess() {
            if (!presenterHelper.isViewAttached()) { return; }

            presenterHelper.getView().onSignUpSuccess();
        }

        @Override
        public void onSignUpFailure() {
            if (!presenterHelper.isViewAttached()) { return; }

            presenterHelper.getView().onSignupFailure();
        }
    };

    public LoginPresenter(LoginMvp.Model model) {
        this.model = model;
        this.presenterHelper = new MvpPresenter<>();
    }


    @Override
    public void attachView(LoginMvp.View view) {
        presenterHelper.attachView(view);
        model.attachCallback(modelCallback);
    }

    @Override
    public void detachView() {
        presenterHelper.detachView();
        model.detachCallback();
    }

    public void doLogin(String login, String password) {
        model.doLogin(login, password);
    }

    public void doSignUp(String login, String password) {
        model.doSignUp(login, password);
    }

    public void refreshSession() {
        model.refreshSession();
    }
}
