package com.goodshop.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

import com.stackmob.android.sdk.common.StackMobCommon;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.api.StackMobQuery.Ordering;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

public class ShopListClass {
	static public String[] shopName;
	static public String[] shopSummary;
	static public Integer[] shopCode;
	static public String[] shopPhone;
	static public String[] shopPhoto;
	static public Double[] shopLat;
	static public Double[] shopLon;
	static public int nShop = 0;
	
	static public String[] eventTitle;
	static public String[] eventWeek;
	static public Double[] eventAd;
	static public int nEvent = 0;
	
	private static DataLoadCallback mCallback;
/*	static public Shopinfo[] shopinfo;
	static public HashMap<String, Shopinfo> shopinfoMap = new HashMap<String, Shopinfo>();*/
	
/*	static private boolean dataexist = false;
	static private boolean adAvailable = false;*/
	static public Context mContext;
	public static SharedPreferences mPrefs;
	
/*	static public boolean needToloadAD = false;
	static private boolean needToUpdateDB = false;
	static private boolean needToUpdeateEV = false;
	static private boolean needToUpdateIMG = false;
	
	static private DataLoadCallback mCallback;
	static private DataLoadCallback mCallbackAdvertisement;
	
	
	
	static private int newadVersion;
	static private int newdbVersion;
	static private int newevVersion;
	static private int newimgVersion;*/
	
