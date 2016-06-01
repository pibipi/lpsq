package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.example.lpsq.MainActivity;
import com.example.lpsq.R;

public class OnePService extends Service {
	private LinearLayout mFloatLayout;
	private String TAG = "OnePService";
	private Handler mHandler;
	private int flag = 1;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					System.out.println("case 1````" + mFloatLayout.VISIBLE);
					mFloatLayout.setVisibility(View.INVISIBLE);
					mFloatLayout.setVisibility(View.VISIBLE);
					System.out.println("ispush stop"
							+ JPushInterface
									.isPushStopped(getApplicationContext()));
					if (!JPushInterface.isPushStopped(getApplicationContext())) {
						JPushInterface.init(getApplicationContext());
					}
					break;
				case 2:
					System.out.println("case 2````" + mFloatLayout.VISIBLE);
					mFloatLayout.setVisibility(View.VISIBLE);
					// createFloatView();
					break;

				default:
					break;
				}

				super.handleMessage(msg);
			}

		};
		createFloatView();
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (flag == 1) {
						flag = 2;
					} else if (flag == 2) {
						flag = 1;
					}
					System.out.println(flag + "flag");
					Message msg = new Message();
					msg.what = flag;
					mHandler.sendMessage(msg);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}).start();

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		System.out.println("on destroy");
		createFloatView();
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void createFloatView() {
		// 定义浮动窗口布局

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
						- mFloatView.getMeasuredHeight();
				System.out.println(mFloatView.getMeasuredHeight());
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
				startActivity(new Intent(OnePService.this, MainActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP));
				// Toast.makeText(OnePService.this, "onClick",
				// Toast.LENGTH_SHORT)
				// .show();
			}
		});
	}

}
