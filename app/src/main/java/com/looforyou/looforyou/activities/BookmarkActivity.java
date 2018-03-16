package com.looforyou.looforyou.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

public class BookmarkActivity extends AppCompatActivity {
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        TabControl tabb = new TabControl(this);
        tabb.tabs(BookmarkActivity.this,R.id.tab_bookmarks);
        //hey all
        showActionBar();
        bindActionBar();
    }



    private void showActionBar() {
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setCustomView(R.layout.action_bar_main);
//        view =getSupportActionBar().getCustomView();
    }

    public void bindActionBar(){

    }

}
