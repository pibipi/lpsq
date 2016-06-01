package com.example.lpsq;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.utils.MyUtils;

import android.R.color;
import android.app.Activity;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneActivity extends Activity implements OnClickListener {
	private TextView type;
	private TextView num;
	private TextView name;
	private TextView _time;
	private TextView time;
	private ImageView close;
	private Button history_data;
	private TextView textview;
	private String text = "";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_phone);
		init();
		String phone = getIntent().getStringExtra("phone");
		if (phone != null) {
			String[] str = phone.split("#");
			// 通话类型
			if (Integer.valueOf(str[0]) == 1) {
				type.append("接入");
			} else if (Integer.valueOf(str[0]) == 2) {
				type.append("拨出");
			}
			// 通话号码
			num.append(str[1] + "");
			// 通讯录昵称
			name.append(str[2] + "");
			// 通话时长
			_time.append(Float.valueOf(str[3]) / 1000f + "秒");
			// 通话时间
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.valueOf(str[4]));
			time.append(format.format(calendar.getTime()));
			text = "\tTA在" + MyUtils.long2StringTime(Long.valueOf(str[4]))
					+ type.getText().toString() + "了" + str[1] + "(昵称："
					+ str[2] + ")，" + type.getText().toString() + "时间"
					+ (int) (Float.valueOf(str[3]) / 1000) + "秒";
		} else {
			text = getIntent().getStringExtra("text");
			history_data.setVisibility(View.INVISIBLE);
		}
		textview.setText(text);
	}

	private void init() {
		textview = (TextView) findViewById(R.id.textview);
		type = (TextView) findViewById(R.id.type);
		num = (TextView) findViewById(R.id.num);
		name = (TextView) findViewById(R.id.name);
		_time = (TextView) findViewById(R.id.time);
		time = (TextView) findViewById(R.id.content);
		close = (ImageView) findViewById(R.id.close);
		history_data = (Button) findViewById(R.id.history_data);

		close.setOnClickListener(this);
		history_data.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close:
			finish();
			break;
		case R.id.history_data:
			Intent i = new Intent(PhoneActivity.this, MainActivity2.class);
			i.putExtra("page", 3);
			i.putExtra("page_2", 1);
			startActivity(i);
			break;
		default:
			break;
		}
	}

}
