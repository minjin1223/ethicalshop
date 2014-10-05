package com.goodshop.app;

import java.util.ArrayList;
import java.util.HashMap;

import com.goodshop.app.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends Activity {
	
	private ArrayList<Integer> Array_Data = new ArrayList<Integer>();
	private ShopListAdapter adapter;
	private ListView mList;	
	@Override
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		EditText findText=(EditText)findViewById(R.id.input);
		findText.setOnFocusChangeListener(new EditTextOnFocusChangeListener());
		findText.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s){
				findName(s.toString());
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		});
	
		mList = (ListView) findViewById(R.id.Cusom_List);
		adapter = new ShopListAdapter(this,	android.R.layout.simple_list_item_1, Array_Data);
		mList.setAdapter(adapter);	
		for(int i=0; i < ShopListClass.nShop; i++)
			Array_Data.add(i);
		adapter.notifyDataSetChanged();
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

	public void findName(final String findString){
		Array_Data.clear();
		
		if(findString.length()>0){
			for(int i=0; i<ShopListClass.nShop;i++){
				if(SoundSearcher.matchString(ShopListClass.shopName[i], findString)){
					Array_Data.add(i);
				}
			}
		}
		else{
			for(int i=0;i<ShopListClass.nShop;i++){
				Array_Data.add(i);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	public static class SoundSearcher 
	{ 
		private static final char HANGUL_BEGIN_UNICODE = 44032; // �� 
		private static final char HANGUL_LAST_UNICODE = 55203; // �R
		private static final char HANGUL_BASE_UNIT = 588;//������ ���� ������ ���ڼ�
	 //����
		private static final char[] INITIAL_SOUND = { '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��' }; 
	 
	 
	 /**
	  * �ش� ���ڰ� INITIAL_SOUND���� �˻�.
	  * @param searchar
	  * @return
	  */

	 /**
	  * �ش� ������ ������ ��´�.
	  *  
	  * @param c �˻��� ����
	  * @return
	  */
		private static char getInitialSound(char c) { 
			int hanBegin = (c - HANGUL_BEGIN_UNICODE); 
			int index = hanBegin / HANGUL_BASE_UNIT; 
			return INITIAL_SOUND[index]; 
		} 
	 
	 
	 
	 /** * �˻��� �Ѵ�. �ʼ� �˻� �Ϻ� ������. 
	  * @param value : �˻� ��� ex> �ʼ��˻��մϴ� 
	  * @param search : �˻��� ex> ���ˤ��դ� 
	  * @return ��Ī �Ǵ°� ã���� true ��ã���� false. */ 
		public static boolean matchString(String value, String search){ 
			 int t = 0; 
			 int seof = value.length() - search.length(); 
			 int slen = search.length(); 
			 char searchar;
			 char valchar;
			 if(seof < 0) 
				 return false; //�˻�� �� ��� false�� �����Ѵ�. 
			 for(int i = 0; i <= seof; i++){ 
				 t = 0; 
				 while(t < slen){ 
					 searchar = search.charAt(t);
					 valchar = value.charAt(i+t);
					 if( (0x3131 <= searchar && searchar <= 0x314E) && (HANGUL_BEGIN_UNICODE <= valchar && valchar <= HANGUL_LAST_UNICODE) ){ 
						 //���� ���� char�� �ʼ��̰� value�� �ѱ��̸�
						 if(getInitialSound(valchar)==searchar) 
							 //������ �ʼ����� ������ ���Ѵ�
							 t++; 
						 else 
							 break; 
					 } else { 
						 //char�� �ʼ��� �ƴ϶��
						 if(Character.toLowerCase(valchar) == Character.toLowerCase(searchar)) 
						 //�׳� ������ ���Ѵ�. 
							 t++; 
						 else 
							 break; 
					 } 
				 } 
				 if(t == slen) 
					 return true; //��� ��ġ�� ����� ã���� true�� �����Ѵ�. 
			 } 
			 return false; //��ġ�ϴ� ���� ã�� �������� false�� �����Ѵ�.
	 	}
	}
}


