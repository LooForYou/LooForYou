package com.looforyou.looforyou.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.content.Context;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.looforyou.looforyou.Manifest;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.Models.Coordinates;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.BathroomDeserializer;
import com.looforyou.looforyou.utilities.BathroomSerializer;
import com.looforyou.looforyou.utilities.HttpPost;
import com.looforyou.looforyou.utilities.LooLoader;
import com.looforyou.looforyou.utilities.TabControl;
import static com.looforyou.looforyou.Constants.GET_BATHROOMS;

import org.apache.commons.lang3.ObjectUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.looforyou.looforyou.Constants.GET_BATHROOMS;
import static com.looforyou.looforyou.Constants.UPLOAD_IMAGE;

/**
 * This is the activity class for adding new bathrooms
 *
 * @author Peter Bouris
 */

public class AddABathroomActivity extends AppCompatActivity {

    public enum Hours {
        HOURS_OPEN, HOURS_CLOSED, MAINTENANCE_START, MAINTANANCE_END
    }

    private Bathroom resultFromRequest = null;
    /* Text view for Page Title */
    private TextView add_a_bathroom;

    private Hours hourType;
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
    private EditText getEditBathroomDescription;

    private TextView bathroom_attributes;
    private TextView bathroom_type;

    /* Radio Buttons to select bathroom Gender */
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private RadioButton radioNeutral;

    /* Check Boxes to select amenities */
    private static CheckBox checkBox_free;
    private static CheckBox checkBox_disabled;
    private static CheckBox checkBox_parking;
    private static CheckBox checkBox_locked;
    private static CheckBox checkBox_mirrors;
    private static CheckBox checkBox_diaperTable;

    private static CheckBox checkBox_Mon;
    private static CheckBox checkBox_Tue;
    private static CheckBox checkBox_Wed;
    private static CheckBox checkBox_Thu;
    private static CheckBox checkBox_Fri;
    private static CheckBox checkBox_Sat;
    private static CheckBox checkBox_Sun;

