package com.artigile.android.placesapi.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.artigile.android.placesapi.R;
import com.artigile.android.placesapi.api.model.Place;
import com.google.common.io.ByteStreams;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author IoaN, 11/9/12 10:02 PM
 */
public class PlacesArrayAdapter extends ArrayAdapter<Place> {
    private final Context context;

    private static Map<String, Bitmap> cachedImages = new HashMap<String, Bitmap>();

    public PlacesArrayAdapter(Context context) {
        super(context, R.layout.list_place);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_place, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        final ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
        textView.setText(getItem(position).toString());

        // Change icon based on name
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                if (!cachedImages.containsKey(getItem(position).getIcon())) {
                    cachedImages.put(getItem(position).getIcon(), loadBitmap(getItem(position).getIcon()));
                }
                return cachedImages.get(getItem(position).getIcon());
            }

            @Override
            protected void onPostExecute(Bitmap s) {
                super.onPostExecute(s);
                imageView.setImageBitmap(s);
            }
        }.execute("sdfsd");
        return rowView;
    }

    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            int IO_BUFFER_SIZE = 1024 * 1024;
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            ByteStreams.copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        } catch (IOException e) {
            Log.e("TAG", "Could not load Bitmap from: " + url);
        } finally {
            closeStream(in);
            closeStream(out);
        }

        return bitmap;
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}