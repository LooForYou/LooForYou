package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.Models.Coordinates;


import org.json.JSONArray;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.support.v7.widget.AppCompatDrawableManager.get;

/**
 * Created by ibreaker on 3/11/2018.
 */

public class BathroomDeserializer implements JsonDeserializer<Bathroom>{
    @Override
    public Bathroom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
//        JsonObject jsonObject = new JsonParser.parse(json).getAsJsonObject();

            Calendar open = Calendar.getInstance();
            Calendar closed = Calendar.getInstance();
            Calendar maintenanceStart = Calendar.getInstance();
            Calendar maintenanceEnd = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH); //pattern for date parsing from string to time
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        String stringOpen = jsonObject.get("start_time").getAsString();
        if(!stringOpen.equals("") && stringOpen != null) {
            try {
                open.setTime(dateFormat.parse(stringOpen));
                Log.v("time:::",String.valueOf(dateFormat.parse(stringOpen)));
            } catch (ParseException e) {
                e.printStackTrace();
                try {
                    Log.v("time:::","parse start time failed");
                    Log.v("time:::",String.valueOf(dateFormat.parse(stringOpen)));
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }else {
            open = null;
        }

        String stringClosed = jsonObject.get("end_time").getAsString();
        if(!stringClosed.equals("") && stringClosed != null) {
            try {
                closed.setTime(dateFormat.parse(stringClosed));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            closed = null;
        }

        String stringMaintenanceStart = jsonObject.get("maintenance_start").getAsString();
        if(!stringMaintenanceStart.equals("") && stringMaintenanceStart != null) {
            try {
                maintenanceStart.setTime(dateFormat.parse(stringMaintenanceStart));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            maintenanceStart = null;
        }

        String stringMaintenanceEnd = jsonObject.get("maintenance_end").getAsString();
        if(!stringMaintenanceEnd.equals("") && stringMaintenanceEnd != null) {
            try {
                maintenanceEnd.setTime(dateFormat.parse(stringMaintenanceEnd));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            maintenanceEnd = null;
        }


//        ArrayList<String> maintenanceDays = new ArrayList<>(Arrays.asList(jsonObject.get("maintenance_days").getAsString()));
        ArrayList<String> amenities = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        JsonArray jsonAmenities = jsonObject.getAsJsonArray("amenities");
        JsonArray jsonDescriptions = jsonObject.getAsJsonArray("descriptions");
        for(int i = 0; i < jsonAmenities.size(); i++){ amenities.add(jsonAmenities.get(i).toString().replaceAll("\"","")); }
        for(int i = 0; i < jsonDescriptions.size(); i++){ descriptions.add(jsonDescriptions.get(i).toString().replaceAll("\"","")); }

        Coordinates coord = new Coordinates(
                jsonObject.get("latitude").getAsDouble(),
                jsonObject.get("longitude").getAsDouble()
        );
            Bathroom bathroom = new Bathroom(
                    jsonObject.get("id").getAsString(),
                    jsonObject.get("name").getAsString(),
                    coord,
                    jsonObject.get("rating").getAsDouble(),
                    open != null ? open.getTime() : null,
                    closed != null ? closed.getTime() : null,
                    maintenanceStart != null ? maintenanceStart.getTime() : null,
                    maintenanceEnd != null ? maintenanceEnd.getTime() : null,
                    jsonObject.get("maintenance_days").getAsString(),
                    jsonObject.get("bookmarked").getAsBoolean(),
                    amenities,
                    descriptions,
                    jsonObject.get("address").getAsString()
            );
            if(!jsonObject.get("image_url").getAsString().equals("")){
                Log.v("drawable::",jsonObject.get("image_url").getAsString());
                bathroom.setImageURL(jsonObject.get("image_url").getAsString());
            }
            return bathroom;
/*        Bathroom theBathroom = new Bathroom();
//            theBathroom.setName(jsonObject.get("name").getAsString());
        return theBathroom;*/
    }
}
