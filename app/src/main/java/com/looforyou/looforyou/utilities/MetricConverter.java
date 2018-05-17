package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * A small utility class used for miscellaneous conversions between various formats
 *
 * @author mingtau li
 */

public class MetricConverter {

    /**
     * converts dp to px
     *
     * @param context activity context
     * @param dp      dp value to be converted
     * @return float px value
     */
    public static float dpToPx(Context context, float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    /**
     * converts sp to px
     *
     * @param context activity context
     * @param sp      sp value to be converted
     * @return float px value
     */
    public static float sptoPx(Context context, float sp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
    }

    /**
     * converts dx to dp
     *
     * @param context activity context
     * @param px      px value to be converted
     * @return float dp value
     */
    public static float pxToDp(Context context, float px) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, r.getDisplayMetrics());
    }

    /**
     * returns distance in miles from two points in the coordinate system
     *
     * @param point1 LagLng coordinates of first point
     * @param point2 LagLng coordinates of second point
     * @return double distance between given points
     */
    public static Double distanceBetweenInMiles(LatLng point1, LatLng point2) {
        double milesPerMeter = 0.000621371;
        if (point1 == null || point2 == null) {
            return null;
        }
        return SphericalUtil.computeDistanceBetween(point1, point2) * milesPerMeter;
    }


}
