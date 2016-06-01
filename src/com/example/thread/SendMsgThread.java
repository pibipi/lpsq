package com.example.thread;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.db.DBHelper;
import com.example.db.GeneralData;
import com.example.db.LocData;
import com.example.db.PhoneData;
import com.example.db.SMSData;
import com.example.utils.MyUtils;

public class SendMsgThread extends Thread {
	private GeneralData generalData;
	private Context context;
	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private Handler mHandler;

	public SendMsgThread(GeneralData generalData, Context context) {
		super();
		this.generalData = generalData;
		this.context = context;
	}

	public SendMsgThread(GeneralData generalData, Context context,
			Handler mHandler) {
		super();
		this.generalData = generalData;
		this.context = context;
		this.mHandler = mHandler;
	}

	public void run() {
		dbHelper = new DBHelper(context, "lpsq", null, 1);
		db = dbHelper.getWritableDatabase();
		String alert = generalData.getAlert();
		String content = generalData.getContent();
		String id = generalData.getId();
		int type = generalData.getType();
		Log.e("sendmsgthread", generalData.toString());
		if (MyUtils.isConnect(context)) {
			try {
				DefaultHttpClient httpClient;
				HttpPost httpPost;
				httpClient = new DefaultHttpClient();
				httpPost = new HttpPost("https://api.jpush.cn/v3/push");
				httpPost.addHeader("Authorization",
						"Basic OGI1NzE2MGU1NmY5YzM3YWJiOGRhY2FhOjFmNTY1YTczY2MxNmVmZDFmNDg5M2FmNA==");
				httpPost.addHeader("Content-Type", "application/json");
				JSONObject object = new JSONObject();
				object.put("platform", "all");
				JSONObject notification = new JSONObject();
				JSONObject android = new JSONObject();
				JSONArray id_array = new JSONArray();
				id_array.put(0, id);
				JSONObject registration_id = new JSONObject();
				registration_id.put("registration_id", id_array);
				object.put("audience", registration_id);
				JSONObject extras = new JSONObject();

				switch (type) {
				case GeneralData.TYPE_REQUEST_LOCATION:
					android.put("alert", "");
					extras.put("content", content);
					extras.put("type", type + "");
					break;
				case GeneralData.TYPE_LOCATION:
					android.put("alert", alert);
					extras.put("content", content);
					extras.put("type", type + "");
					break;
				case GeneralData.TYPE_CHAT_MESSAGE:
					android.put("alert", alert);
					extras.put("content", content);
					extras.put("type", type + "");
					break;
				case GeneralData.TYPE_SMS:
					android.put("alert", alert);
					extras.put("content", content);
					extras.put("type", type + "");
					break;
				case GeneralData.TYPE_PHONE:
					android.put("alert", alert);
					extras.put("content", content);
					extras.put("type", type + "");
					break;
				case GeneralData.TYPE_REQUEST_ADDRESS:
					android.put("alert", "");
					extras.put("content", content);
					extras.put("type", type + "");
					break;
				case GeneralData.TYPE_ADDRESS:
					android.put("alert", alert);
					extras.put("content", content);
					extras.put("type", type + "");
					break;
				default:
					break;
				}

				android.put("extras", extras);
				notification.put("android", android);
				object.put("notification", notification);
				httpPost.setEntity(new StringEntity(object.toString(), "utf-8"));
				HttpResponse httpResponse = httpClient.execute(httpPost);
				Log.e("sendmsgthread", httpResponse.getStatusLine()
						.getStatusCode() + "");
				System.out.println(object.toString());
				String[] str = content.split("#");
				if (httpResponse.getStatusLine().getStatusCode() != 200) {
					Message msg = new Message();
					msg.what = 400;
					switch (type) {
					case GeneralData.TYPE_SMS:
						SMSData sd = new SMSData(Integer.valueOf(str[0]),
								str[1], str[2], str[3], str[4]);
						dbHelper.insert_not_smsdata(sd, db);
						break;
					case GeneralData.TYPE_PHONE:
						PhoneData pd = new PhoneData(Integer.valueOf(str[0]),
								str[1], str[2], str[4], str[3]);
						dbHelper.insert_not_phonedata(pd, db);

						break;
					case GeneralData.TYPE_CHAT_MESSAGE:
						mHandler.sendMessage(msg);
						break;
					case GeneralData.TYPE_REQUEST_LOCATION:
						mHandler.sendMessage(msg);
						break;
					case GeneralData.TYPE_REQUEST_ADDRESS:
						mHandler.sendMessage(msg);
						break;
					default:
						break;
					}
				} else {
					Message msg = new Message();
					msg.what = 200;
					switch (type) {
					case GeneralData.TYPE_SMS:
						SMSData sd = new SMSData(Integer.valueOf(str[0]),
								str[1], str[2], str[3], str[4]);
						dbHelper.delete_not_smsdata(sd, db);
						break;
					case GeneralData.TYPE_PHONE:
						PhoneData pd = new PhoneData(Integer.valueOf(str[0]),
								str[1], str[2], str[4], str[3]);
						dbHelper.delete_not_phonedata(pd, db);
						break;
					case GeneralData.TYPE_CHAT_MESSAGE:
						String[] chat_msg = content.split("#");
						dbHelper.insert_chatdata("发送", chat_msg[0],
								chat_msg[1], db);
						mHandler.sendMessage(msg);
						break;
					case GeneralData.TYPE_REQUEST_LOCATION:
						mHandler.sendMessage(msg);
						break;
					case GeneralData.TYPE_REQUEST_ADDRESS:
						mHandler.sendMessage(msg);
						break;
					default:
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// 没有网络连接
			String[] str = content.split("#");
			switch (type) {
			case GeneralData.TYPE_SMS:
				SMSData sd = new SMSData(Integer.valueOf(str[0]), str[1],
						str[2], str[3], str[4]);
				dbHelper.insert_not_smsdata(sd, db);
				break;
			case GeneralData.TYPE_PHONE:

				PhoneData pd = new PhoneData(Integer.valueOf(str[0]), str[1],
						str[2], str[4], str[3]);
				dbHelper.insert_not_phonedata(pd, db);
				break;
			default:
				break;
			}
		}
	}
}