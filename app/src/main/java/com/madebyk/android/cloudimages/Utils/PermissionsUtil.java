package com.madebyk.android.cloudimages.Utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public final class PermissionsUtil{


    public static boolean checkPermissions(Context context, String[] permissions){
        for(String permission : permissions){
            if(checkPermission(context, permission) == false){
                return false;
            }
        }
        return true;
    }

    //Checks individual permissions
    public static boolean checkPermission(Context context, String permission){
        if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    //Checking storage for downloading and uploading
    public static boolean StoragePermissionAccess(Context context){
        return checkPermissions(context, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
    }
}
