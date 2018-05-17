package com.looforyou.looforyou.utilities;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.looforyou.looforyou.Models.Reviewer;

import java.lang.reflect.Type;

/**
 * Custom tool to deserialize Reviewer data retrieved from the server.
 * This class converts the server's json response to custom a custom Reviewer object
 *
 * @author mingtau li
 */

public class ReviewerDeserializer implements JsonDeserializer<Reviewer> {
    /**
     * override factory deserializer for custom reviewer deserialization
     *
     * @param json    jsonElement object containing reviewer info
     * @param typeOfT specified type for deserializing json
     * @param context Context of JSON Deserializer
     * @return Reviewer object containing all reviewer info
     */
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
