package com.artigile.android.placesapi.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.HttpResponseCache;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author IoaN, 11/14/12 8:09 PM
 */

public class BitmapLoader {

    private static Map<String, Bitmap> cachedImages = new HashMap<String, Bitmap>();

    public static Bitmap loadBitmap(String urlString) {
        if (cachedImages.containsKey(urlString)) {
            return cachedImages.get(urlString);
        }
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("Cache-Control", "only-if-cached");
            connection.setUseCaches(true);
            cachedImages.put(urlString, BitmapFactory.decodeStream((InputStream) connection.getContent()));
            return cachedImages.get(urlString);
        } catch (Exception e) {

        }
        return null;
    }

}
