package com.looforyou.looforyou.utilities;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.Models.Coordinates;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ibreaker on 3/11/2018.
 */

public class BathroomDeserializer implements JsonDeserializer<Bathroom>{
/*    public static JsonDeserializer<Bathroom> deserializer = new JsonDeserializer<Bathroom>() {
        @Override
        public Bathroom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();*/
/*
*//*
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 1988);
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date dateRepresentation = cal.getTime();
*//*

            Date date = new Date(
                    jsonObject.get("start_time").getAsInt(),
                    jsonObject.get("month").getAsInt(),
                    jsonObject.get("day").getAsInt()
            );

             ArrayList<String> maintenanceDays = new ArrayList<>(Arrays.asList(jsonObject.get("maintenance_days").getAsString()));
            ArrayList<String> amenities = new ArrayList<>(Arrays.asList(jsonObject.get("amenities").getAsString()));
            ArrayList<String> descriptions = new ArrayList<>(Arrays.asList(jsonObject.get("descriptions").getAsString()));

            Coordinates coord = new Coordinates(
                    jsonObject.get("latitude").getAsDouble(),
                    jsonObject.get("longitude").getAsDouble()
            );
//            return new Bathroom(
//                    jsonObject.get("name").getAsString(),
//                    coord,
//                    jsonObject.get("rating").getAsDouble(),
//                    new Date(),
//                    new Date(),
//                    new Date(),
//                    new Date(),
//                    maintenanceDays,
//                    jsonObject.get("bookmarked").getAsBoolean(),
//                    amenities,
//                    descriptions
//            );
            Bathroom theBathroom = new Bathroom();
//            theBathroom.setName(jsonObject.get("name").getAsString());
            return theBathroom;
//        }
//    };*/
    @Override
    public Bathroom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
//        JsonObject jsonObject = new JsonParser.parse(json).getAsJsonObject();
/*
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 1988);
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date dateRepresentation = cal.getTime();
*/
        Log.v("bathroom deserializer",jsonObject.get("name").getAsString());
        Date date = new Date(
                jsonObject.get("start_time").getAsInt(),
                jsonObject.get("month").getAsInt(),
                jsonObject.get("day").getAsInt()
        );

        ArrayList<String> maintenanceDays = new ArrayList<>(Arrays.asList(jsonObject.get("maintenance_days").getAsString()));
        ArrayList<String> amenities = new ArrayList<>(Arrays.asList(jsonObject.get("amenities").getAsString()));
        ArrayList<String> descriptions = new ArrayList<>(Arrays.asList(jsonObject.get("descriptions").getAsString()));

        Coordinates coord = new Coordinates(
                jsonObject.get("latitude").getAsDouble(),
                jsonObject.get("longitude").getAsDouble()
        );
            return new Bathroom(
                    jsonObject.get("name").getAsString(),
                    coord,
                    jsonObject.get("rating").getAsDouble(),
                    new Date(),
                    new Date(),
                    new Date(),
                    new Date(),
                    maintenanceDays,
                    jsonObject.get("bookmarked").getAsBoolean(),
                    amenities,
                    descriptions,
                    jsonObject.get("address").getAsString()
            );
/*        Bathroom theBathroom = new Bathroom();
//            theBathroom.setName(jsonObject.get("name").getAsString());
        return theBathroom;*/
    }
}
