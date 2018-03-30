package com.looforyou.looforyou;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.looforyou.looforyou.Models.Token;
import com.looforyou.looforyou.utilities.HttpPost;
import com.looforyou.looforyou.utilities.TokenDeserializer;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.looforyou.looforyou.Constants.LOGIN;
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
    public  void check_HttpPost() throws  Exception{

        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "looforyou@gmail.com");
        login.put("password", "123");

        HttpPost post = new HttpPost(login);
        String result = post.execute(LOGIN).get();
        assertFalse(result.isEmpty());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Token.class, new TokenDeserializer());
        Gson gson = gsonBuilder.create();
        Token token = gson.fromJson(result, Token.class);
        assertNotNull(token);
        assertFalse(token.getID().isEmpty());
        assertTrue(token.getUserID().equals("5abd917a0c75fc5f5f08cdd0"));
    }
}
