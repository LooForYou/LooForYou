package com.looforyou.looforyou.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.looforyou.looforyou.Manifest;
import com.looforyou.looforyou.Models.Bathroom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.looforyou.looforyou.Constants.*;

/**
 * Created by ibreaker on 3/11/2018.
 */

public class LooLoader {
    private static final String TAG = "TEST FEEDLIST LooLoader";

    //    private static final String API_URL = "http://ec2-54-183-105-234.us-west-1.compute.amazonaws.com:9000/api/Bathrooms?access_token=pBWBnDboL5RSFunF6E08EZJGD1skk9kkX3xAKJwDK4VLhVgHg0nYdvUjz6Oh7401\n";
    public static List<Bathroom> loadBathrooms(Context context, String... sortBy) {

        String API_URL = GET_BATHROOMS;
        try {
            List<Bathroom> bathroomList = new ArrayList<>();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Bathroom.class, new BathroomDeserializer());
            Gson gson = gsonBuilder.create();

            //Loopback Async http requiest
            String result;
            HttpGet getRequest = new HttpGet();
            result = getRequest.execute(API_URL).get();
            Log.v("result_val", result);
            ArrayList<Bathroom> bathrooms;
            if (!result.equals("")) {
                bathrooms = new ArrayList<Bathroom>(Arrays.asList(gson.fromJson(result, Bathroom[].class)));
            } else {
                bathrooms = new ArrayList<Bathroom>();
                Bathroom noBathroom = new Bathroom("");
                noBathroom.setName("We weren't able to find bathrooms in the area");
                noBathroom.setImageURL("were-sorry.png");
                noBathroom.setDescriptions(new ArrayList<String>(Arrays.asList("please try again")));
                bathrooms.add(noBathroom);
            }

            if (sortBy.length > 0) {
                LocationManager locationManager = (LocationManager)
                        context.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();

                //get current location
                @SuppressLint("MissingPermission") final Location currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                if(String.valueOf(sortBy[0]).equalsIgnoreCase("distance")){
                    Log.v("looLoader","sorting by distance:");
                    Collections.sort(bathrooms, new Comparator<Bathroom>() {
                        @Override
                        public int compare(Bathroom b1, Bathroom b2) {
                            double dist1 = MetricConverter.distanceBetweenInMiles(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),new LatLng(b1.getLatLng().latitude,b1.getLatLng().longitude));
                            double dist2 = MetricConverter.distanceBetweenInMiles(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),new LatLng(b2.getLatLng().latitude,b2.getLatLng().longitude));
                            if(dist2 < dist1) return 1;
                            else if(dist2 > dist1) return -1;
                            return 0;
                        }
                    });

                }
            }

                for(Bathroom b : bathrooms){
                    Log.v(TAG,"Bathroom:\n "+b);
                    bathroomList.add(b);
                }


            return bathroomList;
        }catch (Exception e){
            Log.d(TAG,"parseException " + e);
            e.printStackTrace();
            return null;
        }
    }

    /* helper function to load json file from assets folder */
    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream is=null;
        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG,"path "+jsonFileName);
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d(TAG,"write exception");
            return null;
        }
        return json;
    }
}
