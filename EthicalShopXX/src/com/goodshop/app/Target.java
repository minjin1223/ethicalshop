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
		text.setText("���� ����,���� ������ goodshopapp@gmail.com ���� �����ֽø� ������ ó���ϵ��� �ϰڽ��ϴ�!\n �����մϴ�!");
	}
}
