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
import android.widget.TextView;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ShowPopup(getWindow().getDecorView());

        Log.v("mylog","this is a test log");
        TabControl tabb = new TabControl(this);
        tabb.tabs(ProfileActivity.this,R.id.tab_profile);

        };

        public void ShowPopup(View v){
            TextView txtclose;
            Button register;
            myDialog.setContentView(R.layout.activity_login);
            txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
            txtclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            register = (Button)myDialog.findViewById(R.id.bRegister);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                    registerPop(getWindow().getDecorView());
                }
            });
            myDialog.show();
        }

        public void registerPop(View v){
            TextView txtcloseReg;
            myDialog.setContentView(R.layout.activity_register);
            txtcloseReg = (TextView) myDialog.findViewById(R.id.txtcloseReg);
            txtcloseReg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            myDialog.show();
        }
}

