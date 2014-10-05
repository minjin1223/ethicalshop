package com.goodshop.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHandler {
	private DBHelper helper;
	private SQLiteDatabase db;
	
	private DBHandler(Context context){
		this.helper = new DBHelper(context);
		this.db = helper.getWritableDatabase();
	}
	
	public DBHandler(SQLiteDatabase db){
		this.db = db;
	}
	
	public static DBHandler open(Context context) throws SQLException{
		DBHandler handler = new DBHandler(context);
		return handler;
	}
	
	public void close(){
		helper.close();
	}
	
	public long insertShops(String shopId, String shopname, String photoURL, String phone, String summary, int code, Double shopLat, Double shopLon ){
		ContentValues values = new ContentValues();
		values.put(DBHelper.Shops.SHOP_ID_TAG, shopId);
		values.put(DBHelper.Shops.SHOP_NAME_TAG, shopname);
		values.put(DBHelper.Shops.PHOTOURL_TAG, photoURL);
		values.put(DBHelper.Shops.PHONE_TAG, phone);
		values.put(DBHelper.Shops.SUMMARY_TAG, summary);
		values.put(DBHelper.Shops.CODE_TAG, code);
		values.put(DBHelper.Shops.LAT_TAG, shopLat);
		values.put(DBHelper.Shops.LON_TAG, shopLon);
		
		return db.insert(DBHelper.Shops.Table, null, values);
	}
	
	public void replaceShops(String shopId, String shopname, String photoURL, String phone, String summary, Integer code, Double shopLat, Double shopLon){
		//Log.d("mylog","replace");
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(DBHelper.Shops.SHOP_ID_TAG, shopId);
		initialValues.put(DBHelper.Shops.SHOP_NAME_TAG, shopname);
		initialValues.put(DBHelper.Shops.PHOTOURL_TAG, photoURL);
		initialValues.put(DBHelper.Shops.PHONE_TAG, phone);
		initialValues.put(DBHelper.Shops.SUMMARY_TAG, summary);
		initialValues.put(DBHelper.Shops.CODE_TAG, code);
		initialValues.put(DBHelper.Shops.LAT_TAG, shopLat);
		initialValues.put(DBHelper.Shops.LON_TAG, shopLon);		
		db.replace(DBHelper.Shops.Table, null, initialValues);
	}
	
	
	public long insertEvent(String eventtitle, String eventweek, Float adversion){
		ContentValues values = new ContentValues();
		values.put(DBHelper.Event.EVENTTITLE_TAG, eventtitle);
		values.put(DBHelper.Event.EVENTWEEK_TAG, eventweek);
		values.put(DBHelper.Event.ADVERSION_TAG, adversion);
		
		return db.insert(DBHelper.Event.Table, null, values);		
	}
	
	public void replaceEvent(Double adversion, String eventtitle, String eventweek){
		ContentValues initialValues = new ContentValues();
		initialValues.put(DBHelper.Event.EVENTTITLE_TAG, eventtitle);
		initialValues.put(DBHelper.Event.EVENTWEEK_TAG, eventweek);
		initialValues.put(DBHelper.Event.ADVERSION_TAG, adversion);
		
		db.replace(DBHelper.Event.Table, null, initialValues);
	}
	
	public void deleteAll(){
		db.delete(DBHelper.Shops.Table, null, null);
	}
	
	public void deleteEvents(){
		db.delete(DBHelper.Event.Table, null, null);
	}
	

	public int getEvents(){
		Cursor cursor = db.query(false, DBHelper.Event.Table,  DBHelper.Event.attributes, 
				null, null, null, null, DBHelper.Event.ADVERSION_TAG+" DESC", null);
		int num_of_rows = 0;
		if(cursor != null){
			cursor.moveToFirst();
			num_of_rows = cursor.getCount();
			ShopListClass.nEvent = num_of_rows;
			ShopListClass.eventTitle = new String[num_of_rows];
			ShopListClass.eventWeek = new String[num_of_rows];
			ShopListClass.eventAd = new Double[num_of_rows];
			int i = 0;
			do{
				ShopListClass.eventTitle[i] = cursor.getString(cursor.getColumnIndex(DBHelper.Event.EVENTTITLE_TAG));
				ShopListClass.eventWeek [i] = cursor.getString(cursor.getColumnIndex(DBHelper.Event.EVENTWEEK_TAG));
				ShopListClass.eventAd[i] = cursor.getDouble(cursor.getColumnIndex(DBHelper.Event.ADVERSION_TAG));
				i++;
			}while(cursor.moveToNext());
			cursor.close();
		}
		return num_of_rows;
	}
	
	public int getShops(){
		Cursor cursor = db.query(false, DBHelper.Shops.Table,  DBHelper.Shops.attributes, 
								null, null, null, null, DBHelper.Shops.SHOP_NAME_TAG, null);
		int num_of_rows = 0;
		if( cursor != null ){
			cursor.moveToFirst();
			//int col_index_ID = cursor.getColumnIndex(DBHelper.Shops.SHOP_ID_TAG);
			int col_index_NAME = cursor.getColumnIndex(DBHelper.Shops.SHOP_NAME_TAG);
			//int col_index_PHOTO = cursor.getColumnIndex(DBHelper.Shops.PHOTOURL_TAG);
			int col_index_PHONE = cursor.getColumnIndex(DBHelper.Shops.PHONE_TAG);
			int col_index_SUM = cursor.getColumnIndex(DBHelper.Shops.SUMMARY_TAG);
			int col_index_CODE = cursor.getColumnIndex(DBHelper.Shops.CODE_TAG);
			int col_index_LAT = cursor.getColumnIndex(DBHelper.Shops.LAT_TAG);
			int col_index_LON = cursor.getColumnIndex(DBHelper.Shops.LON_TAG);
			int col_index_PHOTO = cursor.getColumnIndex(DBHelper.Shops.PHOTOURL_TAG);
			num_of_rows = cursor.getCount();
			ShopListClass.shopName = new String[num_of_rows];
			ShopListClass.shopSummary = new String[num_of_rows];
			ShopListClass.shopCode = new Integer[num_of_rows];
			ShopListClass.shopPhone = new String[num_of_rows];
			ShopListClass.shopLat = new Double[num_of_rows];
			ShopListClass.shopLon = new Double[num_of_rows];
			ShopListClass.shopPhoto = new String[num_of_rows];
			for(int i=0;i<num_of_rows;i++){
				ShopListClass.shopName[i] = cursor.getString(col_index_NAME);
				ShopListClass.shopSummary [i] = cursor.getString(col_index_SUM);
				ShopListClass.shopCode[i] = cursor.getInt(col_index_CODE);
				ShopListClass.shopLat[i] = cursor.getDouble(col_index_LAT);
				ShopListClass.shopLon[i] = cursor.getDouble(col_index_LON);
				ShopListClass.shopPhone[i] = cursor.getString(col_index_PHONE);
				ShopListClass.shopPhoto[i] = cursor.getString(col_index_PHOTO);
				cursor.moveToNext();
			}
			cursor.close();
		}
		return num_of_rows;
	}
	
/*	public Cursor selectShopInfo(String shopID){
		Cursor cursor = db.query(false, DBHelper.ShopInfo.Table, DBHelper.ShopInfo.attributes, 
								DBHelper.ShopInfo.SHOP_ID_TAG+"="+shopID, null, null, null, null, null);
		if( cursor != null )
			cursor.moveToFirst();

		return cursor;
	}
	
	public int getUserPoint(String userID){
		String[] columns = {DBHelper.User.POINT_TAG};
		Cursor cursor = db.query(false, DBHelper.User.Table, columns, DBHelper.User.USERID_TAG+"="+userID, null, null, null, null, null);
		if(cursor != null){
			cursor.moveToFirst();
			return cursor.getInt(cursor.getColumnIndex(DBHelper.User.POINT_TAG));
		}
		return -1;
	}
	*/
	
	/*	public long insertShopInfo(String shopId, String phone, String address, String opentime, String close, String discount, String menu, double lat, double lon ){
	ContentValues values = new ContentValues();
	values.put(DBHelper.ShopInfo.SHOP_ID_TAG, shopId);
	values.put(DBHelper.ShopInfo.PHONENUM_TAG, phone);
	values.put(DBHelper.ShopInfo.ADDRESS_TAG, address);
	values.put(DBHelper.ShopInfo.OPENTIME_TAG, opentime);
	values.put(DBHelper.ShopInfo.CLOSEDAY_TAG, close);
	values.put(DBHelper.ShopInfo.DISCOUNT_TAG, discount);
	values.put(DBHelper.ShopInfo.MENU_TAG, menu);
	values.put(DBHelper.ShopInfo.LAT_TAG, lat);
	values.put(DBHelper.ShopInfo.LON_TAG, lon);
	
	return db.insert(DBHelper.ShopInfo.Table, null, values);
}*/
	
/*	public long insertUser(String userID, Integer point, String email){
		ContentValues values = new ContentValues();
		values.put(DBHelper.User.USERID_TAG, userID);
		values.put(DBHelper.User.POINT_TAG, point);
		values.put(DBHelper.User.EMAIL_TAG, email);
		values.put(DBHelper.User.NEEDTOUP_TAG, 1);
		
		return db.insert(DBHelper.User.Table, null, values);
	}
	
	public long insertNfc(String shopid, String date, String userid){
		ContentValues values = new ContentValues();
		values.put(DBHelper.Nfc.SHOP_ID_TAG, shopid);
		values.put(DBHelper.Nfc.DATE_TAG, date);
		values.put(DBHelper.Nfc.USERID_TAG, userid);
		
		return db.insert(DBHelper.Nfc.Table, null, values);		
	}*/
	// 가게 정보 deliete는 가게 이름으로??
	// 

}