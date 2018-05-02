package com.looforyou.looforyou.activities;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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

import android.Manifest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BathroomCardFragmentPagerAdapter;
import com.looforyou.looforyou.adapters.MapCardFragmentPagerAdapter;
import com.looforyou.looforyou.fragments.BathroomViewFragment;
import com.looforyou.looforyou.utilities.GPSManager;
import com.looforyou.looforyou.utilities.ImageConverter;
import com.looforyou.looforyou.utilities.LooLoader;
import com.looforyou.looforyou.utilities.MetricConverter;
import com.looforyou.looforyou.utilities.ShadowTransformer;
import com.looforyou.looforyou.utilities.TabControl;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.looforyou.looforyou.Constants.SORT_BY_DISTANCE;
import static com.looforyou.looforyou.utilities.Stars.getStarDrawableResource;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener, BathroomViewFragment.OnFragmentInteractionListener {
    private Toolbar toolbar;
    private ActionMenuView actionMenu;
    private final String GMAPS_TAG = "GException";
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 100;

    private final int UPDATE_INTERVAL = 30000; //10 sec
    private final int FASTEST_INTERVAL = 10000; //10 sec
    private GoogleMap googleMap;
    private final float DEFAULT_ZOOM = 14.0f;
    private LocationManager locationManager;
    private Marker lastMarkerClicked;
    private BitmapDescriptor defaultMarker;
    private Location mLastKnownLocation = null;
    // Construct a GeoDataClient.
    GeoDataClient mGeoDataClient = null;
    // Construct a PlaceDetectionClient.
    PlaceDetectionClient mPlaceDetectionClient = null;
    // Construct a FusedLocationProviderClient.
    FusedLocationProviderClient mFusedLocationProviderClient = null;
    CameraPosition mCameraPosition;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private LocationRequest mLocationRequest;
    private View view;
    private SupportMapFragment mapFragment;
    private ImageButton mapDirectionsButton;
    private double markerLatitude;

    private double markerLongitude;
    private List<Bathroom> bathroomList;

    private Handler mHandler;
    private LinearLayout extraInfo;
    private TextView extraInfoBathroomName;
    private ImageView extraInfoImage;
    private TextView extraInfoDistance;
    private TextView extraInfoAddress;
    private ImageView extraInfoStars;
    private TextView extraInfoReviewNumber;
    private ImageView extraInfoAccessibility;
    private ImageView extraInfoKeyless;
    private ImageView extraInfoFree;
    private ImageView extraInfoParking;
    private GoogleApiClient googleApiClient;

    private HashMap<Marker, Integer> markerList = new HashMap<Marker, Integer>();
    private int markerIndex;

    private ViewPager viewPager;
    private MapCardFragmentPagerAdapter pagerAdapter;
    private int pagerCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        showActionBar();
        bindActionBar();

        mCameraPosition = null;
        mLastKnownLocation = null;

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        TabControl tabb = new TabControl(this);
        tabb.tabs(MapActivity.this, R.id.tab_map);

        initializeComponents();

    }

    public void initializeComponents() {
        mHandler = new Handler();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = getFusedLocationProviderClient(this);
        bathroomList = LooLoader.loadBathrooms(this.getApplicationContext(),SORT_BY_DISTANCE);
        mapDirectionsButton = (ImageButton) findViewById(R.id.toilet_directions);
        mapDirectionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapDirectionsClick(v);
            }
        });

    }
    public void initializeMarkers(GoogleMap map, List<Bathroom> bathrooms) {
        map.clear();
        for(int i=0; i< bathrooms.size(); i++){
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(bathrooms.get(i).getCoordinates().latitude,bathrooms.get(i).getCoordinates().longitude))
                    .title(bathrooms.get(i).getName())
                    .anchor(0.5f, 0.5f)
                    .snippet(bathrooms.get(i).getAddress())
                    .icon(defaultMarker));
                    marker.setTag(bathrooms.get(i));
            markerList.put(marker, i);
        }

    }

    public void onMapDirectionsClick(View view){
        view.setVisibility(View.GONE);
        Uri markerUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+markerLatitude+","+markerLongitude);
        Intent directionsIntent = new Intent(Intent.ACTION_VIEW,markerUri);
        directionsIntent.setPackage("com.google.android.apps.maps");
        if(directionsIntent.resolveActivity(getPackageManager()) != null){
            startActivity(directionsIntent);
        }else {
            Toast.makeText(this,"Unable to route", Toast.LENGTH_SHORT).show();
            Log.v(GMAPS_TAG,"map directions failed");
        }
    }

    //when options in toolbar menu are created
    private void showActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setCustomView(R.layout.action_bar_map);
        view = getSupportActionBar().getCustomView();
    }

    public void bindActionBar(){
        ImageButton profileButton= (ImageButton)view.findViewById(R.id.action_bar_profile_map);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this,"profile...", Toast.LENGTH_SHORT).show();

            }
        });

        final LinearLayout searchBar= (LinearLayout)view.findViewById(R.id.action_bar_search_map);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //start autoplaces search
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MapActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.v(GMAPS_TAG, "repairableException " + e);
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    Log.v(GMAPS_TAG, "notAvailableException " + e);
                }
            }
        });

        ImageButton sortButton= (ImageButton)view.findViewById(R.id.action_bar_sort_map);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapActivity.this,"sort by...", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        lastMarkerClicked = null;

        getDeviceLocation();
        updateLocationUI();
        startLocationUpdates();


        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);
        googleMap.setOnMarkerClickListener(this);

        defaultMarker = ImageConverter.drawableToBitmapDescriptor(getResources().getDrawable(R.drawable.ic_toilet_marker_23_36));
        initializeMarkers(googleMap,bathroomList);
        initializePageViewer();
        //TODO override google dialog fragment to display more data

        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            String myLat = String.valueOf(location.getLatitude());
                            String myLon = String.valueOf(location.getLongitude());
                            Log.v(GMAPS_TAG, myLat);
                            Log.v(GMAPS_TAG, myLon);
