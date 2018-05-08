package com.looforyou.looforyou.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BookmarksAdapter;
import com.looforyou.looforyou.adapters.BookmarksListItem;
import com.looforyou.looforyou.fragments.LoginFragment;
import com.looforyou.looforyou.utilities.BookmarksUtil;
import com.looforyou.looforyou.utilities.TabControl;
import com.looforyou.looforyou.utilities.UserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the activity class for viewing bookmarked bathrooms
 *
 * @author mingtau li
 */

public class BookmarkActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {
    /* global handler used for loading fragments */
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        /* highlights tab associated with activity */
        TabControl tabb = new TabControl(this);
        tabb.tabs(BookmarkActivity.this, R.id.tab_bookmarks);

        /* change actionbar title */
        getSupportActionBar().setTitle("My Bookmarks");

        /* request gps permission from user */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        }
        /* loads login fragment using handler if user is not logged in */
        mHandler = new Handler();
        if (!new UserUtil(this).isLoggedIn()) {
            loadLoginFragment();
        } else {
            onLoggedIn();
        }
    }

    /**
     * This method loads necessary data if user is logged in
     * */
    public void onLoggedIn() {
        loadBookmarks();
    }

    /**
     * This method loads all bathrooms user has bookmarked
     * */
    public void loadBookmarks() {
        FrameLayout bookmarksContainer = (FrameLayout) findViewById(R.id.bookmarks_scroll_container);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.bookmarks_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* retrieve all bookmarked bathrooms*/
        ArrayList<Bathroom> bookmarkedBathrooms = new BookmarksUtil(this).getBookmarkedBathrooms();
        List<BookmarksListItem> bookmarksListItems = new ArrayList<>();

        /* if bookmarks exist, add bookmarked bathrooms to display accordingly */
        if (bookmarkedBathrooms != null && bookmarkedBathrooms.size() > 0) {
            bookmarksContainer.setVisibility(View.VISIBLE);
            for (Bathroom bath : bookmarkedBathrooms) {
                BookmarksListItem bookmarksListItem = new BookmarksListItem(bath);
                bookmarksListItems.add(bookmarksListItem);
            }
        }

        /* set bookmarked bathrooms to adapter for display */
        BookmarksAdapter adapter = new BookmarksAdapter(bookmarksListItems, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadLoginFragment() {
        /* set toolbar title */
        getSupportActionBar().setTitle("Log In or Sign Up");

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                 /* update the main content by replacing view with fragment */
                final Fragment fragment = new LoginFragment();
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }

            ;
        };

        /*If mPendingRunnable is not null, then add to the message queue*/
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
