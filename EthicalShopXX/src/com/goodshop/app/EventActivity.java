package com.goodshop.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.goodshop.app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EventActivity extends Activity {
	
	//class ShopListAdapter extends ArrayAdapter<Integer>;
	private ListView mList;
	private EventListAdapter adapter;
	private final File BASEDIR = new ContextWrapper(ShopListClass.mContext).getDir("IMAGE", Context.MODE_PRIVATE);		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event);
		mList = (ListView) findViewById(R.id.Event_List);
		
		adapter = new EventListAdapter(this, android.R.layout.simple_list_item_1, null);
		mList.setAdapter(adapter);
		mList.setOnItemClickListener(adapter);
		TextView pointtv = (TextView) findViewById(R.id.cur_point);
		pointtv.setText(String.valueOf(UserStatus.point));
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Builder d = new AlertDialog.Builder(this);
		d.setMessage("정말 종료하시겠습니까?");
		d.setPositiveButton("예", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		d.show();
	}

	private class EventListAdapter extends ArrayAdapter<Integer> implements OnItemClickListener{
		//private ArrayList<Integer> indexs;
		private Context mContext;
		private LayoutInflater inflater;
		private Dialog dialog;
		
		public EventListAdapter(Context context, int textViewResourceId,
				ArrayList<Integer> arraydata) {
			super(context, textViewResourceId, arraydata);
			// TODO Auto-generated constructor stub
			//indexs = arraydata;
			mContext = context;
			inflater = LayoutInflater.from(context);
			//onitemclicklistener = this;
		}
		
	
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ShopListClass.nEvent;
		}


		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.event_detail, null);
			}
				//ImageView iv = ViewHolder.get(convertView, R.id.custom_list_image);
			TextView eventweek = ViewHolder.get(convertView, R.id.event_week);
			TextView eventtitle = ViewHolder.get(convertView, R.id.event_title);
			
			eventweek.setText(ShopListClass.eventWeek[position]);
			eventtitle.setText(ShopListClass.eventTitle[position]);
			
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			Log.d("mylog","onItemClick position"+position);
			new AdvertisementLoader().execute(position);
		}
		
	}
	
	private class AdvertisementLoader extends AsyncTask<Integer, Void, Bitmap> { 
		  //private ImageView i = new ImageView(GalleryActivity.this);
		private ProgressDialog mProgress;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgress = new ProgressDialog(EventActivity.this);
			mProgress.setMessage("서버와 통신중입니다...");
			mProgress.setIndeterminate(true);
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setCancelable(false);
			mProgress.show();
		}			
	
		@Override
		protected Bitmap doInBackground(Integer... params) { 
		    if(NetworkUtil.getConnectivityStatus(ShopListClass.mContext) != NetworkUtil.TYPE_NOT_CONNECTED){
				try{
					Log.d("mylog","i got here");
			    	File finalPath = new File(BASEDIR,"ad"+ShopListClass.eventAd[params[0]]+".jpg");
					if(finalPath.exists()){
						Log.d("mylog","i got here2");
						return BitmapFactory.decodeStream(new FileInputStream(finalPath));
					}else{
						Log.d("mylog","i got here3");
						Bitmap result;
				    	
				    	String IMAGEURL = "http://res.cloudinary.com/goodshopapp/image/upload/"+"ad"+ShopListClass.eventAd[params[0]]+".jpg";
						Log.d("mylog","request URL:"+IMAGEURL);
				    	URL url = new URL(IMAGEURL);
						DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
						int reqWidth = metrics.widthPixels;
						int reqHeight = metrics.heightPixels;
						Log.d("mylog","my phones size: reqWidth"+reqWidth+" reqHeight"+reqHeight);
						result = MyBitmapFactory.DecodeBitmapFromURL(url, reqWidth, reqHeight);
						if(result == null)
							return null;
						FileOutputStream fOut = new FileOutputStream(finalPath);
						result.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
						fOut.close();
						return result;
					}
			    }catch(IOException e){
			    	Log.d("mylog","ImageLoader "+e.toString());
			    	return null;
			    }
			}else{
				return null;
			}
		}
		
		
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			mProgress.dismiss();
		}

		@Override     
		protected void onPostExecute(final Bitmap result) {
			mProgress.dismiss();
			if(result != null){
				new Dialog(EventActivity.this, android.R.style.Theme) {
				    private ImageView adimage;
				    private Bitmap adBitmap = null;
				    private CheckBox checkbox;
					@Override
					protected void onCreate(Bundle savedInstanceState) {
						    super.onCreate(savedInstanceState);
				            setContentView(R.layout.popupdialog_nocheck);
				            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				                    WindowManager.LayoutParams.MATCH_PARENT);
				            adimage = (ImageView) findViewById(R.id.adImage);
				            Button closebtn = (Button) findViewById(R.id.closebtn);
				            closebtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dismiss();
								}
							});
				      }
				      
				      public void onWindowFocusChanged(boolean hasFocus) {
				    	    // TODO Auto-generated method stub
				    	    super.onWindowFocusChanged(hasFocus);
				    	    if(adBitmap==null){
				    	    	adBitmap = Bitmap.createScaledBitmap(result, adimage.getWidth(), adimage.getHeight(), true);
				    	    	adimage.setImageBitmap(adBitmap);
				            }
				      }
	
					@Override
					public void onBackPressed() {
						// TODO Auto-generated method stub
						dismiss();
					}
				}.show();
			}else{
				Toast.makeText(EventActivity.this, "Sorry. Can't load image", Toast.LENGTH_LONG).show();
			}
		}
	}	
	
	/*@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();
		super.onDestroy();
	}*/

}
