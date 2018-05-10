package com.looforyou.looforyou.utilities;

import android.os.AsyncTask;
import android.provider.MediaStore;

import com.looforyou.looforyou.APIConnectionException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

/**
 * Created by quanl on 3/16/2018.
 */

public class HttpPost extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "POST";
    public static  final  int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    private JSONObject json;
    private File file;

    public HttpPost(Map<String, String> data){
        if (data != null){
            this.json = new JSONObject(data);
            this.file = null;
        }
    }

    public HttpPost(JSONObject data){
        if (data != null){
            this.json = data;
            this.file = null;
        }
    }

    public HttpPost(File image){
        if (image.exists()){
            this.json = null;
            this.file = image;
        }
    }

    protected String postWithJson(HttpURLConnection connection) throws IOException, APIConnectionException{
        connection.setRequestProperty("Content-Type", "application/json");
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(this.json.toString());
        writer.flush();

        int statusCode = connection.getResponseCode();
        if (statusCode == 200) {
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine = "";
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();
            return stringBuilder.toString();
        }else{
            throw new APIConnectionException("Status Code: " + statusCode);
        }
    }

    protected String postWithFile(HttpURLConnection connection) throws IOException{
        String boundary = "looforyou";
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
        stream.writeBytes("--" + boundary + "\r\n");
        stream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"" + file.getName() + "\"\r\n\r\n");

        FileInputStream input = new FileInputStream(file);

        int available = input.available();
        int maxBuffer = 1024 * 1024;
        int bufferSize = Math.min(available, maxBuffer);
        byte[] buffer = new byte[bufferSize];

        int res = input.read(buffer, 0, bufferSize);

        while (res > 0){
            stream.write(buffer, 0, bufferSize);
            available = input.available();
            bufferSize = Math.min(available, maxBuffer);
            res = input.read(buffer, 0, bufferSize);
        }

        stream.writeBytes("\r\n--" + boundary + "--\r\n");
        stream.flush();
        stream.close();
        input.close();

        int statusCode = connection.getResponseCode();

        if (statusCode == 200) {
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine = "";
            while ((inputLine = reader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            reader.close();
            streamReader.close();
            return stringBuilder.toString();
        }

        return "";
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
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);

            if (this.json != null){
                result = postWithJson(connection);
            }else if (this.file != null){
                result = postWithFile(connection);
            }

            connection.disconnect();
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