package com.goodshop.app;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stackmob.android.sdk.common.StackMobCommon;
import com.goodshop.app.R;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class ChangeNickname extends Activity implements OnClickListener{
	private EditText changeNick;
	private Context mContext;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 	
		setContentView(R.layout.changenick); 
		changeNick = (EditText) findViewById(R.id.text_nick);
		changeNick.setOnFocusChangeListener(new EditTextOnFocusChangeListener());
		ImageButton okBtn = (ImageButton) findViewById(R.id.okbtn_nick);
		setResult(RESULT_FIRST_USER);
		okBtn.setOnClickListener(this);
		mContext = this;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(NetworkUtil.getConnectivityStatus(ShopListClass.mContext)!=NetworkUtil.TYPE_NOT_CONNECTED){
			Map<String, String> args = new HashMap<String, String>();
			args.put("email", changeNick.getEditableText().toString());
			StackMobCommon.getStackMobInstance().put("user", UserStatus.id, args, new StackMobCallback() {
			    @Override 
			    public void success(String responseBody) {
			        //PUT succeeded
			    	UserStatus.email = changeNick.getEditableText().toString();
			    	Editor ed = ShopListClass.mPrefs.edit();
			    	ed.putString("email", UserStatus.email);
			    	ed.commit();
			    	setResult(RESULT_OK);
			    	handler.sendEmptyMessage(0);
			    	finish();
			    }
			    @Override 
			    public void failure(StackMobException e) {
			        //PUT failed
			    	Log.d("mylog", "tempquery() failut()"+e.toString());
			    	handler.sendEmptyMessage(1);
			    	finish();
			    }
			});
		}else{
			Toast.makeText(ChangeNickname.this, "인터넷 접속이 원활하지 않습니다. 잠시 후 다시 이용해주세요!", Toast.LENGTH_LONG).show();
		}
	}
	
	final Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 0:
				Toast.makeText(ChangeNickname.this, "이메일 주소가 성공적으로 변경되었습니다.", Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(ChangeNickname.this, "이메일 주소를 변경하지 못하였습니다.", Toast.LENGTH_LONG).show();
		    	break;
			}
		};
	};
}
