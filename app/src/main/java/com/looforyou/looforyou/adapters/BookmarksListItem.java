package com.looforyou.looforyou.adapters;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.Models.Review;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.MetricConverter;

import java.text.DecimalFormat;

/**
 * Created by ibreaker on 4/9/2018.
 */

public class BookmarksListItem {
    private Review review;
    private String reviewer;
    private String content;
    private String profilePicture;
    private int points;
    private int rating;
    private Bathroom bathroom;

    public BookmarksListItem(Bathroom bathroom) {
        this.bathroom = bathroom;

    }

    public String getPicture() {
        return bathroom.getImageURL();
    }
    public String getName() {
        return bathroom.getName();
    }
    public String getContent() {
        return bathroom.getAddress();
    }
    public int getRating() {
        return (int) Math.round(bathroom.getRating());
    }

    public String getId() {
        return bathroom.getId();
    }

    public String getDistance(Location location) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            double dist = MetricConverter.distanceBetweenInMiles(new LatLng(location.getLatitude(), location.getLongitude()),bathroom.getLatLng());
            return df.format(dist) + " mi";
        }catch(Exception e) {
            Log.v("bookmarks exception",e.getMessage());
        }
        return "distance unavailable";
    }

    public LatLng getLocation() {
        return bathroom.getLatLng();
    }

}
