package com.goodshop.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stackmob.android.sdk.common.StackMobCommon;
import com.goodshop.app.R;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class Forgotpw extends Activity {
	private ImageButton findpwbtn;
	private EditText stunum;
	private String id;
	private String email;
	private StackMob stackmob;
	private ProgressDialog mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpw);
		
		mProgress = new ProgressDialog(Forgotpw.this);
		mProgress.setMessage("Searching ...");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		
		findpwbtn = (ImageButton) findViewById(R.id.find_btn);
		stunum = (EditText) findViewById(R.id.text_id);
		
		findpwbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(NetworkUtil.getConnectivityStatus(getApplicationContext())!=NetworkUtil.TYPE_NOT_CONNECTED){
					id = stunum.getText().toString();
					if(id.length() < 1){
						handler.sendEmptyMessage(0);
						return;
					}
					

					mProgress.show();
					stackmob = StackMobCommon.getStackMobInstance();
					stackmob.forgotPassword(id, new StackMobCallback(){
						@Override
						public void success(String arg0) {
							// TODO Auto-generated method stub
							//Toast.makeText(Forgotpw.this, "��ϵ� �̸��� �ּҷ� ������ �߼۵Ǿ����ϴ�.", Toast.LENGTH_LONG).show();
							Log.d("mylog","forgotpassword success which message "+arg0);
							handler.sendEmptyMessage(2);
						}
					
						@Override
						public void failure(StackMobException arg0) {
							// TODO Auto-generated method stub
							Log.d("mylog","forgotpassword failure"+arg0.toString());
							handler.sendEmptyMessage(3);
						}
					});
	/*				HashMap args = new HashMap<String,String>();
					args.put("username", id);
					stackmob.get("user", args, new StackMobCallback(){
						@Override        	
						public void success(String responseBody) {
								String temp="{"+"\"aba\":"+responseBody+"}";
								
								JSONObject json = null;
								JSONArray jarray = null;
								
								try {
									json=new JSONObject(temp);
									email = json.getString("email");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								stackmob.forgotPassword(id, new StackMobCallback(){
									@Override
									public void success(String arg0) {
										// TODO Auto-generated method stub
										Toast.makeText(Forgotpw.this, "��ϵ� �̸��� �ּ� "+email+" �� �̸����� �߼۵Ǿ����ϴ�.", Toast.LENGTH_LONG).show();
									}
								
									@Override
									public void failure(StackMobException arg0) {
										// TODO Auto-generated method stub
										
									}
								});
						}
	
						@Override
						public void failure(StackMobException e) {
							handler.sendEmptyMessage(1);
						}
					});*/
					//("shops", new StackMobCallback()
				}else{
					Toast.makeText(Forgotpw.this, "���ͳ� ������ ��Ȱ���� �ʽ��ϴ�. ��� �� �ٽ� �̿��� �ּ���.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case 0:
				Toast.makeText(Forgotpw.this, "�й��� �Է����ּ���!", Toast.LENGTH_LONG).show();
				stunum.setFocusable(true);
				break;
			case 1:
				Toast.makeText(Forgotpw.this, "��ϵ��� ���� �й��Դϴ�.", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				mProgress.dismiss();
				Toast.makeText(Forgotpw.this, "��ϵ� �̸��� �ּҷ� ������ �߼۵Ǿ����ϴ�.", Toast.LENGTH_LONG).show();
				break;
			case 3:
				mProgress.dismiss();
				Toast.makeText(Forgotpw.this, "��й�ȣ ã�⿡ �����Ͽ����ϴ�.", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

}
