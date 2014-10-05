
package com.goodshop.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stackmob.android.sdk.common.StackMobCommon;
import com.goodshop.app.R;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class Setting extends Activity implements OnItemClickListener, OnClickListener{
	//    protected static final String TAG = "HHHHHH";
	ArrayList<Custom_List_Data1> nick_list;
	ArrayList<Custom_List_Data2> notice_list;
	ArrayList<Custom_List_Data3> program_list;
	Custom_List_Adapter1 nick_adapter;
	ProgressDialog mProgress;

	StackMob stackmob;
	String id, queryResult,nick;
	boolean guestFlag=false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting); 

		ListView listView = (ListView) findViewById(R.id.list_notice);
		ListView listView2 = (ListView) findViewById(R.id.list_notice2);
		ListView listView3 = (ListView) findViewById(R.id.list_notice3);
		TextView textView = (TextView) findViewById(R.id.loginnickname);
		nick_list = new ArrayList<Custom_List_Data1>();
		notice_list = new ArrayList<Custom_List_Data2>();
		program_list = new ArrayList<Custom_List_Data3>();

		mProgress = new ProgressDialog(this);
		mProgress.setMessage("������ ������Դϴ�...");
		mProgress.setIndeterminate(true);
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);

		//�� List�� ������ ��� �κ� 
		nick_list.add(new Custom_List_Data1("  "+UserStatus.email));
		notice_list.add(new Custom_List_Data2(" ���� ����Ʈ�� ?"));
		notice_list.add(new Custom_List_Data2(" ���� ����"));
		//notice_list.add(new Custom_List_Data2(" ���ݷ� �ڽ�"));
		program_list.add(new Custom_List_Data3(" ����  ����,  1.00"));

		//Nick List���� ������ ������� SharedPreference�� ����� email���� �̿��� DB���� �г����� ������ 
		nick_adapter = new Custom_List_Adapter1(this, android.R.layout.simple_list_item_1, nick_list);
		listView3.setAdapter(nick_adapter);
		Custom_List_Adapter2 notice_adapter = new Custom_List_Adapter2(this, android.R.layout.simple_list_item_1, notice_list);
		listView.setAdapter(notice_adapter);
		Custom_List_Adapter3 program_adapter = new Custom_List_Adapter3(this, android.R.layout.simple_list_item_1, program_list);
		listView2.setAdapter(program_adapter);
		listView.setOnItemClickListener(this);
		listView2.setOnItemClickListener(this);
		listView3.setOnItemClickListener(this);
		ImageButton logoutbtn = (ImageButton) findViewById(R.id.logoutbtn);
		logoutbtn.setOnClickListener(this);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Builder d = new AlertDialog.Builder(this);
		d.setMessage("���� �����Ͻðڽ��ϱ�?");
		d.setPositiveButton("��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		d.setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		d.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("�α׾ƿ� �Ͻðڽ��ϱ�?")
	       .setCancelable(false)
	       .setPositiveButton("��", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	       			UserStatus.is_login = false;
	        	   	Editor ed = ShopListClass.mPrefs.edit();
	       			ed.putBoolean("autologin", false);
	       			ed.commit();
	       			
	       			Intent intent1 = new Intent(Setting.this, Signin.class);
	                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	                startActivity(intent1);
	       			finish();
	           }
	       })
	       .setNegativeButton("�ƴϿ�", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	             dialog.cancel();
	           }
	       });
		 builder.create().show();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub

		v.setBackgroundColor(Color.argb(00, 56, 38, 32));
		String str1="Custom_List_Data2";
		String str2="Custom_List_Data1";
		String str3="Custom_List_Data3";
		String selectedList=parent.getItemAtPosition(position).toString();
		//3¥�� ����Ʈ�� ���õǾ������� �׼� 
		if(selectedList.indexOf(str1)!=-1){
			//			Toast.makeText(Setting.this, "3 List", Toast.LENGTH_SHORT).show();
			switch(position){
			case 0: 
				Intent i=new Intent(Setting.this, IntroEthicalShop.class );
				startActivity(i);
				break;
			case 1:
				Intent i2=new Intent(Setting.this, Target.class );
				startActivity(i2);
				break;
			//case 2:
			//	Intent i3=new Intent(Setting.this, Share.class );
			//	startActivity(i3);
			//	break;
			}
		}
		//�г��� ���� ����Ʈ�� ���õǾ������� �׼� 
		else if(selectedList.indexOf(str3)==-1){
			Intent i=new Intent(Setting.this, ChangeNickname.class );
			startActivityForResult(i, 101);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == Activity.RESULT_OK){
			nick_list.clear();
			nick_list.add(new Custom_List_Data1("  "+UserStatus.email));
			nick_adapter.notifyDataSetChanged();
		}
	}

	//�г����� ���� �ƴ��� 
	class Custom_List_Adapter1 extends ArrayAdapter<Custom_List_Data1> {

		private ArrayList<Custom_List_Data1> items;
		private Context mContext;

		public Custom_List_Adapter1(Context context, int textViewResourceId,ArrayList<Custom_List_Data1> items) {
			super(context, textViewResourceId, items);
			this.items = items; 
			mContext = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {

				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.custom_list1, null);
			}

			Custom_List_Data1 custom_list_data = items.get(position);

			if (custom_list_data != null) {
				//ImageView iv = (ImageView)v.findViewById(R.id.listmage2x);
				TextView tv_Main = (TextView) v.findViewById(R.id.listtitle2x);

				// ���� item�� position�� �´� �̹����� ���� �־��ش�.          
				tv_Main.setText(custom_list_data.getMain_Title());
				//iv.setImageResource(R.drawable.es_shoptable_icon_etcx);
			}

			return v;
		}


	}

	class Custom_List_Data1 {
		private String Main_Title;

		public Custom_List_Data1(String _Main_Title) { this.setMain_Title(_Main_Title); }
		public String getMain_Title() {	return Main_Title; }
		public void setMain_Title(String main_Title){ Main_Title = main_Title;}
	}

	//�˸� ����Ʈ�� ���� �ƴ��� 
	class Custom_List_Adapter2 extends ArrayAdapter<Custom_List_Data2> {

		private ArrayList<Custom_List_Data2> items;
		private Context mContext;

		public Custom_List_Adapter2(Context context, int textViewResourceId,ArrayList<Custom_List_Data2> items) {
			super(context, textViewResourceId, items);
			this.items = items; 
			mContext = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {

				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.custom_list2, null);
			}
			
			Custom_List_Data2 custom_list_data = items.get(position);
			if (custom_list_data != null) {
				//ImageView iv = (ImageView)v.findViewById(R.id.listmage2);
				TextView tv_Main = (TextView) v.findViewById(R.id.listtitle2);

				// ���� item�� position�� �´� �̹����� ���� �־��ش�.          
				tv_Main.setText(custom_list_data.getMain_Title());
				//iv.setImageResource(R.drawable.es_shoptable_icon_etcx);
			}

			return v;
		}


	}

	class Custom_List_Data2 {
		private String Main_Title;

		public Custom_List_Data2(String _Main_Title) { this.setMain_Title(_Main_Title); }
		public String getMain_Title() {	return Main_Title; }
		public void setMain_Title(String main_Title){ Main_Title = main_Title;}
	}


	//���������� ���� �ƴ��� 
	class Custom_List_Adapter3 extends ArrayAdapter<Custom_List_Data3> {

		private ArrayList<Custom_List_Data3> items;

		public Custom_List_Adapter3(Context context, int textViewResourceId,ArrayList<Custom_List_Data3> items) {
			super(context, textViewResourceId, items);
			this.items = items; 
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {

				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.custom_list3, null);
			}
			Custom_List_Data3 custom_list_data = items.get(position);

			if (custom_list_data != null) {
				TextView tv_Main = (TextView) v.findViewById(R.id.list_left);

				// ���� item�� position�� �´� �̹����� ���� �־��ش�.          
				tv_Main.setText(custom_list_data.getMain_Title());
			}

			return v;
		}


	}

	class Custom_List_Data3 {
		private String Main_Title;

		public Custom_List_Data3(String _Main_Title) { this.setMain_Title(_Main_Title); }
		public String getMain_Title() {	return Main_Title; }
		public void setMain_Title(String main_Title){ Main_Title = main_Title;}
	}


}
