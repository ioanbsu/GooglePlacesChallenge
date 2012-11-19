package com.artigile.android.aroundme.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            connection.addRequestProperty("Cache-Control", "max-stale=" + maxStale);
            connection.setUseCaches(true);
            cachedImages.put(urlString, BitmapFactory.decodeStream((InputStream) connection.getContent()));
            return cachedImages.get(urlString);
        } catch (Exception e) {
            System.out.println("hello there");
        }
        return null;
    }

}
