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
 * This is the recycler view list item for bookmarks
 *
 * @author: mingtau li
 */

public class BookmarksListItem {
    /* Bathroom object for data display */
    private Bathroom bathroom;

    /** Constructor for bookmark list item
     * @param bathroom Bathroom object
     * */
    public BookmarksListItem(Bathroom bathroom) {
        this.bathroom = bathroom;

    }

    /**
     * getter for bathroom picture
     * @return String url of bathroom
     * */
    public String getPicture() {
        return bathroom.getImageURL();
    }

    /**
     * getter for bathroom name
     * @return String bathroom name
     * */
    public String getName() {
        return bathroom.getName();
    }

    /**
     * getter for bathroom address
     * @return String address of bathroom
     * */
    public String getContent() {
        return bathroom.getAddress();
    }

    /**
     * getter for bathroom rating
     * @return int rounded rating of bathroom
     * */
    public int getRating() {
        return (int) Math.round(bathroom.getRating());
    }

    /**
     * getter for bathroom id
     * @return String id of bathroom
     * */
    public String getId() {
        return bathroom.getId();
    }

    /**
     * getter for Bathroom distance from specified location
     * @paramm location origin location
     * @return String distance from bathroom to specified location
     * */
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

    /**
     * getter for bathroom location
     * @return LatLng bathroom latitude-longitude object
     * */
    public LatLng getLocation() {
        return bathroom.getLatLng();
    }

}
