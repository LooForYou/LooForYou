package com.looforyou.looforyou.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.looforyou.looforyou.Models.Bathroom;

import java.lang.reflect.Type;

public class BathroomSerializer implements JsonSerializer<Bathroom> {

    @Override
    public JsonElement serialize(final Bathroom bathroom, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", bathroom.getName());
        jsonObject.addProperty("rating", bathroom.getRating());
        jsonObject.addProperty("latitude", bathroom.getCoordinates().latitude);
        jsonObject.addProperty("longitude", bathroom.getCoordinates().longitude);
        jsonObject.addProperty("address", bathroom.getAddress());
        jsonObject.addProperty("start_time", bathroom.getStartTime().toString());
        jsonObject.addProperty("end_time", bathroom.getEndTime().toString());
        jsonObject.addProperty("maintenance_start", bathroom.getMaintenanceStart().toString());
        jsonObject.addProperty("maintenance_end", bathroom.getMaintenanceEnd().toString());
        jsonObject.addProperty("maintenance_days", bathroom.getMaintenanceDays());
        jsonObject.addProperty("bookmarked", bathroom.getBookmarked());

        final JsonArray amenities = new JsonArray();
        for (final String amenity : bathroom.getAmenities()) {
            final JsonPrimitive value = new JsonPrimitive(amenity);
            amenities.add(value);
        }

        jsonObject.add("amenities", amenities);

        final JsonArray desciprtions = new JsonArray();
        for (final String desciption : bathroom.getDescriptions()) {
            final JsonPrimitive value = new JsonPrimitive(desciption);
            desciprtions.add(value);
        }

        jsonObject.add("descriptions", desciprtions);

        jsonObject.addProperty("image_url", "");
        return jsonObject;
    }

}
