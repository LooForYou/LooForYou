package com.looforyou.looforyou;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.looforyou.looforyou.Models.Token;
import com.looforyou.looforyou.utilities.HttpPost;
import com.looforyou.looforyou.utilities.TokenDeserializer;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.looforyou.looforyou.Constants.GET_BATHROOMS;
import static com.looforyou.looforyou.Constants.LOGIN;
import static com.looforyou.looforyou.Constants.UPLOAD_IMAGE;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.looforyou.looforyou", appContext.getPackageName());
    }

    @Test
    public void check_Upload() throws  Exception{
        Context appContext = InstrumentationRegistry.getTargetContext();
        AssetManager manager = appContext.getAssets();
        InputStream in = manager.open("temp_toilet_1.jpg");

        File file = File.createTempFile("image", ".jpg");
        file.deleteOnExit();

        FileOutputStream out = new FileOutputStream(file);
        int read = 0;
        int size = in.available();
        byte[] bytes = new byte[size];

        while ((read = in.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        in.close();
        out.close();
        assertTrue(file.exists());

        //Look Here to call Httppost --------------
        HttpPost post = new HttpPost(file);
        String url = GET_BATHROOMS + "5ac7bc93fc0c031481610f1a" + UPLOAD_IMAGE;
        String result = post.execute(url).get();
        //-----------------------------------------
        assertFalse(result.isEmpty());

    }

//    @Test
//    public  void check_HttpPost() throws  Exception{
//
//        Map<String, String> login = new HashMap<String, String>();
//        login.put("email", "looforyou@gmail.com");
//        login.put("password", "123");
//
//        HttpPost post = new HttpPost(login);
//        String result = post.execute(LOGIN).get();
//        assertFalse(result.isEmpty());
//
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(Token.class, new TokenDeserializer());
//        Gson gson = gsonBuilder.create();
//        Token token = gson.fromJson(result, Token.class);
//        assertNotNull(token);
//        assertFalse(token.getID().isEmpty());
//    }
}