  static public void init(Context context){
		mContext = context;
		//mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
	    if(!mPrefs.getBoolean("updatecomplete", true) && NetworkUtil.getConnectivityStatus(mContext) != NetworkUtil.TYPE_NOT_CONNECTED){
		    Thread pointUpdateThread = new Thread(new Runnable(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Map<String, String> args = new HashMap<String, String>();
					args.put("point", String.valueOf(UserStatus.point));
					
					StackMobCommon.getStackMobInstance().put("user", UserStatus.id, args, new StackMobCallback() {
					    @Override 
					    public void success(String responseBody) {
					        //PUT succeeded
					    	Log.d("mylog", "tempquery() success()");
					    	Editor ed = ShopListClass.mPrefs.edit();
					    	ed.putBoolean("updatecomplete", true);
					    	ed.commit();
					    }
					    @Override 
					    public void failure(StackMobException e) {
					        //PUT failed
					    	Log.d("mylog", "tempquery() failut()");
					    	Editor ed = ShopListClass.mPrefs.edit();
					    	ed.putBoolean("updatecomplete", false);
					    	ed.commit();
					    }
					});
			}});
	    	pointUpdateThread.start();
	    }
	}
  static public void UseLocalData(){
	  DBHandler handler = DBHandler.open(mContext);
	  nShop = handler.getShops();
	  nEvent = handler.getEvents();
	  handler.close();
  }
  static public void getEventListFromServer(final DataLoadCallback callback){
	    ServerConnector.getRecentData(mPrefs.getLong("LASTMODEVENT", 0), ServerConnector.EVENTS, new ServerConnector.QueryCallBack(){

			@Override
			public void success(String responseBody) {
				Log.d("mylog","success"+responseBody);
				String jsonarrString="{"+"\"aba\":"+responseBody+"}";
				Object[][] parseresult = JSONParser.parseJsonArray(jsonarrString, "aba", DBHelper.Event.attributes);
				DBHandler handler = DBHandler.open(mContext);
				for(int row=0;row<parseresult.length;row++){
					handler.replaceEvent((Double)parseresult[row][0], (String)parseresult[row][1], (String)parseresult[row][2]);
				}
				nEvent = handler.getEvents();
				handler.close();
				Editor ed = mPrefs.edit();
				ed.putLong("LASTMODEVENT", System.currentTimeMillis());
				ed.commit();
				Log.d("mylog","just before afterDataLoad");
				mCallback = callback;
				myhandler.sendEmptyMessage(0);
				//callback.afterDataLoad(true);
			}

			@Override
			public void failure(String responseBody) {
				//never reach here
			}
	    	
	    });
//	    String[] fields = {"adversion", "eventtitle", "eventweek"};
//	    StackMob stackmob = StackMobCommon.getStackMobInstance();
//	    StackMobQuery query = new StackMobQuery("Event")
//						  .select(Arrays.asList(fields))
//						  .fieldIsGreaterThanOrEqualTo("lastmoddate", String.valueOf(mPrefs.getLong("LASTMODEVENT", 0)));
//	    stackmob.get(query, new StackMobCallback() {
//			
//			@Override
//			public void success(String responseBody) {
//				// TODO Auto-generated method stub
//
//			}
//			
//			@Override
//			public void failure(StackMobException arg0) {
//				// TODO Auto-generated method stub
//				//never reach here
//			}
//		});
    }
	
	final static Handler myhandler = new Handler(){
  		public void handleMessage(android.os.Message msg) {
  			mCallback.afterDataLoad(true);
  		};
  	};
  
	static public void getShopListFromServer() 
	{
//		StackMob stackmob = StackMobCommon.getStackMobInstance();
		//stackmob.get("shops",getCondition, 
		
		ServerConnector.getRecentData( mPrefs.getLong("LASTMODSHOP", 0) , ServerConnector.SHOPS , new ServerConnector.QueryCallBack() {
			
			@Override        	
			public void success(String responseBody) {
				Log.d("mylog","shop success"+responseBody);
				String jsonarrString="{"+"\"aba\":"+responseBody+"}";
				Object[][] parseresult = JSONParser.parseJsonArray(jsonarrString, "aba", DBHelper.Shops.attributes);
				DBHandler handler = DBHandler.open(mContext);
				for(int row=0;row<parseresult.length;row++){
					handler.replaceShops((String)parseresult[row][0], (String)parseresult[row][1], (parseresult[row][2]!=null?(String)(parseresult[row][2]):null), (parseresult[row][3]!=null?(String)(parseresult[row][3]):null), (parseresult[row][4]!=null?(String)(parseresult[row][4]):null),
										  code_parsing((String)parseresult[row][5]), (parseresult[row][6]!=null?(Double)(parseresult[row][6]):null), (parseresult[row][7]!=null?(Double)(parseresult[row][7]):null));
				}
				nShop = handler.getShops();
				handler.close();
				
				Editor ed = mPrefs.edit();
				ed.putLong("LASTMODSHOP", System.currentTimeMillis());
				ed.commit();
			}
			@Override
			public void failure(String responseBody) {
				Log.d("mylog","ShopList.java, getDataFromServer(), failure() "+responseBody);
			}
		});
		
//		StackMobQuery query = new StackMobQuery("shops")
//		  .select(Arrays.asList(DBHelper.Shops.attributes))
//		  .fieldIsGreaterThanOrEqualTo("lastmoddate", String.valueOf(mPrefs.getLong("LASTMODSHOP", 0)));
//				stackmob.get(query, new StackMobCallback() {
	}

	static private int code_parsing(String code){
		//6 h t y x - c b f e x 
		int len = code.length();
		int ret = 0;
		
		 if(1<=len){
		 	switch(code.charAt(0)){
		 	case 'p' : ret = 100; break;
		 	case 'h' : 
		 	default: 
		 		ret = 0;
		 	}
		 } 
		
		 if(2<=len){
			switch(code.charAt(1)){
			case 'h': ret += 45; 
					  return ret;//hospital
			case '6': ret += 10; break;//yuk
			case 'y': ret += 20; break;//yang
			case 't': ret += 30; break;//terminal
			case 'x': 
			default:
				ret += 0; break;//extra
			}
		}
		if(3<=len){
			switch(code.charAt(2)){
			case 'c': ret += 1; break;//cafe
			case 'b': ret += 2; break;//beauty
			case 'f': ret += 3; break;//food
			case 'e': ret += 4; break;//entertainment
			case 'x': 
			default:
				ret += 0; break;//extra
			}			
		}
		return ret;
	}
	
	private static class JSONParser{
		public static Object[][] parseJsonArray(String jsonArrStr, String arrName, String []fields){
			try {
				JSONArray jarray=new JSONObject(jsonArrStr).getJSONArray(arrName);
				int num_row = jarray.length();
				Object[][] result = new Object[num_row][fields.length];
				
				int row = 0;
				int col = 0;
				for(row=0; row<num_row;row++){
					JSONObject e = jarray.getJSONObject(row);
					col = 0;
					for(String field: fields)
						result[row][col++] = e.has(field) ? e.get(field) : null;
				}
				return result;
			}catch(JSONException e){
				Log.d("mylog","parseJsonArray() JSONException "+e.toString());
				return null;
			}
		}
	}
	
	/*static public void tempQuery(){
	
	shopinfo = new Shopinfo[nShop];
	DBHandler handler = DBHandler.open(mContext);
	for(int i = 0; i < nShop; i++){
		Cursor cursor = handler.selectShopInfo(shopId[i]);
		int phoneI = cursor.getColumnIndex(DBHelper.ShopInfo.PHONENUM_TAG); 
		int addrI = cursor.getColumnIndex(DBHelper.ShopInfo.ADDRESS_TAG);
		int otI = cursor.getColumnIndex(DBHelper.ShopInfo.OPENTIME_TAG);
		int cdI = cursor.getColumnIndex(DBHelper.ShopInfo.CLOSEDAY_TAG);
		int disI = cursor.getColumnIndex(DBHelper.ShopInfo.DISCOUNT_TAG);
		int menuI = cursor.getColumnIndex(DBHelper.ShopInfo.MENU_TAG);
		int latI = cursor.getColumnIndex(DBHelper.ShopInfo.LAT_TAG);
		int lonI = cursor.getColumnIndex(DBHelper.ShopInfo.LON_TAG);
		
		Shopinfo shop = new Shopinfo();
		shop.phoneNum = cursor.getString(phoneI);
		shop.shopAddress = cursor.getString(addrI);
		shop.openTime = cursor.getString(otI);
		shop.closeDay = cursor.getString(cdI);
		shop.discount= cursor.getString(disI);
		shop.menu= cursor.getString(menuI);
		shop.lat = cursor.getDouble(latI);
		shop.lon = cursor.getDouble(lonI);
		shopinfo[i] = shop;
		cursor.close();
	}
	handler.close();
	
	for(int i = 0; i < nShop; i++){
		Map<String, String> args2 = new HashMap<String, String>();
		args2.put("phone", shopinfo[i].phoneNum);
		args2.put("name", shopName[i]);
		args2.put("summary", shopinfo[i].discount);
		StackMobCommon.getStackMobInstance().put("shops", shopId[i], args2, new StackMobCallback() {
		    @Override 
		    public void success(String responseBody) {
		        //PUT succeeded
		    	Log.d("mylog", "tempquery() success()");
		    }
		    @Override 
		    public void failure(StackMobException e) {
		        //PUT failed
		    	Log.d("mylog", "tempquery() failut()");
		    }
		});
	}
}*/
	