//                            extraInfoBathroomName.setText("Testing purposes: \n\n\nCurrent Location: " + myLat + ", " + myLon);
                        }
                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(GMAPS_TAG, "Place:" + place.toString());
                Log.i(GMAPS_TAG, "Place:" + place.getLatLng().latitude);
               googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), DEFAULT_ZOOM));
                //clears map every time new location is inputted
//                googleMap.clear();
                mapDirectionsButton.setVisibility(View.GONE);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(GMAPS_TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION) {
            if ((permissions.length == 2 &&
                    permissions[0].equals(permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                }
                getDeviceLocation();
                updateLocationUI();
                startLocationUpdates();
                return;

            } else {
                // Permission was denied. Display an error message.
                Log.v(GMAPS_TAG, "permission denied.");
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
       viewPager.setVisibility(View.VISIBLE);


        updateMarkerIcon(marker);
        lastMarkerClicked = marker;

        markerIndex = markerList.get(marker);
        setPagerInfo((Bathroom) marker.getTag());

        mapDirectionsButton.setVisibility(View.VISIBLE);
        markerLatitude = marker.getPosition().latitude;
        markerLongitude = marker.getPosition().longitude;
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(marker.getPosition().latitude-.009,marker.getPosition().longitude)), (int) DEFAULT_ZOOM, null);
        return true;
    }

    public void updateMarkerIcon(Marker marker) {
        if (lastMarkerClicked != null) {
            lastMarkerClicked.setIcon(defaultMarker);
        }
        marker.setIcon(ImageConverter.drawableToBitmapDescriptor(getResources().getDrawable(R.drawable.ic_toilet_marker_36_55)));
    }
    public void setPagerInfo(Bathroom bathroom) {
        viewPager.setCurrentItem(markerIndex,true);
        updateCardDistance(bathroom);
    }

    public void updateCardDistance(Bathroom bathroom) {
        try {
            extraInfoDistance = pagerAdapter.getCardViewAt(viewPager.getCurrentItem()).findViewById(R.id.map_extra_info_distance);
            DecimalFormat df = new DecimalFormat("0.00");
            double dist = MetricConverter.distanceBetweenInMiles(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),bathroom.getLatLng());
            extraInfoDistance.setText(df.format(dist) + " mi");
        }catch(Exception e) {
            Log.v("home exception",e.getMessage());
        }
    }

    private void updateLocationUI() {
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
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
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
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
                        try {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), (int) DEFAULT_ZOOM));
                        }catch (Exception e){
                            Log.v("GMAPS_TAG",e.getMessage());
                        }
                    } else {
                        Log.d(GMAPS_TAG, "Current location is null. Using defaults.");
                        Log.e(GMAPS_TAG, "Exception: %s", task.getException());
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

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        // Create the location request to start receiving updates
//        mLocationRequest = new LocationRequest();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check to see if GPS is enabled
        new GPSManager(this).checkGPS();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mLastKnownLocation = location;
        Log.v("testLocation", "current location map: " + String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));
    }


    public Location getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadBathroomView(final Bathroom bathroom) {
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
                bundle.putParcelable("current bathroom",bathroom);
                fragment.setArguments(bundle);
                fragmentTransaction.commitAllowingStateLoss();

                getSupportActionBar().setCustomView(R.layout.action_bar_back);
                view = getSupportActionBar().getCustomView();

                ImageButton backButton= (ImageButton) view.findViewById(R.id.action_bar_back_button);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MapActivity.super.onBackPressed();
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

    public void updateMarkerFromPager(int pagerPage) {
        Marker marker = null;
        for (Marker key : markerList.keySet()){
            if(bathroomList.get(viewPager.getCurrentItem()).getId() == ((Bathroom)key.getTag()).getId()){
                marker = key;
                break;
            }
        }
        updateMarkerIcon(marker);
        lastMarkerClicked = marker;
        marker.showInfoWindow();
        LatLng newLatLng = new LatLng(marker.getPosition().latitude-.009,marker.getPosition().longitude);
        CameraUpdate center = CameraUpdateFactory.newLatLng(newLatLng);
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(DEFAULT_ZOOM);
        googleMap.moveCamera(zoom);
        googleMap.animateCamera(center, 400,null);
    }
    /* ========================================================================pageviewer */
    private void initializePageViewer() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new MapCardFragmentPagerAdapter(getSupportFragmentManager(), MetricConverter.dpToPx(this, 2));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
//        viewPager.setPageMargin(5);
        viewPager.setVisibility(View.GONE);
        for (Bathroom b : bathroomList) {
            pagerAdapter.addCardFragment(b);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
                updateCardDistance(bathroomList.get(position));


                pagerAdapter.getCardViewAt(position).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MapActivity.this,bathroomList.get(position).getName()+" clicked", Toast.LENGTH_SHORT).show();
                        loadBathroomView((Bathroom)lastMarkerClicked.getTag());
                    }
                });
            }

            @Override
            public void onPageSelected(int position) {
                updateMarkerFromPager(position);
            }



            @Override
            public void onPageScrollStateChanged(int state) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
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
