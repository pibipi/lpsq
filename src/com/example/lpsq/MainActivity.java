package com.example.lpsq;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.example.db.GeneralData;
import com.example.service.OnePService;
import com.example.service.PhoneService;
import com.example.service.SendNotMsgService;
import com.example.thread.SendMsgThread;
import com.example.utils.CountDownButtonHelper;
import com.example.utils.MyUtils;
import com.example.utils.SharedpreferencesUtil;

public class MainActivity extends Activity implements OnClickListener {
	private Button send;
	private EditText edit_text;
	private TextView text;
	private Handler mHandler;
	private Button get_loc;
	private Button binding;
	private Button my_code;
	private Button phone_his;
	private Button sms_his;
	private Button chat_his;
	private Button loc_his;
	private static final String TAG = "MainActivity";
	private final static int SCANNIN_GREQUEST_CODE = 1;
	// 离线地图
	private MKOfflineMap mOffline;

	private SharedpreferencesUtil sharedpreferencesUtil;

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		init_offline_map();
		id = sharedpreferencesUtil.getTargetId();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				initID();
			}
		}, 3000);
		System.out.println(MyUtils.isConnect(getApplicationContext()) + "wifi");
		startService(new Intent(MainActivity.this, PhoneService.class));
		startService(new Intent(MainActivity.this, SendNotMsgService.class));

	}

	private void initID() {
		String regId = JPushInterface
				.getRegistrationID(getApplicationContext());
		System.out.println(regId + "regid");
		if (sharedpreferencesUtil.getTargetId().equals("")) {
			sharedpreferencesUtil.saveTargetId(regId);
		}
		id = sharedpreferencesUtil.getTargetId();
		System.out.println(id + "id");
	}

	private void init() {
		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		my_code = (Button) findViewById(R.id.my_code);
		binding = (Button) findViewById(R.id.binding);
		send = (Button) findViewById(R.id.send);
		get_loc = (Button) findViewById(R.id.get_loc);
		edit_text = (EditText) findViewById(R.id.edit_text);
		text = (TextView) findViewById(R.id.text);
		phone_his = (Button) findViewById(R.id.phone_his);
		sms_his = (Button) findViewById(R.id.sms_his);
		chat_his = (Button) findViewById(R.id.chat_his);
		loc_his = (Button) findViewById(R.id.loc_his);

		phone_his.setOnClickListener(this);
		sms_his.setOnClickListener(this);
		chat_his.setOnClickListener(this);
		loc_his.setOnClickListener(this);

		send.setOnClickListener(this);
		get_loc.setOnClickListener(this);
		binding.setOnClickListener(this);
		my_code.setOnClickListener(this);

		String str = "";
		Bundle b = getIntent().getExtras();
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
					Toast.makeText(getApplicationContext(), "发送成功", 0).show();
					break;
				case 400:
					Toast.makeText(getApplicationContext(), "发送失败", 0).show();
					break;
				default:
					break;
				}
			}

		};
	}

	private void init_offline_map() {
		SDKInitializer.initialize(getApplicationContext());
		mOffline = new MKOfflineMap();
		mOffline.init(new MKOfflineMapListener() {
			@Override
			public void onGetOfflineMapState(int type, int state) {
				// TODO Auto-generated method stub
				System.out.println("onGetOfflineMapState type" + type + "state"
						+ state);
				MKOLUpdateElement update = mOffline.getUpdateInfo(state);
				// 处理下载进度更新提示
				if (update != null) {
					Log.e("offlinemap", String.format("%s : %d%%",
							update.cityName, update.ratio));
				}
			}
		});
		// 获取城市可更新列表
		ArrayList<MKOLSearchRecord> records = mOffline.getOfflineCityList();
		System.out.println(records.size() + "size");
		for (MKOLSearchRecord mkolSearchRecord : records) {
			// Log.e("城市", mkolSearchRecord.cityName);
		}
		// 开始下载离线地图，传入参数为cityID, cityID表示城市的数字标识。
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mOffline.start(289);
			}
		}).start();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			String str = edit_text.getText().toString();
			String content = System.currentTimeMillis() + "#" + str;
			GeneralData gd = new GeneralData(str, content,
					GeneralData.TYPE_CHAT_MESSAGE, id);
			new SendMsgThread(gd, getApplicationContext(), mHandler).start();
			// new SendMsgThread(data).start();
			send.setClickable(false);
			CountDownButtonHelper helper = new CountDownButtonHelper(send,
					"已发送", 3, 1);
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
			new SendMsgThread(gd2, getApplicationContext(), mHandler).start();
			get_loc.setClickable(false);
			CountDownButtonHelper helper2 = new CountDownButtonHelper(get_loc,
					"已请求", 5, 1);
			helper2.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
				@Override
				public void finish() {
					get_loc.setText("请求位置");
					get_loc.setClickable(true);
				}
			});
			helper2.start();
			break;
		case R.id.binding:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, MipcaActivityCapture.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			break;
		case R.id.my_code:
			Intent i = new Intent(MainActivity.this, MyCodeActivity.class);
			startActivity(i);
			break;
		case R.id.phone_his:
			startActivity(new Intent(MainActivity.this,
					PhoneHistoryActivity.class));
			break;
		case R.id.sms_his:
			startActivity(new Intent(MainActivity.this,
					SMSHistoryActivity.class));
			break;
		case R.id.chat_his:
			startActivity(new Intent(MainActivity.this,
					ChatHistoryActivity.class));
			break;
		case R.id.loc_his:
			startActivity(new Intent(MainActivity.this,
					LocationHistoryActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				// 显示扫描到的内容
				sharedpreferencesUtil.saveTargetId(bundle.getString("result"));
				// TODo 设置按钮隐藏

			}
			break;
		}
	}

	@Override
	protected void onResume() {
		// registerReceiver(receiver, new IntentFilter(
		// "com.example.jj1101.MESSAGE_RECEIVED"));
		// registerReceiver(new TimeTickReceiver(), new IntentFilter(
		// Intent.ACTION_TIME_TICK));
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		startService(new Intent(MainActivity.this, OnePService.class));
		// unregisterReceiver(receiver);
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
//			startService(new Intent(MainActivity.this, OnePService.class));
			// createFloatView();
			// Toast.makeText(getApplicationContext(), "press home", 0).show();
			System.out.println("keycode_home");
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void createFloatView() {
		// 定义浮动窗口布局
		final LinearLayout mFloatLayout;
		final WindowManager.LayoutParams wmParams;
		// 创建浮动窗口设置布局参数的对象
		final WindowManager mWindowManager;

		final Button mFloatView;
		wmParams = new WindowManager.LayoutParams();
		// 获取的是WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager) getApplication().getSystemService(
				getApplication().WINDOW_SERVICE);
		Log.i("floatview", "mWindowManager--->" + mWindowManager);
		// 设置window type
		wmParams.type = LayoutParams.TYPE_PHONE;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		// 以屏幕左上角为原点，设置x、y初始值，相对于gravity
		wmParams.x = 0;
		wmParams.y = 0;

		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		/*
		 * // 设置悬浮窗口长宽数据 wmParams.width = 200; wmParams.height = 80;
		 */

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		// 获取浮动窗口视图所在布局
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.test1p, null);
		// 添加mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);
		// 浮动窗口按钮
		mFloatView = (Button) mFloatLayout.findViewById(R.id.ttt);

		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth() / 2);
		Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight() / 2);
		// 设置监听浮动窗口的触摸移动
		mFloatView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
				wmParams.x = (int) event.getRawX()
						- mFloatView.getMeasuredWidth() / 2;
				Log.i(TAG, "RawX" + event.getRawX());
				Log.i(TAG, "X" + event.getX());
				// 减25为状态栏的高度
				wmParams.y = (int) event.getRawY()
						- mFloatView.getMeasuredHeight() / 2 - 25;
				Log.i(TAG, "RawY" + event.getRawY());
				Log.i(TAG, "Y" + event.getY());
				// 刷新
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false; // 此处必须返回false，否则OnClickListener获取不到监听
			}
		});

		mFloatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

}
