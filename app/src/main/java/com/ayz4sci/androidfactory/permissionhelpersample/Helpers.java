package com.ayz4sci.androidfactory.permissionhelpersample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ayz4sci.androidfactory.permissionhelper.PermissionHelper;

import pl.tajchert.nammu.PermissionCallback;

/**
 * Created by Ayz4sci on 02/06/16.
 */
public class Helpers {

    public static void callNumber(final Activity activity, String telNumber) {
        String number = "tel:" + telNumber;
        final Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));

        PermissionHelper.verifyPermissions(
                new String[]{Manifest.permission.CALL_PHONE},
                new PermissionCallback() {
                    @Override
                    public void permissionGranted() {
                        activity.startActivity(callIntent);
                    }

                    @Override
                    public void permissionRefused() {
                    }
                });
    }
}
