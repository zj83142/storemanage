package com.zj.storemanag.util;

public class StrUtil {

	/** 判断传入的字符串是否为空,为空返回true,不为空返回false */
	public static boolean isNotEmpty(String str) {
		if (str != null && !str.equalsIgnoreCase("null") && str.length() > 0
				&& !str.trim().equals("")) {
			return true;
		}
		return false;
	}

	// 验证是否为数字
	public static boolean isNumber(String str) {
		if(!StrUtil.isNotEmpty(str)) return false;
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("[0-9,a-z,A-Z]*");
		java.util.regex.Matcher match = pattern.matcher(str);
		return match.matches();
	}

	/** 过滤string */
	public static String filterStr(String str) {
		if (isNotEmpty(str))
			return str.trim();
		return "";
	}

	public static boolean isIntegerOrDouble(String value) {
		if (!isNotEmpty(value)) {
			return false;
		}
		return isInteger(value) || isDouble(value);
	}

	/*** 判断字符串是否是整数 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/*** 判断字符串是否是浮点数 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/** 组合字符串类型 value+"_"+name */
	public static String comStr(String name, String value) {
		if (isNotEmpty(value)) {
			name = value + "_" + filterStr(name);
		}
		if (name.endsWith("_")) {
			name = name.substring(0, name.length() - 1);
		}
		return name;
	}

	public static String slipName(String value) {
		if (isNotEmpty(value)) {
			int index = value.indexOf("_");
			if (index != -1) {
				return value.substring(index + 1);
			} else {
				return value;
			}
		}
		return "";
	}
	
	public static String slipValue(String value) {
		if (isNotEmpty(value)) {
			int index = value.indexOf("_");
			if (index != -1) {
				return value.substring(0, index);
			} else {
				return value;
			}
		}
		return "";
	}
}
