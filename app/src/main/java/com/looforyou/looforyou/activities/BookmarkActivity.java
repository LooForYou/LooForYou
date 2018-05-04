package com.looforyou.looforyou.activities;

import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.fragments.LoginFragment;
import com.looforyou.looforyou.utilities.HttpGet;
import com.looforyou.looforyou.utilities.TabControl;
import com.looforyou.looforyou.utilities.UserUtil;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.GET_USERS;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;

public class BookmarkActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener{
    private View view;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        TabControl tabb = new TabControl(this);
        tabb.tabs(BookmarkActivity.this,R.id.tab_bookmarks);

        getSupportActionBar().setTitle("My Bookmarks");

        mHandler = new Handler();
        if(!new UserUtil(this).isLoggedIn()) {
            loadLoginFragment();
        }else {
            onLoggedIn();
        }
    }


    public void onLoggedIn() {
        /*
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

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        */
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
