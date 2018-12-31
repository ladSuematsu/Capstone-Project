package com.ladsuematsu.capstoneproject.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class SystemSettingsUtils {

    public static void openAppSettingDetails(Activity activity) {
        Intent intent = new Intent()
                            .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);

        activity.startActivity(intent);
    }

}
