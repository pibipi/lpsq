package com.example.db;

public class GeneralData {
	public static final int TYPE_LOCATION = 1;// 位置信息
	public static final int TYPE_REQUEST_LOCATION = 2;// 请求位置
	public static final int TYPE_SMS = 3;// 短信信息
	public static final int TYPE_PHONE = 4;// 电话信息
	public static final int TYPE_CHAT_MESSAGE = 5;// 普通聊天信息
	public static final int TYPE_ADDRESS = 6;// 请求获取通讯录
	public static final int TYPE_REQUEST_ADDRESS = 7;// 请求获取通讯录
	//
	private String alert;
	private String content;
	private int type;
	private String id;

	public GeneralData(String alert, String content, int type, String id) {
		super();
		this.alert = alert;
		this.content = content;
		this.type = type;
		this.id = id;
	}

	@Override
	public String toString() {
		return "GeneralData [alert=" + alert + ", content=" + content
				+ ", type=" + type + ", id=" + id + "]";
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
