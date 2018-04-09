package com.looforyou.looforyou.utilities;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.looforyou.looforyou.Models.Token;

import java.lang.reflect.Type;

/**
 * Created by quanl on 3/29/2018.
 */

public class TokenDeserializer implements JsonDeserializer<Token>{
    @Override
    public Token deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        Token token = new Token(
                jsonObj.get("id").getAsString(),
                jsonObj.get("userId").getAsString()
        );

        return  token;
    }
}
