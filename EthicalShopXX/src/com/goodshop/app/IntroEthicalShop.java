package com.goodshop.app;

import com.goodshop.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class IntroEthicalShop extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.intro_ethical); 
		
		TextView mStudentId = (TextView)findViewById(R.id.idTextview);
		TextView mCurPoint = (TextView)findViewById(R.id.curpoint);
		
		mStudentId.setText(UserStatus.id);
		mCurPoint.setText(String.valueOf(UserStatus.point));
		//text.setText("\n���� ���� ���� �ѵ����б� HGU ���� ����� �Ű��� \"�Ҽ� ���\" ���ø����̼�����  ���� ��ϵ� ������ ���� ���� Ȯ�� �� ����������  ���� ��� ���ͱ��� ��ȸ�� ȯ���ϴ� Application�Դϴ�.\n\n�� ���� �ѵ����б� �� ���� ���� ���� ���� 2011�� ����������ȸ �ְ� LBS APP&WEB Idea ���������� ����� ������ ���̵���� �Ϻθ� �ѵ����б��� ��Ȳ�� ���߾� ������ ������ �ѵ����б� HGU���� �Բ� �����ϴ� ���� �Դϴ�.\n\n���ÿ� ������ Ȥ�� ���ǻ����� �Ʒ��� ������ �ֽø� ���� �������� ������� ���� ���Ը� ���ٵ��� ����ϰڽ��ϴ�.\n\n����: jwj0831@gmail.com\nƮ����: https://twitter.com/ethicalshop1004\n���̽���: http://www.facebook.com/pages/Ethicalshop-Community/267421009997283");
	}
}
