package com.looforyou.looforyou.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BathroomCardFragmentPagerAdapter;
import com.looforyou.looforyou.fragments.BathroomCardFragment;
import com.looforyou.looforyou.fragments.BathroomViewFragment;
import com.looforyou.looforyou.utilities.LooLoader;
import com.looforyou.looforyou.utilities.MetricConverter;
import com.looforyou.looforyou.utilities.ShadowTransformer;
import com.looforyou.looforyou.utilities.TabControl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This is the home screen that displays initial bathroom info to the user
 * It contains at-a-glance information about the bathrooms
 *
 * @author mingtau li
 * @author phoenix grimmett
 */

public class MainActivity extends AppCompatActivity implements BathroomViewFragment.OnFragmentInteractionListener {
    /*  tag for logs */
    private final String LOGTAG = "Main_Activity";
    /* constant to keep track of google maps search request */
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    /* constant to keep track of fine location permission */
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 100;
    /* view for action bar */
    private View view;
    /* custom adapter for viewing bathroom cards */
    private BathroomCardFragmentPagerAdapter pagerAdapter;
    /* viewpager to display bathroom cards*/
    private ViewPager viewPager;
    /* supports swipe down to refresh */
    private SwipeRefreshLayout swipeContainer;
    /* keeps track of new location from google search bar */
    private Location newLocation = null;
    /* keeps track of last known location */
    private Location mLastKnownLocation = null;
    /* keeps a list of all bathrooms retrieved from server */
    private List<Bathroom> feedList;
    /* keeps list of all bathrooms retrieved from viewpager */
    private ArrayList<Bathroom> bathrooms;
    /* handler used for loading fragments */
    private Handler mHandler = null;
    /* displays list of amenities*/
    private TextView amenities;
    /* displays hours of operation */
    private TextView hoursOfOperation;
    /* displays hours of maintenance */
    private TextView maintenanceHours;
    /* Dialog object to display new dialog */
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* initialize new dialog */
        myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        /* initialize handler */
        mHandler = new Handler();

        /* set up custom actionbar */
        showActionBar();
        bindActionBar();

        /* highlights tab associated with activity */
        TabControl tabb = new TabControl(this);
        tabb.tabs(MainActivity.this, R.id.tab_home);

        /* make sure User has GPS enabled */
        askForGPS();

