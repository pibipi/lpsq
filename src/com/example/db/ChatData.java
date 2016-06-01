package com.example.db;

public class ChatData {
	private String type;
	private String time;
	private String content;

	public ChatData(String type, String time, String content) {
		super();
		this.type = type;
		this.time = time;
		this.content = content;
	}

	@Override
	public String toString() {
		return "ChatData [type=" + type + ", time=" + time + ", content="
				+ content + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
