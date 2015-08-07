package com.lovejoy777.rommate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lovejoy777 on 25/06/15.
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
    private Context context;
    private String url;
    private ImageView imageView;

    public ImageLoadTask(Context context, String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
        this.context=context;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        if (!(result == null)){
            imageView.setImageBitmap(result);
                imageView.setImageBitmap(Bitmap.createScaledBitmap(result, (int) (result.getWidth() * 0.4), (int) (result.getHeight() * 0.4), true));
        }else{
            Toast.makeText(context,"There seems to be a problem with the selected Theme", Toast.LENGTH_SHORT).show();
        }
    }
}
