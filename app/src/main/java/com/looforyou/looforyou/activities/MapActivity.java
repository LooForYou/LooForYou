package com.looforyou.looforyou.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.BitmapGenerator;
import com.looforyou.looforyou.utilities.TabControl;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private Toolbar toolbar;
    private ActionMenuView actionMenu;
    private final String GMAPS_TAG = "GException";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

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
        tabb.tabs(MapActivity.this,R.id.tab_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, actionMenu.getMenu());

        TextView textView = (TextView) findViewById(R.id.custom_search_layout);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) textView.getLayoutParams();
        params.width = (int) dpToPx(200);
        textView.setLayoutParams(params);

        //look for custom search bar actionLayout
        for(int i = 0; i< actionMenu.getMenu().size();i++){
            final MenuItem menuItem = actionMenu.getMenu().getItem(i);
            if(menuItem.getItemId() == R.id.action_google_search){
                View view = menuItem.getActionView();
                if(view != null){
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

/*                            menuItem.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                                @Override
                                public void onPlaceSelected(Place place) {
                                    // TODO: Get info about the selected place.
                                    Log.i(TAG, "Place: " + place.getName());//get place details here
                                }

                                @Override
                                public void onError(Status status) {
                                    // TODO: Handle the error.
                                    Log.i(TAG, "An error occurred: " + status);
                                }
                            });*/

                        }
                    });
                }
            }
        }
//        return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Toast.makeText(this,item.getTitle(), Toast.LENGTH_SHORT);
        if(id == R.id.action_google_search) {
            int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
            String GMAPS_TAG = "GException";
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(MapActivity.this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                //test filter
//                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
//                        .build();


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
        Log.v("GException",String.valueOf(item));
        return super.onOptionsItemSelected(item);

    }
    /* end menu*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng longBeach = new LatLng(33.783123,-118.113707);
        BitmapDescriptor icon = BitmapGenerator.drawableToBitmapDescriptor(getResources().getDrawable(R.drawable.map_sit_36));
        googleMap.addMarker(new MarkerOptions()
                .position(longBeach)
                .title("custom marker")
                .anchor(0.5f,0.5f)
                .snippet("custom snippet")
                .icon(icon));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(longBeach));
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
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
    public float dpToPx(float dp) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public float pxToDp(float px) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, r.getDisplayMetrics());
    }

}
