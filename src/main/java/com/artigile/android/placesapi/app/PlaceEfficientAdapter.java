package com.artigile.android.placesapi.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.artigile.android.placesapi.R;
import com.artigile.android.placesapi.api.model.Place;
import com.artigile.android.placesapi.app.model.Cheeses;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/16/12
 * Time: 4:16 PM
 */
public class PlaceEfficientAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Bitmap mIcon1;
    private final List<Place> DATA;



    public PlaceEfficientAdapter(Context context, List<Place> DATA) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.DATA = DATA;
        mInflater = LayoutInflater.from(context);
     //   mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);

    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        return DATA.size();
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficent to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    public Object getItem(int position) {
        return DATA.get(position);
    }

    /**
     * Use the array index as a unique id.
     *
     * @see android.widget.ListAdapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Make a view to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        final ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_place, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.placeInListDetails);
            holder.icon = (ImageView) convertView.findViewById(R.id.placeInListLogo);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        if (mIcon1 == null) {
            // Change icon based on name
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    return BitmapLoader.loadBitmap(((Place) getItem(position)).getIcon());
                }

                @Override
                protected void onPostExecute(Bitmap s) {
                    super.onPostExecute(s);
                    mIcon1 = s;
                    holder.icon.setImageBitmap(mIcon1);

                }
            }.execute();
        } else {
            holder.icon.setImageBitmap(mIcon1);
        }

        // Bind the data efficiently with the holder.
        holder.text.setText(DATA.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        TextView text;
        ImageView icon;
    }


}