/*	static private void getShopInfoFromServer(){
		new Thread(new Runnable() {
			public void run() {
				    String[] fields = {DBHelper.ShopInfo.SHOP_ID_TAG,DBHelper.ShopInfo.PHONENUM_TAG,DBHelper.ShopInfo.ADDRESS_TAG,DBHelper.ShopInfo.OPENTIME_TAG,
				    				   DBHelper.ShopInfo.CLOSEDAY_TAG,DBHelper.ShopInfo.DISCOUNT_TAG,DBHelper.ShopInfo.MENU_TAG,"location"};
					StackMob stackmob = StackMobCommon.getStackMobInstance();
					StackMobQuery query = new StackMobQuery("shop_info")
											  .select(Arrays.asList(fields));
					stackmob.get(query, new StackMobCallback() {
						@Override        	
						public void success(String responseBody) {
							//Log.d("mylog",responseBody);
							Log.d("mylog","getShopinfo body: "+responseBody);
							String queryResult = "{"+"\"aba\":"+responseBody+"}";
							
							JSONObject json = null;
							JSONArray jarray = null;
							try{
								json=new JSONObject(queryResult);
								
								jarray=json.getJSONArray("aba");
								DBHandler handler = DBHandler.open(mContext);
								for(int i=0;i<jarray.length();i++){
									JSONObject e = jarray.getJSONObject(i);
									Shopinfo shop = new Shopinfo();
									shop.shopId = e.has(DBHelper.ShopInfo.SHOP_ID_TAG) ? e.getString(DBHelper.ShopInfo.SHOP_ID_TAG) : "";
									shop.phoneNum = e.has(DBHelper.ShopInfo.PHONENUM_TAG) ? e.getString(DBHelper.ShopInfo.PHONENUM_TAG) : "";
									shop.shopAddress = e.has(DBHelper.ShopInfo.ADDRESS_TAG) ? e.getString(DBHelper.ShopInfo.ADDRESS_TAG) : "";
									shop.openTime = e.has(DBHelper.ShopInfo.OPENTIME_TAG) ? e.getString(DBHelper.ShopInfo.OPENTIME_TAG) : "";
									shop.closeDay = e.has(DBHelper.ShopInfo.CLOSEDAY_TAG) ? e.getString(DBHelper.ShopInfo.CLOSEDAY_TAG) : "";
									shop.discount= e.has(DBHelper.ShopInfo.DISCOUNT_TAG) ? e.getString(DBHelper.ShopInfo.DISCOUNT_TAG) : "";
									shop.menu= e.has(DBHelper.ShopInfo.MENU_TAG) ? e.getString(DBHelper.ShopInfo.MENU_TAG) : "";
									
									if( e.has("location") ){
										String loc = "{"+"\"aba\":["+e.getString("location")+"]}";
										e = new JSONObject(loc).getJSONArray("aba").getJSONObject(0);
										shop.lat = e.has("lat") ? e.getDouble("lat") : 0;
										shop.lon = e.has("lon") ? e.getDouble("lon") : 0;
									}else{
										shop.lat = 0;
										shop.lon = 0;
									}
									handler.insertShopInfo(shop.shopId, shop.phoneNum, shop.shopAddress, shop.openTime, shop.closeDay, shop.discount, shop.menu, shop.lat, shop.lon);
									shopinfoMap.put(shop.shopId, shop);
									if( i == targetindex){
										mHandler.sendEmptyMessage(i);
									}
								}
								
								Log.d("mylog","I get here shopinfoload[index]");
//								galleryImage.refreshDrawableState();
								
								handler.close();
								Editor ed = mPrefs.edit();
								ed.putInt("DBVERSION", newdbVersion);
								ed.commit();
							}catch(JSONException e){
								Log.d("mylog","getshopinfo() JSONException");
								shopId[0] = "error";
								mHandler.sendEmptyMessage(0);
							}
						}
						@Override
						public void failure(StackMobException e) {
							Log.d("mylog","getshopinfo() failure()"+e.toString());
							shopId[0] = "error";
							mHandler.sendEmptyMessage(0);
						}
					});
				}
		}).start();		
	}*/

