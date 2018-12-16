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
import com.ladsuematsu.capstoneproject.DrugstorePanelInfo;
import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.newplace.activity.NewPlaceActivity;
import com.ladsuematsu.capstoneproject.overview.mvp.OverviewMvp;
import com.ladsuematsu.capstoneproject.overview.mvp.model.OverviewModel;
import com.ladsuematsu.capstoneproject.overview.mvp.presenter.OverviewPresenter;
import com.ladsuematsu.capstoneproject.search.PlaceSearchActivity;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity {

    private static final float DEFAULT_MAP_ZOOM = 15.0F;
    private static final String DETAIL_PANEL_TAG = "detail_panel_tag";

    private OverviewPresenter presenter;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private SearchView searchView;
    private SupportPlaceAutocompleteFragment placeAutoCompleteFragment;
    private final HashMap<String, Marker> markerMapping = new HashMap<>();

    private GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            String placeKey = (String) marker.getTag();

            presenter.selectPlace(placeKey);

            return true;
        }
    };
    private OverviewMvp.View observer = new OverviewMvp.View() {
        @Override
        public void
        addMarkerForCurrentLocation(double latitude, double longitude) {
            LatLng coordinates = new LatLng(latitude, longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, DEFAULT_MAP_ZOOM));
            map.setMyLocationEnabled(true);
        }

        @Override
        public void removeMarker(String placeKey) {
            if (markerMapping.containsKey(placeKey)) {
                markerMapping.get(placeKey).remove();
            }
        }

        @Override
        public void onSelectedPlace(PlaceEntry placeEntry) {
            DrugstorePanelInfo panelInfo = DrugstorePanelInfo.newInstance(placeEntry);

            panelInfo.show(getSupportFragmentManager(), DETAIL_PANEL_TAG);
        }

        @Override
        public void onPlaceLoaded(PlaceEntry place) {
        }

        @Override
        public void addMarker(String key, double latitude, double longitude) {
            LatLng coordinates = new LatLng(latitude, longitude);
            Marker marker = map.addMarker(new MarkerOptions().position(coordinates));
            marker.setTag(key);

            markerMapping.put(key, marker);
        }

    };

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
            map.setOnMarkerClickListener(markerClickListener);

            presenter.requestPlacesInVicinity();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViews();

        presenter = new OverviewPresenter(new OverviewModel(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        placeAutoCompleteFragment = (SupportPlaceAutocompleteFragment) getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutoCompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);

        presenter.attachView(observer);
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
