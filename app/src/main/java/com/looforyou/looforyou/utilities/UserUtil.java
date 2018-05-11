package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.looforyou.looforyou.Models.Token;
import com.looforyou.looforyou.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.GET_USERS;
import static com.looforyou.looforyou.Constants.LOGIN;
import static com.looforyou.looforyou.Constants.LOGOUT;
import static com.looforyou.looforyou.Constants.ONE_YEAR;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;

/**
 * A utility class used for User-specific data such as saving tokens, ids, etc.
 * Handles user login and logout as well
 *
 * @author mingtau li
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

    public UserUtil(Context context) {
        this.context = context;
        userIDPreferences = context.getSharedPreferences(context.getString(R.string.saved_userID), Context.MODE_PRIVATE);
        userTokenPreferences = context.getSharedPreferences(context.getString(R.string.saved_userToken), Context.MODE_PRIVATE);

        userID = userIDPreferences.getString(context.getString(R.string.saved_userID), defaultValue);
        userToken = userTokenPreferences.getString(context.getString(R.string.saved_userToken), defaultValue);

        userIDEditor = userIDPreferences.edit();
        userTokenEditor = userTokenPreferences.edit();

    }

    /**
     * attempts to log user in using servercall
     *
     * @param username username input field
     * @param password password input field
     * @param context  parent context
     */
    public static boolean logIn(EditText username, TextInputEditText password, Context context) {
        /* set up arguments for server call */
        Map<String, String> credentials = new HashMap<String, String>();
        if (username.getText().toString().toLowerCase().contains("@")) {
            credentials.put("email", username.getText().toString().toLowerCase().trim());
        } else {
            credentials.put("username", username.getText().toString().toLowerCase().trim());
        }
        credentials.put("password", password.getText().toString());
        credentials.put("ttl", String.valueOf(ONE_YEAR));

        /* log in via server call */
        HttpPost post = new HttpPost(credentials);
        String result = null;
        try {
            result = post.execute(LOGIN).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (result.isEmpty()) {
            Toast.makeText(context, "invalid username or password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Token.class, new TokenDeserializer());
            Gson gson = gsonBuilder.create();
            Token token = gson.fromJson(result, Token.class);

            /* set userToken and ID to shared preferences */
            setUserToken(token.getID());
            setUserID(token.getUserID());
        }
        return true;
    }

    /**
     * checks to see if user is logged in by comparing userId retrieved from server to user id stored in preferences.
     * if matched, user is logged in
     * return boolean logged in
     */
    public static boolean isLoggedIn() {
        HttpGet getAuthenticatedUser = new HttpGet();
        String result = null;
        try {
            result = getAuthenticatedUser.execute(GET_USERS + userID + TOKEN_QUERY + userToken).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (result.isEmpty()) return false;
        String id = "";
        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        try {
            id = jsonObject.get("id").getAsString();

        } catch (Exception e) {
        }
        return id.equals(userID);
    }

    /**
     * getter for user id
     *
     * @return String user id
     */
    public static String getUserID() {
        return userID;
    }

    /**
     * saves user id to sharedpreferences
     *
     * @param id user Id
     */
    public static void setUserID(String id) {
        userIDEditor.putString(context.getString(R.string.saved_userID), id);
        userIDEditor.commit();
    }

    /**
     * retrieves user token
     *
     * @return String saved userToken
     */
    public static String getUserToken() {
        return userToken;
    }

    /**
     * saves user token to sharedpreferences
     *
     * @param id user Id
     */
    public static void setUserToken(String id) {
        userTokenEditor.putString(context.getString(R.string.saved_userToken), id);
        userTokenEditor.commit();
    }

    /* logs user out
    * @return String logOut result */
    public static String LogOut() {
        HttpPost post = new HttpPost(new JSONObject());
        String result = null;
        setUserID(defaultValue);
        setUserToken(defaultValue);
        try {
            result = post.execute(LOGOUT + TOKEN_QUERY + userToken).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
