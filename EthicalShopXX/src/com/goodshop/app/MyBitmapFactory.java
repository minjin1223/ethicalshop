package com.goodshop.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;

public class MyBitmapFactory {
	public static Bitmap DecodeBitmapFromURL(URL url, int reqWidth, int reqHeight){
		try{
			//URLConnection ucon = url.openConnection();
			//InputStream inputstream = ucon.getInputStream();
			
		    final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
		    //inputstream.close();
		    //inputstream = ucon.getInputStream();
		    
		    // Calculate inSampleSize
		    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		    	
		    // Decode bitmap with inSampleSize set
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
	    }catch(IOException e){
	    	Log.d("mylog","ImageLoader "+e.toString());
	    	return null;
	    }
	}
	
	public static Bitmap DecodeBitmapFromFile(File file, int targetHeight, int targetWidth){
	    if(file.exists()){
			final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(file.getAbsolutePath());
		    
		    options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
		 
		    options.inJustDecodeBounds = false;
		    return BitmapFactory.decodeFile(file.getAbsolutePath());
	    }else
	    	return null;
	}
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);
	    
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	public static int calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    Log.d("mylog","calculateInsamepleSize images original width:"+width+"height"+height);
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}

}
