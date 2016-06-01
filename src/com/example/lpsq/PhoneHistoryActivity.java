package com.example.lpsq;

import java.util.ArrayList;

import com.example.db.DBHelper;
import com.example.db.GeneralData;
import com.example.db.PhoneData;
import com.example.utils.MyUtils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PhoneHistoryActivity extends Activity implements
		OnItemClickListener {
	private ListView history_phone_list;
	private ArrayList<PhoneData> local_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_phone);
		init();
		local_data = getData();
		history_phone_list.setAdapter(new MyAdapter(getApplicationContext()));
		history_phone_list.setOnItemClickListener(this);
	}

	private void init() {
		history_phone_list = (ListView) findViewById(R.id.history_phone_list);
		local_data = new ArrayList<PhoneData>();
	}

	private ArrayList<PhoneData> getData() {
		DBHelper dbHelper = new DBHelper(getApplicationContext(), "lpsq", null,
				1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from PHONEDATA", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String str_phone = Integer.valueOf(cursor.getInt(cursor
					.getColumnIndex("type")))
					+ "#"
					+ cursor.getString(cursor.getColumnIndex("num"))
					+ "#"
					+ cursor.getString(cursor.getColumnIndex("dis_name"))
					+ "#"
					+ cursor.getString(cursor.getColumnIndex("_time"))
					+ "#"
					+ cursor.getString(cursor.getColumnIndex("time"));
			System.out.println("phone list``" + str_phone);
			PhoneData pd = new PhoneData(Integer.valueOf(cursor.getInt(cursor
					.getColumnIndex("type"))), cursor.getString(cursor
					.getColumnIndex("num")), cursor.getString(cursor
					.getColumnIndex("dis_name")), cursor.getString(cursor
					.getColumnIndex("time")), cursor.getString(cursor
					.getColumnIndex("_time")));
			local_data.add(pd);

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
				convertView = mInflater.inflate(R.layout.list_phone, null);
				viewHolder = new ViewHolder();
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.time);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			PhoneData fd = local_data.get(position);
			String type_text = "";
			if (fd.getType() == 1) {
				type_text = "接入";
			} else {
				type_text = "拨出";
			}
			String text = "\tTA在"
					+ MyUtils.long2StringTime(Long.valueOf(fd.getTime()))
					+ type_text + "了" + fd.getNum() + "(昵称：" + fd.getDis_name()
					+ ")，" + type_text + "时间"
					+ (int) (Float.valueOf(fd.get_time()) / 1000) + "秒";
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
