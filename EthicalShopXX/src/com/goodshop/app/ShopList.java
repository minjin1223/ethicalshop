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

import com.goodshop.app.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;
import android.widget.CheckBox;

import com.goodshop.app.SearchActivity.SoundSearcher;
import com.google.gson.Gson;

public class ShopList extends Activity implements DataLoadCallback, OnClickListener{
	//    protected static final String TAG = "HHHHHH";
	protected ArrayList<Integer> Array_Data = new ArrayList<Integer>();
//	protected Custom_List_Data data;
	protected ShopListAdapter adapter;
	protected ListView mList;
	
	private ImageButton ydk, ter, yuk, hos, extr;
	private ImageButton beauty, cafe, entertain, other, food;
	
	private int searchCode;
	private int subCode;
	
//	Thread lazyLoading;
	private int cursor;
	// 초성검색을 위해 모든 Item의 객체들을 저장할 변수 생성
	private ViewFlipper viewFlipper;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoplist); 

		//	mImage=(ImageView) findViewById(R.id.firstImage);	
		viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
		// first child
		ydk = (ImageButton) findViewById(R.id.yangdukBtn);
		ter = (ImageButton) findViewById(R.id.terminalBtn);
		yuk = (ImageButton) findViewById(R.id.yukguriBtn);
		hos = (ImageButton) findViewById(R.id.hospitalBtn);
		extr = (ImageButton) findViewById(R.id.extraBtn);
		ydk.setOnClickListener(this);
		ter.setOnClickListener(this);
		yuk.setOnClickListener(this);
		hos.setOnClickListener(this);
		extr.setOnClickListener(this);
		beauty = (ImageButton) findViewById(R.id.beautyBtn);
		cafe = (ImageButton) findViewById(R.id.cafeBtn);
		entertain = (ImageButton) findViewById(R.id.entertainBtn);
		other = (ImageButton) findViewById(R.id.othersBtn);
		food = (ImageButton) findViewById(R.id.foodBtn);
		beauty.setOnClickListener(this);
		cafe.setOnClickListener(this);
		entertain.setOnClickListener(this);
		other.setOnClickListener(this);
		food.setOnClickListener(this);		
		// second child
	
		// third child
