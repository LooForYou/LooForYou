package com.looforyou.looforyou.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;

import com.looforyou.looforyou.Models.Bathroom;
import com.looforyou.looforyou.fragments.BathroomCardFragment;

import java.util.ArrayList;
import java.util.List;

//import static com.looforyou.looforyou.fragments.BathroomCardFragment.newInstance;

public class BathroomCardFragmentPagerAdapter extends FragmentStatePagerAdapter implements BathroomCardAdapter {

    private List<BathroomCardFragment> fragments;
    private float baseElevation;
    private ArrayList<Bathroom> bathrooms;

    public BathroomCardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
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

    public void addBathroom(Bathroom bathroom){
        bathrooms.add(bathroom);
    }

    public ArrayList<Bathroom> getBathrooms(){
        return bathrooms;
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return BathroomCardFragment.getInstance(position,bathrooms.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (BathroomCardFragment) fragment);
        return fragment;
    }

//    public BathroomCardFragment initializeCard(BathroomCardFragment fragment, Bathroom bathroom){
//        fragment.setTitle(bathroom.getName());
//
//        return fragment;
//    }

    public void addCardFragment(Bathroom bathroom) {
//        fragments.add(initializeCard(fragment,bathroom));
        addBathroom(bathroom);
        BathroomCardFragment fragment = new BathroomCardFragment();


//        fragment.initializeCard(bathroom);
        Log.v("cardfragment:", "bathroom name: "+bathroom.getName());
        Log.v("cardfragment:","value of fragment: "+String.valueOf(fragment));
//        Log.v("cardfragment:",fragment.toString());
        fragments.add(fragment);
        notifyDataSetChanged();
    }

}
