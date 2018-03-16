package com.looforyou.looforyou.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BathroomCardFragmentPagerAdapter;
import com.looforyou.looforyou.utilities.ImageFromURL;
import com.looforyou.looforyou.utilities.ImageConverter;
import com.looforyou.looforyou.utilities.LooLoader;
import com.looforyou.looforyou.utilities.MetricConverter;
import com.looforyou.looforyou.utilities.ShadowTransformer;
import com.looforyou.looforyou.utilities.TabControl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
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
    private WebView loadedImage;
    private TextView hoursOfOperation;
    private TextView maintenanceHours;

    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDialog = new Dialog(this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        showActionBar();
        bindActionBar();

        //tabs on bottom control
        TabControl tabb = new TabControl(this);
        tabb.tabs(MainActivity.this, R.id.tab_home);

        initializePageViewer();
        initializeDisplayData();
        refreshDisplay(0);
    }

    private void setupView(){

        List<Bathroom> feedList = LooLoader.loadBathrooms(this.getApplicationContext());


//        Log.d("DEBUG", "LoadMoreView.LOAD_VIEW_SET_COUNT " + LoadMoreView.LOAD_VIEW_SET_COUNT);
//        for(int i = 0; i < LoadMoreView.LOAD_VIEW_SET_COUNT; i++){
//            mLoadMoreView.addView(new MenuView.ItemView(this.getApplicationContext(), feedList.get(i)));
//        }
//        mLoadMoreView.setLoadMoreResolver(new LoadMoreView(mLoadMoreView, feedList));
        Log.v("TEST FEEDLIST","feed size: "+feedList.size());
        Log.v("TEST FEEDLIST",String.valueOf(feedList.get(0).getLatLng()));
        Log.v("TEST FEEDLIST",feedList.get(1).getName());
        Log.v("TEST FEEDLIST",feedList.get(2).getEndTime().toString());
        for(Bathroom b: feedList){
            pagerAdapter.addCardFragment(b);
        }

    }

    private void refreshDisplay(int position){
        bathrooms = pagerAdapter.getBathrooms();

        ArrayList<String> amenitiesList = bathrooms.get(position).getAmenities();
        Collections.sort(amenitiesList);
        String formattedAmenities = amenitiesList.toString().replaceAll(", ","\n• ");
        formattedAmenities = "• " + formattedAmenities.substring(1,formattedAmenities.length()-1);
        amenities.setText(formattedAmenities);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        Date startTime = bathrooms.get(position).getStartTime();
        String start = null;
        if(startTime != null){
            try {
                start = sdf.format(startTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Date endTime = bathrooms.get(position).getEndTime();
        String end = null;
        if(endTime != null){
            try {
                end = sdf.format(endTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(startTime == null || endTime == null){
            hoursOfOperation.setText("Unknown");
        } else if(start.equals(end)){
            hoursOfOperation.setText("24HR");
        }else{
            hoursOfOperation.setText(start+" to "+end);
        }

        Date maintenanceStartTime = bathrooms.get(position).getMaintenanceStart();
        String mStart = null;
        if(startTime != null){
            try {
                mStart = sdf.format(maintenanceStartTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Date maintenanceEndTime = bathrooms.get(position).getMaintenanceEnd();
        String mEnd = null;
        if(endTime != null){
            try {
                mEnd = sdf.format(maintenanceEndTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String maintenanceDays = bathrooms.get(position).getMaintenanceDays().toString();

        if(maintenanceStartTime == null || maintenanceEndTime == null){
            maintenanceHours.setText("Unknown");
        }else{
            maintenanceHours.setText(mStart+" to "+mEnd+"\n"+maintenanceDays);
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

        loadWebviewFromURL(loadedImage,bathrooms.get(position).getImageURL());

    }

    public void loadWebviewFromURL(WebView webview,String url){
        if(url == null || url.equals("")) {
            url = "no-image-uploaded.png";
        }
        String css = "width:100%;height:100%;overflow:hidden;background:url("+url+");background-size:cover;background-position:center center;";
        String html = "<html><body style=\"height:100%;width:100%;margin:0;padding:0;overflow:hidden;\">" + "<div style=\"" + css + "\"></div></body></html>";
        webview.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }
    private void initializeDisplayData() {
        amenities = (TextView) findViewById(R.id.bathroom_amenities);
        loadedImage = (WebView) findViewById(R.id.bathroom_webview);
        hoursOfOperation = (TextView) findViewById(R.id.hours_of_operation);
        maintenanceHours = (TextView) findViewById(R.id.maintenance_hours);

        final ProgressBar Pbar = (ProgressBar) findViewById(R.id.bathroom_progress);
        loadedImage.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress < 100 && Pbar.getVisibility() == ProgressBar.GONE){
                    Pbar.setVisibility(ProgressBar.VISIBLE);
                }
                Pbar.setProgress(progress);
                if(progress == 100) {
                    Pbar.setVisibility(ProgressBar.GONE);
                }
            }
        });

    }
    private void initializePageViewer(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new BathroomCardFragmentPagerAdapter(getSupportFragmentManager(), MetricConverter.dpToPx(this,2));

        setupView();

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);


        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
        fragmentCardShadowTransformer.enableScaling(true);
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.v("scrollchange", "position "+String.valueOf(position));
               /* Drawable[] layers = new Drawable[2];
                layers[0] = previousImage.getDrawable();
                try {
                    layers[1] = bathrooms.get(position).setImage(new ImageFromURL(MainActivity.this).execute(bathrooms.get(position).getImageURL()).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/
                for(int i = 0; i<2;i++){
                    Log.v("scrollchange: drawables", String.valueOf(bathrooms.get(i).getImage()));
                }
//                layers[1] = tempPics.get(position);
//                Log.v("drawable",String.valueOf(pagerAdapter.getBathrooms().get(position).getImage()));
//                pagerAdapter.getBathrooms().get(position).getImage();
//               layers[1] =  pagerAdapter.getBathrooms().get(position).getImage();

//                TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
/*                TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
                currentImage.setImageDrawable(transitionDrawable);
                transitionDrawable.startTransition(150);
                previousImage = currentImage;*/

                refreshDisplay(position);


                if(position == pagerAdapter.getCount() - 1) {

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
                if(ViewPager.SCROLL_STATE_IDLE == state){
//                        Log.v("scrollchange","end of cards "+state);
                    Log.v("scrollchange","end of cards "+state + " position "+String.valueOf(viewPager.getVerticalScrollbarPosition()));

                }



            }
        });
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
                myDialog.setContentView(R.layout.activity_login);
                txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
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

}

