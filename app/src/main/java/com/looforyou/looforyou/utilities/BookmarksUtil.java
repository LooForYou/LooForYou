package com.looforyou.looforyou.utilities;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.looforyou.looforyou.Models.Bathroom;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.ALL_BOOKMARKS;
import static com.looforyou.looforyou.Constants.BOOKMARKS_REL;
import static com.looforyou.looforyou.Constants.GET_USERS;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;

/**
 * A utility class used for bookmarks
 *
 * @author mingtau li
 */

public class BookmarksUtil {
    /* used to retrieve context of parent activity */
    private Context context;

    /**
     * public constructor
     *
     * @param context parent context
     */
    public BookmarksUtil(Context context) {
        this.context = context;
    }

    /**
     * getter for all bookmarked bathrooms
     *
     * @return list of all bookmarked bathrooms from server
     */
    public ArrayList<Bathroom> getBookmarkedBathrooms() {
        /* initialize new http get request */
        HttpGet get = new HttpGet();
        /* execution result of httpget */
        String bookmarkedBathrooms = null;
        /* list of bathroom objects from server */
        ArrayList<Bathroom> bathrooms = new ArrayList();
        try {
            /* userUtil class for user operations */
            UserUtil userUtil = new UserUtil(context);
            /* execute get request for bathrooms */
            bookmarkedBathrooms = get.execute(GET_USERS + userUtil.getUserID() + ALL_BOOKMARKS + TOKEN_QUERY + userUtil.getUserToken()).get();
            /* cancel operation if no bathrooms found */
            if (bookmarkedBathrooms.isEmpty()) return null;
            JsonArray jsonObject = new JsonParser().parse(bookmarkedBathrooms).getAsJsonArray();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Bathroom.class, new BathroomDeserializer());
            Gson gson = gsonBuilder.create();
            try {
                /* populate bathrooms list with bookmarked bathrooms from server */
                bathrooms = new ArrayList<Bathroom>(Arrays.asList(gson.fromJson(jsonObject, Bathroom[].class)));
            } catch (Exception e) {
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bathrooms;
    }

    /**
     * getter for bookmarked ids
     *
     * @return list of ids from bookmarked bathrooms
     */
    public ArrayList<String> getBookmarkedBathroomIds() {
        ArrayList<Bathroom> bathrooms = getBookmarkedBathrooms();
        ArrayList<String> bathroomIds = new ArrayList<String>();
        for (Bathroom b : bathrooms) {
            bathroomIds.add(b.getId());
        }
        return bathroomIds;
    }

    /**
     * performs unbookmarking operation via server call
     *
     * @param bathroomId bathroom id
     * @return boolean success
     */
    public boolean unBookmarkBathroom(String bathroomId) {
        HttpDelete delete = new HttpDelete();
        UserUtil userUtil = new UserUtil(context);
        try {
            /* execute delete request for bookmark */
            delete.execute(GET_USERS + userUtil.getUserID() + BOOKMARKS_REL + bathroomId + TOKEN_QUERY + userUtil.getUserToken());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * performs bookmarking operation via server call
     *
     * @param bathroomId bathroom id
     * @return boolean success
     */
    public boolean bookmarkBathroom(String bathroomId) {
        HttpPut put = new HttpPut(new JSONObject());
        UserUtil userUtil = new UserUtil(context);
        try {
            /* execute delete request for bookmark */
            put.execute(GET_USERS + userUtil.getUserID() + BOOKMARKS_REL + bathroomId + TOKEN_QUERY + userUtil.getUserToken());
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
