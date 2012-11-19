package com.artigile.android.placesapi.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.artigile.android.placesapi.R;
import com.artigile.android.placesapi.api.model.Place;

import java.text.DecimalFormat;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/16/12
 * Time: 4:16 PM
 */
public class PlaceEfficientAdapter extends ArrayAdapter<Place> {
    private LayoutInflater mInflater;
    private Bitmap mIcon1;
    private Location myLocation;

    private DecimalFormat milesFormat=new DecimalFormat( "#,###,###,##0.00" );

    public PlaceEfficientAdapter(Context context, Location myLocation) {
        super(context, R.layout.list_place);
        // Cache the LayoutInflate to avoid asking for a new one each time.
        this.myLocation = myLocation;
        mInflater = LayoutInflater.from(context);
        //   mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);

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
            holder.distanceToPlace = (TextView) convertView.findViewById(R.id.distanceToPlaceInSearchResList);
            holder.placeRating = (RatingBar) convertView.findViewById(R.id.searchResultsListRating);
            holder.icon = (ImageView) convertView.findViewById(R.id.placeInListLogo);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                return BitmapLoader.loadBitmap((getItem(position)).getIcon());
            }

            @Override
            protected void onPostExecute(Bitmap s) {
                super.onPostExecute(s);
                mIcon1 = s;
                holder.icon.setImageBitmap(mIcon1);
            }
        }.execute();

        Location loc = new Location("");
        loc.setLatitude(getItem(position).getGeometry().getLocation().getLat());
        loc.setLongitude(getItem(position).getGeometry().getLocation().getLng());
        loc.distanceTo(myLocation);
        holder.distanceToPlace.setText(milesFormat.format(loc.distanceTo(myLocation) * 0.000621371) + " mi.");
        holder.text.setText(getItem(position).getName());

        if (getItem(position).getRating() != null) {
            holder.placeRating.setVisibility(View.VISIBLE);
            holder.placeRating.setRating(getItem(position).getRating());
        } else {
            holder.placeRating.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView text;
        TextView distanceToPlace;
        RatingBar placeRating;
        ImageView icon;
    }


}

