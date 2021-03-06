package com.looforyou.looforyou.adapters;
/**
 * Interface for Bathroom Card View Adapter
 *
 * @author mingtau li
 */

import android.support.v7.widget.CardView;

public interface BathroomCardAdapter {

    public final int MAX_ELEVATION_FACTOR = 5;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
