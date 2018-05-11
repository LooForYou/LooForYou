package com.looforyou.looforyou.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.Models.Coordinates;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Custom tool to deserialize Bathroom data retrieved from the server.
 * This class converts the server's json response to custom a custom Bathroom object
 *
 * @author mingtau li
 */

public class BathroomDeserializer implements JsonDeserializer<Bathroom> {
    /**
     * Override json deserializer for custom Bathroom Object
     *
     * @param json    jsonElement object containing bathroom info
     * @param typeOfT specified type for deserializing json
     * @param context Context of JSON Deserializer
     * @return Bathroom object containing all bathroom info
     */
    @Override
    public Bathroom deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        /* jsonObject converted from JsonElement */
        JsonObject jsonObject = json.getAsJsonObject();

        /* get new instances of Calendar used for creating dates */
        Calendar open = Calendar.getInstance();
        Calendar closed = Calendar.getInstance();
        Calendar maintenanceStart = Calendar.getInstance();
        Calendar maintenanceEnd = Calendar.getInstance();

        /* specify explicit format for time representation */
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH); //pattern for date parsing from string to time

        /* initialize starting hours of operation */
        String stringOpen = jsonObject.get("start_time").getAsString();
        if (!stringOpen.equals("") && stringOpen != null) {
            try {
                open.setTime(dateFormat.parse(stringOpen));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            open = null;
        }

        /* initialize closing hours of operation */
        String stringClosed = jsonObject.get("end_time").getAsString();
        if (!stringClosed.equals("") && stringClosed != null) {
            try {
                closed.setTime(dateFormat.parse(stringClosed));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            closed = null;
        }

        /* initialize starting hours of maintenance */
        String stringMaintenanceStart = jsonObject.get("maintenance_start").getAsString();
        if (!stringMaintenanceStart.equals("") && stringMaintenanceStart != null) {
            try {
                maintenanceStart.setTime(dateFormat.parse(stringMaintenanceStart));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            maintenanceStart = null;
        }

        /* initialize closing hours of maintenance */
        String stringMaintenanceEnd = jsonObject.get("maintenance_end").getAsString();
        if (!stringMaintenanceEnd.equals("") && stringMaintenanceEnd != null) {
            try {
                maintenanceEnd.setTime(dateFormat.parse(stringMaintenanceEnd));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            maintenanceEnd = null;
        }

        /* lists for storing amenities and descriptions that bathroom offers */
        ArrayList<String> amenities = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();

        /* retrieve amenities and descriptions */
        JsonArray jsonAmenities = jsonObject.getAsJsonArray("amenities");
        JsonArray jsonDescriptions = jsonObject.getAsJsonArray("descriptions");

        /* populate amenities and descriptions with data */
        for (int i = 0; i < jsonAmenities.size(); i++) {
            amenities.add(jsonAmenities.get(i).toString().replaceAll("\"", ""));
        }
        for (int i = 0; i < jsonDescriptions.size(); i++) {
            descriptions.add(jsonDescriptions.get(i).toString().replaceAll("\"", ""));
        }

        /* set bathroom Coordinates from json*/
        Coordinates coord = new Coordinates(
                jsonObject.get("latitude").getAsDouble(),
                jsonObject.get("longitude").getAsDouble()
        );

        /* make new bathroom using previously defined data */
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
        if (!jsonObject.get("image_url").getAsString().equals("")) {
            bathroom.setImageURL(jsonObject.get("image_url").getAsString());
        }

        /* return initialized bathroom object */
        return bathroom;
    }
}
