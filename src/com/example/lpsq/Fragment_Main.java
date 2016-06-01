package com.example.lpsq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.example.db.GeneralData;
import com.example.service.PhoneService;
import com.example.service.SendNotMsgService;
import com.example.thread.SendMsgThread;
import com.example.utils.CountDownButtonHelper;
import com.example.utils.SharedpreferencesUtil;

public class Fragment_Main extends Fragment implements OnClickListener {
	private Button send;
	private Button get_loc;
	private EditText edit_text;
	private TextView text;
	private SharedpreferencesUtil sharedpreferencesUtil;
	private String id;
	private Handler mHandler;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, null);
		init(view);
		getActivity().startService(
				new Intent(getActivity(), PhoneService.class));
		getActivity().startService(
				new Intent(getActivity(), SendNotMsgService.class));
		id = sharedpreferencesUtil.getTargetId();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				initID();
			}
		}, 100);

		return view;
	}

	private void init(View view) {
		context = getContext();
		sharedpreferencesUtil = new SharedpreferencesUtil(getContext());
		send = (Button) view.findViewById(R.id.send);
		get_loc = (Button) view.findViewById(R.id.get_loc);
		edit_text = (EditText) view.findViewById(R.id.edit_text);
		text = (TextView) view.findViewById(R.id.text);
		send.setOnClickListener(this);
		get_loc.setOnClickListener(this);
		//

		String str = "";
		Bundle b = getActivity().getIntent().getExtras();
		if (b != null) {
			str = b.getString(JPushInterface.EXTRA_ALERT) + " ";
		}
		System.out.println(str + "str");
		if (str.equals("")) {
			str = "等待消息……";
		}
		text.setText(str + "");
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 200:
					Toast.makeText(context, "发送成功", 0).show();
					break;
				case 400:
					Toast.makeText(context, "发送失败", 0).show();
					break;
				default:
					break;
				}
			}

		};
	}

	private void initID() {
		String regId = JPushInterface.getRegistrationID(getActivity()
				.getApplicationContext());
		System.out.println(regId + "regid");
		if (sharedpreferencesUtil.getTargetId().equals("")) {
			sharedpreferencesUtil.saveTargetId(regId);
		}
		id = sharedpreferencesUtil.getTargetId();
		System.out.println(id + "id");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			String str = edit_text.getText().toString();
			if (str.equals("")) {
				Toast.makeText(getContext(), "请输入内容", 0).show();
				return;
			}
			String content = System.currentTimeMillis() + "#" + str;
			GeneralData gd = new GeneralData(str, content,
					GeneralData.TYPE_CHAT_MESSAGE, id);
			new SendMsgThread(gd, getContext(), mHandler).start();
			// new SendMsgThread(data).start();
			send.setClickable(false);
			CountDownButtonHelper helper = new CountDownButtonHelper(send,
					"已发送", 10, 1);
			helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
				@Override
				public void finish() {
					send.setText("发送消息");
					send.setClickable(true);
				}
			});
			helper.start();
			edit_text.setText("");
			break;
		case R.id.get_loc:
			// new SendMsgThread(getResources().getString(R.string.SendFlag)
			// + "##" + id).start();
			GeneralData gd2 = new GeneralData("", "请求位置",
					GeneralData.TYPE_REQUEST_LOCATION, id);
			new SendMsgThread(gd2, getContext(), mHandler).start();
			get_loc.setClickable(false);
			CountDownButtonHelper helper2 = new CountDownButtonHelper(get_loc,
					"已请求", 10, 1);
			helper2.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
				@Override
				public void finish() {
					get_loc.setText("请求位置");
					get_loc.setClickable(true);
				}
			});
			helper2.start();
			break;
		default:
			break;
		}
	}

}
