package com.goodshop.app;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.stackmob.android.sdk.common.StackMobCommon;
import com.goodshop.app.R;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class NFCActivity extends Activity {
 
    public static final String MIME_TYPE = "application/com.stackmob.example";
	
    private final int REQUESTCODE = 153;
    
	private NfcAdapter mNfcAdapter;
    private AlertDialog.Builder logindialogbuilder;
    private AlertDialog.Builder pointdialogbuilder;
    private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.nfcactivity);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        context = this;
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
     
        if (!mNfcAdapter.isEnabled()) {
        	Toast.makeText(this, "NFC 읽기가 가능하도록 설정해주세요", Toast.LENGTH_LONG).show();
        	finish();
        } else {
	       if(!StackMobCommon.isStackMobinitialized()){
		   		StackMobCommon.API_KEY = "13b831d9-38b8-4a70-baa5-b3b1a24318e7";
				StackMobCommon.API_SECRET = "7bf32d60-caeb-4c86-bd20-c3a2b5714512";
	    		StackMobCommon.USER_OBJECT_NAME = "user";
	    		StackMobCommon.API_VERSION = 1;
	    		StackMobCommon.API_URL_FORMAT = "api.mob1.stackmob.com/";
	    		StackMobCommon.PUSH_API_URL_FORMAT = "push.mob1.stackmob.com";
	    		   
	    		StackMobCommon.init(getApplicationContext());
	    		ShopListClass.mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    		ShopListClass.mContext = getApplicationContext();
	       }
	        
	        logindialogbuilder = new AlertDialog.Builder(this);
	        logindialogbuilder.setMessage("포인트를 적립하기 위해서는 로그인이 필요합니다. 로그인 화면으로 이동하시겠습니까?")
        	       .setCancelable(false)
        	       .setPositiveButton("예", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	        	   Intent intent = new Intent(NFCActivity.this, Signin.class);
        	        	   startActivityForResult(intent, REQUESTCODE);
        	        	   UserStatus.point++;
        	           }
        	       })
        	       .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
        	           public void onClick(DialogInterface dialog, int id) {
        	             dialog.cancel();
        	             finish();
        	           }
        	       });
	        pointdialogbuilder = new AlertDialog.Builder(this);
	        pointdialogbuilder.setPositiveButton("확인", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					finish();
				}
			}).setCancelable(false);
	        
	        
	        
 	       handleIntent(getIntent());
	    }
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode){
		case REQUESTCODE:
			if(resultCode == RESULT_OK){
				handleIntent(getIntent());
			}else{
				finish();
			}
			break;
		}
	}
	
    @Override
    protected void onResume() {
        super.onResume();
         
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown. 
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }
     
    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);
         
        super.onPause();
    }
     
    @Override
    protected void onNewIntent(Intent intent) { 
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         * 
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }
     
    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
 
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
 
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};
 
        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType("application/com.stackmob.example");//MIME_TEXT_PLAIN);
        	//filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }
         
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }
    
    private void handleIntent(Intent intent) {
       		Log.d("mylog","is login"+!UserStatus.is_login+"autologin"+getSharedPreferences("pref", MODE_PRIVATE).getBoolean("autologin", false));
    	//if(result.equalsIgnoreCase("goodshop")){
	    	long pointlastmod = ShopListClass.mPrefs.getLong("pointlastmod", 0);
    	    if( ((System.currentTimeMillis() - pointlastmod) > 1000 * 600) || UserStatus.id.equals("admin") ){
    	    	if(!UserStatus.is_login && !ShopListClass.mPrefs.getBoolean("autologin", false)){
	        		logindialogbuilder.create().show();
		        }else {
	        	    	UserStatus.id = ShopListClass.mPrefs.getString("id", "");
			        	UserStatus.point = ShopListClass.mPrefs.getInt("point", 0);
			        	UserStatus.point++;
			        	//mStudentId.setText(UserStatus.id);
			        	//mCurPoint.setText(String.valueOf(UserStatus.point));
			        	Editor ed = ShopListClass.mPrefs.edit();
		        	    ed.putInt("point", UserStatus.point);
		        	    ed.putLong("pointlastmod", System.currentTimeMillis());
		        	    ed.commit();
		      
		        	    pointdialogbuilder.setMessage("포인트 적립에 성공하였습니다!\n아이디:"+UserStatus.id+"\n현재 포인트:"+UserStatus.point);
			        	pointdialogbuilder.create().show();
			        	if(NetworkUtil.getConnectivityStatus(ShopListClass.mContext)!=NetworkUtil.TYPE_NOT_CONNECTED){
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
    	    }else{
	        	    pointdialogbuilder.setMessage("같은 기기로는 10분에 한번씩 포인트 적립이 가능합니다!");
		        	pointdialogbuilder.create().show();
    	    }
        /* String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
             
            String type = intent.getType();
            if (MIME_TYPE.equals(type)) {
     
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
                 
            } else {
                Log.d("mylog", "Wrong mime type: " + type);
                Toast.makeText(this, "유효하지 않은 NFC 태그 입니다", Toast.LENGTH_LONG).show();
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
             
            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();
             
            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }*/
    }
    /**
     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
    
    
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
   	 
        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];
             
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag. 
                return null;
            }
     
            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
     
            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e("mylog", "Unsupported Encoding", e);
                        Toast.makeText(context, "유효하지 않은 NFC 태그 입니다", Toast.LENGTH_LONG).show();
                    }
                }
            }
            return null;
        }
         
        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            /*
             * See NFC forum specification for "Text Record Type Definition" at 3.2.1 
             * 
             * http://www.nfc-forum.org/specs/
             * 
             * bit_7 defines encoding
             * bit_6 reserved for future use, must be 0
             * bit_5..0 length of IANA language code
             */
     
            byte[] payload = record.getPayload();
     
            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
     
            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;
             
            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"
             
            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }
         
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
            	Log.d("mylog","is login"+!UserStatus.is_login+"autologin"+getSharedPreferences("pref", MODE_PRIVATE).getBoolean("autologin", false));
            	//if(result.equalsIgnoreCase("goodshop")){
        	    	if(!UserStatus.is_login && !ShopListClass.mPrefs.getBoolean("autologin", false)){
                		logindialogbuilder.create().show();
        	        }else {
        	        	UserStatus.id = ShopListClass.mPrefs.getString("id", "");
        	        	UserStatus.point = ShopListClass.mPrefs.getInt("point", 0);
        	        	UserStatus.point++;
        	        	//mStudentId.setText(UserStatus.id);
        	        	//mCurPoint.setText(String.valueOf(UserStatus.point));
        	        	Editor ed = ShopListClass.mPrefs.edit();
                	    ed.putInt("point", UserStatus.point);
                	    ed.commit();
                	    pointdialogbuilder.setMessage("포인트 적립에 성공하였습니다!\n아이디:"+UserStatus.id+"\n현재 포인트:"+UserStatus.point);
        	        	pointdialogbuilder.create().show();
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
              //  }else{
               // 	Toast.makeText(context, "유효하지 않은 NFC 태그 입니다", Toast.LENGTH_LONG).show();
               // }
            }
        }
    }
}
