package com.looforyou.looforyou.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.looforyou.looforyou.fragments.BathroomCardFragment;

import java.util.ArrayList;
import java.util.List;

public class BathroomCardFragmentPagerAdapter extends FragmentStatePagerAdapter implements BathroomCardAdapter {

    private List<BathroomCardFragment> fragments;
    private float baseElevation;

    public BathroomCardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;

        for(int i = 0; i< 8; i++){
            addCardFragment(new BathroomCardFragment());
        }
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
        return BathroomCardFragment.getInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (BathroomCardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(BathroomCardFragment fragment) {
        fragments.add(fragment);
        notifyDataSetChanged();
    }

}
