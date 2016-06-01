package com.example.receiver;

import com.example.service.PhoneService;
import com.example.service.SendNotMsgService;
import com.example.utils.MyUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimeTickReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_TIME_TICK)) {
			context.startService(new Intent(context, PhoneService.class));
			Log.e("time tick", "TimeChangeReceiver");
			if (!MyUtils.isServiceWork(context,
					"com.example.service.PhoneService")) {
				context.startService(new Intent(context, PhoneService.class));
			}
			if (!MyUtils.isServiceWork(context,
					"com.example.service.SendNotMsgService")) {
				context.startService(new Intent(context,
						SendNotMsgService.class));
			}
		}
	}

}
