package com.example.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("creat table6");
		String sql1 = "create table if not exists "
				+ " SMSDATA "
				+ " (id integer primary key AUTOINCREMENT ,  type varchar(50), num varchar(50),dis_name varchar(50),time varchar(50),content varchar(50))";
		String sql2 = "create table if not exists "
				+ " NOTSMSDATA "
				+ " (id integer primary key AUTOINCREMENT ,  type varchar(50), num varchar(50),dis_name varchar(50),time varchar(50),content varchar(50))";
		String sql3 = "create table if not exists "
				+ " PHONEDATA "
				+ " (id integer primary key AUTOINCREMENT ,  type varchar(50), num varchar(50),dis_name varchar(50),time varchar(50), _time varchar(50))";
		String sql4 = "create table if not exists "
				+ " NOTPHONEDATA "
				+ " (id integer primary key AUTOINCREMENT ,  type varchar(50), num varchar(50),dis_name varchar(50),time varchar(50), _time varchar(50))";
		String sql5 = "create table if not exists "
				+ " LOCATIONDATA "
				+ " (id integer primary key AUTOINCREMENT , time varchar(50) , lon varchar(50) , lat varchar(50) , loc varchar(50))";
		String sql6 = "create table if not exists "
				+ " CHATDATA "
				+ " (id integer primary key AUTOINCREMENT , type varchar(50) , time varchar(50) , content varchar(50))";
		String sql7 = "create table if not exists "
				+ " ADDRESSDATA "
				+ " (id integer primary key AUTOINCREMENT , name varchar(50) , phone varchar(50) )";
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.execSQL(sql4);
		db.execSQL(sql5);
		db.execSQL(sql6);
		db.execSQL(sql7);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void insert_smsdata(SMSData data, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put("type", data.getType() + "");
		cv.put("num", data.getNum());
		cv.put("dis_name", data.getDis_name());
		cv.put("time", data.getTime());
		cv.put("content", data.getContent());
		long row = db.insert("SMSDATA", null, cv);
		db.close();
	}

	public void insert_not_smsdata(SMSData data, SQLiteDatabase db) {
		Cursor cursor = db.rawQuery("select * from NOTSMSDATA where time = ? ",
				new String[] { data.getTime() });
		if (cursor.getCount() == 0) {
			ContentValues cv = new ContentValues();
			cv.put("type", data.getType() + "");
			cv.put("num", data.getNum());
			cv.put("dis_name", data.getDis_name());
			cv.put("time", data.getTime());
			cv.put("content", data.getContent());
			long row = db.insert("NOTSMSDATA", null, cv);
		}
		db.close();
	}

	public void delete_not_smsdata(SMSData data, SQLiteDatabase db) {
		int row = db.delete("NOTSMSDATA", " time = ? ",
				new String[] { data.getTime() });
		System.out.println("delete_not_smsdata" + row);
		db.close();
	}

	public void insert_phonedata(PhoneData data, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put("type", data.getType() + "");
		cv.put("num", data.getNum());
		cv.put("dis_name", data.getDis_name());
		cv.put("time", data.getTime());
		cv.put("_time", data.get_time());
		long row = db.insert("PHONEDATA", null, cv);
		System.out.println("insert_phonedata" + row);
		db.close();
	}

	public void insert_not_phonedata(PhoneData data, SQLiteDatabase db) {
		Cursor cursor = db.rawQuery(
				"select * from NOTPHONEDATA where time = ? ",
				new String[] { data.getTime() });
		if (cursor.getCount() == 0) {
			ContentValues cv = new ContentValues();
			cv.put("type", data.getType() + "");
			cv.put("num", data.getNum());
			cv.put("dis_name", data.getDis_name());
			cv.put("time", data.getTime());
			cv.put("_time", data.get_time());
			long row = db.insert("NOTPHONEDATA", null, cv);
			System.out.println("insert_not_phonedata" + row);
		}
		db.close();
	}

	public void delete_not_phonedata(PhoneData data, SQLiteDatabase db) {
		int row = db.delete("NOTPHONEDATA", " time = ? ",
				new String[] { data.getTime() });
		System.out.println("delete_not_phonedata" + row);
		db.close();
	}

	public void insert_locationdata(LocData data, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put("time", data.getTime());
		cv.put("lon", data.getLon() + "");
		cv.put("lat", data.getLat() + "");
		cv.put("loc", data.getLoc() + "");
		long row = db.insert("LOCATIONDATA", null, cv);
		System.out.println("insert_locationdata" + row);
		db.close();
	}

	public void update_locloc(LocData data, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put("time", data.getTime());
		cv.put("lon", data.getLon() + "");
		cv.put("lat", data.getLat() + "");
		cv.put("loc", data.getLoc() + "");
		db.update("LOCATIONDATA", cv, " time = ? ",
				new String[] { data.getTime() });
		// String sql = "update LOCATIONDATA set loc = " + data.getLoc()
		// + " where time = " + data.getTime();
		// db.execSQL(sql);
		db.close();
	}

	public void insert_chatdata(String type, String time, String content,
			SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put("type", type);
		cv.put("time", time);
		cv.put("content", content);
		long row = db.insert("CHATDATA", null, cv);
		db.close();
	}

	public void update_addressdata(AddressData data, SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put("name", data.getName());
		cv.put("phone", data.getPhone() + "");
		long row = db.insert("ADDRESSDATA", null, cv);
	}
}
