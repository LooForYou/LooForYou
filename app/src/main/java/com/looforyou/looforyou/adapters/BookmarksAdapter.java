package com.looforyou.looforyou.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.BookmarksUtil;
import com.looforyou.looforyou.utilities.HttpDelete;
import com.looforyou.looforyou.utilities.HttpPut;
import com.looforyou.looforyou.utilities.UserUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.looforyou.looforyou.Constants.BOOKMARKS;
import static com.looforyou.looforyou.Constants.BOOKMARKS_REL;
import static com.looforyou.looforyou.Constants.DOWNVOTE;
import static com.looforyou.looforyou.Constants.GET_REVIEWS;
import static com.looforyou.looforyou.Constants.GET_USERS;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;
import static com.looforyou.looforyou.Constants.UPVOTE;
import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;

/**
 * Created by ibreaker on 4/9/2018.
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder>{
    private List<BookmarksListItem> bookmarksListItems;
    private Context context;

    public BookmarksAdapter(List<BookmarksListItem> bookmarksListItems, Context context) {
        this.bookmarksListItems = bookmarksListItems;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final BookmarksListItem bookmarksListItem = bookmarksListItems.get(position);
        Picasso.get().load(bookmarksListItem.getPicture()).fit().into(holder.bathroomPicture);
        final BookmarksUtil bookmarksUtil = new BookmarksUtil(context);
        holder.unbookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.unbookmarkButton.getTag() == null){
                    if(bookmarksUtil.unBookmarkBathroom(bookmarksListItem.getId())){
                        holder.unbookmarkButton.setTag("unbookmarked");
                        holder.unbookmarkButton.setImageResource(R.drawable.ic_bookmark_disabled_20);
                        Toast.makeText(context,bookmarksListItem.getName()+" unbookmarked", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(bookmarksUtil.bookmarkBathroom(bookmarksListItem.getId())){
                        holder.unbookmarkButton.setTag(null);
                        holder.unbookmarkButton.setImageResource(R.drawable.ic_bookmark_enabled_20);
                        Toast.makeText(context,bookmarksListItem.getName()+" bookmarked", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        holder.navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri markerUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + bookmarksListItem.getLocation().latitude + "," + bookmarksListItem.getLocation().longitude);
                Intent directionsIntent = new Intent(Intent.ACTION_VIEW, markerUri);
                directionsIntent.setPackage("com.google.android.apps.maps");
                if (directionsIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(directionsIntent);
                } else {
                    Toast.makeText(context, "Unable to route", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.bathroomRating.setImageResource(getStarDrawableResource(bookmarksListItem.getRating()));
        holder.bathroomName.setText(bookmarksListItem.getName());
        holder.bathroomInfo.setText(bookmarksListItem.getContent());
        Log.v("homee im about to","start getting dist here");
        holder.bathroomDistance.setText(bookmarksListItem.getDistance(getCurrentLocation()));

    }

    public Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //Permission check. NOTE: can't request permission in non-activity because activity context is needed instead of application context, dangerous to do
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
    }

    @Override
    public int getItemCount() {
        return bookmarksListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView bathroomPicture;
        public ImageView unbookmarkButton;
        public ImageView navigation;
        public ImageView bathroomRating;
        public TextView bathroomName;
        public TextView bathroomInfo;
        public TextView bathroomDistance;



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
