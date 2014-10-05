
package com.goodshop.app;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stackmob.android.sdk.common.StackMobCommon;
import com.goodshop.app.R;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;


public class Register extends Activity{
	//    protected static final String TAG = "HHHHHH";
	ProgressDialog mProgress;
	EditText textNick,textEmail, textPW, textPW2;
	String id, email, password, password2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register); 

		ImageButton regButton=(ImageButton) findViewById(R.id.regbutton);
//		ImageButton canButton=(ImageButton) findViewById(R.id.cancelbutton);
		textNick=(EditText)findViewById(R.id.reg_nick);
		textEmail=(EditText)findViewById(R.id.reg_email);
		textPW=(EditText)findViewById(R.id.reg_pass);
		textPW2=(EditText)findViewById(R.id.reg_pass2);
		

		mProgress = new ProgressDialog(this);
		mProgress.setMessage("아이디 생성 중 ...");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);


		regButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				id=textNick.getText().toString();
				email=textEmail.getText().toString();
				password=textPW.getText().toString();
				password2=textPW2.getText().toString();
//				Toast.makeText(Register.this, password+"/"+password2, Toast.LENGTH_LONG).show();
				mProgress.show();
				if(id.length()<1 ||email.length()<1 ||password.length()<1 || password2.length()<1)	handler.sendEmptyMessage(2);
				//else if(id.length() != 8){
				//}
				else if(!(password.equals(password2))) {
					handler.sendEmptyMessage(3);
					textEmail.setFocusable(true);
				}
				else{
					if(NetworkUtil.getConnectivityStatus(ShopListClass.mContext)!=NetworkUtil.TYPE_NOT_CONNECTED){
						ServerConnector.registerUser(id, password, email, new ServerConnector.QueryCallBack() {
							
							@Override
							public void success(String responseBody) {
								// TODO Auto-generated method stub
						    	handler.sendEmptyMessage(0);
							}
							
							@Override
							public void failure(String responseBody) {
								// TODO Auto-generated method stub
								handler.sendEmptyMessage(1);
							}
						});
//						Map<String, String> args = new HashMap<String, String>();
//						args.put("username", id);
//						args.put("email", email);
//						args.put("password", password);
//						args.put("point", "0");
//						StackMobCommon.getStackMobInstance().post("user", args, new StackMobCallback() {
//						    @Override 
//						    public void success(String responseBody) {
//						        //POST succeeded
//						    	handler.sendEmptyMessage(0);
//						    }
//						    @Override 
//						    public void failure(StackMobException e) {
//						        //POST failed
//						    	handler.sendEmptyMessage(1);
//						    }
//						});
					}else{
						mProgress.dismiss();
						Toast.makeText(Register.this, "인터넷 연결 접속이 원활하지 않습니다. 잠시 후 다시 이용해주세요.", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
				Toast.makeText(Register.this, "환영합니다!", Toast.LENGTH_LONG).show();
				finish();
				break;
			case 1:
				Toast.makeText(Register.this, id+"로 이미 가입이 되어있습니다.", Toast.LENGTH_LONG).show();
				textEmail.setFocusable(true);
				break;
			case 2:
				Toast.makeText(Register.this, "정보를 모두 입력하세요.", Toast.LENGTH_LONG).show();
				break;
			case 3:
				Toast.makeText(Register.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();
		super.onDestroy();
	}

}
