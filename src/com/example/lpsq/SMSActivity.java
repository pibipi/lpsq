package com.example.lpsq;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.utils.MyUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SMSActivity extends Activity implements OnClickListener {
	private TextView type;
	private TextView num;
	private TextView name;
	private TextView time;
	private TextView content;
	private ImageView close;
	private Button history_data;
	private TextView textview;
	private String text = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_sms);
		init();
		String sms = getIntent().getStringExtra("sms");
		if (sms != null) {
			String[] str = sms.split("#");
			// 短信类型
			if (Integer.valueOf(str[0]) == 1) {
				type.append("收到");
			} else if (Integer.valueOf(str[0]) == 2) {
				type.append("发送");
			}
			// 短信号码
			num.append(str[1] + "");
			// 通讯录昵称
			name.append(str[2] + "");
			// 短信时间
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.valueOf(str[3]));
			time.append(format.format(calendar.getTime()));
			// 短信内容
			content.append(str[4]);
			//

			text = "\tTA在" + MyUtils.long2StringTime(Long.valueOf(str[3]))
					+ type.getText().toString() + "了" + str[1] + "(昵称："
					+ str[2] + ")短信，内容为：" + str[4];
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
		time = (TextView) findViewById(R.id.time);
		content = (TextView) findViewById(R.id.content);
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
			Intent i = new Intent(SMSActivity.this, MainActivity2.class);
			i.putExtra("page", 3);
			i.putExtra("page_2", 1);
			startActivity(i);
			break;
		default:
			break;
		}
	}
}
