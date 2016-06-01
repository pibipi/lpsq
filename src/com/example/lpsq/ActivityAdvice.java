package com.example.lpsq;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.utils.MyUtils;

public class ActivityAdvice extends Activity implements OnClickListener {
	private ImageView back;
	private EditText advice;
	private EditText contact;
	private Button confirm;
	private Handler mHandler;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advice);
		init();
		contact.setText(MyUtils.getNativePhoneNumber(getApplicationContext()));
	}

	private void init() {
		context=getApplicationContext();
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
		back = (ImageView) findViewById(R.id.back);
		advice = (EditText) findViewById(R.id.advice);
		contact = (EditText) findViewById(R.id.contact);
		confirm = (Button) findViewById(R.id.confirm);
		confirm.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			final String advice_str = advice.getText().toString();
			String contact_str_t = contact.getText().toString();
			if (contact_str_t.equals("")) {
				contact_str_t = "null";
			}
			final String contact_str = contact_str_t;
			if (advice_str.equals("")) {
				Toast.makeText(getApplicationContext(), "请输入意见或建议", 0).show();
			} else {
				new Thread(new Runnable() {
					@Override
					public void run() {

						String uriAPI = "http://qiji.betteridea.cn/face/facesendadvice/?phone=lpsq&qq_email="
								+ contact_str + "&advice=" + advice_str;
						HttpGet httpRequest = new HttpGet(uriAPI);
						HttpResponse httpResponse;
						try {
							httpResponse = new DefaultHttpClient()
									.execute(httpRequest);
							if (httpResponse.getStatusLine().getStatusCode() == 200) {
								Message msg = new Message();
								msg.what = 200;
								mHandler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = 400;
								mHandler.sendMessage(msg);
							}
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}).start();
			}
			break;
		case R.id.back:
			this.finish();
			break;
		default:
			break;
		}
	}
}
