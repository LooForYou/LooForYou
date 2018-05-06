package com.looforyou.looforyou.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.Models.Review;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BookmarksAdapter;
import com.looforyou.looforyou.adapters.BookmarksListItem;
import com.looforyou.looforyou.adapters.ReviewsAdapter;
import com.looforyou.looforyou.adapters.ReviewsListItem;
import com.looforyou.looforyou.fragments.LoginFragment;
import com.looforyou.looforyou.utilities.BathroomDeserializer;
import com.looforyou.looforyou.utilities.BookmarksUtil;
import com.looforyou.looforyou.utilities.HttpGet;
import com.looforyou.looforyou.utilities.TabControl;
import com.looforyou.looforyou.utilities.UserUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.ALL_BOOKMARKS;
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        }
        mHandler = new Handler();
        if(!new UserUtil(this).isLoggedIn()) {
            loadLoginFragment();
        }else {
            onLoggedIn();
        }
    }


    public void onLoggedIn() {
        loadBookmarks();
    }

    public void loadBookmarks() {
        FrameLayout bookmarksContainer = (FrameLayout) findViewById(R.id.bookmarks_scroll_container);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.bookmarks_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        ArrayList<Bathroom> bookmarkedBathrooms = getBookmarkedBathrooms();
        ArrayList<Bathroom> bookmarkedBathrooms = new BookmarksUtil(this).getBookmarkedBathrooms();
        List<BookmarksListItem> bookmarksListItems= new ArrayList<>();

        if (bookmarkedBathrooms != null && bookmarkedBathrooms.size() > 0) {
            bookmarksContainer.setVisibility(View.VISIBLE);
            for (Bathroom bath : bookmarkedBathrooms) {
                BookmarksListItem bookmarksListItem = new BookmarksListItem(bath);
               bookmarksListItems.add(bookmarksListItem);
            }
        }

        BookmarksAdapter adapter = new BookmarksAdapter(bookmarksListItems, getApplicationContext());
        recyclerView.setAdapter(adapter);
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
