package com.looforyou.looforyou.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

public class AddABathroomActivity extends AppCompatActivity {

    private TextView add_a_bathroom;
    private ImageView bathroom_image;
    private TextView bathroom_image_link;
    private EditText editBathroomName;
    private EditText editBathroomLocation;
    private EditText editBathroomInfo;
    private TextView bathroom_attributes;
    private TextView bathroom_type;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private RadioButton radioNeutral;
    private ToggleButton bathroom_accessible;
    private ToggleButton bathroom_free;
    private ToggleButton bathroom_keyless;
    private ToggleButton bathroom_parking;
    private ToggleButton bathroom_mirrors;
    private ToggleButton bathroom_baby_station;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_bathroom);

        ImageView bathroom_image = (ImageView)findViewById(R.id.bathroom_image);

        TextView add_a_bathroom = (TextView)findViewById(R.id.add_a_bathroom_title);
        TextView bathroom_image_link = (TextView)findViewById(R.id.bathroom_image_link);
        TextView bathroom_attributes = (TextView)findViewById(R.id.bathroom_attributes);
        TextView bathroom_type = (TextView)findViewById(R.id.bathroom_type);

        EditText editBathroomName = (EditText)findViewById(R.id.editBathroomName);
        EditText editBathroomLocation = (EditText)findViewById(R.id.editBathroomLocation);
        EditText editBathroomInfo = (EditText)findViewById(R.id.editBathroomInfo);

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        RadioButton radioMale = (RadioButton)findViewById(R.id.radioMale);
        RadioButton radioFemale = (RadioButton)findViewById(R.id.radioFemale);
        RadioButton radioNeutral = (RadioButton)findViewById(R.id.radioNeutral);

        ToggleButton bathroom_accessible = (ToggleButton)findViewById(R.id.bathroom_accessible);
        bathroom_accessible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ToggleButton bathroom_free = (ToggleButton)findViewById(R.id.bathroom_free);
        bathroom_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ToggleButton bathroom_keyless = (ToggleButton)findViewById(R.id.bathroom_keyless);
        bathroom_keyless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ToggleButton bathroom_parking = (ToggleButton)findViewById(R.id.bathroom_parking);
        bathroom_parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ToggleButton bathroom_mirrors = (ToggleButton)findViewById(R.id.bathroom_mirrors);
        bathroom_mirrors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ToggleButton bathroom_baby_station = (ToggleButton)findViewById(R.id.bathroom_baby_station);
        bathroom_baby_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TabControl tabb = new TabControl(this);
        tabb.tabs(AddABathroomActivity.this,R.id.tab_add_bathroom);
        //test comment

        //checkButton();
        //checkHandler();
        //checkToggle();
    }

    public void checkButton(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "Selected Radio Button" + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    public void checkHandler(View view) {
        String bathroomName = editBathroomName.getText().toString();
        String bathroomLocation = editBathroomLocation.getText().toString();
    }

    public void checkToggle(View view) {
    }

    public void uploadImage(View view) {
    }
}
