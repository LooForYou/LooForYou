package com.looforyou.looforyou.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.v("mylog","this is a test log");
        TabControl tabb = new TabControl(this);
        tabb.tabs(ProfileActivity.this,R.id.tab_profile);
        };

}

