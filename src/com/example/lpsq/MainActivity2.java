package com.example.lpsq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import cn.jpush.android.api.JPushInterface;

import com.example.service.OnePService;
import com.example.utils.SharedpreferencesUtil;

@SuppressLint("NewApi")
public class MainActivity2 extends FragmentActivity implements
		OnCheckedChangeListener {
	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	private RadioButton main1;
	private RadioButton main2;
	private RadioButton main3;
	private RadioButton main4;
	private SharedpreferencesUtil sharedpreferencesUtil;
	private final static int SCANNIN_GREQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		init();
		// changeFragment(new Fragment_Main(), true);
		int page = getIntent().getIntExtra("page", 1);
		int page_2 = getIntent().getIntExtra("page_2", 1);
		switch (page) {
		case 1:
			main1.setChecked(true);
			changeFragment(new Fragment_Main(), true);
			break;
		case 2:
			main2.setChecked(true);
			changeFragment(new Fragment_Address(), true);
			break;
		case 3:
			main3.setChecked(true);
			Fragment_History fragment_History = new Fragment_History();
			Bundle bundle = new Bundle();
			bundle.putInt("page_2", page_2);
			fragment_History.setArguments(bundle);
			changeFragment(fragment_History, true);
			break;
		case 4:
			main4.setChecked(true);
			changeFragment(new Fragment_My(), true);
			break;
		default:
			break;
		}
	}

	private void init() {

		sharedpreferencesUtil = new SharedpreferencesUtil(
				getApplicationContext());
		fragmentManager = getSupportFragmentManager();
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		main1 = (RadioButton) findViewById(R.id.main1);
		main2 = (RadioButton) findViewById(R.id.main2);
		main3 = (RadioButton) findViewById(R.id.main3);
		main4 = (RadioButton) findViewById(R.id.main4);
	}

	public void changeFragment(Fragment fragment, boolean isInit) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, fragment);
		if (!isInit) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.main1:
			changeFragment(new Fragment_Main(), true);
			break;
		case R.id.main2:
			changeFragment(new Fragment_Address(), true);
			break;
		case R.id.main3:
			changeFragment(new Fragment_History(), true);
			break;
		case R.id.main4:
			changeFragment(new Fragment_My(), true);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		startService(new Intent(MainActivity2.this, OnePService.class));
		// unregisterReceiver(receiver);
		super.onPause();
		JPushInterface.onPause(this);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// switch (requestCode) {
	// case SCANNIN_GREQUEST_CODE:
	// if (resultCode == RESULT_OK) {
	// Bundle bundle = data.getExtras();
	// // 显示扫描到的内容
	// sharedpreferencesUtil.saveTargetId(bundle.getString("result"));
	// // TODo 设置按钮隐藏
	//
	// }
	// break;
	// }
	// }

}
