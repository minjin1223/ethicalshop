package com.goodshop.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.goodshop.app.R;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShopListAdapter extends ArrayAdapter<Integer> implements OnClickListener {

	private ArrayList<Integer> indexs;
	private Context mContext;
	private LayoutInflater inflater;
	private static File BASEDIR = new ContextWrapper(ShopListClass.mContext).getDir("IMAGE", Context.MODE_PRIVATE);
	
	public ShopListAdapter(Context context, int textViewResourceId,
			ArrayList<Integer> items) {
		super(context, textViewResourceId, items);
		this.indexs = items;
		mContext = context;
		inflater = LayoutInflater.from(context);
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_list, null);
		}
		ImageView shopimage = ViewHolder.get(convertView, R.id.shopimage);
		ImageView shopicon = ViewHolder.get(convertView, R.id.group_btn_icon);
		ImageView shopgroup = ViewHolder.get(convertView, R.id.group_btn_hgu);
		TextView tv_Main = ViewHolder.get(convertView, R.id.custom_list_title_main);
		TextView tv_Sub = ViewHolder.get(convertView, R.id.custom_list_title_sub);
		ImageButton callbtn = ViewHolder.get(convertView, R.id.callbtn);
		ImageButton mapbtn = ViewHolder.get(convertView, R.id.mapbtn);
		// 현재 item의 position에 맞는 이미지와 글을 넣어준다.          
//Image				Bitmap temp = custom_list_data.getImage_ID();
//Image				temp = getRoundedCornerBitmap(temp);
//Image				iv.setImageBitmap(temp);
		int index = indexs.get(position);
		
//		if(ShopListClass.shopImageFlag[index]){
//			shopimage.setImageBitmap(ShopListClass.readShopImageFromMemory(index));
//		}else{
//			shopimage.setImageResource(R.drawable.es_shoptable_img_1);
//		}
		Bitmap img = shopImage(index);
		if(img == null){
			shopimage.setImageResource(R.drawable.es_shoptable_img_1);
			if( ShopListClass.shopPhoto[index] != null
				&& NetworkUtil.getConnectivityStatus(ShopListClass.mContext) != NetworkUtil.TYPE_NOT_CONNECTED)
				new ImageLoader().execute(index);
		}else{
			shopimage.setImageBitmap(img);
		}
		tv_Main.setText(ShopListClass.shopName[index]);
		tv_Sub.setText(ShopListClass.shopSummary[index]);
		callbtn.setTag(index);
		mapbtn.setTag(index);
		callbtn.setOnClickListener(this);
		mapbtn.setOnClickListener(this);
		int code=Integer.valueOf( ShopListClass.shopCode[index] );
		switch(code%10){
		case 0: shopicon.setImageResource(R.drawable.icon_etc);
		break;
		case 1: shopicon.setImageResource(R.drawable.icon_cafe);//cafe
		break;
		case 2: shopicon.setImageResource(R.drawable.icon_cafe);//beauty
		break;
		case 3: shopicon.setImageResource(R.drawable.icon_food);//food
		break;
		case 4: shopicon.setImageResource(R.drawable.icon_entertain);//entertainment
		break;
		case 5: shopicon.setImageResource(R.drawable.icon_hospital);//hospital
		break;
		}
		if(code>=100){
			shopgroup.setImageResource(R.drawable.group_btn_pop2);
		}else{
			shopgroup.setImageResource(R.drawable.group_btn_hgu2);
		}
		
		return convertView;
	}
	
  	public Bitmap shopImage(int index){
  		try{
			File finalPath = new File(BASEDIR,ShopListClass.shopPhoto[index]+".jpg");
		    if(finalPath.exists()){
		    	return BitmapFactory.decodeStream(new FileInputStream(finalPath));
			}
		    return null;
  		}catch(FileNotFoundException e){
  			return null;
  		}
  	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int index = (Integer)v.getTag();
		Log.d("mylog","ShopListAdapter onClick() Clicked Shop Name is "+ShopListClass.shopName[index]);
		switch(v.getId()){
		case R.id.callbtn:
			if(ShopListClass.shopPhone[index]==null){
				Toast.makeText(mContext, "전화 번호가 등록되어 있지 않습니다", Toast.LENGTH_SHORT).show();
			}else{
				Uri num=Uri.parse("tel:"+ ShopListClass.shopPhone[index]);
				Intent dial = new Intent(Intent.ACTION_DIAL,num);
				mContext.startActivity(dial);
			}
			break;
		case R.id.mapbtn:
			if(ShopListClass.shopLat[index] == 0 && ShopListClass.shopLon[index] == 0){
				Toast.makeText(mContext, "위치 정보가 등록되어 있지 않습니다", Toast.LENGTH_SHORT).show();
			}else{
				Intent i=new Intent(mContext, MapViewActivity.class);
				i.putExtra("index", index);
				mContext.startActivity(i);
			}			
			break;
		}
	}
	
	private class ImageLoader extends AsyncTask<Integer, Void, Void> { 
		  //private ImageView i = new ImageView(GalleryActivity.this);

		  @Override
		  protected Void doInBackground(Integer... params) { 
		    try{
		    	Bitmap result;
		    	int index = params[0];
		    	String IMAGEURL = "http://res.cloudinary.com/goodshopapp/image/upload/"+ShopListClass.shopPhoto[index]+".jpg";
				URL url = new URL(IMAGEURL);
				URLConnection ucon = url.openConnection();
				InputStream inputstream = ucon.getInputStream();
				result = BitmapFactory.decodeStream(inputstream);
				//result = MyBitmapFactory.DecodeBitmapFromURL(url,100,70);
				File finalPath = new File(BASEDIR,ShopListClass.shopPhoto[index]+".jpg");
				FileOutputStream fOut = new FileOutputStream(finalPath);
				result.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.close();
				return null;
		    }catch(IOException e){
		    	Log.d("mylog","ImageLoader "+e.toString());
		    	return null;
		    }
		  }

		  @Override     
		  protected void onPostExecute(Void arg) {
		     notifyDataSetChanged();
		  }
	}

//	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		// TODO Auto-generated method stub
	/*	shopIdForCount= Array_Data.get(position).getId();
		//		Toast.makeText(ShopList.this, shopIdForCount, Toast.LENGTH_SHORT).show();
		try {
			StackMobQuery query = new StackMobQuery("user").fieldIsEqualTo("email", email);
			StackMobCommon.getStackMobInstance().get(query, new StackMobCallback() {
				@Override public void success(String responseBody) {
					//GET succeeded
					queryResult = "{"+"\"aba\":"+responseBody+"}";
					//					Toast.makeText(ShopList.this, responseBody, Toast.LENGTH_SHORT).show();
					handler.sendEmptyMessage(4);
				}
				@Override public void failure(StackMobException e) {
					//GET failed
				}
			});
		}catch (Exception e) {}*/
//		int index = indexs.get(position);
		
//		Intent i=new Intent(context, SelectedItemView.class );
//		i.putExtra("title", ShopListClass.getshopMainTitle(index));
//Image		i.putExtra("image", Array_Data.get(position).getImage_ID());
//		i.putExtra("id", ShopListClass.getshopId(index));
//		i.putExtra("type", Integer.valueOf( ShopListClass.getshopType(index) ));
//		i.putExtra("index", position);
//Image		i.putExtra("address", Array_Data.get(position).getImage_add());
//Image		i.putExtra("flag", Array_Data.get(position).isImage_flag());
//		context.startActivity(i);
//	}
/*	private voidBitmap getRoundedCornerBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;

	}*/

}