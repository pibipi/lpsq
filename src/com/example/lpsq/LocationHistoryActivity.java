package com.example.lpsq;

import java.util.ArrayList;

import com.example.db.ChatData;
import com.example.db.DBHelper;
import com.example.db.GeneralData;
import com.example.db.LocData;
import com.example.db.PhoneData;
import com.example.utils.MyUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class LocationHistoryActivity extends Activity implements
		OnItemClickListener {
	private ListView history_loc_list;
	private ArrayList<LocData> local_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_loc);
		init();
		local_data = getData();
		history_loc_list.setAdapter(new MyAdapter(getApplicationContext()));
		history_loc_list.setOnItemClickListener(this);
	}

	private void init() {
		history_loc_list = (ListView) findViewById(R.id.history_loc_list);
		local_data = new ArrayList<LocData>();
	}

	private ArrayList<LocData> getData() {
		DBHelper dbHelper = new DBHelper(getApplicationContext(), "lpsq", null,
				1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from LOCATIONDATA", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String str_loc = cursor.getString(cursor.getColumnIndex("time"))
					+ "#" + cursor.getString(cursor.getColumnIndex("lon"))
					+ "#" + cursor.getString(cursor.getColumnIndex("lat"))
					+ "#" + cursor.getString(cursor.getColumnIndex("loc"));
			System.out.println("loc list``" + str_loc);
			LocData cd = new LocData(Double.valueOf(cursor.getString(cursor
					.getColumnIndex("lon"))), Double.valueOf(cursor
					.getString(cursor.getColumnIndex("lat"))),
					cursor.getString(cursor.getColumnIndex("time")),
					cursor.getString(cursor.getColumnIndex("loc")));
			local_data.add(cd);

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
				convertView = mInflater.inflate(R.layout.list_loc, null);
				viewHolder = new ViewHolder();
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.time);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			LocData ld = local_data.get(position);
			String text = "\t"
					+ MyUtils.long2StringTime(Long.valueOf(ld.getTime()))
					+ "获取到位置信息：" + ld.getLoc();
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
		System.out.println("dianji" + position);
		Intent i = new Intent(LocationHistoryActivity.this, LocationMapActivity.class);
		i.putExtra("lon", local_data.get(position).getLon());
		i.putExtra("lat", local_data.get(position).getLat());
		i.putExtra("time", local_data.get(position).getTime());
		i.putExtra("loc", local_data.get(position).getLoc());
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		this.finish();
	}
}
