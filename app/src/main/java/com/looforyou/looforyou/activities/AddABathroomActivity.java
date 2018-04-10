package com.looforyou.looforyou.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
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

        final ImageView bathroom_image = (ImageView) findViewById(R.id.bathroom_image);

        final TextView add_a_bathroom = (TextView) findViewById(R.id.add_a_bathroom_title);
        final TextView bathroom_image_link = (TextView) findViewById(R.id.bathroom_image_link);
        final TextView bathroom_attributes = (TextView) findViewById(R.id.bathroom_attributes);
        final TextView bathroom_type = (TextView) findViewById(R.id.bathroom_type);

        final EditText editBathroomName = (EditText) findViewById(R.id.editBathroomName);
        final EditText editBathroomLocation = (EditText) findViewById(R.id.editBathroomLocation);
        final EditText editBathroomInfo = (EditText) findViewById(R.id.editBathroomInfo);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        final RadioButton radioNeutral = (RadioButton) findViewById(R.id.radioNeutral);

        final ToggleButton bathroom_accessible = (ToggleButton)findViewById(R.id.bathroom_accessible);
        bathroom_accessible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_accessible.setTextOn("Disabled");
                    bathroom_accessible.getBackground().setAlpha(255);
                } else {
                    bathroom_accessible.setTextOff("Not Disabled");
                    bathroom_accessible.getBackground().setAlpha(0);
                }
            }
        });

        final ToggleButton bathroom_free = (ToggleButton)findViewById(R.id.bathroom_free);
        bathroom_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_free.setTextOn("Free");
                } else {
                    bathroom_free.setTextOff("Not Free");
                }
            }
        });

        final ToggleButton bathroom_keyless = (ToggleButton)findViewById(R.id.bathroom_keyless);
        bathroom_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_keyless.setTextOn("Unlocked");
                } else {
                    bathroom_keyless.setTextOff("Locked");
                }
            }
        });

        final ToggleButton bathroom_parking = (ToggleButton)findViewById(R.id.bathroom_parking);
        bathroom_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_parking.setTextOn("Parking");
                } else {
                    bathroom_parking.setTextOff("No Parking");
                }
            }
        });

        final ToggleButton bathroom_mirrors = (ToggleButton)findViewById(R.id.bathroom_mirrors);
        bathroom_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_mirrors.setTextOn("Mirrors");
                } else {
                    bathroom_mirrors.setTextOff("No Mirrors");
                }
            }
        });

        final ToggleButton bathroom_baby_station = (ToggleButton)findViewById(R.id.bathroom_baby_station);
        bathroom_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_baby_station.setTextOn("Free");
                } else {
                    bathroom_baby_station.setTextOff("Not Free");
                }
            }
        });

        final TabControl tabb = new TabControl(this);
        tabb.tabs(AddABathroomActivity.this,R.id.tab_add_bathroom);
        //test comment
    }

    private void rbCheck(View view) {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = (RadioButton)findViewById(radioButtonID);
        Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
    }

    private void checkHandler() {
        String bathroomName = editBathroomName.getText().toString();
        String bathroomLocation = editBathroomLocation.getText().toString();
        String bathroomInfo = editBathroomInfo.getText().toString();
    }

    private void uploadImage() {

    }

}
