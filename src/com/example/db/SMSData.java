package com.example.db;

public class SMSData {
	public static int SMS_IN = 1;
	public static int SMS_OUT = 2;
	//
	private int type;
	private String num;
	private String dis_name;
	private String time;
	private String content;

	public SMSData(int type, String num, String dis_name, String time,
			String content) {
		super();
		this.type = type;
		this.num = num;
		this.dis_name = dis_name;
		this.time = time;
		this.content = content;
	}

	public SMSData() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "SMSData [type=" + type + ", num=" + num + ", dis_name="
				+ dis_name + ", time=" + time + ", content=" + content + "]";
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
