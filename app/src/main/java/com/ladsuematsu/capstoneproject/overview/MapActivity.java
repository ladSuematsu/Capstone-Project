package com.ladsuematsu.capstoneproject.overview;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.newplace.activity.NewPlaceActivity;
import com.ladsuematsu.capstoneproject.search.PlaceSearchActivity;

public class MapActivity extends AppCompatActivity {

    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private SearchView searchView;

    PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {
        String TAG = "PLACES_API";

        @Override
        public void onPlaceSelected(com.google.android.gms.location.places.Place place) {
            Log.i(TAG, "PlaceEntry: " + place.getName());
        }

        @Override
        public void onError(Status status) {
            Log.i(TAG, "An error occurred: " + status);

        }
    };

    private final OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;

            LatLng home = new LatLng(0.0D, 0.0D);
            map.addMarker(new MarkerOptions().position(home).title(getString(R.string.current_position))).setTag("NON");
            map.moveCamera(CameraUpdateFactory.newLatLng(home));
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    return marker.getTag().equals("NON");
                }
            });
        }
    };

    private SupportPlaceAutocompleteFragment placeAutoCompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setupViews();
    }

    @Override
    protected void onStart() {
        super.onStart();

        placeAutoCompleteFragment = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutoCompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);

    }

    private void setupViews() {
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPlace();
            }
        });

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(onMapReadyCallback);

        searchView = findViewById(R.id.search_bar);
        searchView.setIconifiedByDefault(false);

//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((SearchView) v).setIconified(false);
//            }
//        });
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//
//            }
//        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), PlaceSearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
    }

    private void addNewPlace() {
        Intent intent = new Intent(this, NewPlaceActivity.class);
        startActivity(intent);
    }

}