    /* Button to save and add new bathroom */
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_bathroom);

        getSupportActionBar().setTitle("Add a New Bathroom");

        //prevents keyboard from popping up on first load
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final ImageView bathroom_image = findViewById(R.id.bathroom_image);


        final TextView bathroom_image_link = findViewById(R.id.bathroom_image_link);

        final TextView bathroom_attributes = findViewById(R.id.bathroom_attributes);
        final TextView bathroom_type = findViewById(R.id.bathroom_type);

        final EditText editBathroomName = findViewById(R.id.editBathroomName);
        final EditText editBathroomLocation = findViewById(R.id.editBathroomLocation);

        final Calendar myCalendar = Calendar.getInstance();
        final String date_time = "";
        final int[] mHour = {myCalendar.get(Calendar.HOUR_OF_DAY)};
        final int[] mMinute = {myCalendar.get(Calendar.MINUTE)};
        final EditText editBathroomDescription = findViewById(R.id.editBathroomDescription);
        final EditText editBathroomHours_open = findViewById(R.id.editBathroomHours_open);
        final EditText editBathroomHours_closed = findViewById(R.id.editBathroomHours_closed);
        final EditText editMaintenanceHours_start = findViewById(R.id.editMaintenanceHours_start);
        final EditText editMaintenanceHours_end = findViewById(R.id.editMaintenanceHours_end);
        final TimePickerDialog.OnTimeSetListener myTime = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

                switch (hourType){
                    case HOURS_OPEN:
                        editBathroomHours_open.setText(dateFormat.format(myCalendar.getTime()));
                        break;
                    case HOURS_CLOSED:
                        editBathroomHours_closed.setText(dateFormat.format(myCalendar.getTime()));
                        break;
                    case MAINTENANCE_START:
                        editMaintenanceHours_start.setText(dateFormat.format(myCalendar.getTime()));
                        break;
                    case MAINTANANCE_END:
                        editMaintenanceHours_end.setText(dateFormat.format(myCalendar.getTime()));
                        break;
                }
            }
        };

        editBathroomHours_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourType = Hours.HOURS_OPEN;
                new TimePickerDialog(AddABathroomActivity.this, myTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                        false).show();
            }
        });

        editBathroomHours_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourType = Hours.HOURS_CLOSED;
                new TimePickerDialog(AddABathroomActivity.this, myTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                        false).show();
            }
        });

        editMaintenanceHours_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourType = Hours.MAINTENANCE_START;
                new TimePickerDialog(AddABathroomActivity.this, myTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                        false).show();
            }
        });

        editMaintenanceHours_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hourType = Hours.MAINTANANCE_END;
                new TimePickerDialog(AddABathroomActivity.this, myTime, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),
                        false).show();
            }
        });

        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnClickListener(new View.OnClickListener() {
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            final RadioButton radioButton = (RadioButton) findViewById(radioButtonID);
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

        checkBox_free = findViewById(R.id.checkBox_free);
        checkBox_disabled = findViewById(R.id.checkBox_disabled);
        checkBox_parking = findViewById(R.id.checkBox_parking);
        checkBox_locked = findViewById(R.id.checkBox_locked);
        checkBox_mirrors = findViewById(R.id.checkBox_mirrors);
        checkBox_diaperTable = findViewById(R.id.checkBox_diaperTable);

        checkBox_Mon = findViewById(R.id.checkBox_Mon);
        checkBox_Tue = findViewById(R.id.checkBox_Tue);
        checkBox_Wed = findViewById(R.id.checkBox_Wed);
        checkBox_Thu = findViewById(R.id.checkBox_Thu);
        checkBox_Fri = findViewById(R.id.checkBox_Fri);
        checkBox_Sat = findViewById(R.id.checkBox_Sat);
        checkBox_Sun = findViewById(R.id.checkBox_Sun);

        final Button submitButton = findViewById(R.id.submitBathroom);
        //final String bathroomName = editBathroomName.getText().toString();
        //final String bathroomLocation = editBathroomLocation.getText().toString();
        //final String bathroomHours_open = editBathroomHours_open.getText().toString();
        //final String bathroomHours_closed = editBathroomHours_closed.getText().toString();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //construct the bathroom object using user inputs
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                    //DateFormat format = new SimpleDateFormat("hh:mm");
                    Date hoursOpen = dateFormat.parse(editBathroomHours_open.getText().toString());
                    Date hoursClosed = dateFormat.parse(editBathroomHours_closed.getText().toString());
                    Date maintenanceStart = dateFormat.parse(editMaintenanceHours_start.getText().toString());
                    Date maintenanceEnd = dateFormat.parse(editMaintenanceHours_end.getText().toString());

                    ArrayList<String> descriptions = new ArrayList<String>(Arrays.asList(editBathroomDescription.getText().toString().split("\n")));

                    Location location = LooLoader.getCurrentLocation();
                    Coordinates coordinates = new Coordinates(location.getLatitude(), location.getLongitude());

                    Bathroom bathroom = new Bathroom(editBathroomName.getText().toString(), coordinates, hoursOpen, hoursClosed,
                                                    maintenanceStart, maintenanceEnd, createMaintenaceDays(),
                                                    createAmenities(), descriptions, editBathroomLocation.getText().toString());

                            //Bathroom newBathroom = new Bathroom();
                    final GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(Bathroom.class, new BathroomSerializer());
                    builder.setPrettyPrinting();
                    final Gson gsonReq = builder.create();
                    String json = gsonReq.toJson(bathroom);

                    JSONObject myObject = new JSONObject(json);
                    HttpPost post = new HttpPost(myObject);
                    String result = post.execute(GET_BATHROOMS).get();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(Bathroom.class, new BathroomDeserializer());
                    Gson gsonRes = gsonBuilder.create();
                    resultFromRequest = gsonRes.fromJson(result, Bathroom.class);
                    bathroom_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                if(ActivityCompat.checkSelfPermission((AddABathroomActivity.this), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                    ActivityCompat.requestPermissions(AddABathroomActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
                                }else{
                                    openGallery();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParseException e){
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        final TabControl tabb = new TabControl(this);
        tabb.tabs(AddABathroomActivity.this,R.id.tab_add_bathroom);
        //test comment


    }
/*
    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editMaintenanceDays.setText(sdf.format(myCalendar.getTime()));
    }
*/
    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch (requestCode){
            case RESULT_LOAD_IMAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }else{

                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
                Uri uri = data.getData();
                String[] filePathCol = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filePathCol, null, null, null);
                cursor.moveToFirst();
                int colomnIndex = cursor.getColumnIndex(filePathCol[0]);
                String filePath = cursor.getString(colomnIndex);
                cursor.close();

                if (resultFromRequest != null) {
                    File file = new File(filePath);
                    HttpPost post = new HttpPost(file);
                    String url = GET_BATHROOMS + resultFromRequest.getId() + UPLOAD_IMAGE;
                    post.execute(url);
                    bathroom_image.setImageURI(uri);
                }
        }
    }

    private String createMaintenaceDays(){
        ArrayList<String> maintenaceDays = new ArrayList<String>();
        String result = "";

        if (checkBox_Mon.isChecked())
            maintenaceDays.add("Mondays");

        if (checkBox_Tue.isChecked())
            maintenaceDays.add("Tuesdays");

        if (checkBox_Wed.isChecked())
            maintenaceDays.add("Wednesdays");

        if (checkBox_Thu.isChecked())
            maintenaceDays.add("Thursdays");

        if (checkBox_Fri.isChecked())
            maintenaceDays.add("Fridays");

        if (checkBox_Sat.isChecked())
            maintenaceDays.add("Saturdays");

        if (checkBox_Sun.isChecked())
            maintenaceDays.add("Sundays");

        for (int i = 0; i < maintenaceDays.size(); i++){
            result += maintenaceDays.get(i);

            if (i < (maintenaceDays.size() - 1)){
                result += ", ";
            }
        }

        return result;
    }

    private ArrayList<String> createAmenities(){
        ArrayList<String> amenities = new ArrayList<String>();

        if (checkBox_free.isChecked())
            amenities.add("free");

        if (checkBox_disabled.isChecked())
            amenities.add("disabled");

        if (checkBox_parking.isChecked())
            amenities.add("parking");

        if (checkBox_locked.isChecked())
            amenities.add("locked");

        if (checkBox_mirrors.isChecked())
            amenities.add("mirrors");

        if (checkBox_diaperTable.isChecked())
            amenities.add("diaper table");

        return  amenities;
    }
/*
    private void rbCheck(View view) {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        final RadioButton radioButton = (RadioButton)findViewById(radioButtonID);
        Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_LONG).show();
    }
*/
    private void uploadImage(View v) {


    }

    public void submitBathroom(View v) {

    }




}
