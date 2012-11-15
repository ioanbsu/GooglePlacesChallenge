package com.artigile.android.placesapi;

import android.app.Application;
import android.net.http.HttpResponseCache;
import android.util.Log;

import java.io.File;

/**
 * @author IoaN, 11/8/12 6:37 PM
 */
public class PlacesApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        enableHttpResponseCache();
    }

    private void enableHttpResponseCache() {
        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDir = new File(getCacheDir(), "http");
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            Log.d("ApplicationCache", "HTTP response cache is unavailable.");
        }
    }

}
