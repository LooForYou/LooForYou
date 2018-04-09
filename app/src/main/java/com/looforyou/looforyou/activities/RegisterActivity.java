package com.looforyou.looforyou.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.looforyou.looforyou.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etRegFName = (EditText)findViewById(R.id.etRegFName);
        final EditText etRegLName = (EditText)findViewById(R.id.etRegLName);
        final EditText etRegEmail = (EditText)findViewById(R.id.etRegEmail);
        final EditText etRegPass = (EditText) findViewById(R.id.etRegPassword);
        final EditText etRegConfPassword = (EditText)findViewById(R.id.etRegConfPassword);

        final Button bSubmit = (Button) findViewById(R.id.bRegSubmit);
    }
}
