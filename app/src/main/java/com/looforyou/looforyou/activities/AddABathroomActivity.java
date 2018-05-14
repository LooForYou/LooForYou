package com.looforyou.looforyou.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.content.Context;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

import org.apache.commons.lang3.ObjectUtils;

/**
 * This is the activity class for adding new bathrooms
 *
 * @author Peter Bouris
 */

public class AddABathroomActivity extends AppCompatActivity {

    /* Text view for Page Title */
    private TextView add_a_bathroom;

    /* Image view for Bathroom Image upload */
    private ImageView bathroom_image;
    private static final int RESULT_LOAD_IMAGE = 1;
    private TextView bathroom_image_link;

    /* Fields to edit Bathroom information */
    private EditText editBathroomName;
    private EditText editBathroomLocation;
    private EditText editBathroomInfo;

    private TextView bathroom_attributes;
    private TextView bathroom_type;

    /* Radio Buttons to select bathroom Gender */
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private RadioButton radioNeutral;

    /* Toggle Buttons to select amenities */
    private ToggleButton bathroom_accessible;
    private ToggleButton bathroom_free;
    private ToggleButton bathroom_keyless;
    private ToggleButton bathroom_parking;
    private ToggleButton bathroom_mirrors;
    private ToggleButton bathroom_baby_station;

    /* Button to save and add new bathroom */
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_bathroom);

        getSupportActionBar().setTitle("Add a New Bathroom");
        
        //prevents keyboard from popping up on first load
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final ImageView bathroom_image = (ImageView) findViewById(R.id.bathroom_image);
        bathroom_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

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

        radioGroup.setOnClickListener(new View.OnClickListener() {
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            final RadioButton radioButton = (RadioButton)findViewById(radioButtonID);
            @Override
            public void onClick(View v) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radioMale:
                        Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
                    case R.id.radioFemale:
                        Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
                    case R.id.radioNeutral:
                        Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
                }
            }
        });

        final ToggleButton bathroom_accessible = (ToggleButton)findViewById(R.id.bathroom_accessible);
        bathroom_accessible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_accessible.setTextOn("Disabled");
                    bathroom_accessible.getBackground().mutate().setAlpha(255);
                    bathroom_accessible.setTextColor(Color.WHITE);
                } else {
                    bathroom_accessible.setTextOff("Not Disabled");
                    bathroom_accessible.getBackground().mutate().setAlpha(25);
                    bathroom_accessible.setTextColor(Color.BLACK);
                }
            }
        });

        final ToggleButton bathroom_free = (ToggleButton)findViewById(R.id.bathroom_free);
        bathroom_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_free.setTextOn("Free");
                    bathroom_free.getBackground().mutate().setAlpha(255);
                    bathroom_free.setTextColor(Color.WHITE);
                } else {
                    bathroom_free.setTextOff("Not Free");
                    bathroom_free.getBackground().mutate().setAlpha(25);
                    bathroom_free.setTextColor(Color.BLACK);
                }
            }
        });

        final ToggleButton bathroom_keyless = (ToggleButton)findViewById(R.id.bathroom_keyless);
        bathroom_keyless.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_keyless.setTextOn("Unlocked");
                    bathroom_keyless.getBackground().mutate().setAlpha(255);
                    bathroom_keyless.setTextColor(Color.WHITE);
                } else {
                    bathroom_keyless.setTextOff("Locked");
                    bathroom_keyless.getBackground().mutate().setAlpha(25);
                    bathroom_keyless.setTextColor(Color.BLACK);
                }
            }
        });

        final ToggleButton bathroom_parking = (ToggleButton)findViewById(R.id.bathroom_parking);
        bathroom_parking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_parking.setTextOn("Parking");
                    bathroom_parking.getBackground().mutate().setAlpha(255);
                    bathroom_parking.setTextColor(Color.WHITE);
                } else {
                    bathroom_parking.setTextOff("No Parking");
                    bathroom_parking.getBackground().mutate().setAlpha(25);
                    bathroom_parking.setTextColor(Color.BLACK);
                }
            }
        });

        final ToggleButton bathroom_mirrors = (ToggleButton)findViewById(R.id.bathroom_mirrors);
        bathroom_mirrors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_mirrors.setTextOn("Mirrors");
                    bathroom_mirrors.getBackground().mutate().setAlpha(255);
                    bathroom_mirrors.setTextColor(Color.WHITE);
                } else {
                    bathroom_mirrors.setTextOff("No Mirrors");
                    bathroom_mirrors.getBackground().mutate().setAlpha(25);
                    bathroom_mirrors.setTextColor(Color.BLACK);
                }
            }
        });

        final ToggleButton bathroom_baby_station = (ToggleButton)findViewById(R.id.bathroom_baby_station);
        bathroom_baby_station.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    bathroom_baby_station.setTextOn("Diaper Table");
                    bathroom_baby_station.getBackground().mutate().setAlpha(255);
                    bathroom_baby_station.setTextColor(Color.WHITE);
                } else {
                    bathroom_baby_station.setTextOff("No Table");
                    bathroom_baby_station.getBackground().mutate().setAlpha(25);
                    bathroom_baby_station.setTextColor(Color.BLACK);
                }
            }
        });

        final Button submitButton = (Button)findViewById(R.id.submitBathroom);
        String bathroomName = editBathroomName.getText().toString();
        String bathroomLocation = editBathroomLocation.getText().toString();
        String bathroomInfo = editBathroomInfo.getText().toString();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddABathroomActivity.this,"Bathroom Added", Toast.LENGTH_LONG).show();
            }
        });

        final TabControl tabb = new TabControl(this);
        tabb.tabs(AddABathroomActivity.this,R.id.tab_add_bathroom);
        //test comment


    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            bathroom_image.setImageURI(selectedImage);
        }
    }


    private void rbCheck(View view) {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = (RadioButton)findViewById(radioButtonID);
        Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
    }
/*
    private void checkHandler() {
        String bathroomName = editBathroomName.getText().toString();
        String bathroomLocation = editBathroomLocation.getText().toString();
        String bathroomInfo = editBathroomInfo.getText().toString();
    }
*/
    private void uploadImage(View v) {


    }

    public void submitBathroom(View v) {

    }


}
