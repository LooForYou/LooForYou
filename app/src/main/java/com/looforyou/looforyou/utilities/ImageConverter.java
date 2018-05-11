package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class used for converting image formats
 *
 * @author mingtau li
 */

public class ImageConverter {

    /**
     * converts drawable to bitmapDescriptor format
     *
     * @param drawable Drawable asset
     */
    public static BitmapDescriptor drawableToBitmapDescriptor(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    /**
     * returns bitmap from Android assets
     *
     * @param context  parent context
     * @param filename image name from assets folder
     * @return Bitmap from assets
     */
    public static Bitmap BitmapFromAsset(Context context, String filename) {
        AssetManager assetManager = context.getAssets();
        InputStream stream = null;
        try {
            stream = assetManager.open(filename);
            return BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * returns drawable from Android assets
     *
     * @param context  parent context
     * @param filename image name from assets folder
     * @return Drawable from assets
     */
    public static Drawable DrawableFromAsset(Context context, String filename) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(context.getAssets().open(filename), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }

}
