package com.looforyou.looforyou.utilities;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Authored by BooG on 4/5/18
 */
public class HttpDelete extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "DELETE";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private JSONObject params;

    @Override
    protected String doInBackground(String... params) {
        String stringUrl = params[0];
        String result = "";
        String inputLine = "";

        try {
            URL apiUrl = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setDoOutput(true);
            connection.connect();

            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader br = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while((inputLine = br.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            br.close();
            streamReader.close();
            result = stringBuilder.toString();
            connection.disconnect();

        } catch (IOException e){
            e.printStackTrace();
            result = "";
        }

        return result;
    }
    @Override
    protected void onPostExecute(String result) { super.onPostExecute(result); }

}