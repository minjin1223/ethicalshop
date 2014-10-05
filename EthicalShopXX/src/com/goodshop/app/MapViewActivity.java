package com.goodshop.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.goodshop.app.R;

public class MapViewActivity extends FragmentActivity  {
	private Drawable icon;
	private MapController mapController;
	private Double lon,lat;
	GoogleMap mGoogleMap;
	GeoPoint geoPoint=null;
	LatLng point;


	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map);

		mGoogleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapview)).getMap();
		
		//mGoogleMap.setBuiltInZoomControls(true);

		//mapController=mapView.getController();
		//mapController.setZoom(19);

		Intent intent = getIntent();
		int index = intent.getIntExtra("index", 0);
		lon=ShopListClass.shopLon[index];
		lat=ShopListClass.shopLat[index];
		Log.d("mylog","lat:"+lat+"lon:"+lon);

		
		point = new LatLng(lat,lon);
		//mapController.animateTo(geoPoint);
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
		mGoogleMap.addMarker(new MarkerOptions().position(point).title(ShopListClass.shopName[index])).showInfoWindow();
		//		overlay = mapView.getOverlays();
		//icon = this.getResources().getDrawable(R.drawable.es_map_img_1x);
		//itemizedOverlay = new IconItemizedOverlay(icon);

		//		OverlayItem overlayItem = new OverlayItem(geoPoint,"","");
		//		itemizedOverlay.addItems(overlayItem);
		//mapView.getOverlays().add(itemizedOverlay);
		//		me = new MyLocationOverlay(this, mapView);
		//		mapView.getOverlays().add(me);
		// 
		// 
		//		mapView.postInvalidate();
		//		overlay.add(itemizedOverlay);
//		me = new MyLocationOverlay(this, mapView);
//		mapView.getOverlays().add(me);
		//				me.getMyLocation();

//		mapView.postInvalidate();

	}

//	private void move() {
//		// TODO Auto-generated method stub
//		//		Toast.makeText(getApplicationContext(), "00", Toast.LENGTH_SHORT).show();
//		Double latitude = myLocation.getLatitude()*1E6;
//
//		Double longitude = myLocation.getLongitude()*1E6;
//		GeoPoint geoPoint = new GeoPoint(latitude.intValue(), longitude.intValue());
//		mapController.animateTo(geoPoint);
//		//		mapController.setCenter(geoPoint);
//	}
//
//	private void getMyLocation() {
//		// TODO Auto-generated method stub
//		LocationManager locationManager;
//		LocationListener locationListener;
//
//		locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		if (locationManager == null){
//			Toast.makeText(getApplicationContext(), "Location Manager is Null", Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		locationListener=new LocationListener(){
//
//			@Override
//			public void onLocationChanged(Location location) {
//				// TODO Auto-generated method stub
//				myLocation=location;
//				Toast.makeText(getApplicationContext(), "onLocationChanged", Toast.LENGTH_SHORT).show();
//				move();
//			}
//
//			@Override
//			public void onProviderDisabled(String provider) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "onProviderDisabled", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onProviderEnabled(String provider) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "onProviderEnabled", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onStatusChanged(String provider, int status,
//					Bundle extras) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "onStatusChanged", Toast.LENGTH_SHORT).show();
//			}
//
//		};
//		Toast.makeText(getApplicationContext(), "UpdateRequest", Toast.LENGTH_SHORT).show();
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//	}

}