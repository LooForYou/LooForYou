package com.looforyou.looforyou.activities;

import android.Manifest;
import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.MapCardFragmentPagerAdapter;
import com.looforyou.looforyou.fragments.BathroomViewFragment;
import com.looforyou.looforyou.utilities.ImageConverter;
import com.looforyou.looforyou.utilities.LooLoader;
import com.looforyou.looforyou.utilities.MetricConverter;
import com.looforyou.looforyou.utilities.TabControl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.looforyou.looforyou.Constants.SORT_BY_DISTANCE;

/**
 * This is the map view that displays custom location markers
 * and cards for bathrooms in the area
 *
 * @author mingtau li
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener, BathroomViewFragment.OnFragmentInteractionListener {
    /* log tag */
    private final String LOGTAG = "GException";
    /* constant for keeping track of autocomplate location request */
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    /* constant for keeping track of location tracking */
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 100;
    /* constant for location update interval */
    private final int UPDATE_INTERVAL = 30000; //30 sec
    /* constant for fastest location update interval */
    private final int FASTEST_INTERVAL = 10000; //10 sec
    /* constant for default map zoom */
    private final float DEFAULT_ZOOM = 14.0f;
    /* constant for keeping track of camera position */
    private static final String KEY_CAMERA_POSITION = "camera_position";
    /* constant for keeping track of location */
    private static final String KEY_LOCATION = "location";
    /* googlemap object for map functions */
    private GoogleMap googleMap;
    /* keeps track of last marker clicked*/
    private Marker lastMarkerClicked;
    /* default marker image */
    private BitmapDescriptor defaultMarker;
    /* keeps track of last known location */
    private Location mLastKnownLocation = null;
    /* GeoDataClient for geolocation */
    GeoDataClient mGeoDataClient = null;
    /* PlaceDetectionClient for detecting location */
    PlaceDetectionClient mPlaceDetectionClient = null;
    /* FusedLocationProviderClient for getting current location */
    FusedLocationProviderClient mFusedLocationProviderClient = null;
    /* saves camera position */
    CameraPosition mCameraPosition;
    /* makes request for location providers/services */
    private LocationRequest mLocationRequest;
    /* view for action bar */
    private View view;
    /* map support fragment */
    private SupportMapFragment mapFragment;
    /* Button for navigation */
    private ImageButton mapDirectionsButton;
    /* keeps track of marker latitude */
    private double markerLatitude;
    /* keeps track of marker longitude */
    private double markerLongitude;
    /* list of all bathrooms in server */
    private List<Bathroom> bathroomList;
    /* handler used for loading fragments */
    private Handler mHandler;
    /* extra bathroom card information */
    private TextView extraInfoDistance;
    /* keeps track of all markers on the map */
    private HashMap<Marker, Integer> markerList = new HashMap<Marker, Integer>();
    /* keeps track of marker index in markerlist */
    private int markerIndex;
    /* viewPager for bathroom card display */
    private ViewPager viewPager;
    /* adapter for bathroom viewPager */
    private MapCardFragmentPagerAdapter pagerAdapter;
    /* keeps track of bathroom card location for infinite scrolling */
    private int pagerCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /* set up custom actionbar */
        showActionBar();
        bindActionBar();

        /* initialize map elements */
        mCameraPosition = null;
        mLastKnownLocation = null;

        /* retrieve saved location from parcelable */
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        /* highlights tab associated with activity */
        TabControl tabb = new TabControl(this);
        tabb.tabs(MapActivity.this, R.id.tab_map);

        /* initialize map components */
        initializeComponents();

    }

    /**
     * initializes all components for map usage and display
     */
    public void initializeComponents() {
        mHandler = new Handler();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = getFusedLocationProviderClient(this);
        bathroomList = LooLoader.loadBathrooms(this.getApplicationContext(), SORT_BY_DISTANCE);
        mapDirectionsButton = (ImageButton) findViewById(R.id.toilet_directions);
        mapDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapDirectionsClick(v);
            }
        });
    }

    /**
     * adds markers to map
     *
     * @param map       Google Map object
     * @param bathrooms list of bathrooms
     */
    public void initializeMarkers(GoogleMap map, List<Bathroom> bathrooms) {
        /* clear any existing markers */
        map.clear();
        /* add markers to map based on location of bathrooms in bathroom list */
        for (int i = 0; i < bathrooms.size(); i++) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(bathrooms.get(i).getCoordinates().latitude, bathrooms.get(i).getCoordinates().longitude))
                    .title(bathrooms.get(i).getName())
                    .anchor(0.5f, 0.5f)
                    .snippet(bathrooms.get(i).getAddress())
                    .icon(defaultMarker));
            marker.setTag(bathrooms.get(i));
            /* add marker to marker map */
            markerList.put(marker, i);
        }

    }

    /**
     * handler for navigation directions in Google maps app
     */
    public void onMapDirectionsClick(View view) {
        view.setVisibility(View.GONE);
        Uri markerUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + markerLatitude + "," + markerLongitude);
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW, markerUri);
        /* use google maps app */
        directionsIntent.setPackage("com.google.android.apps.maps");
        if (directionsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(directionsIntent);
        } else {
            Toast.makeText(this, "Unable to route", Toast.LENGTH_SHORT).show();
            Log.v(LOGTAG, "map directions failed");
        }
    }

    /**
     * set up custom action bar
     */
    private void showActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.action_bar_map);
        view = getSupportActionBar().getCustomView();
    }

    /**
     * bind clicklisteners to actionbar
     */
    public void bindActionBar() {
        ImageButton profileButton = (ImageButton) view.findViewById(R.id.action_bar_profile_map);
        /* bind clicklistener to profileButton */
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this, "profile...", Toast.LENGTH_SHORT).show();

            }
        });

        final LinearLayout searchBar = (LinearLayout) view.findViewById(R.id.action_bar_search_map);
        /* bind clicklistener to google maps search */
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //start autoplaces search
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MapActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.v(LOGTAG, "repairableException " + e);
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.v(LOGTAG, "notAvailableException " + e);
                }
            }
        });

        ImageButton sortButton = (ImageButton) view.findViewById(R.id.action_bar_sort_map);
        /* bind clicklistener for sorting */
        //TODO implement sort
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this, "sort by...", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /* save camera and location position */
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /* initializes components onto map once it's ready */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        /* initialize googleMap object */
        this.googleMap = googleMap;
        /* initialize last marker clicked*/
        lastMarkerClicked = null;

        /* get device location */
        getDeviceLocation();
        /* set up and start location updates */
        updateLocationUI();
        startLocationUpdates();

        /* permission check */
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }

        /* set up initial google maps options */
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        googleMap.setOnMarkerClickListener(this);

        /* populate markers onto map */
        defaultMarker = ImageConverter.drawableToBitmapDescriptor(getResources().getDrawable(R.drawable.ic_toilet_marker_23_36));
        initializeMarkers(googleMap, bathroomList);
        initializePageViewer();

        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                         /* Got last known location. In some rare situations this can be null */
                        if (location != null) {
                            //Test
                            /*String myLat = String.valueOf(location.getLatitude());
                            String myLon = String.valueOf(location.getLongitude());
                            Log.v(LOGTAG, myLat);
                            Log.v(LOGTAG, myLon);*/
                        }
                    }
                });
    }

    /* on activity result for google maps search */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                /* moves map camera to new location */
                Place place = PlaceAutocomplete.getPlace(this, data);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), DEFAULT_ZOOM));
                mapDirectionsButton.setVisibility(View.GONE);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(LOGTAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    /* overide permission results for custom functionality */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION) {
            if ((permissions.length == 2 &&
                    permissions[0].equals(permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                /* request permissions if not given */
                if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                    return;
                }
                /* if permission granted, start location updates */
                getDeviceLocation();
                updateLocationUI();
                startLocationUpdates();
                return;

            } else {
                /* Permission was denied. Display an error message */
                Log.v(LOGTAG, "permission denied.");
            }
        }
    }

    /* override marker click functionality
    * @param marker marker clicked
    * @return boolean override default behavior*/
    @Override
    public boolean onMarkerClick(Marker marker) {
        /* show bathroom cards */
        viewPager.setVisibility(View.VISIBLE);

        /* updata marker info */
        updateMarkerIcon(marker);
        lastMarkerClicked = marker;
        markerIndex = markerList.get(marker);
        markerLatitude = marker.getPosition().latitude;
        markerLongitude = marker.getPosition().longitude;

        /* set bathroom card location based on marker*/
        setPagerInfo((Bathroom) marker.getTag());

        /* enable navigation*/
        mapDirectionsButton.setVisibility(View.VISIBLE);

        return true;
    }

    /**
     * updates custom marker icon from clicked marker
     *
     * @param marker clicked marker
     */
    public void updateMarkerIcon(Marker marker) {
        if (lastMarkerClicked != null) {
            lastMarkerClicked.setIcon(defaultMarker);
        }
        marker.setIcon(ImageConverter.drawableToBitmapDescriptor(getResources().getDrawable(R.drawable.ic_toilet_marker_36_55)));
    }

    /**
     * sets current bathroom card based on last marker clicked
     *
     * @param bathroom current bathroom object
     */
    public void setPagerInfo(Bathroom bathroom) {
        viewPager.setCurrentItem(markerIndex, true);
        updateCardDistance(bathroom);
    }

    /**
     * updates distance of bathroom card
     *
     * @param bathroom current bathroom object
     */
    public void updateCardDistance(Bathroom bathroom) {
        try {
            extraInfoDistance = pagerAdapter.getCardViewAt(viewPager.getCurrentItem()).findViewById(R.id.map_extra_info_distance);
            DecimalFormat df = new DecimalFormat("0.00");
            double dist = MetricConverter.distanceBetweenInMiles(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), bathroom.getLatLng());
            extraInfoDistance.setText(df.format(dist) + " mi");
        } catch (Exception e) {
            Log.i(LOGTAG, e.getMessage());
        }
    }

    /**
     * enables location tracking
     */
    private void updateLocationUI() {
        /* permission check */
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        if (googleMap == null) {
            return;
        }
        try {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);

        } catch (SecurityException e) {
            Log.i(LOGTAG, e.getMessage());
        }
    }

    /**
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
    private void getDeviceLocation() {
        /* permission check */
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        try {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                         /* Set the map's camera position to the current location of the device. */
                        mLastKnownLocation = (Location) task.getResult();
                        try {
                            /* animate camera to found location */
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), (int) DEFAULT_ZOOM));
                        } catch (Exception e) {
                            Log.i(LOGTAG, e.getMessage());
                        }
                    } else {
                        Log.i(LOGTAG, "Current location is null. Using defaults.");
                        Log.i(LOGTAG, String.valueOf(task.getException()));
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    /**
     * requests GPS permission
     */
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
    }

    /**
     * Triggers new location updates at interval
     */
    protected void startLocationUpdates() {
        /* perission check */
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        /* Create the location request to start receiving updates */
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        /* Create LocationSettingsRequest object using location request */
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        /* Check whether location settings are satisfied */
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        /* permission check */
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        /* new Google API SDK v11 uses getFusedLocationProviderClient(this) to request location */
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());

    }

    /**
     * update last known location
     */
    public void onLocationChanged(Location location) {
        mLastKnownLocation = location;
    }


    /**
     * Returns respective fragment that user
     * selected from navigation menu
     */
    private void loadBathroomView(final Bathroom bathroom) {
        /* set toolbar title */
        getSupportActionBar().setTitle("Bathroom View");

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                /* update the main content by replacing fragments */
                final Fragment fragment = new BathroomViewFragment();
                final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);

                /* send message to fragment */
                Bundle bundle = new Bundle();
                bundle.putParcelable("current bathroom", bathroom);
                fragment.setArguments(bundle);

                /* commit fragment transaction */
                fragmentTransaction.commitAllowingStateLoss();

                /* set up custom actionbar for fragment */
                getSupportActionBar().setCustomView(R.layout.action_bar_back);
                view = getSupportActionBar().getCustomView();

                /* bind actionbar back button to listener */
                ImageButton backButton = (ImageButton) view.findViewById(R.id.action_bar_back_button);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MapActivity.super.onBackPressed();
                        showActionBar();
                        bindActionBar();
                    }
                });

            }

            ;
        };

       /*  If mPendingRunnable is not null, then add to the message queue */
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        /* refresh toolbar menu */
        invalidateOptionsMenu();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * updates google maps camera to respective map marker based on pagerpage
     */
    public void updateMarkerFromPager() {
        Marker marker = null;
        /* get current bathroom marker from markerList */
        for (Marker key : markerList.keySet()) {
            if (bathroomList.get(viewPager.getCurrentItem()).getId().equals(((Bathroom) key.getTag()).getId())) {
                marker = key;
                break;
            }
        }

        /* update marker UI*/
        updateMarkerIcon(marker);
        lastMarkerClicked = marker;
        marker.showInfoWindow();

        /* get new location shifted up and update google camera (so markers don't get covered by bathroom cards) */
        LatLng newLatLng = new LatLng(marker.getPosition().latitude - .009, marker.getPosition().longitude);
        CameraUpdate center = CameraUpdateFactory.newLatLng(newLatLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(DEFAULT_ZOOM);
        googleMap.moveCamera(zoom);
        googleMap.animateCamera(center, 400, null);
    }

    /**
     * sets up viewPager for bathroom card display
     */
    private void initializePageViewer() {
        /* initialize viewPager */
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        /* initialize pager adapter */
        pagerAdapter = new MapCardFragmentPagerAdapter(getSupportFragmentManager(), MetricConverter.dpToPx(this, 2));

        /* set pagerAdapter to viewPager*/
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setVisibility(View.GONE);

        /* add new card fragments to pagerAdapter cardview */
        for (Bathroom b : bathroomList) {
            pagerAdapter.addCardFragment(b);
        }

        /* set custom functions to viewPager listener */
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                updateCardDistance(bathroomList.get(position));

                /* set click listener to pagerAdapter to load bathroom view fragment */
                pagerAdapter.getCardViewAt(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadBathroomView((Bathroom) lastMarkerClicked.getTag());
                    }
                });
            }

            @Override
            public void onPageSelected(int position) {
                updateMarkerFromPager();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    /* enable endless scrolling */
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        int position = viewPager.getCurrentItem();
                        int lastView = viewPager.getAdapter().getCount() - 1;
                        if (position < lastView) {
                            pagerCounter = 0;
                        } else if (position == lastView) {
                            pagerCounter++;
                            if (pagerCounter == bathroomList.size() - 1) {
                                viewPager.setOffscreenPageLimit(0);
                                viewPager.setCurrentItem(0, true);
                                pagerCounter = 0;
                            }
                        }
                    }

                }

            }

        });
    }
}
