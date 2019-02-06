package com.madebyk.android.cloudimages.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class MimeTypeUtils {

    private static final String TAG = "MimeTypeUtils";

    public static String getMimeType(String path){
        return MimeTypeMap.getFileExtensionFromUrl(path);
    }
}
