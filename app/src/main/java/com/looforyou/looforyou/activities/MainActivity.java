package com.looforyou.looforyou.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.looforyou.looforyou.Constants;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BathroomCardFragmentPagerAdapter;
import com.looforyou.looforyou.fragments.BathroomViewFragment;
import com.looforyou.looforyou.utilities.ImageFromURL;
import com.looforyou.looforyou.utilities.ImageConverter;
import com.looforyou.looforyou.utilities.LooLoader;
import com.looforyou.looforyou.utilities.MetricConverter;
import com.looforyou.looforyou.utilities.ShadowTransformer;
import com.looforyou.looforyou.utilities.TabControl;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.looforyou.looforyou.Constants.*;

public class MainActivity extends AppCompatActivity implements BathroomViewFragment.OnFragmentInteractionListener{
    private Toolbar toolbar;
    private ActionMenuView actionMenu;
    private final String GMAPS_TAG = "GException";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private View view;
    private ImageView currentImage;
    private ImageView previousImage;
    private BathroomCardFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private GestureDetector gestureScanner;
    private ArrayList<Bathroom> bathrooms;
    private TextView amenities;
    private TextView hoursOfOperation;
    private TextView maintenanceHours;
    private final int UPDATE_INTERVAL = 10000; //10 sec
    private final int FASTEST_INTERVAL = 2000; //2 sec
    private LocationRequest mLocationRequest;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 100;
    private Location mLastKnownLocation = null;
    private Handler mHandler = null;
    FusedLocationProviderClient mFusedLocationProviderClient = null;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mHandler = new Handler();
        showActionBar();
        bindActionBar();

        //tabs on bottom control
        TabControl tabb = new TabControl(this);
        tabb.tabs(MainActivity.this, R.id.tab_home);

        getDeviceLocation();
        startLocationUpdates();

