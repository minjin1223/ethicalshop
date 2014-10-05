

package com.goodshop.app;

import com.goodshop.app.R;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class TabhostActivity extends TabActivity implements OnTabChangeListener{
	TabHost tabHost;
	/** Called when the activity is first created. */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		tabHost=getTabHost();
		tabHost.setOnTabChangedListener(this);
		tabHost.setup();
		
		TabHost.TabSpec spec;
		spec=tabHost.newTabSpec("Shop");
		spec.setIndicator("",getResources().getDrawable(R.drawable.es_tabicon_home));//es_shoptable_tabicon_homex)); 
		spec.setContent(new Intent(this, ShopList.class));
		tabHost.addTab(spec);  

		spec=tabHost.newTabSpec("Event");
		spec.setContent(new Intent(this, EventActivity.class));
		spec.setIndicator("", getResources().getDrawable(R.drawable.es_tabicon_event));//es_shoptable_tabicon_qrcodex));
		tabHost.addTab(spec);

		spec=tabHost.newTabSpec("Search");
		spec.setContent(new Intent(this, SearchActivity.class));
		spec.setIndicator("", getResources().getDrawable(R.drawable.es_tabicon_search));
		tabHost.addTab(spec);

		spec=tabHost.newTabSpec("Setting");
		spec.setContent(new Intent(this, Setting.class));
		spec.setIndicator("", getResources().getDrawable(R.drawable.es_tabicon_setting));//es_shoptable_tabicon_settingx));
		tabHost.addTab(spec);
		
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++){
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.rgb(234,97,78));
			tabHost.getTabWidget().setStripEnabled(false);
		}

		tabHost.setCurrentTab(0);
		tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.rgb(84,59,46));
		
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		Log.d("mylog","onTabChanged");
		for(int i=0;i<tabHost.getTabWidget().getChildCount();i++){
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.rgb(234,97,78));
		}
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.rgb(84,59,46));
	}
}