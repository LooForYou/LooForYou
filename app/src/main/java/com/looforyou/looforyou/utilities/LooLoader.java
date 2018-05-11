package com.looforyou.looforyou.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.looforyou.looforyou.Models.Bathroom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static com.looforyou.looforyou.Constants.GET_BATHROOMS;


/**
 * Utility class used to load bathrooms from server
 *
 * @author mingtau li
 */

public class LooLoader {
    /* logtag for logs */
    private static final String TAG = "LooLoader feedlist";
    /* application context */
    private static Context appContext;

    /**
     * method used for loading bathrooms
     * @param context parent activity context
     * @param sortBy varArgs for optional sorting values */
    public static List<Bathroom> loadBathrooms(Context context, String... sortBy) {
        appContext = context;
        String API_URL = GET_BATHROOMS;
        try {
            List<Bathroom> bathroomList = new ArrayList<>();

            /* initialize gsonbuilder and set adapter to bathroom deserializer */
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Bathroom.class, new BathroomDeserializer());
            Gson gson = gsonBuilder.create();

            /* Loopback Async http requiest */
            String result;
            HttpGet getRequest = new HttpGet();
            result = getRequest.execute(API_URL).get();
            ArrayList<Bathroom> bathrooms;
            if (!result.equals("")) {
                /* populate list of bathrooms from server */
                bathrooms = new ArrayList<Bathroom>(Arrays.asList(gson.fromJson(result, Bathroom[].class)));
            } else {
                /* return blank custom bathrom object */
                bathrooms = new ArrayList<Bathroom>();
                Bathroom noBathroom = new Bathroom("");
                noBathroom.setName("We weren't able to find bathrooms in the area");
                noBathroom.setImageURL("were-sorry.png");
                noBathroom.setDescriptions(new ArrayList<String>(Arrays.asList("please try again")));
                bathrooms.add(noBathroom);
            }

            /* if sortBy has parameters, apply sort */
            if (sortBy.length > 0) {
                final Location currentLocation = getCurrentLocation();
                /* if sort by distance available, sort it from location closest to user */
                if (String.valueOf(sortBy[0]).equalsIgnoreCase("distance")) {
                    Log.v("looLoader", "sorting by distance:");
                    Collections.sort(bathrooms, new Comparator<Bathroom>() {
                        @Override
                        public int compare(Bathroom b1, Bathroom b2) {
                            double dist1 = MetricConverter.distanceBetweenInMiles(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(b1.getLatLng().latitude, b1.getLatLng().longitude));
                            double dist2 = MetricConverter.distanceBetweenInMiles(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(b2.getLatLng().latitude, b2.getLatLng().longitude));
                            if (dist2 < dist1) return 1;
                            else if (dist2 > dist1) return -1;
                            return 0;
                        }
                    });

                }
            }
            return bathrooms;
        } catch (Exception e) {
            Log.d(TAG, "parseException " + e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method looks for best location provider by checking availablility of gps or network location
     * @return Location most accurate location
     */
    public static Location getCurrentLocation() {
        /* new location manager for obtaining current location */
        LocationManager mLocationManager = (LocationManager) appContext.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String p : providers) {
            /* permission check */
            if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location location = mLocationManager.getLastKnownLocation(p);
            if (location == null) {
                continue;
            }
            /* find best location available from list of providers */
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }
        return bestLocation;
    }

}
