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
		//text.setText("\n착한 가게 앱은 한동대학교 HGU 샵과 연계된 신개념 \"소셜 기부\" 애플리케이션으로  앱의 등록된 가맹점 광고 정보 확인 및 가맹점에서  통해 얻는 수익금을 사회로 환원하는 Application입니다.\n\n이 앱은 한동대학교 내 착한 가게 팀이 지난 2011년 방송통신위원회 주관 LBS APP&WEB Idea 공모전에서 대상을 수상한 아이디어의 일부를 한동대학교의 상황에 맞추어 제작한 것으로 한동대학교 HGU샵과 함께 제공하는 서비스 입니다.\n\n사용시에 문제점 혹은 건의사항은 아래로 문의해 주시면 더욱 발전적인 모습으로 착한 가게를 가꾸도록 노력하겠습니다.\n\n메일: jwj0831@gmail.com\n트위터: https://twitter.com/ethicalshop1004\n페이스북: http://www.facebook.com/pages/Ethicalshop-Community/267421009997283");
	}
}
