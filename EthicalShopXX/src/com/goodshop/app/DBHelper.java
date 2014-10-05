package com.goodshop.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final String ID_TAG = "_ID";
	
	public DBHelper(Context context){
		super(context,"GoodShopDB.db",null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		CreateTable(db, Shops.Table, Shops.attributes, Shops.types);
		CreateTable(db, Event.Table, Event.attributes, Event.types);
	//	CreateTable(db, ShopInfo.Table, ShopInfo.attributes, ShopInfo.types);
	//	CreateTable(db, User.Table, User.attributes, User.types);
	//	CreateTable(db, Nfc.Table, Nfc.attributes, Nfc.types);
	}
	
	public static class Shops{
		static final String Table = "Shops";
		static public final String SHOP_ID_TAG = "shops_id";
		static public final String SHOP_NAME_TAG = "name";
		static public final String PHOTOURL_TAG = "photo";
		static public final String PHONE_TAG = "phone";
		static public final String SUMMARY_TAG = "summary";
		static public final String CODE_TAG = "code";
		static public final String LAT_TAG = "lat";
		static public final String LON_TAG = "lon";
		public static final String[] attributes = {SHOP_ID_TAG, SHOP_NAME_TAG, PHOTOURL_TAG, PHONE_TAG, SUMMARY_TAG, CODE_TAG, LAT_TAG, LON_TAG};
		public static final String[] types = {"TEXT","TEXT","TEXT","TEXT","TEXT","INTEGER", "FLOAT", "FLOAT"};	
	}	
	
/*	public static class ShopInfo{
		static final String Table = "ShopInfo";
		//public static final String RESTAURANTNAME = Table+"resName"; // TEXT type
		static public final String SHOP_ID_TAG = "shop_info_id";
		static public final String PHONENUM_TAG = "phone_number";
		static public final String ADDRESS_TAG = "address";
		static public final String OPENTIME_TAG = "opentime";
		static public final String CLOSEDAY_TAG = "closeday";
		static public final String DISCOUNT_TAG = "discount";
		static public final String MENU_TAG = "menu";
		static public final String LAT_TAG = "lat";
		static public final String LON_TAG = "lon";
		public static final String[] attributes = {SHOP_ID_TAG,PHONENUM_TAG,ADDRESS_TAG,OPENTIME_TAG,CLOSEDAY_TAG,DISCOUNT_TAG,MENU_TAG,LAT_TAG,LON_TAG};
		public static final String[] types = {"TEXT","TEXT","TEXT","TEXT","TEXT","TEXT","TEXT","FLOAT","FLOAT"};	
	}
*/	
	public static class Event{
		static final String Table = "Event";
		static public final String ADVERSION_TAG = "adversion";
		static public final String EVENTTITLE_TAG = "eventtitle";
		static public final String EVENTWEEK_TAG = "eventweek";
		public static final String[] attributes = {ADVERSION_TAG, EVENTTITLE_TAG, EVENTWEEK_TAG};
		public static final String[] types = {"FLOAT","TEXT","TEXT"};
	}
	
/*	public static class User{
		static final String Table = "User";
		static public final String USERID_TAG = "userid";
		static public final String POINT_TAG = "point";
		static public final String EMAIL_TAG = "email";
		static public final String NEEDTOUP_TAG = "update";
		public static final String[] attributes = {USERID_TAG, POINT_TAG, EMAIL_TAG, NEEDTOUP_TAG};
		public static final String[] types = {"TEXT","INTEGER","TEXT","INTEGER"};
	}
	
	public static class Nfc{
		static final String Table = "Nfc";
		static public final String SHOP_ID_TAG = "shop_id";
		static public final String DATE_TAG = "date";
		static public final String USERID_TAG = "userid";
		public static final String[] attributes = {USERID_TAG, DATE_TAG, SHOP_ID_TAG};
		public static final String[] types = {"TEXT","TEXT","TEXT"};
	}*/
	
	private void CreateTable(SQLiteDatabase db,String Table, String[] attributes, String[] types){
		StringBuffer stb = new StringBuffer("CREATE TABLE ");
		stb.append(Table);
		stb.append("(");
		stb.append(ID_TAG);
		stb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		for( int i=0; i<attributes.length; i++){
			stb.append(attributes[i]);
			stb.append(" ");
			stb.append(types[i]);
			if(i==0)
				stb.append(" not null unique");
			stb.append(",");
		}
		stb.insert( stb.length()-1 , ");");
	
		db.execSQL(stb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		onCreate(db);
	}

}
