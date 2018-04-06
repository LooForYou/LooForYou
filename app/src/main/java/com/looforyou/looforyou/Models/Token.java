package com.looforyou.looforyou.Models;

import com.google.gson.annotations.JsonAdapter;
import com.looforyou.looforyou.utilities.TokenDeserializer;

import java.io.Serializable;

/**
 * Created by quanl on 3/29/2018.
 */

@JsonAdapter(TokenDeserializer.class)
public class Token implements Serializable {
    private String id;
    private String userID;

    public Token(){
        id = "";
        userID = "";
    }

    public  Token(String id, String userID){
        this.id = id;
        this.userID = userID;
    }

    public String getID(){
        return this.id;
    }

    public String getUserID(){
        return userID;
    }

    public void setID(String id){
        this.id = id;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public String tokenizeURL(String url){
        String result = url;

        if (url.charAt(url.length() - 1) != '?'){
            result += '?';
        }

        result += ("access_token=" + this.id);

        return  result;
    }

    @Override
    //Add to end of request url that requires authorization
    public String toString() {
        return "access_token=" + this.id;
    }
}
