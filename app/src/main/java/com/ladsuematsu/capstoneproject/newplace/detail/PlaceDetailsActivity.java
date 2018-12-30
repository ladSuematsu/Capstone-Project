package com.ladsuematsu.capstoneproject.newplace.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ladsuematsu.capstoneproject.AuthWatcher;
import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.core.adapter.DayListenerObserver;
import com.ladsuematsu.capstoneproject.core.adapter.PlaceDetailsAdapter;
import com.ladsuematsu.capstoneproject.core.adapter.PlaceEditAdapter;
import com.ladsuematsu.capstoneproject.core.data.adapter.PlacesAdapter;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.login.activity.LoginActivity;
import com.ladsuematsu.capstoneproject.newplace.activity.NewPlaceActivity;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;
import com.ladsuematsu.capstoneproject.newplace.mvp.presenter.NewPlacePresenter;
import com.ladsuematsu.capstoneproject.overview.MapActivity;

public class PlaceDetailsActivity extends AppCompatActivity  {
    private static final String LOG_TAG = PlaceDetailsActivity.class.getSimpleName();

    private final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private final NewPlacePresenter newPlacePresenter = new NewPlacePresenter();

    private AuthWatcher authWatcher;

    private AuthWatcher.AuthListener authWatcherListener = new AuthWatcher.AuthListener() {
        @Override
        public void onValidated() {

            if (editStartMenuItem == null) { return; }

            editStartMenuItem.setVisible(true);

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
        public void onSearchAddress() {}

        @Override
        public void refreshFields() {
            daysAdapter.notifyDataSetChanged();
        }

        @Override
        public void refreshFields(int position) {
            daysAdapter.notifyItemChanged(position);
        }

        @Override
        public void onEditWeek(int hourOfDay, int minutes) {}
    };

    private PlaceDetailsAdapter daysAdapter;
    private RecyclerView formFields;
    private MenuItem editStartMenuItem;

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

        newPlacePresenter.load();
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
        authWatcher.refreshSession();

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

    private void setupViews() {
        ActionBar navigationBar = getSupportActionBar();
        navigationBar.setDisplayHomeAsUpEnabled(true);

        formFields = findViewById(R.id.form_fields);

        formFields.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        formFields.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

}

