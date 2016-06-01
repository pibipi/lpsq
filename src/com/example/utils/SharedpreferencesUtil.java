package com.example.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedpreferencesUtil {
	private Context context;
	private String NAME = "LPSQ";
	private String REGID = "REGID";

	public SharedpreferencesUtil(Context context) {
		super();
		this.context = context;
	}

	public void saveTargetId(String id) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(REGID, id);
		editor.commit();
	}

	public String getTargetId() {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				Context.MODE_PRIVATE);
		String id = preferences.getString(REGID, "");
		return id;

	}
}