/*	static private void getShopImageFromServer(){
		new Thread(new Runnable(){
			@SuppressLint("NewApi")
			public void run(){
				Log.d("flowlog","getShopImageFromServer() run()");
				URL url;
				InputStream inputstream = null;
				OutputStream outputstream = null;
				try {
					for(int i=0; i < nShop; i++){
						if(shopPhoto[i] != null){
							String IMAGEURL = "http://res.cloudinary.com/goodshopapp/image/upload/"+shopPhoto[i]+".jpg";
							url = new URL(IMAGEURL);
							URLConnection ucon = url.openConnection();
							inputstream = ucon.getInputStream();
							adimage = BitmapFactory.decodeStream(inputstream);
							File directory = new ContextWrapper(mContext).getDir("IMAGE", Context.MODE_PRIVATE);
							File finalPath = new File(directory,shopPhoto[i]+".jpg");
							FileOutputStream fOut = new FileOutputStream(finalPath);
							adimage.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
							fOut.close();
							//shopImageFlag[i] = true;
						}
					//	shopImageFlag[i] = false;
					}
					Editor ed = mPrefs.edit();
					ed.putInt("IMGVERSION", newimgVersion);
					ed.commit();
				
					Log.d("flowlog","getShopImageFromServer() Success");
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d("flowlog","getShopImageFromServer() Exception");
					e.printStackTrace();
				}
			}
		}).start();	
	}*/	
	
	/*	static public void ShopInfoDataLoad(int index, DataLoadCallback callback){
	mCallback = callback;
	targetindex = index;
	Log.d("mylog","@ShopInfoDataLoad(index)");
	if(shopinfoMap.containsKey(shopId[index])){//shopinfoload[index] == true){
		Log.d("mylog","@ShopInfoDataLoad(index) shopinfoload[index]==true shopMainTitle[index]:"+shopMainTitle[index]);
		mCallback.afterDataLoad(true);
	}else{
		Log.d("mylog","@ShopInfoDataLoad(index) shopinfoload[index]!=true");
		if(!needToUpdateDB){
			DBHandler handler = DBHandler.open(mContext);
			Cursor cursor = handler.selectShopInfo(shopId[index]);
			int phoneI = cursor.getColumnIndex(DBHelper.ShopInfo.PHONENUM_TAG); 
			int addrI = cursor.getColumnIndex(DBHelper.ShopInfo.ADDRESS_TAG);
			int otI = cursor.getColumnIndex(DBHelper.ShopInfo.OPENTIME_TAG);
			int cdI = cursor.getColumnIndex(DBHelper.ShopInfo.CLOSEDAY_TAG);
			int disI = cursor.getColumnIndex(DBHelper.ShopInfo.DISCOUNT_TAG);
			int menuI = cursor.getColumnIndex(DBHelper.ShopInfo.MENU_TAG);
			int latI = cursor.getColumnIndex(DBHelper.ShopInfo.LAT_TAG);
			int lonI = cursor.getColumnIndex(DBHelper.ShopInfo.LON_TAG);
			
			Shopinfo shop = new Shopinfo();
			shop.phoneNum = cursor.getString(phoneI);
			shop.shopAddress = cursor.getString(addrI);
			shop.openTime = cursor.getString(otI);
			shop.closeDay = cursor.getString(cdI);
			shop.discount= cursor.getString(disI);
			shop.menu= cursor.getString(menuI);
			shop.lat = cursor.getDouble(latI);
			shop.lon = cursor.getDouble(lonI);
			Log.d("mylog"," "+shop.phoneNum+" "+shop.shopAddress+" "+shop.menu+shopId[index]+" "+shopMainTitle[index]);
			shopinfoMap.put(shopId[index], shop);
			
			cursor.close();
			handler.close();
			mCallback.afterDataLoad(true);
		}
	}
}*/
/*	static private void getAdvertisementImageFromServer(){
		new Thread(new Runnable(){
			@SuppressLint("NewApi")
			public void run(){
				URL url;
				InputStream inputstream = null;
				OutputStream outputstream = null;
				try {
					String IMAGEURL = ADVER_URL+newadVersion+".jpg";
					url = new URL(IMAGEURL);
					URLConnection ucon = url.openConnection();
					
					inputstream = ucon.getInputStream();
					
					adimage = BitmapFactory.decodeStream(inputstream);
					
					adAvailable = true;
					mHandler.sendEmptyMessage(-8);
					Log.d("mylog","getAdvertisementImageFromServer() adAvailable "+adAvailable);
					
					File directory = new ContextWrapper(mContext).getDir("IMAGE", Context.MODE_PRIVATE);
					File finalPath = new File(directory,"ad"+newadVersion+".jpg");
					FileOutputStream fOut = new FileOutputStream(finalPath);
					adimage.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					fOut.close();
					Editor ed = mPrefs.edit();
					ed.putInt("ADVERSION", newadVersion);
					ed.commit();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					adAvailable = false;
					mHandler.sendEmptyMessage(-8);
		        } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					adAvailable = false;
					mHandler.sendEmptyMessage(-8);
				}
			}
		}).start();
	}*/
	
