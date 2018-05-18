package com.looforyou.looforyou.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.looforyou.looforyou.R;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    private static final String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUserName = (EditText)findViewById(R.id.etUsername);
        final EditText etPass = (EditText) findViewById(R.id.etPassword);

        final TextView tvFBstatus = (TextView) findViewById(R.id.tvFBstatus);

        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        final LoginButton bFBLogin = (LoginButton) findViewById(R.id.bFBLogin);
        bFBLogin.setReadPermissions(Arrays.asList(EMAIL));
        
        callbackManager = CallbackManager.Factory.create();
        bFBLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                tvFBstatus.setText("Login Success!");
                etUserName.setText(loginResult.getAccessToken().getUserId());

            }

            @Override
            public void onCancel() {
                tvFBstatus.setText("Login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                tvFBstatus.setText("Oops! Something went wrong!");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
