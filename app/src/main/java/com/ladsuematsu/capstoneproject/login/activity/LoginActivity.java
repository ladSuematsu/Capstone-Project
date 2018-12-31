package com.ladsuematsu.capstoneproject.login.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ladsuematsu.capstoneproject.core.fragment.NetworkCheckerHeadlessFragment;
import com.ladsuematsu.capstoneproject.overview.MapActivity;
import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.login.mvp.LoginMvp;
import com.ladsuematsu.capstoneproject.login.mvp.model.LoginModel;
import com.ladsuematsu.capstoneproject.login.mvp.presenter.LoginPresenter;
import com.ladsuematsu.capstoneproject.util.UiUtils;

public class LoginActivity extends AppCompatActivity implements NetworkCheckerHeadlessFragment.NetworkCheckerCallback {

    private CoordinatorLayout rootView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private NetworkCheckerHeadlessFragment networkChecker;
    private LoginPresenter presenter;

    private LoginMvp.View mvpView = new LoginMvp.View() {
        @Override
        public void onLoginSuccess() {
            showProgress(false);

            startSession();
        }

        @Override
        public void onLoginFailure() {
            showProgress(false);

            UiUtils.showSnackbar(rootView, getString(R.string.error_incorrect_credentials),
                        null, Snackbar.LENGTH_SHORT, null);
        }

        @Override
        public void onSignUpSuccess() {
            showProgress(false);

            startSession();
        }

        @Override
        public void onSignupFailure() {
            showProgress(false);

            UiUtils.showSnackbar(rootView, getString(R.string.error_signup_failure),
                    null, Snackbar.LENGTH_SHORT, null);
        }

        private void startSession() {
            showProgress(false);

            startActivity(new Intent(LoginActivity.this, MapActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(new LoginModel());

        setupViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.attachView(mvpView);

        FragmentManager fragmentManager = getSupportFragmentManager();
        networkChecker = (NetworkCheckerHeadlessFragment) fragmentManager.findFragmentByTag(NetworkCheckerHeadlessFragment.DEFAULT_TAG);
        if (networkChecker == null) {
            networkChecker = NetworkCheckerHeadlessFragment.getInstance();

            fragmentManager.beginTransaction()
                    .add(networkChecker, NetworkCheckerHeadlessFragment.DEFAULT_TAG)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        networkChecker.checkNetworkStatus();
    }

    @Override
    protected void onStop() {
        super.onStop();

        showProgress(false);

        presenter.detachView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onNetworkActive() { presenter.refreshSession(); }

    @Override
    public void onNoNetwork() {
        UiUtils.showSnackbar(rootView, getString(R.string.error_no_network), null, Snackbar.LENGTH_LONG, null);
    }

    private void setupViews() {
        ActionBar navigationBar = getSupportActionBar();
        navigationBar.setDisplayHomeAsUpEnabled(true);

        // Get root View
        rootView = findViewById(R.id.root_view);

        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button emailSignUpButton = findViewById(R.id.email_sign_up);
        emailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptSignUp() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        presenter.doSignUp(email, password);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        presenter.doLogin(email, password);
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);

        ActionBar navigationBar = getSupportActionBar();
        navigationBar.setDisplayHomeAsUpEnabled(!show);

    }

}

