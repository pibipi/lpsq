package com.example.lpsq;

import com.example.utils.SharedpreferencesUtil;

import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Fragment_My extends Fragment implements OnClickListener {
	private RelativeLayout binding;
	private RelativeLayout my_code;
	private RelativeLayout my_msg;
	private RelativeLayout advice;
	private RelativeLayout update;
	private RelativeLayout cancel_bind;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private AlertDialog cancle_bind_Dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my, null);
		init(view);
		return view;
	}

	private void init(View view) {
		cancle_bind_Dialog = new AlertDialog.Builder(getContext()).create();
		binding = (RelativeLayout) view.findViewById(R.id.binding);
		my_code = (RelativeLayout) view.findViewById(R.id.my_code);
		my_msg = (RelativeLayout) view.findViewById(R.id.my_msg);
		advice = (RelativeLayout) view.findViewById(R.id.advice);
		update = (RelativeLayout) view.findViewById(R.id.update);
		cancel_bind = (RelativeLayout) view.findViewById(R.id.cancel_bind);
		//
		binding.setOnClickListener(this);
		my_code.setOnClickListener(this);
		my_msg.setOnClickListener(this);
		advice.setOnClickListener(this);
		update.setOnClickListener(this);
		cancel_bind.setOnClickListener(this);
		//

	}

	private void showDialog() {
		cancle_bind_Dialog.show();

		Window window = cancle_bind_Dialog.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		cancle_bind_Dialog.getWindow().setContentView(
				R.layout.dialog_cancle_bind);
		cancle_bind_Dialog.getWindow().findViewById(R.id.yes)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						new SharedpreferencesUtil(getContext())
								.saveTargetId("");
						cancle_bind_Dialog.dismiss();
					}
				});
		cancle_bind_Dialog.getWindow().findViewById(R.id.no)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						cancle_bind_Dialog.dismiss();
					}
				});
		cancle_bind_Dialog.setCancelable(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_code:
			Intent i = new Intent(getActivity(), MyCodeActivity.class);
			startActivity(i);
			break;
		case R.id.binding:
			Intent intent = new Intent();
			intent.setClass(getActivity(), MipcaActivityCapture.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			break;
		case R.id.advice:
			startActivity(new Intent(getActivity(), ActivityAdvice.class));
			break;
		case R.id.cancel_bind:
			showDialog();
			break;
		case R.id.update:
			Toast.makeText(getContext(), "当前已是最新版本！", 0).show();
			break;
		case R.id.my_msg:
			Toast.makeText(getContext(), "暂无消息！", 0).show();
			break;
		default:
			break;
		}
	}

}
