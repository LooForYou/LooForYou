package com.looforyou.looforyou.utilities;

import com.looforyou.looforyou.R;

/**
 * utility class that simply returns drawable assets based on given value
 *
 * @author mingtau li
 */
public class Stars {

    /**
     * retrieve corresponding drawable image resource from given value
     *
     * @param num numerical represenataion of stars
     * @return int resource id of drawable
     */
    public static int getStarDrawableResource(int num) {
        switch (num) {
            case 0:
                return R.drawable.no_stars_90_15;
            case 1:
                return R.drawable.one_star_90_15;
            case 2:
                return R.drawable.two_stars_90_15;
            case 3:
                return R.drawable.three_stars_90_15;
            case 4:
                return R.drawable.four_stars_90_15;
            case 5:
                return R.drawable.five_stars_90_15;
            default:
                return R.drawable.no_stars_90_15;
        }
    }
}
