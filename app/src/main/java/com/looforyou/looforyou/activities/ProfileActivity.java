package com.looforyou.looforyou.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.GET_BATHROOMS;
import static com.looforyou.looforyou.Constants.GET_USERS;
import static com.looforyou.looforyou.Constants.TOKEN_QUERY;
import static com.looforyou.looforyou.Constants.UPLOAD_IMAGE;

/**
 * This is activity for displaying user profile information
 *
 * @author mingtau li
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

    public final int PICK_IMAGE = 1234;

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

                /* TODO NOTE comment to activate intent to choose new profile picture from gallery */
/*                profilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image*//*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                    }
                });*/

            } catch (Exception e) {
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


/* TODO update profile image from activity result */
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            final Uri imageUri = data.getData();
            String extension;
            if(imageUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)){
                final MimeTypeMap mime = MimeTypeMap.getSingleton();
                extension = mime.getExtensionFromMimeType(getContentResolver().getType(imageUri));
            }else {
                extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(imageUri.getPath())).toString());
            }

//            //////////////////////////////////////

            String fileName = imageUri.getPath().substring(imageUri.getPath().lastIndexOf(":")+1);
            Uri selectedImageUri = data.getData();
            String imagepath = selectedImageUri.getPath();
            File imageFile = new File(imagepath);


//            Toast.makeText(this,"the path: "+extension, Toast.LENGTH_SHORT).show();
            Toast.makeText(this,"the path: "+fileName, Toast.LENGTH_SHORT).show();
            String result = "null";

            HttpPost post = new HttpPost(imageFile);

            String url = GET_BATHROOMS + (new UserUtil(this).getUserID()) + UPLOAD_IMAGE;
            try {
                result = post.execute(url).get();
                Log.v("testresultcode test","test");
            } catch (InterruptedException e) {
                Log.v("testresultcode result","fail");
                e.printStackTrace();
            } catch (ExecutionException e) {
                Log.v("testresultcode result","fail");
                e.printStackTrace();
            }
            Log.v("testresultcode result",result);

        }
    }*/


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

