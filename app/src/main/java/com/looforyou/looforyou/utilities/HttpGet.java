package com.looforyou.looforyou.utilities;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by quanl on 3/16/2018.
 */

public class HttpGet extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "GET";
    public static  final  int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String result = "";
        String inputLine = "";


        try{
            URI apiUri = new URI(stringUrl);
            URL apiUrl = apiUri.toURL();
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while((inputLine = reader.readLine())!= null){
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();
            result = stringBuilder.toString();
            connection.disconnect();

        }catch(IOException e){
            e.printStackTrace();
            result = "";
        }catch(URISyntaxException e){
            e.printStackTrace();
            result = "";
        }

        return result;
    }
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}
