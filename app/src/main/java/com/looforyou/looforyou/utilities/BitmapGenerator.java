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
 * Created by ibreaker on 2/26/2018.
 */

public class BitmapGenerator {
    public static BitmapDescriptor drawableToBitmapDescriptor(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

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
