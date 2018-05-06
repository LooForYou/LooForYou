package com.looforyou.looforyou.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.fragments.MapCardFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the adapter for displaying bathroom cards on map
 *
 * @author: mingtau li
 */

public class MapCardFragmentPagerAdapter extends FragmentStatePagerAdapter implements BathroomCardAdapter {
    /* list of Bathroom Card Fragments */
    private List<MapCardFragment> fragments;
    /* elevation of card */
    private float baseElevation;
    /* list of bathrooms to display */
    private ArrayList<Bathroom> bathrooms;

    /**
     * Constructor for Bathroom Fragment Adapter
     * @param fm Fragment Manager Object
     * @param baseElevation default elevation of card
     * */
    public MapCardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        fragments = new ArrayList<>();
        bathrooms = new ArrayList<Bathroom>();
        this.baseElevation = baseElevation;
        //test inserting bathrooms
/*        for(int i = 0; i< 8; i++){
            Bathroom br = new Bathroom();
            br.setName("BATHROOM "+(i+1));
            addCardFragment(br);
        }*/
    }

    /**
     * adds a new bathroom to list of bathrooms
     * */
    public void addBathroom(Bathroom bathroom) {
        bathrooms.add(bathroom);
    }

    /* getter for bathrooms list
    * @return ArrayList<Bathroom> list of bathrooms
    * */
    public ArrayList<Bathroom> getBathrooms() {
        return bathrooms;
    }

    /**
     *  returns elevation of card
     * @return float card elevation amount
     * */
    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    /**
     * getter for cardView
     * @param position current position
     * @return CardView cardview based on current position
     * */
    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    /**
     * returns number of fragments
     * @return int number of fragments
     * */
    @Override
    public int getCount() {
        return fragments.size();
    }

    /** getter for current fragment
     * @param position current position
     * @return Fragment Bathroom card fragment based on position
     * */
    @Override
    public Fragment getItem(int position) {
        return MapCardFragment.getInstance(position, bathrooms.get(position));
    }

    /**
     * instantiates Fragments
     * @param container Viewgroup container
     * @param position current position
     * @return Object instantiated fragment
     * */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (MapCardFragment) fragment);
        return fragment;
    }

    /**
     * adds new Bathroom fragment to fragment list
     * @param bathroom Bathroom to add
     * */
    public void addCardFragment(Bathroom bathroom) {
        addBathroom(bathroom);
        MapCardFragment fragment = new MapCardFragment();
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    /**
     * wipes fragment and bathroom data
     * */
    public void clear() {
        bathrooms.clear();
        fragments.clear();
        notifyDataSetChanged();
    }

}
