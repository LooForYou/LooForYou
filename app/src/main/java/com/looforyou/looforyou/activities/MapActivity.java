package com.looforyou.looforyou.activities;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.BitmapGenerator;
import com.looforyou.looforyou.utilities.MetricConverter;
import com.looforyou.looforyou.utilities.TabControl;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener {
    private Toolbar toolbar;
    private ActionMenuView actionMenu;
    private final String GMAPS_TAG = "GException";
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 100;

    private final int UPDATE_INTERVAL = 10000; //10 sec
    private final int FASTEST_INTERVAL = 2000; //2 sec
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
    private TextView testText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mCameraPosition = null;
        mLastKnownLocation = null;

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        testText = (TextView) findViewById(R.id.testText);
        //replace default actionbar with custom toolbar layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //get rid of title


        //set click listener on custom toolbar menu
        actionMenu = (ActionMenuView) toolbar.findViewById(R.id.leftMenu);
        actionMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });

        TabControl tabb = new TabControl(this);
        tabb.tabs(MapActivity.this, R.id.tab_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = getFusedLocationProviderClient(this);


        testText.setText("getting location...");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, actionMenu.getMenu());

        TextView textView = (TextView) findViewById(R.id.custom_search_layout);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView.getLayoutParams();
        params.width = (int) MetricConverter.dpToPx(this,200);
        textView.setLayoutParams(params);

        //look for custom search bar actionLayout
        for (int i = 0; i < actionMenu.getMenu().size(); i++) {
            final MenuItem menuItem = actionMenu.getMenu().getItem(i);
            if (menuItem.getItemId() == R.id.action_google_search) {
                View view = menuItem.getActionView();
                if (view != null) {
                    //set listener on searchbar
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

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
                }
            }
        }
        return super.onCreateOptionsMenu(menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT);
        if (id == R.id.action_google_search) {
            int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
            String GMAPS_TAG = "GException";
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(MapActivity.this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

            } catch (GooglePlayServicesRepairableException e) {
                Toast.makeText(this, "repairableException", Toast.LENGTH_SHORT);
                Log.v(GMAPS_TAG, "repairableException " + e);
            } catch (GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
                Toast.makeText(this, "ServicesNotAvailableException", Toast.LENGTH_SHORT);
                Log.v(GMAPS_TAG, "notAvailableException " + e);
            }
        }
//        }else {
        Log.v("GException", String.valueOf(item));
        return super.onOptionsItemSelected(item);

    }
    /* end menu*/

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


        //test add custom marker
        LatLng longBeach = new LatLng(33.783123, -118.113707);
        LatLng longBeach2 = new LatLng(33.783516, -118.118719);
        defaultMarker = BitmapGenerator.drawableToBitmapDescriptor(getResources().getDrawable(R.drawable.ic_toilet_marker_23_36));

        //TODO override google dialog fragment to display more data
        googleMap.addMarker(new MarkerOptions()
                .position(longBeach)
                .title("bathroom 1")
                .anchor(0.5f, 0.5f)
                .snippet("bathroom 1 info\ninfo2")
                .icon(defaultMarker));

        googleMap.addMarker(new MarkerOptions()
                .position(longBeach2)
                .title("bathroom 2")
                .anchor(0.5f, 0.5f)
                .snippet("custom info")
                .icon(defaultMarker));

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
                            testText.setText("Current Location: " + myLat + ", " + myLon);
                        }
                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(GMAPS_TAG, "Place:" + place.toString());
                Log.i(GMAPS_TAG, "Place:" + place.getLatLng().latitude);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), DEFAULT_ZOOM));
                //clears map every time new location is inputted
                googleMap.clear();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(GMAPS_TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions();
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationButtonClickListener((GoogleMap.OnMyLocationButtonClickListener) this);
                googleMap.setOnMyLocationClickListener((GoogleMap.OnMyLocationClickListener) this);
                return;

            } else {
                // Permission was denied. Display an error message.
                Log.v(GMAPS_TAG, "permission denied.");
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.v(GMAPS_TAG, "marker clicked");
        if (lastMarkerClicked != null) {
            lastMarkerClicked.setIcon(defaultMarker);
        }
        marker.setIcon(BitmapGenerator.drawableToBitmapDescriptor(getResources().getDrawable(R.drawable.ic_toilet_marker_36_55)));
        lastMarkerClicked = marker;
        return true;
    }

    private void updateLocationUI() {
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
        try {
            Task locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
//                        Log.v(GMAPS_TAG,String.valueOf(getCurrentLocation().getLatitude())); //delete me
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
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

//    public float dpToPx(float dp) {
//        Resources r = getResources();
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
//    }
//
//    public float pxToDp(float px) {
//        Resources r = getResources();
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, r.getDisplayMetrics());
//    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

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
        //what to do when location determined
        mLastKnownLocation = location;
        testText.setText("current location: " + String.valueOf(location.getLatitude()) + ", " + String.valueOf(location.getLongitude()));
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



}
