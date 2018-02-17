package com.looforyou.looforyou.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.util.Log;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.activities.AddABathroomActivity;
import com.looforyou.looforyou.activities.BookmarkActivity;
import com.looforyou.looforyou.activities.MainActivity;
import com.looforyou.looforyou.activities.MapActivity;
import com.looforyou.looforyou.activities.ProfileActivity;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by ibreaker on 2/16/2018.
 */

public class TabControl {
    private Context context;

    public TabControl(Context context) {
        this.context = context;
    }

    public void tabs(final Context packageContext, int currentTab) {
        Activity activity = (Activity) context;
        BottomBar bottomBar = (BottomBar) activity.findViewById(R.id.bottomBar);
        bottomBar.selectTabWithId(currentTab);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                String packageDir = "class com.looforyou.looforyou.activities.";
                switch (tabId) {
                    case R.id.tab_home:
                        if(!context.getClass().toString().trim().contains(packageDir + "MainActivity")) {
                            Intent homeIntent = new Intent(packageContext, MainActivity.class);
                            context.startActivity(homeIntent);
                        }
                        break;
                    case R.id.tab_profile:
                        if(!context.getClass().toString().trim().contains(packageDir + "ProfileActivity")) {
                            Intent profileIntent = new Intent(packageContext, ProfileActivity.class);
                            context.startActivity(profileIntent);
                        }
                        break;
                    case R.id.tab_add_bathroom:
                        if(!context.getClass().toString().trim().contains(packageDir + "AddABathroomActivity")) {
                            Intent AddIntent = new Intent(packageContext, AddABathroomActivity.class);
                            context.startActivity(AddIntent);
                        }
                        break;
                    case R.id.tab_map:
                        if(!context.getClass().toString().trim().contains(packageDir + "MapActivity")) {
                            Intent mapIntent = new Intent(packageContext, MapActivity.class);
                            context.startActivity(mapIntent);
                        }
                        break;
                    case R.id.tab_bookmarks:
                        if(!context.getClass().toString().trim().contains(packageDir + "BookmarkActivity")) {
                            Intent bookMarksIntent = new Intent(packageContext, BookmarkActivity.class);
                            context.startActivity(bookMarksIntent);
                        }
                }
            }
        });
    }
}
