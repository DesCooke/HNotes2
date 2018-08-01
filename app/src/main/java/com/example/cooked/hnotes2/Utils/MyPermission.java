package com.example.cooked.hnotes2.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class MyPermission
{
    public static boolean checkIfAlreadyhavePermission(Activity activity)
    {
        int read_permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write_permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (read_permission == PackageManager.PERMISSION_GRANTED &&
                write_permission == PackageManager.PERMISSION_GRANTED) {
            return (true);
        }
        return (false);
    }

    public static void requestForSpecificPermission(Activity activity)
    {
        while (!com.example.cooked.hnotes2.Utils.MyPermission.checkIfAlreadyhavePermission(activity))
        {
            ActivityCompat.requestPermissions
            (
                activity,
                new String[]
                {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
               101
            );
        }
    }
}