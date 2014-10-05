package com.goodshop.app;

import com.goodshop.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Target extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		setContentView(R.layout.target); 
		
		TextView text=(TextView)findViewById(R.id.target);
		text.setText("각종 건의,문의 사항은 goodshopapp@gmail.com 으로 보내주시면 성실히 처리하도록 하겠습니다!\n 감사합니다!");
	}
}
