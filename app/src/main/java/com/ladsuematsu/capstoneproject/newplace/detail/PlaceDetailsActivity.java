package com.ladsuematsu.capstoneproject.newplace.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.core.adapter.PlaceDetailsAdapter;
import com.ladsuematsu.capstoneproject.core.data.adapter.AuthWatcher;
import com.ladsuematsu.capstoneproject.core.data.adapter.PlacesAdapter;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.fragment.NetworkCheckerHeadlessFragment;
import com.ladsuematsu.capstoneproject.newplace.activity.NewPlaceActivity;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;
import com.ladsuematsu.capstoneproject.newplace.mvp.presenter.NewPlacePresenter;
import com.ladsuematsu.capstoneproject.util.UiUtils;

public class PlaceDetailsActivity extends AppCompatActivity implements NetworkCheckerHeadlessFragment.NetworkCheckerCallback {
    private static final String LOG_TAG = PlaceDetailsActivity.class.getSimpleName();

    private final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private final NewPlacePresenter newPlacePresenter = new NewPlacePresenter();

    private AuthWatcher authWatcher;

    private AuthWatcher.AuthListener authWatcherListener = new AuthWatcher.AuthListener() {
        @Override
        public void onValidated() {

            newPlacePresenter.load();

        }

        @Override
        public void onInvalidated() {

            if (editStartMenuItem == null) { return; }

            editStartMenuItem.setVisible(false);

        }

        @Override
        public void onRefreshInvalidated() {

            if (editStartMenuItem == null) { return; }

            editStartMenuItem.setVisible(false);

        }
    };

    private final NewPlaceMvp.View viewImplementation = new NewPlaceMvp.View() {

        @Override
        public void requestEdit(String key) {
            Intent intent = new Intent(PlaceDetailsActivity.this, NewPlaceActivity.class)
                                .putExtra(NewPlaceMvp.EXTRA_PLACE_KEY, key);
            startActivity(intent);
        }

        @Override
        public void onPlaceSavedSuccess() {
            setResult(RESULT_OK);
            finish();
        }

        @Override
        public void showProgress(boolean show) {
            progress.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onSearchAddress() {}

        @Override
        public void refreshFields() {
            formFields.setVisibility(View.VISIBLE);
            editStartMenuItem.setVisible(true);
            daysAdapter.notifyDataSetChanged();
        }

        @Override
        public void refreshFields(int position) {
            formFields.setVisibility(View.VISIBLE);
            editStartMenuItem.setVisible(true);
            daysAdapter.notifyItemChanged(position);
        }

        @Override
        public void onEditWeek(int hourOfDay, int minutes) {}
    };

    private CoordinatorLayout rootView;
    private PlaceDetailsAdapter daysAdapter;
    private RecyclerView formFields;
    private MenuItem editStartMenuItem;
    private NetworkCheckerHeadlessFragment networkChecker;
    private View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        setupViews();

        authWatcher = AppComponent.getInstance().getAuthWatcher();

        Intent intent = getIntent();
        String placeKey = intent.hasExtra(NewPlaceMvp.EXTRA_PLACE_KEY) ? intent.getStringExtra(NewPlaceMvp.EXTRA_PLACE_KEY) : "";
        newPlacePresenter.setLoadParameters(placeKey);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authWatcher.attach(authWatcherListener);
        newPlacePresenter.attachView(viewImplementation);

        if (daysAdapter == null || formFields.getAdapter() == null) {

            daysAdapter = new PlaceDetailsAdapter(getLayoutInflater(), newPlacePresenter);
            formFields.setAdapter(daysAdapter);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            newPlacePresenter.onSelectedPlace(new PlacesAdapter(this, data));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_place, menu);
        editStartMenuItem = menu.findItem(R.id.action_edit_place_start);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        networkChecker = (NetworkCheckerHeadlessFragment) fragmentManager.findFragmentByTag(NetworkCheckerHeadlessFragment.DEFAULT_TAG);
        if (networkChecker == null) {
            networkChecker = NetworkCheckerHeadlessFragment.getInstance();

            fragmentManager.beginTransaction()
                    .add(networkChecker, NetworkCheckerHeadlessFragment.DEFAULT_TAG)
                    .commit();
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_place_start) {
            newPlacePresenter.editPlace();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onNetworkActive() {

        authWatcher.refreshSession();

    }

    @Override
    public void onNoNetwork() {
        UiUtils.showSnackbar(rootView, getString(R.string.error_no_network), null, Snackbar.LENGTH_LONG, null);
        formFields.setVisibility(View.INVISIBLE);
        editStartMenuItem.setVisible(false);
    }

    private void setupViews() {
        rootView = findViewById(R.id.root_view);
        ActionBar navigationBar = getSupportActionBar();
        navigationBar.setDisplayHomeAsUpEnabled(true);
        navigationBar.setTitle(R.string.title_activity_place_details);

        progress = findViewById(R.id.progress);

        formFields = findViewById(R.id.form_fields);

        formFields.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        formFields.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

}

