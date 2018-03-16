package com.looforyou.looforyou.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

public class ProfileActivity extends AppCompatActivity {
    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myDialog = new Dialog(this);
        Log.v("mylog","this is a test log");
        TabControl tabb = new TabControl(this);
        tabb.tabs(ProfileActivity.this,R.id.tab_profile);

        };

        public void ShowPopup(View v){
            TextView txtclose;
            myDialog.setContentView(R.layout.activity_login);
            txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.show();
    }
}

