package com.example.db;

public class PhoneData {

	// type varchar(50), num integer,dis_name varchar(50),time
	// varchar(50),content varchar(50))
	public static int PHONE_IN = 1;
	public static int PHONE_OUT = 2;
	//
	private int type;
	private String num;
	private String dis_name;
	private String time;
	private String _time;

	public PhoneData(int type, String num, String dis_name, String time,
			String _time) {
		super();
		this.type = type;
		this.num = num;
		this.dis_name = dis_name;
		this.time = time;
		this._time = _time;
	}

	public PhoneData() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SMSData [type=" + type + ", num=" + num + ", dis_name="
				+ dis_name + ", time=" + time + ", _time=" + _time + "]";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getDis_name() {
		return dis_name;
	}

	public void setDis_name(String dis_name) {
		this.dis_name = dis_name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String get_time() {
		return _time;
	}

	public void set_time(String _time) {
		this._time = _time;
	}

}
