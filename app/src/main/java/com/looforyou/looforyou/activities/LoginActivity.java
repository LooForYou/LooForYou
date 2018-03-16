package com.looforyou.looforyou.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.looforyou.looforyou.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUserName = (EditText)findViewById(R.id.etUsername);
        final EditText etPass = (EditText) findViewById(R.id.etPassword);

        final Button popLogin = (Button) findViewById(R.id.popLogin);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        final Button bFB = (Button) findViewById(R.id.bFBLogin);
        final Button bGoogle = (Button) findViewById(R.id.bGoogleLogin);
    }
}
