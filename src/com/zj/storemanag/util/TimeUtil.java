package com.zj.storemanag.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getSystemTimeStr() {
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}
	
	public static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

	public static String getCurrentDate() {
		Date date = new Date();
		String time = sdfDate.format(date);
		return time;
	}
	
	public static SimpleDateFormat sdfRFID = new SimpleDateFormat("yyyyMMddHHmmss");

	public static String getTempRFId() {
		Date date = new Date();
		String time = sdfRFID.format(date);
		return "T"+time;
	}
	
	public static String lastWeek() {
		Date date = new Date(System.currentTimeMillis());
		int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
		int month = Integer.parseInt(new SimpleDateFormat("MM").format(date));
		int day = Integer.parseInt(new SimpleDateFormat("dd").format(date)) - 6;
		if (day < 1) {
			month -= 1;
			if (month == 0) {
				year -= 1;
				month = 12;
			}
			if (month == 4 || month == 6 || month == 9 || month == 11) {
				day = 30 + day;
			} else if (month == 1 || month == 3 || month == 5 || month == 7
					|| month == 8 || month == 10 || month == 12) {
				day = 31 + day;
			} else if (month == 2) {
				if (year == 0 || (year % 4 == 0 && year != 0))
					day = 29 + day;
				else
					day = 28 + day;
			}
		}
		String y = year + "";
		String m = "";
		String d = "";
		if (month < 10)
			m = "0" + month;
		else
			m = month + "";
		if (day < 10)
			d = "0" + day;
		else
			d = day + "";
		return y + "-" + m + "-" + d;
	}
}
