package com.looforyou.looforyou.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TabControl tabb = new TabControl(this);
        tabb.tabs(ProfileActivity.this,R.id.tab_profile);

        Button b = (Button) findViewById(R.id.blogin);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, Pop.class));
            }
        });
    }
}
