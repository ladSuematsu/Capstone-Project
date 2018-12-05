package com.ladsuematsu.capstoneproject.newplace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.core.data.adapter.PlacesAdapter;
import com.ladsuematsu.capstoneproject.newplace.mvp.NewPlaceMvp;
import com.ladsuematsu.capstoneproject.newplace.mvp.presenter.NewPlacePresenter;

public class NewPlaceActivity extends AppCompatActivity  {

    private final static int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private final NewPlacePresenter mapPresenter = new NewPlacePresenter();

    private EditText addressForm;
    private EditText nameForm;
    private Button searchAddress;
    private Button savePlace;

    private final View.OnClickListener searchAddressClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            triggerAddressSearch();
        }
    };

    private final View.OnClickListener savePlaceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mapPresenter.savePlace(nameForm.getText().toString());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_place);

        setupViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapPresenter.attachView(viewImplementation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            mapPresenter.onSelectedPlace(new PlacesAdapter(this, data));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapPresenter.detachView();
    }

    private void setupViews() {
        nameForm = findViewById(R.id.name);
        addressForm = findViewById(R.id.address);
        searchAddress = findViewById(R.id.search_address);
        savePlace = findViewById(R.id.save_place);

        searchAddress.setOnClickListener(searchAddressClickListener);
        savePlace.setOnClickListener(savePlaceClickListener);
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

