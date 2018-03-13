package com.looforyou.looforyou.activities;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.R;
import com.looforyou.looforyou.adapters.BathroomCardFragmentPagerAdapter;
import com.looforyou.looforyou.utilities.ImageConverter;
import com.looforyou.looforyou.utilities.LooLoader;
import com.looforyou.looforyou.utilities.MetricConverter;
import com.looforyou.looforyou.utilities.ShadowTransformer;
import com.looforyou.looforyou.utilities.TabControl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionMenuView actionMenu;
    private final String GMAPS_TAG = "GException";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private View view;
    private ArrayList<Drawable> tempPics;
    private ImageView currentImage;
    private ImageView previousImage;
    private BathroomCardFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private GestureDetector gestureScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showActionBar();
        bindActionBar();

        //tabs on bottom control
        TabControl tabb = new TabControl(this);
        tabb.tabs(MainActivity.this, R.id.tab_home);

        initializePageViewer();

//        setupView();
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
//          tempPics.add(BitmapGenerator.DrawableFromAsset(MainActivity.this, "no-image-uploaded.png")); //delete me
        }
        //delete me
        for(int i= 0;i<pagerAdapter.getCount();i++){
            if(i%3 == 1){
                tempPics.add(ImageConverter.DrawableFromAsset(this,"no-image-uploaded.png"));
            }else if(i%3 == 2) {
                tempPics.add(ImageConverter.DrawableFromAsset(this,"temp_toilet_1.jpg"));
            }else {
                tempPics.add(ImageConverter.DrawableFromAsset(this,"temp_toilet_2.jpg"));
            }
        }
        //end delet me
    }

    private void initializePageViewer(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new BathroomCardFragmentPagerAdapter(getSupportFragmentManager(), MetricConverter.dpToPx(this,2));
        tempPics = new ArrayList<Drawable>();
        setupView();



        currentImage = (ImageView) findViewById(R.id.selected_location_image);
        previousImage = currentImage;
        currentImage.setImageDrawable(tempPics.get(0));

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
                Drawable[] layers = new Drawable[2];

                layers[0] = previousImage.getDrawable();
                layers[1] = tempPics.get(position);


                TransitionDrawable transitionDrawable = new TransitionDrawable(layers);
                currentImage.setImageDrawable(transitionDrawable);
                transitionDrawable.startTransition(150);
                previousImage = currentImage;

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
//                Log.v("Custom Tag","profile button clicked");
                Toast.makeText(MainActivity.this,"profile...", Toast.LENGTH_SHORT).show();

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

