package com.jarylan.drawgraph.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class Tools {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static float dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return dpValue * scale + 0.5f;
	}
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	 */
	public static float px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return pxValue / scale + 0.5f;
	}

	/**
	 * 获取系统时间
	 *
	 * @return
	 */
	public static String getNowTime() {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date(System.currentTimeMillis());
		String str = sp.format(d);
		return str;
	}
}