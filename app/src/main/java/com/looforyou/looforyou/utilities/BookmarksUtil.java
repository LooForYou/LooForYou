package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.ALL_BOOKMARKS;
import static com.looforyou.looforyou.Constants.BOOKMARKS_REL;
import static com.looforyou.looforyou.Constants.GET_USERS;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;

/**
 * Created by ibreaker on 5/6/2018.
 */

public class BookmarksUtil {
    private Context context;

    public BookmarksUtil(Context context) {
        this.context = context;
    }

    public ArrayList<Bathroom> getBookmarkedBathrooms() {
        HttpGet get = new HttpGet();
        String bookmarkedBathrooms = null;
        ArrayList<Bathroom> bathrooms = new ArrayList();
        try {
            UserUtil userUtil = new UserUtil(context);
            bookmarkedBathrooms = get.execute(GET_USERS+userUtil.getUserID()+ALL_BOOKMARKS+TOKEN_QUERY+userUtil.getUserToken()).get();
            if(bookmarkedBathrooms.isEmpty()) return null;
            JsonArray jsonObject = new JsonParser().parse(bookmarkedBathrooms).getAsJsonArray();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Bathroom.class, new BathroomDeserializer());
            Gson gson = gsonBuilder.create();

            try {
                bathrooms = new ArrayList<Bathroom>(Arrays.asList(gson.fromJson(jsonObject, Bathroom[].class)));
            }catch(Exception e){}

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bathrooms;
    }

    public ArrayList<String> getBookmarkedBathroomIds() {
        ArrayList<Bathroom> bathrooms = getBookmarkedBathrooms();
        ArrayList<String> bathroomIds = new ArrayList<String>();
        for(Bathroom b : bathrooms) {
            bathroomIds.add(b.getId());
        }
        return bathroomIds;
    }

    public boolean unBookmarkBathroom(String bathroomId) {
        HttpDelete delete = new HttpDelete();
        UserUtil userUtil = new UserUtil(context);
        try {
            delete.execute(GET_USERS + userUtil.getUserID() + BOOKMARKS_REL + bathroomId + TOKEN_QUERY + userUtil.getUserToken());
        }catch(Exception e) {
            return false;
        }
        return true;
    }

    public boolean bookmarkBathroom(String bathroomId) {
        HttpPut put = new HttpPut(new JSONObject());
        UserUtil userUtil = new UserUtil(context);
        try {
            put.execute(GET_USERS + userUtil.getUserID() + BOOKMARKS_REL + bathroomId + TOKEN_QUERY + userUtil.getUserToken());
        }catch(Exception e){
            return false;
        }
        return true;
    }


}
