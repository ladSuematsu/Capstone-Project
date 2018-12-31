package com.ladsuematsu.capstoneproject.overview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ladsuematsu.capstoneproject.AuthWatcher;
import com.ladsuematsu.capstoneproject.DrugstorePanelInfo;
import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.core.di.component.AppComponent;
import com.ladsuematsu.capstoneproject.core.entity.PlaceEntry;
import com.ladsuematsu.capstoneproject.core.fragment.LocationPermissionCheckerHeadlessFragment;
import com.ladsuematsu.capstoneproject.core.fragment.PermissionCheckerHeadlessFragment;
import com.ladsuematsu.capstoneproject.login.activity.LoginActivity;
import com.ladsuematsu.capstoneproject.newplace.activity.NewPlaceActivity;
import com.ladsuematsu.capstoneproject.overview.mvp.OverviewMvp;
import com.ladsuematsu.capstoneproject.overview.mvp.model.OverviewModel;
import com.ladsuematsu.capstoneproject.overview.mvp.presenter.OverviewPresenter;
import com.ladsuematsu.capstoneproject.util.SystemSettingsUtils;
import com.ladsuematsu.capstoneproject.util.UiUtils;

import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements PermissionCheckerHeadlessFragment.PermissionCheckerCallback {

    private static final float DEFAULT_MAP_ZOOM = 15.0F;
    private static final String DETAIL_PANEL_TAG = "detail_panel_tag";

    private PermissionCheckerHeadlessFragment locationPermissionChecker;
    private AuthWatcher authWatcher;
    private OverviewPresenter presenter;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private final HashMap<String, Marker> markerMapping = new HashMap<>();
    private ImageButton logoutButton;
    private FloatingActionButton addPlaceFab;
    private FloatingActionButton loginFab;

    private AuthWatcher.AuthListener authWatcherListener = new AuthWatcher.AuthListener() {
        @Override
        public void onValidated() {

            addPlaceFab.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            loginFab.setVisibility(View.GONE);

            if (map != null) {
                presenter.requestPlacesInVicinity();
            }

        }

        @Override
        public void onInvalidated() {

            startActivity(new Intent(MapActivity.this, LoginActivity.class));

        }

        @Override
        public void onRefreshInvalidated() {

            logoutButton.setVisibility(View.GONE);
            addPlaceFab.setVisibility(View.GONE);
            loginFab.setVisibility(View.VISIBLE);

            presenter.requestPlacesInVicinity();
        }
    };

    private View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            authWatcher.doLogout();
        }
    };

    private View.OnClickListener onLoginClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
            startActivity(intent);

        }
    };

    private View.OnClickListener onPlaceAddClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent intent = new Intent(MapActivity.this, NewPlaceActivity.class);
            startActivity(intent);


        }
    };

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

        @Override
        public void refreshMarker(String uid, double latitude, double longitude) {

            if (markerMapping.containsKey(uid)) {
                markerMapping.get(uid).setPosition(new LatLng(latitude, longitude));
            } else {
                addMarker(uid, latitude, longitude);
            }
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

        presenter = new OverviewPresenter(new OverviewModel(this));
        authWatcher = AppComponent.getInstance().getAuthWatcher();

        setupViews();

    }

    @Override
    protected void onStart() {
        super.onStart();

        authWatcher.attach(authWatcherListener);
        presenter.attachView(observer);

        FragmentManager fragmentManager = getSupportFragmentManager();

        locationPermissionChecker = (PermissionCheckerHeadlessFragment) fragmentManager.findFragmentByTag(LocationPermissionCheckerHeadlessFragment.DEFAULT_TAG);
        if (locationPermissionChecker == null) {
            locationPermissionChecker = LocationPermissionCheckerHeadlessFragment.getInstance();

            fragmentManager.beginTransaction()
                    .add(locationPermissionChecker, LocationPermissionCheckerHeadlessFragment.DEFAULT_TAG)
                    .commit();
        }

    }

    boolean postResume = false;

    @Override
    protected void onResume() {
        super.onResume();

        authWatcher.refreshSession();
        locationPermissionChecker.checkPermissions();

    }

    @Override
    protected void onStop() {
        postResume = false;

        authWatcher.detach();
        presenter.detachView();

        super.onStop();
    }

    @Override
    public void onLocationPermissionGranted() { presenter.requestPlacesInVicinity(); }

    @Override
    public void onLocationPermissionDenied() {
        UiUtils.showSnackbar(addPlaceFab, getString(R.string.error_must_enable_location_permissions), getString(R.string.open_app_settings), Snackbar.LENGTH_INDEFINITE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemSettingsUtils.openAppSettingDetails(MapActivity.this);
            }
        });
    }

    private void setupViews() {
        setContentView(R.layout.activity_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        logoutButton = findViewById(R.id.log_out);
        logoutButton.setOnClickListener(logoutClick);

        addPlaceFab = findViewById(R.id.fab);
        addPlaceFab.setOnClickListener(onPlaceAddClickListener);

        loginFab = findViewById(R.id.login_fab);
        loginFab.setOnClickListener(onLoginClick);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(onMapReadyCallback);

    }
}
