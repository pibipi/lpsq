package com.example.lpsq;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.db.DBHelper;
import com.example.db.PhoneData;
import com.example.db.SMSData;
import com.example.utils.MyUtils;

public class SMSHistoryActivity extends Activity implements OnItemClickListener {
	private ListView history_sms_list;
	private ArrayList<SMSData> local_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_sms);
		init();
		local_data = getData();
		history_sms_list.setAdapter(new MyAdapter(getApplicationContext()));
		history_sms_list.setOnItemClickListener(this);
	}

	private void init() {
		history_sms_list = (ListView) findViewById(R.id.history_sms_list);
		local_data = new ArrayList<SMSData>();
	}

	private ArrayList<SMSData> getData() {
		DBHelper dbHelper = new DBHelper(getApplicationContext(), "lpsq", null,
				1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from SMSDATA", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String str_sms = Integer.valueOf(cursor.getInt(cursor
					.getColumnIndex("type")))
					+ "#"
					+ cursor.getString(cursor.getColumnIndex("num"))
					+ "#"
					+ cursor.getString(cursor.getColumnIndex("dis_name"))
					+ "#"
					+ cursor.getString(cursor.getColumnIndex("time"))
					+ "#"
					+ cursor.getString(cursor.getColumnIndex("content"));
			System.out.println("sms list``" + str_sms);
			SMSData sd = new SMSData(Integer.valueOf(cursor.getInt(cursor
					.getColumnIndex("type"))), cursor.getString(cursor
					.getColumnIndex("num")), cursor.getString(cursor
					.getColumnIndex("dis_name")), cursor.getString(cursor
					.getColumnIndex("time")), cursor.getString(cursor
					.getColumnIndex("content")));
			local_data.add(sd);

			cursor.moveToNext();
		}
		cursor.close();
		return local_data;
	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return local_data.size();
		}

		@Override
		public Object getItem(int position) {
			return local_data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_sms, null);
				viewHolder = new ViewHolder();
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.time);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			SMSData sd = local_data.get(position);
			String type_text = "";
			if (sd.getType() == 1) {
				type_text = "收到";
			} else {
				type_text = "发出";
			}
			String text = "\tTA在"
					+ MyUtils.long2StringTime(Long.valueOf(sd.getTime()))
					+ type_text + "了" + sd.getNum() + "(昵称：" + sd.getDis_name()
					+ ")短信，内容为：" + sd.getContent();
			viewHolder.time.setText(text + "");

			return convertView;
		}
	}

	private static class ViewHolder {
		private TextView time;

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		System.out.println("dianji" + position);
	}
}