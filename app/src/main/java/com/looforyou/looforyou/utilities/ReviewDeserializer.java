package com.looforyou.looforyou.utilities;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.looforyou.looforyou.Models.Review;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Custom tool to deserialize Review data retrieved from the server.
 * This class converts the server's json response to custom a custom Review object
 *
 * @author mingtau li
 */

public class ReviewDeserializer implements JsonDeserializer<Review> {
    /**
     * override factory deserializer for custom review deserialization
     *
     * @param json    jsonElement object containing review info
     * @param typeOfT specified type for deserializing json
     * @param context Context of JSON Deserializer
     * @return Reviewer object containing all review info
     */
    @Override
    public Review deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int rating = jsonObject.get("rating").getAsInt();
        String content = jsonObject.get("content").getAsString();

        Calendar timeCreated = Calendar.getInstance();
        Calendar timeUpdated = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH); //pattern for date parsing from string to time
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        String timeCreatedString = null;
        try {
            timeCreatedString = jsonObject.get("time_created").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (timeCreatedString != null && !timeCreatedString.equals("")) {
            try {
                timeCreated.setTime(dateFormat.parse(timeCreatedString));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            timeCreated = null;
        }

        String timeUpdatedString = null;
        try {
            timeUpdatedString = jsonObject.get("time_updated").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (timeUpdatedString != null && !timeUpdatedString.equals("")) {
            try {
                timeUpdated.setTime(dateFormat.parse(timeUpdatedString));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            timeUpdated = null;
        }


        int likes = 0;
        try {
            likes = jsonObject.get("likes").getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String id = null;
        try {
            id = jsonObject.get("id").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String bathroomId = null;
        try {
            bathroomId = jsonObject.get("bathroomId").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String submittedById = "";
        try {
            submittedById = jsonObject.get("submittedById").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            submittedById = jsonObject.get("accountId").getAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* create new review object initialized with deserialized data to be returned */
        Review review = new Review(rating, content, likes, id, bathroomId, submittedById);
        if (timeCreated != null) review.setTimeCreated(timeCreated.getTime());
        if (timeUpdated != null) review.setTimeUpdated(timeUpdated.getTime());
        return review;

    }
}
