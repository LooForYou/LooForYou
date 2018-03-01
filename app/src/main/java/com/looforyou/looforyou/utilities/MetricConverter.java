package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by ibreaker on 2/28/2018.
 */

public class MetricConverter {
    public static float dpToPx(Context context, float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static float pxToDp(Context context, float px) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, r.getDisplayMetrics());
    }

    public static float pxToSp(Context context, float sp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
    }

}