/*	static public Bitmap readImageFromMemory(int adversion){
		try {
			File directory = new ContextWrapper(mContext).getDir("IMAGE", Context.MODE_PRIVATE);
			File finalPath = new File(directory,"ad"+adversion+".jpg");
			if(finalPath.exists())
				return BitmapFactory.decodeStream(new FileInputStream(finalPath));
			else{
				return BitmapFactory.decodeResource(mContext.getResources(), R.id.adImage);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	static public Bitmap readShopImageFromMemory(int index){
		try {
			File directory = new ContextWrapper(mContext).getDir("IMAGE", Context.MODE_PRIVATE);
			File finalPath = new File(directory,shopPhoto[index]+".jpg");
			return BitmapFactory.decodeStream(new FileInputStream(finalPath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}*/
	
//	static public void ShopDataLoad(DataLoadCallback callback){
	/*	mCallback = callback;
		if(!dataexist){
			if(needToUpdateDB){
				getShopListFromServer();
			}else{
				//DB에서 불러온다.
				Log.d("mylog","ShopDataLoad from DB start");
				DBHandler handler = DBHandler.open(mContext);
				nShop = handler.getShops();
				handler.close();
				if(nShop == 0)
					dataexist = false;
				else
					dataexist = true;
				if(mCallback!=null)
					mCallback.afterDataLoad(dataexist);
				Log.d("mylog","ShopDataLoad from DB end nShop"+nShop);
			}
			if(needToUpdateIMG){
				getShopImageFromServer();
			}else{
				for(int i=0; i<nShop; i++){
					shopImageFlag[i] = (shopPhoto[i] != null);
				}
			}
		}
		if(mCallback!=null)
			mCallback.afterDataLoad(dataexist);*/
//	}
	
/*	static public void EventDataLoad(){
		if(needToUpdeateEV){
			getEventListFromServer();
		}else{
			DBHandler handler = DBHandler.open(mContext);
			handler.getEvents();
			handler.close();
		}
	}*/
	
