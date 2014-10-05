
package com.goodshop.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stackmob.android.sdk.common.StackMobCommon;
import com.goodshop.app.R;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
 
public class Signin extends Activity implements OnItemClickListener{
	ArrayList<Custom_List_Data4> list2;
	ProgressDialog mProgress;
	EditText textID,textPW;
	String id, password;
	CheckBox checkBox;
	int flag=0;	//0:Manual Login, 1:Auto-Login, 
	boolean isCalledForResult;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login); 

		ListView listView2 = (ListView) findViewById(R.id.list_register2);
		ImageButton loginButton=(ImageButton) findViewById(R.id.loginbutton);
		ImageButton lostButton=(ImageButton) findViewById(R.id.lostbutton);
//		ImageButton findpwButton = (ImageButton) findViewById(R.id.forgotpwbutton);
		checkBox=(CheckBox)findViewById(R.id.auto_check);
		textID=(EditText)findViewById(R.id.textID);
		textPW=(EditText)findViewById(R.id.textPW);

		mProgress = new ProgressDialog(this);
		mProgress.setMessage("로그인 중입니다 ...");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);

		loginButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				id=textID.getText().toString();

				password=textPW.getText().toString();
				mProgress.show();
				if(id.length()<1 || password.length()<1){	
					mProgress.dismiss();
					Toast.makeText(Signin.this, "ID와 Password를 입력하세요.", Toast.LENGTH_LONG).show();
				}else{
					if(NetworkUtil.getConnectivityStatus(ShopListClass.mContext)!=NetworkUtil.TYPE_NOT_CONNECTED){
						Map<String, String> args = new HashMap<String, String>();
						args.put("username", id);
						args.put("password", password);
						ServerConnector.login(id, password, new ServerConnector.QueryCallBack(){

							@Override
							public void success(String response) {
								// TODO Auto-generated method stub
								Log.d("mylog","login response"+response);
								
								String temp="{"+"\"aba\":"+response+"}";
	
								JSONObject json = null;
								JSONArray jarray = null;
								try {
									json=new JSONObject(temp);
									jarray=json.getJSONArray("aba");
									
									JSONObject e = jarray.getJSONObject(0);//new JSONObject(response);
								
									UserStatus.is_login = true;
									UserStatus.id = textID.getText().toString();
									UserStatus.email = e.has("email") ? e.getString("email") : "";
									UserStatus.point = e.has("point") ? Integer.valueOf(e.getString("point")) : 0;
									
									SharedPreferences.Editor ed=ShopListClass.mPrefs.edit();
									ed.putString("id", UserStatus.id);
									ed.putInt("point", UserStatus.point);
									ed.putString("email", UserStatus.email);
									ed.putBoolean("autologin", checkBox.isChecked());
									ed.commit();
									handler.sendEmptyMessage(0);
								}catch(JSONException e){
									
								}
								mProgress.dismiss();
							}

							@Override
							public void failure(String responseBody) {
								mProgress.dismiss();
								handler.sendEmptyMessage(1);								
							}
							
						});
						
						
//						StackMobCommon.getStackMobInstance().login(args, new StackMobCallback() {
//							@Override 
//							public void success(String response) {
//								//login succeeded
//							}
//							
//							@Override 
//							public void failure(StackMobException e) {
//								//login failed
//
//							}
//						});
					}else{
						mProgress.dismiss();
						Toast.makeText(Signin.this, "인터넷 접속이 원활하지 않습니다. 잠시 후 다시 이용해 주세요", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		lostButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Signin.this, Forgotpw.class);
				startActivity(intent);
				//handler.sendEmptyMessage(3);
			}
		});
		
/*		findpwButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Signin.this, Forgotpw.class);
				startActivity(intent);
			}
		});*/
		list2 = new ArrayList<Custom_List_Data4>();
		list2.add(new Custom_List_Data4("착한가게 계정 생성"));
		Custom_List_Adapter4 adapter1 = new Custom_List_Adapter4(this, android.R.layout.simple_list_item_1, list2);
		listView2.setAdapter(adapter1);
		listView2.setOnItemClickListener(this);
		
		if( (isCalledForResult = (getCallingActivity() != null)) ){
			Log.d("mylog","isCalledForResult"+isCalledForResult);
			setResult(RESULT_FIRST_USER);
		}
		
		if(ShopListClass.mPrefs.getBoolean("autologin", false)){
			UserStatus.id = ShopListClass.mPrefs.getString("id", "");
			UserStatus.point = ShopListClass.mPrefs.getInt("point", 0);
			UserStatus.email = ShopListClass.mPrefs.getString("email", "");
			UserStatus.is_login = true;
			
			Intent intent = new Intent(Signin.this, TabhostActivity.class);
			startActivity(intent);
			Log.d("mylog","I back here UserStatus.is_login "+UserStatus.is_login);
			if(UserStatus.is_login == true)
				finish();
		}
	
	}
	final Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			mProgress.dismiss();
			switch(msg.what)
			{
			case 0:
/*				SharedPreferences.Editor ed=prefs.edit();
				ed.putString("id", textID.getText().toString());
				ed.putInt("point", 0);
				ed.putBoolean("autologin", checkBox.isChecked());
				ed.commit();*/

				if(isCalledForResult){
					setResult(RESULT_OK);
					finish();
				}else{
					Intent intent=new Intent(Signin.this, TabhostActivity.class);
					startActivity(intent);
					if(UserStatus.is_login == true)
						finish();
				}
				break;
			case 1:
				Toast.makeText(Signin.this, "ID와 Password를 확인하세요.", Toast.LENGTH_LONG).show();
				break;
			case 2:
				Toast.makeText(Signin.this, "ID와 Password를 입력하세요.", Toast.LENGTH_LONG).show();
				break;
			case 3:
				SharedPreferences.Editor ed1=ShopListClass.mPrefs.edit();
				ed1.putString("id", "Guest");
				ed1.putBoolean("checked", false);
				ed1.commit();
				Intent intent2=new Intent(Signin.this, TabhostActivity.class);
				startActivity(intent2);
				finish();
			}
		}
	};
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
		String str1="Custom_List_Data5";
		String str2="Custom_List_Data4";
		String selectedList=parent.getItemAtPosition(position).toString();

		if(selectedList.indexOf(str1)==-1 && selectedList.indexOf(str2)!=-1){
			Intent i=new Intent(Signin.this, Register.class );
			startActivity(i);	
		}
		else if(selectedList.indexOf(str1)!=-1 && selectedList.indexOf(str2)==-1){
			//Intent i=new Intent(Signin.this, sub1.class );
			//startActivity(i);
		}

	}

	class Custom_List_Adapter4 extends ArrayAdapter<Custom_List_Data4> {

		private ArrayList<Custom_List_Data4> items;

		public Custom_List_Adapter4(Context context, int textViewResourceId,ArrayList<Custom_List_Data4> items) {
			super(context, textViewResourceId, items);
			this.items = items; 
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.custom_list4, null);
			}
			Custom_List_Data4 custom_list_data = items.get(position);
			if (custom_list_data != null) {
				TextView tv_Main = (TextView) v.findViewById(R.id.list_center2);
				// 현재 item의 position에 맞는 이미지와 글을 넣어준다.          
				tv_Main.setText(custom_list_data.getMain_Title());
			}
			return v;
		}
	}

	class Custom_List_Data4 {
		private String Main_Title;

		public Custom_List_Data4(String _Main_Title) { this.setMain_Title(_Main_Title); }
		public String getMain_Title() {	return Main_Title; }
		public void setMain_Title(String main_Title){ Main_Title = main_Title;}
	}
}