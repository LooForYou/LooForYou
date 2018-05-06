package com.looforyou.looforyou.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * This is activity for displaying user profile information
 *
 * @author: mingtau li
 * @author Peter Bouris
 * @author Phoenix Grimmett
 */

public class ProfileActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {
    /* handler used for loading fragments */
    private Handler mHandler = null;
    /* Imageview for profile picture */
    ImageView profilePic = null;
    /* textview for user's name */
    TextView name = null;
    /* textview for user's username */
    TextView username = null;
    /* Button for logging out */
    Button logout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* change title of actionbar */
        getSupportActionBar().setTitle("My Profile");

        /* initialize handler */
        mHandler = new Handler();

        /* highlights tab associated with activity */
        TabControl tabb = new TabControl(this);
        tabb.tabs(ProfileActivity.this, R.id.tab_profile);

        /* intialize current Views */
        initializeComponents();

        /* if user is not logged in, load login fragment */
        if (!new UserUtil(this).isLoggedIn()) {
            loadLoginFragment();
        } else {
            onLoggedIn();
        }
        /* bind clicklistener to logout button*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* closes activity and logs out */
                finish();
                new UserUtil(ProfileActivity.this).LogOut();
                startActivity(getIntent());
            }
        });
    }

    /**
     * initializes current items in views
     * */
    public void initializeComponents() {
        profilePic = (ImageView) findViewById(R.id.profilePic);
        name = (TextView) findViewById(R.id.name);
        username = (TextView) findViewById(R.id.username);
        logout = (Button) findViewById(R.id.logout);
        Picasso.get().load(R.drawable.no_image_uploaded_3).fit().centerCrop().into(profilePic);
    }

    /**
     * defines what system should do if user is logged in
     * */
    public void onLoggedIn() {
        HttpGet get = new HttpGet();
        String accountInfo = null;
        try {
            /* get user data from server */
            UserUtil userUtil = new UserUtil(this);
            accountInfo = get.execute(GET_USERS + userUtil.getUserID() + TOKEN_QUERY + userUtil.getUserToken()).get();
            if (accountInfo.isEmpty()) return;
            JsonObject jsonObject = new JsonParser().parse(accountInfo).getAsJsonObject();
            try {
                /* display user name */
                name.setText(jsonObject.get("first_name").getAsString() + " " + jsonObject.get("last_name").getAsString());
            } catch (Exception e) {
            }
            try {
                /* display username */
                username.setText("@" + jsonObject.get("username").getAsString());
            } catch (Exception e) {
            }
            try {
                /* display profile picture */
                String pf = jsonObject.get("image_url").getAsString();
                if (!pf.contains("freedomappapk") && !pf.isEmpty()) {
                    Picasso.get().load(pf).fit().centerCrop().into(profilePic);
                }

            } catch (Exception e) {
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns respective fragment that user
     * selected from navigation menu
     */
    private void loadLoginFragment() {
        /* set toolbar title */
        getSupportActionBar().setTitle("Log In or Sign Up");

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                /* update the main content by replacing fragments */
                final Fragment fragment = new LoginFragment();
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }

            ;
        };


        /* If mPendingRunnable is not null, then add to the message queue */
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        /* refresh toolbar menu */
        invalidateOptionsMenu();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

