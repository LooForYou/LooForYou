package com.looforyou.looforyou.utilities;

/**
 * Copyright 2016 Rúben Sousa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * ShadowTransformer allows main viewPager to scale to size
 * obtained from online tutorial template 3/15/18
*/

import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.View;

import com.looforyou.looforyou.adapters.BathroomCardAdapter;

public class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {

    private ViewPager viewPager;
    private BathroomCardAdapter cardAdapter;
    private float lastOffset;
    private boolean scalingEnabled;

    public ShadowTransformer(ViewPager viewPager, BathroomCardAdapter adapter) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        cardAdapter = adapter;
    }

    public void enableScaling(boolean enable) {
        if (scalingEnabled && !enable) {
            // shrink main card
            CardView currentCard = cardAdapter.getCardViewAt(viewPager.getCurrentItem());
            if (currentCard != null) {
                currentCard.animate().scaleY(1);
                currentCard.animate().scaleX(1);
            }
        }else if(!scalingEnabled && enable){
            // grow main card
            CardView currentCard = cardAdapter.getCardViewAt(viewPager.getCurrentItem());
            if (currentCard != null) {
                //enlarge the current item
                currentCard.animate().scaleY(1.1f);
                currentCard.animate().scaleX(1.1f);
            }
        }
        scalingEnabled = enable;
    }

    @Override
    public void transformPage(View page, float position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int realCurrentPosition;
        int nextPosition;
        float baseElevation = cardAdapter.getBaseElevation();
        float realOffset;
        boolean goingLeft = lastOffset > positionOffset;

        // If we're going backwards, onPageScrolled receives the last position
        // instead of the current one
        if (goingLeft) {
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        } else {
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffset;
        }

        // Avoid crash on overscroll
        if (nextPosition > cardAdapter.getCount() - 1
                || realCurrentPosition > cardAdapter.getCount() - 1) {
            return;
        }

        CardView currentCard = cardAdapter.getCardViewAt(realCurrentPosition);

        // This might be null if a fragment is being used
        // and the views weren't created yet
        if (currentCard != null) {
            if (scalingEnabled) {
                currentCard.setScaleX((float) (1 + 0.1 * (1 - realOffset)));
                currentCard.setScaleY((float) (1 + 0.1 * (1 - realOffset)));
            }
            currentCard.setCardElevation((baseElevation + baseElevation
                    * (BathroomCardAdapter.MAX_ELEVATION_FACTOR - 1) * (1 - realOffset)));
        }

        CardView nextCard = cardAdapter.getCardViewAt(nextPosition);

        // We might be scrolling fast enough so that the next (or previous) card
        // was already destroyed or a fragment might not have been created yet
        if (nextCard != null) {
            if (scalingEnabled) {
                nextCard.setScaleX((float) (1 + 0.1 * (realOffset)));
                nextCard.setScaleY((float) (1 + 0.1 * (realOffset)));
            }
            nextCard.setCardElevation((baseElevation + baseElevation
                    * (BathroomCardAdapter.MAX_ELEVATION_FACTOR - 1) * (realOffset)));
        }

        lastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}