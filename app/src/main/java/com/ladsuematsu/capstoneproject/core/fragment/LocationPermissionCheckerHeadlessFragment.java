package com.ladsuematsu.capstoneproject.core.fragment;

import android.Manifest;

public class LocationPermissionCheckerHeadlessFragment extends PermissionCheckerHeadlessFragment {

    public static final String DEFAULT_TAG = LocationPermissionCheckerHeadlessFragment.class.getSimpleName();

    private static final String[] permissions = new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static PermissionCheckerHeadlessFragment getInstance() {
        PermissionCheckerHeadlessFragment fragment = new LocationPermissionCheckerHeadlessFragment();

        return fragment;
    }

    @Override
    public boolean hasSelfPermissionsGranted() {
        return hasSelfPermisionsGranted(permissions);
    }

    @Override
    public void requestAppPermissions() {
        requestAppPermissions(permissions);
    }

    @Override
    public int getRequestCode() {
        return 101;
    }
}
