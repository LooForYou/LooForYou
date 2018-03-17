package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by ibreaker on 3/11/2018.
 */

public class LooLoader {
    private static final String TAG = "TEST FEEDLIST LooLoader";
    //    private static final String API_URL = "http://ec2-54-183-105-234.us-west-1.compute.amazonaws.com:9000/api/Bathrooms?access_token=pBWBnDboL5RSFunF6E08EZJGD1skk9kkX3xAKJwDK4VLhVgHg0nYdvUjz6Oh7401\n";
    public static List<Bathroom> loadBathrooms(Context context){
    final String API_URL = context.getResources().getString(R.string.loopbacks_api_root)+"Bathrooms";
        try{
            List<Bathroom> bathroomList = new ArrayList<>();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Bathroom.class, new BathroomDeserializer());
            Gson gson = gsonBuilder.create();

            //Loopback Async http requiest
            String result;
            HttpGet getRequest = new HttpGet();
            result = getRequest.execute(API_URL).get();
            //
            ArrayList<Bathroom> bathrooms = new ArrayList<Bathroom>(Arrays.asList(gson.fromJson(result, Bathroom[].class)));

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
