package com.ladsuematsu.capstoneproject.newplace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.core.adapter.DayAdapter;
import com.ladsuematsu.capstoneproject.core.data.adapter.PlacesAdapter;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;
import com.ladsuematsu.capstoneproject.newplace.mvp.presenter.NewPlacePresenter;

public class NewPlaceActivity extends AppCompatActivity  {

    private final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private final NewPlacePresenter newPlacePresenter = new NewPlacePresenter();

    private EditText addressForm;
    private EditText nameForm;
    private Button searchAddress;
    private RecyclerView days;

    private final View.OnClickListener searchAddressClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            triggerAddressSearch();
        }
    };

    private final NewPlaceMvp.View viewImplementation = new NewPlaceMvp.View() {
        @Override
        public void onSelectedName (String name){
            nameForm.setText(name);
        }

        @Override
        public void onSelectedAddress(String address){
            addressForm.setText(address);
        }

        @Override
        public void onPlaceSavedSuccess() {
            setResult(RESULT_OK);
            finish();
        }
    };
    private DayAdapter daysAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        setupViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        newPlacePresenter.attachView(viewImplementation);

        if (daysAdapter == null || days.getAdapter() == null) {

            daysAdapter = new DayAdapter(getLayoutInflater(), newPlacePresenter);
            days.setAdapter(daysAdapter);

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
    protected void onStop() {
        super.onStop();
        newPlacePresenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_place, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit_place_save) {
            newPlacePresenter.savePlace(nameForm.getText().toString());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        nameForm = findViewById(R.id.name);
        addressForm = findViewById(R.id.address);
        searchAddress = findViewById(R.id.search_address);
        days = findViewById(R.id.open_days);

        searchAddress.setOnClickListener(searchAddressClickListener);

        days.setNestedScrollingEnabled(false);
        days.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        days.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void triggerAddressSearch() {
        try {
            Intent placeAutocompleteIntent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                                    .build(this);

            startActivityForResult(placeAutocompleteIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

}

