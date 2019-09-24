package com.ladsuematsu.capstoneproject.core.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ladsuematsu.capstoneproject.external.util.PermissionUtil;

/**
 * Reference source:
 * https://medium.com/@ali.muzaffar/use-headless-fragment-for-android-m-run-time-permissions-and-to-check-network-connectivity-b48615f6272d
 */
public abstract class PermissionCheckerHeadlessFragment extends Fragment {

    private PermissionCheckerCallback callback;
    private boolean isDenied = false;
    private boolean requestCheck = false;

    public PermissionCheckerHeadlessFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PermissionCheckerCallback) {
            callback = (PermissionCheckerCallback) context;
        } else {
            throw new IllegalArgumentException("Activity implement PermissionCheckerHeadlessFragment.PermissionCheckerCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        callback = null;

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == getRequestCode()) {

            requestCheck = true;

            if (PermissionUtil.verifyPermissions(grantResults)) {

                isDenied = false;
                callback.onLocationPermissionGranted();

            } else {

                isDenied = true;
                callback.onLocationPermissionDenied();

            }

        } else{

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }


    }

    protected boolean hasSelfPermisionsGranted(String[] permissions) {
        return PermissionUtil.hasSelfPermission(getActivity(), permissions);
    }

    protected void requestAppPermissions(@NonNull String[] permissions) {

        requestPermissions(permissions, getRequestCode());

    }

    public abstract boolean hasSelfPermissionsGranted();

    public abstract void requestAppPermissions();

    public abstract int getRequestCode();

    public void checkPermissions() {
        if (hasSelfPermissionsGranted()) {

            if (!requestCheck) {
                callback.onLocationPermissionGranted();
            }


        } else if (!isDenied) {

            requestAppPermissions();

        } else if (!requestCheck) {

            callback.onLocationPermissionDenied();

        }

        requestCheck = false;
    }

    public interface PermissionCheckerCallback {

        void onLocationPermissionGranted();
        void onLocationPermissionDenied();

    }

}
