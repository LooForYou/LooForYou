package com.looforyou.looforyou;

/**
 * Created by quanl on 3/23/2018.
 */

public class APIConnectionException extends Exception {
    public APIConnectionException(){
        super("Connection to the api failed");
    }

    public  APIConnectionException(String message){
        super(message);
    }
}
