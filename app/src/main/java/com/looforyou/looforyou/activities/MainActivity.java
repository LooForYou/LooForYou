package com.looforyou.looforyou.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.utilities.TabControl;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionMenuView leftMenu;
    private final String GMAPS_TAG = "GException";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //replace default actionbar with custom toolbar layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //get rid of title

        //set click listener on custom toolbar menu
        leftMenu = (ActionMenuView) toolbar.findViewById(R.id.leftMenu);
        leftMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });

        //tabs on bottom control
        TabControl tabb = new TabControl(this);
        tabb.tabs(MainActivity.this, R.id.tab_home);
    }

    //when options in toolbar menu are created
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, leftMenu.getMenu());

        //look for custom search bar actionLayout
        for(int i = 0; i< leftMenu.getMenu().size();i++){
            final MenuItem menuItem = leftMenu.getMenu().getItem(i);
            if(menuItem.getItemId() == R.id.google_search){
                View view = menuItem.getActionView();
                if(view != null){
                    //set listener on searchbar
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            
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
        if(id == R.id.google_search) {
            int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
            String GMAPS_TAG = "GException";
            try {
                Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
//                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
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
//        }
//        if (id == R.id.menu_search) {
//            // Method #3
//            try {
//                Intent intent = new PlaceAutocomplete.IntentBuilder
//                        (PlaceAutocomplete.MODE_FULLSCREEN)
////                        .setBoundsBias(BOUNDS_MOUNTAIN_VIEW)
//                        .build(MainActivity.this);
//                startActivityForResult(intent, 1);
//            } catch (GooglePlayServicesRepairableException |
//                    GooglePlayServicesNotAvailableException e) {
//                e.printStackTrace();
//            }
//            return true;
//        }
        return super.onOptionsItemSelected(item);

    }
    /* end menu*/


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


}
