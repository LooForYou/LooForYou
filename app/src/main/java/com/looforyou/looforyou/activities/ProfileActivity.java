package com.looforyou.looforyou.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.fragments.LoginFragment;
import com.looforyou.looforyou.utilities.HttpGet;
import com.looforyou.looforyou.utilities.HttpPost;
import com.looforyou.looforyou.utilities.TabControl;
import com.looforyou.looforyou.utilities.UserUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.GET_USERS;
import static com.looforyou.looforyou.Constants.LOGIN;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;

public class ProfileActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {
    private Handler mHandler = null;
    private View view = null;
    ImageView profilePic = null;
    TextView name = null;
    TextView username = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("My Profile");

        mHandler = new Handler();
        view = getSupportActionBar().getCustomView();

        TabControl tabb = new TabControl(this);
        tabb.tabs(ProfileActivity.this, R.id.tab_profile);

        profilePic = (ImageView) findViewById(R.id.profilePic);
        name = (TextView) findViewById(R.id.name);
        username = (TextView) findViewById(R.id.username);
        Button logout = (Button) findViewById(R.id.logout);
        Picasso.get().load(R.drawable.no_image_uploaded_3).fit().centerCrop().into(profilePic);

        if(!new UserUtil(this).isLoggedIn()) {
            loadLoginFragment();
        }else {
            onLoggedIn();
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                new UserUtil(ProfileActivity.this).LogOut();
                startActivity(getIntent());
            }
        });
    }

    public void onLoggedIn() {
        HttpGet get = new HttpGet();
        String accountInfo = null;
        try {
            UserUtil userUtil = new UserUtil(this);
            accountInfo = get.execute(GET_USERS+userUtil.getUserID()+TOKEN_QUERY+userUtil.getUserToken()).get();
            if(accountInfo.isEmpty()) return;
            JsonObject jsonObject = new JsonParser().parse(accountInfo).getAsJsonObject();
            try {
                name.setText(jsonObject.get("first_name").getAsString()+" "+jsonObject.get("last_name").getAsString());
            }catch(Exception e){}
            try {
                username.setText("@"+jsonObject.get("username").getAsString());
            }catch(Exception e){}
            try {
                String pf = jsonObject.get("image_url").getAsString();
                if(!pf.contains("freedomappapk")){
                    Picasso.get().load(pf).fit().centerCrop().into(profilePic);
                }

            }catch(Exception e){}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadLoginFragment() {
        // set toolbar title
        getSupportActionBar().setTitle("Log In or Sign Up");

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                final Fragment fragment = new LoginFragment();
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }

            ;
        };


        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

