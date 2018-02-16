package com.looforyou.looforyou;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class AddABathroomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_bathroom);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabWithId(R.id.tab_add_bathroom);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId) {
                    case R.id.tab_home:
                        Intent homeIntent = new Intent(AddABathroomActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.tab_profile:
                        Intent profileIntent = new Intent(AddABathroomActivity.this, ProfileActivity.class);
                        startActivity(profileIntent);
                        break;
                    case R.id.tab_map:
                        Intent mapIntent = new Intent(AddABathroomActivity.this, MapActivity.class);
                        startActivity(mapIntent);
                        break;
                    case R.id.tab_bookmarks:
                        Intent bookMarksIntent = new Intent(AddABathroomActivity.this, BookmarkActivity.class);
                        startActivity(bookMarksIntent);

                }
            }
        });
    }
}
