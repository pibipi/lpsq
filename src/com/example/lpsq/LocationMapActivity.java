package com.example.lpsq;

import java.util.ArrayList;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.db.DBHelper;
import com.example.db.LocData;

public class LocationMapActivity extends Activity {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private double lat;
	private double lon;
	private String time = "";
	private String loc = "";
	private Handler mHandler;
	private TextView address_text;
	private DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.location_map);
		init();
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(new LatLng(
				lat, lon), 19));
		initAnim();
		if (!loc.equals("")) {
			address_text.setText(loc);
		} else {
			initSearch();
		}
		// mBaiduMap.setMyLocationEnabled(true);
		// MyLocationData locationData=new
		// MyLocationData.Builder().latitude(lat).longitude(lon).accuracy(30).direction(100).build();
		// mBaiduMap.setMyLocationData(locationData);
		// 定义Maker坐标点
		// LatLng point = new LatLng(lat, lon);
		// // 构建Marker图标
		// BitmapDescriptor bitmap =
		// BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
		// // 构建MarkerOption，用于在地图上添加Marker
		// OverlayOptions option = new
		// MarkerOptions().position(point).icon(bitmap);
		// // 在地图上添加Marker，并显示
		// // mBaiduMap.addOverlay(option);
		// final Marker marker = (Marker) (mBaiduMap.addOverlay(option));

	}

	private void initSearch() {
		GeoCoder mSearch = GeoCoder.newInstance();
		mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(
				lat, lon)));
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
				System.out.println("得到位置" + arg0.getAddress());
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				dbHelper.update_locloc(
						new LocData(lon, lat, time, arg0.getAddress() + ""), db);
				address_text.setText(arg0.getAddress() + "");
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void init() {
		dbHelper = new DBHelper(getApplicationContext(), "lpsq", null, 1);
		address_text = (TextView) findViewById(R.id.address_text);
		mHandler = new Handler();
		//
		lon = getIntent().getDoubleExtra("lon", 0);
		lat = getIntent().getDoubleExtra("lat", 0);
		time = getIntent().getStringExtra("time");
		loc = getIntent().getStringExtra("loc");

		System.out.println("lon" + lon + "``lat" + lat);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
	}

	private void initAnim() {
//		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//		BitmapDescriptor bitmap1 = BitmapDescriptorFactory
//				.fromResource(R.drawable.ic_launcher);
//		BitmapDescriptor bitmap2 = BitmapDescriptorFactory
//				.fromResource(R.drawable.ic_launcher2);
//		BitmapDescriptor bitmap3 = BitmapDescriptorFactory
//				.fromResource(R.drawable.ic_launcher3);
//		BitmapDescriptor bitmap4 = BitmapDescriptorFactory
//				.fromResource(R.drawable.ic_launcher4);
//		giflist.add(bitmap1);
//		giflist.add(bitmap2);
//		giflist.add(bitmap3);
//		giflist.add(bitmap4);
		LatLng point = new LatLng(lat, lon);
//		OverlayOptions ooD = new MarkerOptions().position(point).icons(giflist)
//				.zIndex(0).period(10);
		OverlayOptions ooD = new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding))
				.zIndex(0).period(10);
		final Marker mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
		// mHandler.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// mMarkerD.setPosition(new LatLng(lat + 0.01f, lon + 0.01f));
		// }
		// }, 2000);
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
	}
}