        initializePageViewer();
        initializeDisplayData();
        refreshDisplay(0);

    }

    private void setupView() {

        List<Bathroom> feedList = LooLoader.loadBathrooms(this.getApplicationContext());
//        Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + LoadMoreView.LOAD_VIEW_SET_COUNT);
//        for(int i = 0; i < LoadMoreView.LOAD_VIEW_SET_COUNT; i++){
//            mLoadMoreView.addView(new MenuView.ItemView(this.getApplicationContext(), feedList.get(i)));
//        }
//        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));
/*        Log.v("TEST FEEDLIST","feed size: "+feedList.size());
        Log.v("TEST FEEDLIST",String.valueOf(feedList.get(0).getLatLng()));
        Log.v("TEST FEEDLIST",feedList.get(1).getName());
        Log.v("TEST FEEDLIST",feedList.get(2).getEndTime().toString());*/
        for (Bathroom b : feedList) {
            pagerAdapter.addCardFragment(b);
        }

    }

    /* ============================================================================================*/
    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        try {
            mFusedLocationProviderClient = getFusedLocationProviderClient(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
//                        Log.v(GMAPS_TAG,String.valueOf(getCurrentLocation().getLatitude())); //delete me
                    } else {
                        Log.d(GMAPS_TAG, "Current location is null. Using defaults.");
                        Log.e(GMAPS_TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());

    }

    public void onLocationChanged(Location location) {
        //what to do when location determined
        mLastKnownLocation = location;
        Log.v("testLocation", "current location: " + String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));
        updateDistance();
    }

    public Location getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return null;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // GPS location can be null if GPS is switched off
                if (location != null) {
                    onLocationChanged(location);
                    mLastKnownLocation = location;
                } else {
                    Log.v(GMAPS_TAG, "not ready");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(GMAPS_TAG, "Error trying to get last GPS location");
                e.printStackTrace();
            }
        });
        return mLastKnownLocation;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
    }
    /* ============================================================================================*/

    private void refreshDisplay(int position) {
        bathrooms = pagerAdapter.getBathrooms();
        if (bathrooms.size() > 0) {
            ArrayList<String> amenitiesList = bathrooms.get(position).getAmenities();
            Collections.sort(amenitiesList);
            String formattedAmenities = amenitiesList.toString().replaceAll(", ", "\n• ");
            formattedAmenities = "• " + formattedAmenities.substring(1, formattedAmenities.length() - 1);
            amenities.setText(formattedAmenities);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            Date startTime = bathrooms.get(position).getStartTime();
            String start = null;
            if (startTime != null) {
                try {
                    start = sdf.format(startTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Date endTime = bathrooms.get(position).getEndTime();
            String end = null;
            if (endTime != null) {
                try {
                    end = sdf.format(endTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (startTime == null || endTime == null) {
                hoursOfOperation.setText("Unknown");
            } else if (start.equals(end)) {
                hoursOfOperation.setText("24HR");
            } else {
                hoursOfOperation.setText(start + " to " + end);
            }

            Date maintenanceStartTime = bathrooms.get(position).getMaintenanceStart();
            String mStart = null;
            if (maintenanceStartTime != null) {
                try {
                    mStart = sdf.format(maintenanceStartTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Date maintenanceEndTime = bathrooms.get(position).getMaintenanceEnd();
            String mEnd = null;
            if (maintenanceEndTime != null) {
                try {
                    mEnd = sdf.format(maintenanceEndTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String maintenanceDays = bathrooms.get(position).getMaintenanceDays().toString();

            if (maintenanceStartTime == null || maintenanceEndTime == null) {
                maintenanceHours.setText("Unknown");
            } else {
                maintenanceHours.setText(mStart + " to " + mEnd + "\n" + maintenanceDays);
            }

    /*
            ImageView selectedImage = (ImageView) findViewById(R.id.selected_location_image);
    //        bathrooms.get(position).setImage(new ImageFromURL(selectedImage).execute(bathrooms.get(position).getImageURL()));
            try {
                bathrooms.get(position).setImage(new ImageFromURL(this).execute(bathrooms.get(position).getImageURL()).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            currentImage = (ImageView) findViewById(R.id.selected_location_image);
            previousImage = currentImage;*/
            //      currentImage.setImageDrawable(bathrooms.get(position).get);

        }
    }

    private void initializeDisplayData() {
        amenities = (TextView) findViewById(R.id.bathroom_amenities);
        hoursOfOperation = (TextView) findViewById(R.id.hours_of_operation);
        maintenanceHours = (TextView) findViewById(R.id.maintenance_hours);


    }

    private void initializePageViewer() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new BathroomCardFragmentPagerAdapter(getSupportFragmentManager(), MetricConverter.dpToPx(this, 2));

        setupView();

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);


        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                updateDistance();
                pagerAdapter.getCardViewAt(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(MainActivity.this,bathrooms.get(position).getName()+" clicked", Toast.LENGTH_SHORT).show();
                        loadBathroomView();
                    }
                });
            }

            @Override
            public void onPageSelected(int position) {
                Log.v("scrollchange", "position " + String.valueOf(position));
                for (int i = 0; i < 2; i++) {
//                    Log.v("scrollchange: drawables", String.valueOf(bathrooms.get(i).getImage()));
                }
                refreshDisplay(position);



                if (position == pagerAdapter.getCount() - 1) {

/*                            for(int i = 0; i< 100; i++) {
                                //test add new items:
                                pagerAdapter.addCardFragment(new Bathroom()); //delete me
                                tempPics.add(BitmapGenerator.DrawableFromAsset(MainActivity.this, "no-image-uploaded.png"));
                            }*/

//                            viewPager.setCurrentItem(15,true);
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
//                        Log.v("scrollchange","end of cards "+state);
                    Log.v("scrollchange", "end of cards " + state + " position " + String.valueOf(viewPager.getVerticalScrollbarPosition()));

                }

            }

        });


    }

    public void updateDistance() {
        TextView te = (TextView) pagerAdapter.getCardViewAt(viewPager.getCurrentItem()).findViewById(R.id.bathroom_distance);
        if (mLastKnownLocation == null) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            //get current location
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //retrieve lastKnown location if it does not exist
            mLastKnownLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        }
        DecimalFormat df = new DecimalFormat("0.0");

//        double dist = MetricConverter.distanceBetweenInMiles(new LatLng(getLastLocation().getLatitude(),getLastLocation().getLongitude()),bathrooms.get(viewPager.getCurrentItem()).getLatLng());
  //      te.setText(df.format(dist)+" mi");
    }

    //when options in toolbar menu are created
    private void showActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.action_bar_main);
        view = getSupportActionBar().getCustomView();
    }

    public void bindActionBar(){
        ImageButton profileButton= (ImageButton)view.findViewById(R.id.action_bar_profile_main);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtclose;
                Button bSignUp;
                Button bLogin;
                myDialog.setContentView(R.layout.activity_login);
                txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

                bLogin = (Button) myDialog.findViewById(R.id.bLogin);
                bLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        Intent login = new Intent(v.getContext(), ProfileActivity.class);
                        startActivity(login);
                    }
                });
                bSignUp = (Button) myDialog.findViewById(R.id.bRegister);
                bSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        Intent signup = new Intent(v.getContext(), ProfileActivity.class);
                        startActivity(signup);
                    }
                });
                myDialog.show();
            }
        });

        final LinearLayout searchBar= (LinearLayout)view.findViewById(R.id.action_bar_search_main);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //start autoplaces search
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.v(GMAPS_TAG, "repairableException " + e);
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    Log.v(GMAPS_TAG, "notAvailableException " + e);
                }
            }
        });

        ImageButton sortButton= (ImageButton)view.findViewById(R.id.action_bar_sort_main);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.v("Custom Tag","sort clicked");
                Toast.makeText(MainActivity.this,"sort by...", Toast.LENGTH_SHORT).show();

            }
        });

        ImageButton listViewButton= (ImageButton)view.findViewById(R.id.action_bar_list_view_main);
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.v("Custom Tag","view list");
                Toast.makeText(MainActivity.this,"view list...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* results from google search*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(GMAPS_TAG, "Place:" + place.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(GMAPS_TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadBathroomView() {
        // set toolbar title
        getSupportActionBar().setTitle("Bathroom View");

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                final Fragment fragment = new BathroomViewFragment();
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame,fragment);
                fragmentTransaction.addToBackStack(null);

                Bundle bundle = new Bundle();
                bundle.putParcelable("current bathroom",bathrooms.get(viewPager.getCurrentItem()));
                fragment.setArguments(bundle);
                fragmentTransaction.commitAllowingStateLoss();
                pagerAdapter.getCardViewAt(viewPager.getCurrentItem()).setClickable(false); //disable pagerAdapter touch event temporarily

                getSupportActionBar().setCustomView(R.layout.action_bar_back);
                view = getSupportActionBar().getCustomView();

                ImageButton backButton= (ImageButton) view.findViewById(R.id.action_bar_back_button);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.super.onBackPressed();
                        pagerAdapter.getCardViewAt(viewPager.getCurrentItem()).setClickable(true); //reenable pagerAdapter touch event
                        showActionBar();
                        bindActionBar();
                    }
                });

        };
        };


        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

