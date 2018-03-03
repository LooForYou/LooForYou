package com.looforyou.looforyou.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

public class AddABathroomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_bathroom);

        TabControl tabb = new TabControl(this);
        tabb.tabs(AddABathroomActivity.this,R.id.tab_add_bathroom);
        //test comment
    }
}
