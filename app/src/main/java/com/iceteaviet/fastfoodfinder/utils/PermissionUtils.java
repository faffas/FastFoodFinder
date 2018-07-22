package com.iceteaviet.fastfoodfinder.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

/**
 * Created by Genius Doan on 11/8/2016.
 */
public final class PermissionUtils {
    public static final int REQUEST_LOCATION = 1001;
    public static final int REQUEST_CALL_PHONE = 1002;

    private PermissionUtils() {

    }

    public static void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION);
    }

    public static void requestLocationPermission(Fragment fragment) {
        fragment.requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION);
    }

    public static boolean isLocationPermissionGranted(Context context) {
        return !(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    public static void requestCallPhonePermission(Fragment fragment) {
        fragment.requestPermissions(new String[]{
                        Manifest.permission.CALL_PHONE},
                REQUEST_CALL_PHONE);
    }

    public static boolean isCallPhonePermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }
}