/*		ImageButton mapButton=(ImageButton) findViewById(R.id.mapbutton);
		mapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(ShopList.this, MapViewActivity.class);
				
				startActivity(i);
			}
		});*/
		mList = (ListView) findViewById(R.id.Cusom_List);
		adapter = new ShopListAdapter(this,	android.R.layout.simple_list_item_1, Array_Data);
		mList.setAdapter(adapter);

		EditText findText=(EditText)findViewById(R.id.input);
		findText.setOnFocusChangeListener(new EditTextOnFocusChangeListener());
		findText.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s){
				findName(s.toString());
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		});
		
		ShopListClass.init(this.getApplicationContext());
		if(NetworkUtil.getConnectivityStatus(ShopListClass.mContext)!=NetworkUtil.TYPE_NOT_CONNECTED){
			ShopListClass.getEventListFromServer(this);
			ShopListClass.getShopListFromServer();
		}else{
			ShopListClass.UseLocalData();
		}
	}
	
	public void findName(final String findString){
		Array_Data.clear();
		
		if(findString.length()>0){
			for(int i=0; i<ShopListClass.nShop;i++){
				if(searchCode == (ShopListClass.shopCode[i] % 100) && SoundSearcher.matchString(ShopListClass.shopName[i], findString)){
					Array_Data.add(i);
				}
			}
		}
		else{
			for(int i=0;i<ShopListClass.nShop;i++){
				if(searchCode == (ShopListClass.shopCode[i] % 100))
					Array_Data.add(i);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	private void displayList(){
		Array_Data.clear();
		for(int i=0;i<ShopListClass.nShop;i++){
			if(searchCode == (ShopListClass.shopCode[i] % 100))
				Array_Data.add(i);
		}
		adapter.notifyDataSetChanged();
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();

		if(viewFlipper.getDisplayedChild() == 0){
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
		}else if(viewFlipper.getDisplayedChild() == 2){ //if(viewFlipper.getDisplayedChild() == 1 or 2){
			if(searchCode == 45) //hospital
				viewFlipper.setDisplayedChild(0);
			else{
				searchCode -= subCode;
				viewFlipper.showPrevious();
			}
		}else{
			viewFlipper.showPrevious();
		}
		
	}
	//@Override
	/*protected void onDestroy() {
		// TODO Auto-generated method stub
		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();
		super.onDestroy();
	}*/

	@Override
	public void afterDataLoad(boolean sucess) {
		// TODO Auto-generated method stub
		Log.d("mylog","afterDataLoad");
		new AdvertisementLoader().execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		boolean ret = true;
		switch( v.getId() ){
		case R.id.yangdukBtn:
			searchCode = 20; 
			viewFlipper.showNext();
			break;
			//ShopListClass.tempQuery();
		case R.id.yukguriBtn:	
			searchCode = 10; 
			viewFlipper.showNext(); 
			break;
		case R.id.terminalBtn:
			searchCode = 30; 
			viewFlipper.showNext();
			break;
		case R.id.extraBtn:	
			searchCode = 0; 
			viewFlipper.showNext();
			break;
		case R.id.hospitalBtn:
			searchCode = 45;
			displayList();
			viewFlipper.setDisplayedChild(2);
			break;
		default:
			ret = false;
		}
		if(ret)
			return;
		
		switch( v.getId() ){		
		case R.id.beautyBtn:
			subCode = 2;
			break;
		case R.id.cafeBtn:
			subCode = 1;
			break;
		case R.id.entertainBtn:
			subCode = 4;
			break;
		case R.id.othersBtn:
			subCode = 0;
			break;
		case R.id.foodBtn:
			subCode = 3;
			break;
		}
		searchCode += subCode;
		displayList();
		viewFlipper.showNext();
	}
	
	private class AdvertisementLoader extends AsyncTask<Void, Void, Bitmap> { 
		  //private ImageView i = new ImageView(GalleryActivity.this);
		private final File BASEDIR = new ContextWrapper(getApplicationContext()).getDir("IMAGE", Context.MODE_PRIVATE);		
		private ProgressDialog mProgress;
		private boolean newAdexist;
		private boolean userWantAd;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Log.d("mylog","I'am here");
			mProgress = new ProgressDialog(ShopList.this);
			Log.d("mylog","I'am here");
			mProgress.setMessage("서버와 통신중입니다...");
			Log.d("mylog","I'am here");
			mProgress.setIndeterminate(true);
			Log.d("mylog","I'am here");
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			Log.d("mylog","I'am here");
			mProgress.setCancelable(false);
			Log.d("mylog","I'am here");
			mProgress.show();
			Log.d("mylog","I'am here");
			newAdexist = ShopListClass.eventAd.length > 0 &&
						!String.valueOf(ShopListClass.eventAd[0]).equals(ShopListClass.mPrefs.getString("LASTADVERSION", "0"));
			userWantAd = ShopListClass.mPrefs.getBoolean("ADON", false);
			Log.d("mylog","newAdexist "+newAdexist+" userWantAd"+userWantAd);
		}			
	
		@Override
		protected Bitmap doInBackground(Void... params) { 
		    if(newAdexist){
				try{
			    	Bitmap result;
			    	String IMAGEURL = "http://res.cloudinary.com/goodshopapp/image/upload/"+"ad"+ShopListClass.eventAd[0]+".jpg";
					URL url = new URL(IMAGEURL);
					Log.d("mylog","request URL:"+IMAGEURL);
					DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
					int reqWidth = metrics.widthPixels;
					int reqHeight = metrics.heightPixels;
					Log.d("mylog","my phones size: reqWidth"+reqWidth+" reqHeight"+reqHeight);
					//URLConnection ucon = url.openConnection();
					//result = BitmapFactory.decodeStream(ucon.getInputStream());
					result = MyBitmapFactory.DecodeBitmapFromURL(url, reqWidth, reqHeight);
					if(result == null)
						return null;
					File finalPath = new File(BASEDIR,"ad"+ShopListClass.eventAd[0]+".jpg");
					Log.d("mylog","image sotre path "+finalPath.getName());
					FileOutputStream fOut = new FileOutputStream(finalPath);
					result.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					fOut.close();
					
					Editor ed = ShopListClass.mPrefs.edit();
					ed.putString("LASTADVERSION", String.valueOf(ShopListClass.eventAd[0]));
					ed.commit();
					return result;
			    }catch(IOException e){
			    	Log.d("mylog","ImageLoader "+e.toString());
			    	return null;
			    }
			}else if(userWantAd){
				try {
					File finalPath = new File(BASEDIR,"ad"+ShopListClass.eventAd[0]+".jpg");
					if(finalPath.exists())
						return BitmapFactory.decodeStream(new FileInputStream(finalPath));
					else
						return null;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}	
			}else{
				return null;
			}
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			mProgress.dismiss();
		}

		@Override     
		protected void onPostExecute(final Bitmap result) {
			mProgress.dismiss();
			mProgress.dismiss();
			if( (newAdexist || ShopListClass.mPrefs.getBoolean("ADON", false)) && result != null){
				new Dialog(ShopList.this, android.R.style.Theme) {
				    private ImageView adimage;
				    private CheckBox checkbox;
				    private Bitmap adbitmap = null;
					@Override
					protected void onCreate(Bundle savedInstanceState) {
						    super.onCreate(savedInstanceState);
				            setContentView(R.layout.popupdialog);
				            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				                    WindowManager.LayoutParams.MATCH_PARENT);
				            adimage = (ImageView) findViewById(R.id.adImage);
				            checkbox = (CheckBox) findViewById(R.id.checkbox);
				            Button closebtn = (Button) findViewById(R.id.closebtn);
				            checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
								
								@Override
								public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
									// TODO Auto-generated method stub
									Editor editor = ShopListClass.mPrefs.edit();
									editor.putBoolean("ADON", false);
									editor.commit();
									dismiss();
								}
							});
				            closebtn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Editor editor = ShopListClass.mPrefs.edit();
									editor.putBoolean("ADON", true);
									editor.commit();
									dismiss();
								}
							});
				      }
				      
				      public void onWindowFocusChanged(boolean hasFocus) {
				    	    // TODO Auto-generated method stub
				    	    super.onWindowFocusChanged(hasFocus);
				    	    if(adbitmap==null){
				    	    	Log.d("mylog","adimage's width and height "+adimage.getWidth()+" "+adimage.getHeight());
				    	    	adbitmap = Bitmap.createScaledBitmap(result, adimage.getWidth(), adimage.getHeight(), true);
				    	    	adimage.setImageBitmap(adbitmap);
				    	    }
				      }
					@Override
					public void onBackPressed() {
						// TODO Auto-generated method stub
						dismiss();
					}
				}.show();
			}
		}
	}	
	
}
