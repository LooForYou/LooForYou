package com.looforyou.looforyou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.looforyou.looforyou.activities.MainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUserName = (EditText)findViewById(R.id.etUsername);
        final EditText etPass = (EditText) findViewById(R.id.etPassword);

        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        final Button bFB = (Button) findViewById(R.id.bFBLogin);
        final Button bGoogle = (Button) findViewById(R.id.bGoogleLogin);

        bLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(loginIntent);
            }
        });
    }
}
