package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ibreaker on 2/26/2018.
 */

public class ImageConverter {
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

    public static Drawable DrawableFromURL(Context context, String address){
//        URL newurl = null;
        Drawable image = null;
//        try {
//            newurl = new URL(url);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        try {
//            Bitmap tempBitmap = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
//            image = new BitmapDrawable(context.getResources(), tempBitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return image;


//
//        try {
//            java.net.URL url = new java.net.URL(address);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            return image = new BitmapDrawable(context.getResources(), myBitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
        return null;
        }
}
