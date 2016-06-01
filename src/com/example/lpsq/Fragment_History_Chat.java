package com.example.lpsq;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.db.ChatData;
import com.example.db.DBHelper;
import com.example.utils.MyUtils;

public class Fragment_History_Chat extends Fragment implements
		OnItemClickListener {
	private ListView history_chat_list;
	private ArrayList<ChatData> local_data;
	private Context context;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.history_chat, null);
		init(view);
		System.out.println("oncr fragment chat");
		local_data = getData();
		history_chat_list.setAdapter(new MyAdapter(context));
		history_chat_list.setOnItemClickListener(this);
		return view;
	}

	private void init(View view) {
		context=getContext();
		history_chat_list = (ListView) view
				.findViewById(R.id.history_chat_list);
		local_data = new ArrayList<ChatData>();
	}

	private ArrayList<ChatData> getData() {
		DBHelper dbHelper = new DBHelper(getActivity().getApplicationContext(),
				"lpsq", null, 1);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from CHATDATA", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String str_chat = cursor.getString(cursor.getColumnIndex("type"))
					+ "#" + cursor.getString(cursor.getColumnIndex("time"))
					+ "#" + cursor.getString(cursor.getColumnIndex("content"));
			System.out.println("chat list``" + str_chat);
			ChatData cd = new ChatData(cursor.getString(cursor
					.getColumnIndex("type")), cursor.getString(cursor
					.getColumnIndex("time")), cursor.getString(cursor
					.getColumnIndex("content")));
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
				convertView = mInflater.inflate(R.layout.list_chat, null);
				viewHolder = new ViewHolder();
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.time);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			ChatData cd = local_data.get(position);
			String text = "\t"
					+ MyUtils.long2StringTime(Long.valueOf(cd.getTime()))
					+ cd.getType() + "了留言：" + cd.getContent();
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
