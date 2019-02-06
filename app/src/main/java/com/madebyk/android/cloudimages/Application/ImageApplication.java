package com.madebyk.android.cloudimages.Application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class ImageApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
