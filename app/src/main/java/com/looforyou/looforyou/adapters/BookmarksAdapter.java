package com.looforyou.looforyou.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.BookmarksUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;

/**
 * This is the adapter for displaying bookmarks
 *
 * @author: mingtau li
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    /* list custom of Bookmark List Items */
    private List<BookmarksListItem> bookmarksListItems;
    /* activity context*/
    private Context context;

    /**
     * Constructor for Bookmarks Adapter
     *
     * @param bookmarksListItems list of bookmark list items
     * @param context            activity context
     */
    public BookmarksAdapter(List<BookmarksListItem> bookmarksListItems, Context context) {
        this.bookmarksListItems = bookmarksListItems;
        this.context = context;
    }

    /**
     * Inflates bookmarks list items for
     *
     * @param parent   parent ViewGroup of acivity
     * @param viewType viewType id
     * @return viewHolder from inflated layout
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_list_item, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Viewholder on bind functionality
     *
     * @param holder   current ViewHolder
     * @param position current position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        /* get current bookmark list item*/
        final BookmarksListItem bookmarksListItem = bookmarksListItems.get(position);
        /* load bathroom picture for display */
        Picasso.get().load(bookmarksListItem.getPicture()).fit().into(holder.bathroomPicture);
        /* initialize new bookmarksUtil utility */
        final BookmarksUtil bookmarksUtil = new BookmarksUtil(context);

        /* bind clicklistener to bookmark/unbookmark bathrooms */
        holder.unbookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.unbookmarkButton.getTag() == null) {
                    /* unbookmark a bathroom */
                    if (bookmarksUtil.unBookmarkBathroom(bookmarksListItem.getId())) {
                        holder.unbookmarkButton.setTag("unbookmarked");
                        holder.unbookmarkButton.setImageResource(R.drawable.ic_bookmark_disabled_20);
                        Toast.makeText(context, bookmarksListItem.getName() + " unbookmarked", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    /* bookmark a bathroom */
                    if (bookmarksUtil.bookmarkBathroom(bookmarksListItem.getId())) {
                        holder.unbookmarkButton.setTag(null);
                        holder.unbookmarkButton.setImageResource(R.drawable.ic_bookmark_enabled_20);
                        Toast.makeText(context, bookmarksListItem.getName() + " bookmarked", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /* set listener for google maps app navigation */
        holder.navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri markerUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + bookmarksListItem.getLocation().latitude + "," + bookmarksListItem.getLocation().longitude);
                Intent directionsIntent = new Intent(Intent.ACTION_VIEW, markerUri);
                /* use google maps android app */
                directionsIntent.setPackage("com.google.android.apps.maps");
                if (directionsIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(directionsIntent);
                } else {
                    Toast.makeText(context, "Unable to route", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* set display content for bathroom */
        holder.bathroomRating.setImageResource(getStarDrawableResource(bookmarksListItem.getRating()));
        holder.bathroomName.setText(bookmarksListItem.getName());
        holder.bathroomInfo.setText(bookmarksListItem.getContent());
        holder.bathroomDistance.setText(bookmarksListItem.getDistance(getCurrentLocation()));

    }

    /**
     * This method looks for best location provider by checking availablility of gps or network location
     *
     * @return location most accurate location from location providers
     */
    public Location getCurrentLocation() {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        /* permission check */
        for (String p : providers) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            /* skip if location is null */
            Location location = mLocationManager.getLastKnownLocation(p);
            if (location == null) {
                continue;
            }
            /* get best location from providers */
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }
        return bestLocation;
    }

    /**
     * gets number of bookmark list items
     *
     * @return int number of bookmark list items
     */
    @Override
    public int getItemCount() {
        return bookmarksListItems.size();
    }

    /**
     * ViewHolder class that uses RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /* views for bathroom display data */
        public ImageView bathroomPicture;
        public ImageView unbookmarkButton;
        public ImageView navigation;
        public ImageView bathroomRating;
        public TextView bathroomName;
        public TextView bathroomInfo;
        public TextView bathroomDistance;

        /**
         * Constructor for ViewHolder
         * Initializes all view items
         */
        public ViewHolder(View itemView) {
            super(itemView);
            bathroomPicture = (ImageView) itemView.findViewById(R.id.bookmarks_bathroom_picture);
            unbookmarkButton = (ImageView) itemView.findViewById(R.id.bookmarks_bookmarked);
            navigation = (ImageView) itemView.findViewById(R.id.bookmarks_navigation);
            bathroomRating = (ImageView) itemView.findViewById(R.id.bookmarks_rating);
            bathroomName = (TextView) itemView.findViewById(R.id.bookmarks_bathroom_name);
            bathroomInfo = (TextView) itemView.findViewById(R.id.bookmarks_content);
            bathroomDistance = (TextView) itemView.findViewById(R.id.bookmarks_distance);
        }
    }
}
