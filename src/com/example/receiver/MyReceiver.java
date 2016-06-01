package com.example.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import cn.jpush.android.api.JPushInterface;

import com.example.db.AddressData;
import com.example.db.DBHelper;
import com.example.db.GeneralData;
import com.example.db.LocData;
import com.example.db.PhoneData;
import com.example.db.SMSData;
import com.example.lpsq.LocationMapActivity;
import com.example.lpsq.MainActivity2;
import com.example.lpsq.PhoneActivity;
import com.example.lpsq.R;
import com.example.lpsq.SMSActivity;
import com.example.service.GetLocationService;
import com.example.thread.SendMsgThread;
import com.example.utils.MyUtils;
import com.example.utils.SharedpreferencesUtil;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "MyReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
		System.out.println(bundle.getString(JPushInterface.EXTRA_EXTRA)
				+ "extras");
		//
		String content = getExtraContent(bundle);
		int type = 0;
		if (!getExtraType(bundle).equals("")) {
			type = Integer.valueOf(getExtraType(bundle));
		}
		System.out.println(content + "content");
		System.out.println(type + "type");
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息:"
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			// 有用部分
			System.out.println("用户收到了通知``");

			if (type == GeneralData.TYPE_REQUEST_LOCATION) {
				context.startService(new Intent(context,
						GetLocationService.class));
			}
			if (type == GeneralData.TYPE_REQUEST_ADDRESS) {
				GeneralData gd = new GeneralData("通讯录已刷新",
						MyUtils.getPhoneContacts(context),
						GeneralData.TYPE_ADDRESS, new SharedpreferencesUtil(
								context).getTargetId());
				new SendMsgThread(gd, context).start();
			}
			DBHelper dbHelper = new DBHelper(context, "lpsq", null, 1);
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			String[] str = content.split("#");
			switch (type) {
			case GeneralData.TYPE_SMS:
				SMSData sd = new SMSData(Integer.valueOf(str[0]), str[1],
						str[2], str[3], str[4]);
				dbHelper.insert_smsdata(sd, db);
				break;
			case GeneralData.TYPE_PHONE:
				PhoneData pd = new PhoneData(Integer.valueOf(str[0]), str[1],
						str[2], str[4], str[3]);
				dbHelper.insert_phonedata(pd, db);
				break;
			case GeneralData.TYPE_LOCATION:
				LocData ld = new LocData(Double.valueOf(str[1]),
						Double.valueOf(str[2]), str[0], "");
				dbHelper.insert_locationdata(ld, db);
				break;
			case GeneralData.TYPE_CHAT_MESSAGE:
				dbHelper.insert_chatdata("收到", str[0], str[1], db);
				break;
			case GeneralData.TYPE_ADDRESS:
				Log.e(TAG, "TYPE_ADDRESS");
				db.execSQL("delete  from  ADDRESSDATA");
				for (int i = 0; i < str.length - 1; i += 2) {
					AddressData ad = new AddressData(str[i], str[i + 1]);
					dbHelper.update_addressdata(ad, db);
				}
				break;
			default:
				break;
			}
			db.close();
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知" + alert + "content``" + content
					+ "type``" + type);
			switch (type) {
			case GeneralData.TYPE_LOCATION:
				Intent i = new Intent(context, LocationMapActivity.class);
				i.putExtra("lon", Double.valueOf(content.split("#")[1]));
				i.putExtra("lat", Double.valueOf(content.split("#")[2]));
				i.putExtra("time", content.split("#")[0]);
				i.putExtra("loc", "");
				System.out.println("lon"
						+ Double.valueOf(content.split("#")[1]) + "lat"
						+ Double.valueOf(content.split("#")[2]));
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
				break;
			case GeneralData.TYPE_CHAT_MESSAGE:
				Intent i2 = new Intent(context, MainActivity2.class);
				i2.putExtras(bundle);
				i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i2);
				break;
			case GeneralData.TYPE_SMS:
				Intent i_sms = new Intent(context, SMSActivity.class);
				i_sms.putExtra("sms", getExtraContent(bundle));
				i_sms.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i_sms);
				break;
			case GeneralData.TYPE_PHONE:
				Intent i_phone = new Intent(context, PhoneActivity.class);
				i_phone.putExtra("phone", getExtraContent(bundle));
				i_phone.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i_phone);
				break;
			case GeneralData.TYPE_ADDRESS:
				// TODO跳转
				Log.e(TAG, "TYPE_ADDRESS");
				Intent intent_address = new Intent(context, MainActivity2.class);
				intent_address.putExtra("page", 2);
				intent_address.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent_address);
				break;
			default:
				break;
			}
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	private String getExtraType(Bundle bundle) {
		if (bundle == null) {
			return "";
		}
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		String type = "";
		JSONObject extrasJson;
		if (extras == null) {
			return type;
		}
		try {
			extrasJson = new JSONObject(extras);
			type = extrasJson.optString("type");
			return type;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(type + "type");
		return type;

	}

	private String getExtraContent(Bundle bundle) {
		if (bundle == null) {
			return "";
		}
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

		String content = "";
		JSONObject extrasJson;
		if (extras == null) {
			return content;
		}
		try {
			extrasJson = new JSONObject(extras);
			content = extrasJson.optString("content");
			return content;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(content + "content");
		return content;

	}

	private void showPhoneDialog(Context context) {
		AlertDialog reset_Dialog = new AlertDialog.Builder(context).create();
		reset_Dialog.show();
		Window window = reset_Dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.dialogstyle); // 添加动画

		reset_Dialog.getWindow().setContentView(R.layout.dialog_phone);
		// reset_Dialog.getWindow().findViewById(R.id.yes)
		// .setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// startActivity(new Intent(
		// Bind_Device_Anim_Activity.this,
		// MainActivity.class));
		// mBluetoothLeService.disconnect();
		// mBluetoothLeService.closeGatt();
		// mBluetoothLeService.close();
		// Bind_Device_Anim_Activity.this.finish();
		// reset_Dialog.dismiss();
		// }
		// });
		reset_Dialog.setCancelable(false);
	}

}
