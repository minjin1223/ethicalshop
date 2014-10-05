package com.goodshop.app;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.ImageView;
import android.content.Context;

import com.stackmob.android.sdk.common.StackMobCommon;
import com.goodshop.app.R;

public class EthicalShopXX extends Activity {
    /** Called when the activity is first created. */
	ImageView mimage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.first);

		//		animation = new AnimationDrawable();
		//		animation.addFrame(getResources().getDrawable(R.drawable.front), 200);
		//		animation.addFrame(getResources().getDrawable(R.drawable.front), 200);
		//		animation.addFrame(getResources().getDrawable(R.drawable.front), 200);
		//		animation.addFrame(getResources().getDrawable(R.drawable.front), 200);
		//		animation.addFrame(getResources().getDrawable(R.drawable.front), 200);
		//		animation.addFrame(getResources().getDrawable(R.drawable.front), 200);

		//		animation.setOneShot(true);

		mimage = (ImageView)findViewById(R.id.img01);
//		
//		StackMobCommon.API_KEY = "13b831d9-38b8-4a70-baa5-b3b1a24318e7";
//		StackMobCommon.API_SECRET = "7bf32d60-caeb-4c86-bd20-c3a2b5714512";
//		StackMobCommon.USER_OBJECT_NAME = "user";
//		StackMobCommon.API_VERSION = 1;
//		StackMobCommon.API_URL_FORMAT = "api.mob1.stackmob.com";
//		StackMobCommon.PUSH_API_URL_FORMAT = "push.mob1.stackmob.com";
//		   
//		StackMobCommon.init(this.getApplicationContext());
		
		ShopListClass.mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ShopListClass.mContext = getApplicationContext();
		//		imageAnim.setBackgroundDrawable(animation);
	}

	public void onWindowFocusChanged(final boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		//		animation.start();
		TimerTask m = new TimerTask() {

			@Override
			public void run() {
				if(hasFocus==true){
					Intent intent;
					intent = new Intent(EthicalShopXX.this, Signin.class);
					startActivity(intent);
				}
				finish();
			}
		};
		Timer t = new Timer(); 
		t.schedule(m, 2000);

	}
	
/*	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();
		super.onDestroy();
	}*/
}