package com.looforyou.looforyou.utilities;

import android.os.AsyncTask;

import com.looforyou.looforyou.APIConnectionException;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by quanl on 3/16/2018.
 */

public class HttpPost extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "POST";
    public static  final  int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private JSONObject params;

    public HttpPost(Map<String, String> data){
        if (data != null){
            this.params = new JSONObject(data);
        }
    }

    public HttpPost(JSONObject data){
        if (data != null){
            params = data;
        }
    }

    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String result = "";
        String inputLine = "";

        try{
            URL apiUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            if (params != null){
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(this.params.toString());
                writer.flush();
            }

            int statusCode = connection.getResponseCode();
            if (statusCode == 200){
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                while((inputLine = reader.readLine())!= null){
                    stringBuilder.append(inputLine);
                }

                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            }else{
                throw new APIConnectionException("Status Code: " + statusCode);
            }

        }catch(IOException e){
            e.printStackTrace();
            result = "";
        }catch (APIConnectionException e){
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
