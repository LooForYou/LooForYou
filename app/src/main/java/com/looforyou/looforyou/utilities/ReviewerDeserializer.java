package com.looforyou.looforyou.utilities;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.looforyou.looforyou.Models.Review;
import com.looforyou.looforyou.Models.Reviewer;

import java.lang.reflect.Type;

/**
 * Created by ibreaker on 3/11/2018.
 */

public class ReviewerDeserializer implements JsonDeserializer<Reviewer> {
    @Override
    public Reviewer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        String firstName = jsonObject.get("first_name").getAsString();
        String lastName = jsonObject.get("last_name").getAsString();
        String userName = jsonObject.get("username").getAsString();
        String imageUrl = jsonObject.get("image_url").getAsString();
        String email = jsonObject.get("email").getAsString();
        String id = jsonObject.get("id").getAsString();

        Reviewer reviewer = new Reviewer(firstName, lastName, userName, imageUrl, email, id);

        return reviewer;

    }
}
