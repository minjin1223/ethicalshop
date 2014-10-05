package com.goodshop.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.goodshop.app.ServerConnector.QueryCallBack;

public class ServerConnector {
	
	private static final String SERV_IP = "http://175.126.100.133/";
	public static final int SHOPS = 1;
	public static final int EVENTS = 2;
	
	static void login(final String id, final String password, final QueryCallBack callback){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost( SERV_IP + "login/login.php" );		
				    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				    nameValuePairs.add(new BasicNameValuePair("username", String.valueOf(id)));
				    nameValuePairs.add(new BasicNameValuePair("password", String.valueOf(password)));
				    //Select 
				    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    	//HttpResponse response = httpclient.execute(httppost);
			    	ResponseHandler<String> responseHandler = new BasicResponseHandler();
			    	String responseBody = httpclient.execute(httppost, responseHandler);
			    	if(responseBody.equals("fail")){
			    		callback.failure("failure");
			    	}else{
			    		callback.success(responseBody);
			    	}
			    	
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				}
			}
		}).start();
	}
	
	static void registerUser(final String username, final String passwd, final String email, final QueryCallBack callback){
		new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost( SERV_IP + "signup/signup.php" );		
				    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				    nameValuePairs.add(new BasicNameValuePair("username", username));
				    nameValuePairs.add(new BasicNameValuePair("password", passwd));
				    nameValuePairs.add(new BasicNameValuePair("email", email));
				    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    	//HttpResponse response = httpclient.execute(httppost);
			    	ResponseHandler<String> responseHandler = new BasicResponseHandler();
			    	String responseBody = httpclient.execute(httppost, responseHandler);
			    	if(responseBody.equals("fail")){
			    		callback.failure("failure");
			    	}else{
			    		callback.success(responseBody);
			    	}
				}catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				}
			}
			
		}).start();
	}
	
	static void getRecentData(final Long lastmodified, final int relation, final QueryCallBack callback){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String phpPath = null;
					switch(relation){
					case SHOPS:	phpPath = SERV_IP +"content/shop_info.php";
						break;
					case EVENTS:phpPath = SERV_IP +"content/event.php";
						break;
					default: 
						callback.failure("Wrong Query - Query to unknown Table");
						return;
					}
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost( phpPath );		
				    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				    nameValuePairs.add(new BasicNameValuePair("lastModified", String.valueOf(lastmodified)));
				    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    	//HttpResponse response = httpclient.execute(httppost);
			    	ResponseHandler<String> responseHandler = new BasicResponseHandler();
			    	String responseBody = httpclient.execute(httppost, responseHandler);
			    	callback.success(responseBody);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				}
			}
        }).start();
	}
	
	static void changeEmail(final String username, final String newEmail, final QueryCallBack callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost( SERV_IP + "update/changeEmail.php" );		
				    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				    nameValuePairs.add(new BasicNameValuePair("username", username));
				    nameValuePairs.add(new BasicNameValuePair("newEmail", newEmail));
				    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			    	//HttpResponse response = httpclient.execute(httppost);
			    	ResponseHandler<String> responseHandler = new BasicResponseHandler();
			    	String responseBody = httpclient.execute(httppost, responseHandler);
			    	callback.success(responseBody);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					callback.failure(e.getMessage());
				}
			}
        }).start();		
	}
	
	public interface QueryCallBack{
		void success(String responseBody);
		void failure(String responseBody);
	}

}
