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
import android.support.v4.widget.SwipeRefreshLayout;
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
    private final int UPDATE_INTERVAL = 60000; //60 sec
    private final int FASTEST_INTERVAL = 10000; //10 sec
    private LocationRequest mLocationRequest;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 100;
    private Location mLastKnownLocation = null;
    private Handler mHandler = null;
    private FusedLocationProviderClient mFusedLocationProviderClient = null;
    private Dialog myDialog;
    private SwipeRefreshLayout swipeContainer;

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

        initializeComponents();
        initializePageViewer();
        refreshDisplay(0);

    }

    private void setUpPagerData() {
        //TODO refresh data
//        viewPager.setSaveFromParentEnabled(false);
//        pagerAdapter.clear();
        List<Bathroom> feedList = LooLoader.loadBathrooms(this.getApplicationContext());
        for (Bathroom b : feedList) {
            Log.v("feedlistcontent",b.getName());
            pagerAdapter.addCardFragment(b);
            pagerAdapter.notifyDataSetChanged();
        }
        swipeContainer.setRefreshing(false);
    }

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
          }
    }

    private void initializeComponents() {
        amenities = (TextView) findViewById(R.id.bathroom_amenities);
        hoursOfOperation = (TextView) findViewById(R.id.hours_of_operation);
        maintenanceHours = (TextView) findViewById(R.id.maintenance_hours);

        // Setup refresh listener which triggers new data loading
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                setUpPagerData();
                updateDistance();
            }
        });


    }

    private void initializePageViewer() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new BathroomCardFragmentPagerAdapter(getSupportFragmentManager(), MetricConverter.dpToPx(this, 2));


        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);

        setUpPagerData();

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
        Log.v("updating distance","updating distance...");
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

        try {
//            double dist = MetricConverter.distanceBetweenInMiles(new LatLng(getLastLocation().getLatitude(), getLastLocation().getLongitude()), bathrooms.get(viewPager.getCurrentItem()).getLatLng());
            double dist = MetricConverter.distanceBetweenInMiles(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), bathrooms.get(viewPager.getCurrentItem()).getLatLng());
            te.setText(df.format(dist) + " mi");
        }catch(Exception e) {
            swipeContainer.setRefreshing(false);
            Log.v("home exception",e.getMessage());
        }
        swipeContainer.setRefreshing(false);
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
                swipeContainer.setEnabled(false);
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
                        swipeContainer.setEnabled(false);
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

