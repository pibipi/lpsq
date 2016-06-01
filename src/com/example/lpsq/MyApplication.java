package com.example.lpsq;

import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class MyApplication extends Application {

	public MyApplication() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		System.out.println("myapplication");
		SDKInitializer.initialize(getApplicationContext());
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		super.onCreate();
	}
}
