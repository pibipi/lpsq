package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.service.PhoneService;
import com.example.service.SendNotMsgService;
import com.example.utils.MyUtils;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			// TODO
			// 在这里写重新启动service的相关操作
			System.out.println("ACTION_BOOT_COMPLETED");
			if (!MyUtils.isServiceWork(context,
					"com.example.service.PhoneService")) {
				context.startService(new Intent(context, PhoneService.class));
			}
			if (!MyUtils.isServiceWork(context,
					"com.example.service.SendNotMsgService")) {
				context.startService(new Intent(context,
						SendNotMsgService.class));
			}
			// Intent i = new Intent(context, OnePActivity.class);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// context.startActivity(i);
		}
	}
}