        /* initialize all UI components */
        initializeComponents();
        initializePageViewer();
        refreshDisplay(0);

    }

    /**
     * asks for permission to use GPS if not enabled
     * */
    public void askForGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
    }

    /**
     * requests GPS usage permission
     * */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
    }

    /**
     * This method looks for best location provider by checking availablility of gps or network location
     * @return location closest location to user
     */
    public Location getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        /* iterate through all providers to check for non-null location */
        for (String p : providers) {
            /* permission check */
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions();
            }
            /* skip if location returned is null */
            Location location = locationManager.getLastKnownLocation(p);
            if (location == null) {
                continue;
            }
            /* obtain most accurate location*/
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }
        return bestLocation;
    }

    /**
     * sets up pagerData in order to display bathroom cards
     * */
    private void setUpPagerData() {
        /* load bathrooms from server */
        feedList = LooLoader.loadBathrooms(this.getApplicationContext());

        /* sort by distance by default */
        sortByDistance(getCurrentLocation(), feedList);

        /* add bathrooms from retrieved list */
        reloadViewPagerData(feedList);

        /* stop refreshing animation once page is loaded */
        swipeContainer.setRefreshing(false);
    }

    /**
     * clears pagerAdapter and loads new list of bathrooms as needed
     * @param newBathrooms a list of bathroom  objects to display
     * */
    public void reloadViewPagerData(List<Bathroom> newBathrooms) {
        pagerAdapter.clear();
        viewPager.removeAllViews();

        /* add bathrooms to pagerAdapter */
        for (Bathroom b : newBathrooms) {
            pagerAdapter.addCardFragment(b);
            pagerAdapter.notifyDataSetChanged();
        }
         /* force card views to refresh */
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * sorts a list of bathrooms by distance
     * @param location source location on which to apply sort
     * @param list a list of bathroom objects to sort
     * */
    public void sortByDistance(final Location location, List<Bathroom> list) {
        /* permission check */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        /* sort bathroom list using custom comparator */
        Collections.sort(list, new Comparator<Bathroom>() {
            @Override
            public int compare(Bathroom b1, Bathroom b2) {
                double dist1 = MetricConverter.distanceBetweenInMiles(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(b1.getLatLng().latitude, b1.getLatLng().longitude));
                double dist2 = MetricConverter.distanceBetweenInMiles(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(b2.getLatLng().latitude, b2.getLatLng().longitude));
                if (dist2 < dist1) return 1;
                else if (dist2 > dist1) return -1;
                return 0;
            }
        });
    }

    /**
     * populates bathroom card with latest data
     * @param position current position of active bathroom card
     * */
    private void refreshDisplay(int position) {
        /* get list of bathrooms currently in viewPager */
        bathrooms = pagerAdapter.getBathrooms();
        if (bathrooms.size() > 0) {
            /* populate bathroom amenities for display */
            ArrayList<String> amenitiesList = bathrooms.get(position).getAmenities();
            Collections.sort(amenitiesList);
            String formattedAmenities = amenitiesList.toString().replaceAll(", ", "\n• ");
            formattedAmenities = "• " + formattedAmenities.substring(1, formattedAmenities.length() - 1);
            amenities.setText(formattedAmenities);

            /* format start time of operation hours */
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

            /* format end time of operation hours */
            Date endTime = bathrooms.get(position).getEndTime();
            String end = null;
            if (endTime != null) {
                try {
                    end = sdf.format(endTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /* set hours of operation for display */
            if (startTime == null || endTime == null) {
                hoursOfOperation.setText("Unknown");
            } else if (start.equals(end)) {
                hoursOfOperation.setText("24HR");
            } else {
                hoursOfOperation.setText(start + " to " + end);
            }

            /* format maintenance start time */
            Date maintenanceStartTime = bathrooms.get(position).getMaintenanceStart();
            String mStart = null;
            if (maintenanceStartTime != null) {
                try {
                    mStart = sdf.format(maintenanceStartTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /* format maintenance end time */
            Date maintenanceEndTime = bathrooms.get(position).getMaintenanceEnd();
            String mEnd = null;
            if (maintenanceEndTime != null) {
                try {
                    mEnd = sdf.format(maintenanceEndTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /* format maintenance days */
            String maintenanceDays = bathrooms.get(position).getMaintenanceDays().toString();

            /* set hours of maintenance for display */
            if (maintenanceStartTime == null || maintenanceEndTime == null) {
                maintenanceHours.setText("Unknown");
            } else {
                maintenanceHours.setText(mStart + " to " + mEnd + "\n" + maintenanceDays);
            }
        }
    }

    /**
     * initializes Views
     * */
    private void initializeComponents() {
        amenities = (TextView) findViewById(R.id.bathroom_amenities);
        hoursOfOperation = (TextView) findViewById(R.id.hours_of_operation);
        maintenanceHours = (TextView) findViewById(R.id.maintenance_hours);

        /* Setup refresh listener which triggers new data loading */
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDistance();
                reloadViewPagerData(feedList);

            }
        });
    }

    /**
     * initializes pageerviewer and sets up initial data for bathroom cards
     * */
    private void initializePageViewer() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new BathroomCardFragmentPagerAdapter(getSupportFragmentManager(), MetricConverter.dpToPx(this, 2));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);

        /* set up initial data */
        setUpPagerData();

        /* add UI tweaks to bathroom cards */
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                updateDistance();
                /* load new fragments for bathroom views on click */
                pagerAdapter.getCardViewAt(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadBathroomView();
                    }
                });

                /* TODO webView consumes click events, need to customize */
/*                pagerAdapter.getCardViewAt(position).findViewById(R.id.bathroom_webview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v("webviewview", "clicked");
                        loadBathroomView();
                    }
                });*/
            }

            @Override
            public void onPageSelected(int position) {
                refreshDisplay(position);

                /* TODO load more bathrooms if available if users is at end of cards list */
                if (position == pagerAdapter.getCount() - 1) {
/*                            for(int i = 0; i< 100; i++) {
                                // test add new items:
                                pagerAdapter.addCardFragment(new Bathroom()); //delete me
                            }*/
                                // test scroll to specified position
//                            viewPager.setCurrentItem(15,true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
//                    Log.v("scrollchange", "end of cards " + state + " position " + String.valueOf(viewPager.getVerticalScrollbarPosition()));
                }
            }
        });

    }

    /**
     * updates distance from source to target bathroom */
    public void updateDistance() {
        TextView te = (TextView) pagerAdapter.getCardViewAt(viewPager.getCurrentItem()).findViewById(R.id.bathroom_distance);
        if (mLastKnownLocation == null) {
            /* retrieve lastKnown location if it does not exist */
            mLastKnownLocation = getCurrentLocation();
        }
        DecimalFormat df = new DecimalFormat("0.00");

        try {
            /* format distance based on target location */
            //TODO extend to custom google places results location
            double dist = MetricConverter.distanceBetweenInMiles(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), bathrooms.get(viewPager.getCurrentItem()).getLatLng());
            te.setText(df.format(dist) + " mi");
        } catch (Exception e) {
            swipeContainer.setRefreshing(false);
            Log.v(LOGTAG, e.getMessage());
        }
        swipeContainer.setRefreshing(false);
    }

    /**
     * setup custom actionbar
     * */
    private void showActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.action_bar_main);
        view = getSupportActionBar().getCustomView();
    }

    /**
     * bind listeners to custom views in actionbar
     * */
    public void bindActionBar() {
        ImageButton profileButton = (ImageButton) view.findViewById(R.id.action_bar_profile_main);
        /* bind clicklistener to profile button*/
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
                /* bind clicklistener to login button in dialog */
                bLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        Intent login = new Intent(v.getContext(), ProfileActivity.class);
                        startActivity(login);
                    }
                });
                bSignUp = (Button) myDialog.findViewById(R.id.bRegister);
                /* bind clicklistener to signup button in dialog */
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

        final LinearLayout searchBar = (LinearLayout) view.findViewById(R.id.action_bar_search_main);
        /* bind clicklistener to google places search bar */
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /* start autoplaces search */
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MainActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.v(LOGTAG, "repairableException " + e);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.v(LOGTAG, "notAvailableException " + e);
                }
            }
        });

        ImageButton sortButton = (ImageButton) view.findViewById(R.id.action_bar_sort_main);
        /* bind clicklistener to sort button */
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "sort by...", Toast.LENGTH_SHORT).show();

            }
        });

        ImageButton listViewButton = (ImageButton) view.findViewById(R.id.action_bar_list_view_main);
        /* bind clicklistener to list view button */
        // TODO change list viewbutton to getCurrentLocation button instead
        listViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "view list...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* results from google search */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                newLocation = new Location("");
                newLocation.setLatitude(place.getLatLng().latitude);
                newLocation.setLongitude(place.getLatLng().longitude);
                /* reload bathroom cards sorted by location */
                //TODO add option to sort by rating and name as well
                sortByDistance(newLocation, feedList);
                reloadViewPagerData(feedList);
                TextView searchHint = (TextView) findViewById(R.id.custom_search_layout);
                searchHint.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    /**
     * Returns respective fragment that user
     * selected from navigation menu
     */
    private void loadBathroomView() {
        // set toolbar title
        getSupportActionBar().setTitle("Bathroom View");

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                swipeContainer.setEnabled(false);
                /* update the main content by replacing fragments */
                final Fragment fragment = new BathroomViewFragment();
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);

                /* send messges to fragment */
                Bundle bundle = new Bundle();
                bundle.putParcelable("current bathroom", bathrooms.get(viewPager.getCurrentItem()));
                bundle.putParcelable("current location", getCurrentLocation());
                fragment.setArguments(bundle);

                /* commit fragment transaction */
                fragmentTransaction.commitAllowingStateLoss();

                /* temporarily disable pagerAdapter touch event so it doesn't interfere with fragment events */
                pagerAdapter.getCardViewAt(viewPager.getCurrentItem()).setClickable(false);

                /* set up custom actionbar */
                getSupportActionBar().setCustomView(R.layout.action_bar_back);
                view = getSupportActionBar().getCustomView();

                /* bind clicklistener to actionbar back button */
                ImageButton backButton = (ImageButton) view.findViewById(R.id.action_bar_back_button);
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

            }

            ;
        };


         /* If mPendingRunnable is not null, then add to the message queue */
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        /* refresh toolbar menu */
        invalidateOptionsMenu();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

