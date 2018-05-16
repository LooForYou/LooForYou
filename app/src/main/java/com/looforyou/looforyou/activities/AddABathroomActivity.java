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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.HttpPost;
import com.looforyou.looforyou.utilities.TabControl;
import static com.looforyou.looforyou.Constants.GET_BATHROOMS;

import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.GET_BATHROOMS;

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
    private EditText editBathroomHours_open;
    private EditText editBathroomHours_closed;
    private EditText editMaintenanceDays;
    private EditText editMaintenanceHours_start;
    private EditText editMaintenanceHours_end;

    private TextView bathroom_attributes;
    private TextView bathroom_type;

    /* Radio Buttons to select bathroom Gender */
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private RadioButton radioNeutral;

    /* Check Boxes to select amenities */
    private CheckBox checkBox_free;
    private CheckBox checkBox_disabled;
    private CheckBox checkBox_parking;
    private CheckBox checkBox_locked;
    private CheckBox checkBox_mirrors;
    private CheckBox checkBox_diaperTable;


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
        final EditText editBathroomHours_open = (EditText) findViewById(R.id.editBathroomHours_open);
        final EditText editBathroomHours_closed = (EditText) findViewById(R.id.editBathroomHours_closed);

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

        final CheckBox checkBox_free = (CheckBox)findViewById(R.id.checkBox_free);
        checkBox_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        final CheckBox checkBox_disabled = (CheckBox)findViewById(R.id.checkBox_disabled);
        checkBox_disabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        final CheckBox checkBox_parking = (CheckBox)findViewById(R.id.checkBox_parking);
        checkBox_parking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        final CheckBox checkBox_locked = (CheckBox)findViewById(R.id.checkBox_locked);
        checkBox_locked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        final CheckBox checkBox_mirrors = (CheckBox)findViewById(R.id.checkBox_mirrors);
        checkBox_mirrors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        final CheckBox checkBox_diaperTable = (CheckBox)findViewById(R.id.checkBox_diaperTable);
        checkBox_diaperTable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        final Button submitButton = (Button)findViewById(R.id.submitBathroom);
        //final String bathroomName = editBathroomName.getText().toString();
        //final String bathroomLocation = editBathroomLocation.getText().toString();
        //final String bathroomHours_open = editBathroomHours_open.getText().toString();
        //final String bathroomHours_closed = editBathroomHours_closed.getText().toString();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //construct the bathroom object using user inputs
                Bathroom newBathroom = new Bathroom("");
                Gson gson = new Gson();
                String json = gson.toJson(newBathroom);
                try {
                    JSONObject myObject = new JSONObject(json);
                    HttpPost post = new HttpPost(myObject);
                    post.execute(GET_BATHROOMS);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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

        }
    }


    private void rbCheck(View view) {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = (RadioButton)findViewById(radioButtonID);
        Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
    }

    private void uploadImage(View v) {


    }

    public void submitBathroom(View v) {

    }


}