/*	@SuppressLint("NewApi")
	static public void AdImageLoad(DataLoadCallback imageloadcallback){
		mCallbackAdvertisement = imageloadcallback;
		try{
			if(needToloadAD){
				getAdvertisementImageFromServer();
			}else{
				File directory = new ContextWrapper(mContext).getDir("IMAGE", Context.MODE_PRIVATE);
				File finalPath = new File(directory,"ad"+newadVersion+".jpg");
			    Log.d("mylog",finalPath.getAbsolutePath());
				adimage = BitmapFactory.decodeStream(new FileInputStream(finalPath));
			    adAvailable = true;
			    mHandler.sendEmptyMessage(-8);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			adAvailable = false;
			mCallbackAdvertisement.afterDataLoad(adAvailable);
		}
	}*/
	
/*	static public void init(Context context){
		mContext = context;
		//mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		mCallback = callback;
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String[] fields = new String[]{"adVersion","dbVersion","evVersion","imgVersion"};
				StackMobQuery query = new StackMobQuery("CommonParameter").select(Arrays.asList(fields));
				StackMobCommon.getStackMobInstance().get(query, new StackMobCallback(){
					@Override
					public void success(String responsebody) {
						// TODO Auto-generated method stub
						try {
							Log.d("mylog","init() responsebody "+responsebody);
							
							String temp="{"+"\"aba\":"+responsebody+"}";

							JSONObject json = null;
							JSONArray jarray = null;
							
							jarray = new JSONObject(temp).getJSONArray("aba");
							json = jarray.getJSONObject(0);
							
							newadVersion = json.getInt("adVersion");
							int oldadVersion = mPrefs.getInt("ADVERSION", -1); 
							needToloadAD = ( newadVersion != oldadVersion );
							newdbVersion = json.getInt("dbVersion");
							int olddbVersion = mPrefs.getInt("DBVERSION", -1);
							needToUpdateDB = ( newdbVersion != olddbVersion );
							newevVersion = json.getInt("evVersion");
							int oldevVersion = mPrefs.getInt("EVVERSION", -1);
							needToUpdeateEV = ( newevVersion != oldevVersion );
							int oldimgVersion = mPrefs.getInt("IMGVERSION", -1);
							newimgVersion = json.getInt("imgVersion");
							needToUpdateIMG = ( newimgVersion != oldimgVersion );
							Log.d("mylog","init() success() needToUpdateDB "+needToUpdateDB+" needToloadAD "+needToloadAD+"needToUpdateEV"+needToUpdeateEV+"needToUpdateIMG"+needToUpdateIMG);
							if(needToUpdateDB){
								DBHandler handler = DBHandler.open(mContext);
								handler.deleteAll();
								handler.close();
							}
							if(needToUpdeateEV){
								DBHandler handler = DBHandler.open(mContext);
								handler.deleteEvents();
								handler.close();
							}
							mCallback.afterDataLoad(true);
						} catch (JSONException e) {
							needToUpdateDB = false;
							needToloadAD = false;
							Log.d("mylog","init() JSONException");
							mCallback.afterDataLoad(false);
						}
					}
					
					@Override
					public void failure(StackMobException arg0) {
						// TODO Auto-generated method stub
						Log.d("mylog","init() failure()..."+arg0.toString());
						needToUpdateDB = false;
						needToloadAD = false;
						mCallback.afterDataLoad(false);
					}
				});				
			}
		}).start();
	    Thread pointUpdateThread = new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Map<String, String> args = new HashMap<String, String>();
				args.put("point", String.valueOf(UserStatus.point));
				
				StackMobCommon.getStackMobInstance().put("user", UserStatus.id, args, new StackMobCallback() {
				    @Override 
				    public void success(String responseBody) {
				        //PUT succeeded
				    	Log.d("mylog", "tempquery() success()");
				    	Editor ed = ShopListClass.mPrefs.edit();
				    	ed.putBoolean("updatecomplete", true);
				    	ed.commit();
				    }
				    @Override 
				    public void failure(StackMobException e) {
				        //PUT failed
				    	Log.d("mylog", "tempquery() failut()");
				    	Editor ed = ShopListClass.mPrefs.edit();
				    	ed.putBoolean("updatecomplete", false);
				    	ed.commit();
				    }
				});
		}});
	    if(!mPrefs.getBoolean("updatecomplete", true)){
	    	pointUpdateThread.start();
	    }
	}*/
}

/*class Shopinfo{
	String shopId;
	String phoneNum;
	String shopAddress;
	String openTime;
	String closeDay;
	String discount;
	String menu;
	double lon;
	double lat;
}*/
