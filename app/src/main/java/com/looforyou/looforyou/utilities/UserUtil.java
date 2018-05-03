package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.looforyou.looforyou.R;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.LOGIN;
import static com.looforyou.looforyou.Constants.LOGOUT;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;

/**
 * Created by ibreaker on 5/2/2018.
 */

public class UserUtil {
    private static Context context;
    private static String userID;
    private static String userToken;
    private static SharedPreferences userIDPreferences;
    private static SharedPreferences userTokenPreferences;
    private static SharedPreferences.Editor userIDEditor;
    private static SharedPreferences.Editor userTokenEditor;
    private static String defaultValue = "";

    public UserUtil(Context context){
        this.context = context;
        userIDPreferences = context.getSharedPreferences(context.getString(R.string.saved_userID), Context.MODE_PRIVATE);
        userTokenPreferences = context.getSharedPreferences(context.getString(R.string.saved_userToken),Context.MODE_PRIVATE);

        userID = userIDPreferences.getString(context.getString(R.string.saved_userID), defaultValue);
        userToken = userTokenPreferences.getString(context.getString(R.string.saved_userToken), defaultValue);

        userIDEditor = userIDPreferences.edit();
        userTokenEditor = userTokenPreferences.edit();

    }

    public static void setUserID(String id) {
        userIDEditor.putString(context.getString(R.string.saved_userID), id);
        userIDEditor.commit();
    }

    public static void setUserToken(String id) {
        userTokenEditor.putString(context.getString(R.string.saved_userToken), id);
        userTokenEditor.commit();
    }

    public static boolean isLoggedIn() {
        return !userToken.isEmpty();
    }

    public static String getUserID() {
        return userID;
    }

    public static String getUserToken() {
        return userToken;
    }

    public static String LogOut() {
        HttpPost post = new HttpPost(new JSONObject());
        String result = null;
        setUserID(defaultValue);
        setUserToken(defaultValue);
        try {
            result = post.execute(LOGOUT+TOKEN_QUERY+userToken).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
