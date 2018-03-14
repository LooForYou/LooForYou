package com.looforyou.looforyou.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.looforyou.looforyou.activities.MainActivity;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by ibreaker on 3/12/2018.
 */

public class ImageFromURL extends AsyncTask<String,Void,Drawable> {
    private Context context;

    public ImageFromURL(Context context){
        this.context = context;
    }

    protected Drawable doInBackground(String...urls){
        String urlOfImage = urls[0];
        Bitmap bitmap = null;
        try{
            InputStream is = new URL(urlOfImage).openStream();
            bitmap = BitmapFactory.decodeStream(is);

        }catch(Exception e){ // Catch the download exception
            e.printStackTrace();
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /*
        onPostExecute(Result result)
            Runs on the UI thread after doInBackground(Params...).
     */
    protected void onPostExecute(Drawable result){
//        imageView.setImageBitmap(result);
//        imageView.setImageDrawable(result);
    }

}
