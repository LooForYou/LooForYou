package com.looforyou.looforyou;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabWithId(R.id.tab_profile);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId) {
                    case R.id.tab_home:
                        Intent homeIntent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.tab_add_bathroom:
                        Intent AddIntent = new Intent(ProfileActivity.this, AddABathroomActivity.class);
                        startActivity(AddIntent);
                        break;
                    case R.id.tab_map:
                        Intent mapIntent = new Intent(ProfileActivity.this, MapActivity.class);
                        startActivity(mapIntent);
                        break;
                    case R.id.tab_bookmarks:
                        Intent bookMarksIntent = new Intent(ProfileActivity.this, BookmarkActivity.class);
                        startActivity(bookMarksIntent);

                }
            }
        });
    }
}
