package com.example.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyUtils {
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(
				System.currentTimeMillis()));
	}

	public static String long2StringTime(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		Date date = calendar.getTime();
		return new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(date);
	}

	// 根据号码获取联系人的姓名
	public static String getContactNameByNumber(String number, Context context)
			throws Exception {
		String name = "";
		Uri uri = Uri
				.parse("content://com.android.contacts/data/phones/filter/"
						+ number);
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver
				.query(uri,
						new String[] { android.provider.ContactsContract.Data.DISPLAY_NAME },
						null, null, null);
		if (cursor.moveToFirst()) {
			name = cursor.getString(0);
			Log.i("Phoneservice", name);
		}
		cursor.close();
		if (name.equals("")) {
			return "无";
		} else {
			return name;
		}
	}

	/**
	 * 判断某个服务是否正在运行的方法
	 * 
	 * @param mContext
	 * @param serviceName
	 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
	 * @return true代表正在运行，false代表服务没有正在运行
	 */
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(40);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

	/**
	 * 判断有无网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("error", e.toString());
		}
		return false;
	}

	public static String getPhoneContacts(Context context) {
		String str = "";
		ContentResolver resolver = context.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		if (phoneCursor != null) {
			phoneCursor.moveToFirst();
			while (!phoneCursor.isAfterLast()) {
				String phoneNumber = "";
				// 得到联系人名称
				String contactId = phoneCursor.getString(phoneCursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				String username = phoneCursor
						.getString(phoneCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				Cursor phones = resolver.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phones.moveToNext()) {
					phoneNumber = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// 添加Phone的信息
					str = str + username + "#";
					str = str + phoneNumber + "#";
					Log.e("contract", username + "~" + phoneNumber);
				}
				phones.close();
				phoneCursor.moveToNext();
				// TODO 处理username phoneNumber；
			}
			phoneCursor.close();
		}
		System.out.println(str);
		return str;
	}

	/**
	 * 获取电话号码
	 */
	public static String getNativePhoneNumber(Context context) {
		String NativePhoneNumber = null;
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		NativePhoneNumber = telephonyManager.getLine1Number();
		System.out.println(NativePhoneNumber+"```");
		return NativePhoneNumber;
	}
}