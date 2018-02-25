package com.looforyou.looforyou.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        TabControl tabb = new TabControl(this);
        tabb.tabs(MapActivity.this,R.id.tab_map);

    }
}
