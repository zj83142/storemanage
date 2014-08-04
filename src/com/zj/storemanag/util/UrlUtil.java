package com.zj.storemanag.util;

import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import log.Log;

public final class UrlUtil {

	public static final int SIZEID = 10;

	public static String urlEncode(String url) {
		try {
			return URLEncoder.encode(url, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("zj","urlEncode:" + e.toString());
		}
		return "";
	}

	public static String encodeRequestParam(Map<String, String> paramMap) {
		StringBuilder parambuilder = new StringBuilder("");
		try {
			if (paramMap != null && !paramMap.isEmpty()) {
				for (Map.Entry<String, String> entry : paramMap.entrySet()) {
					parambuilder
							.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), "UTF-8"))
							.append("&");
				}
				parambuilder.deleteCharAt(parambuilder.length() - 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("zj","encodeRequestParam:" + e.toString());
		}
		return parambuilder.toString();
	}

	public static String safeXml(String data) {
		data = StrUtil.filterStr(data);
		if (StrUtil.isNotEmpty(data)) {
			data = data.replaceAll("&", "&amp;");
			data = data.replaceAll("<", "&lt;");
			data = data.replaceAll(">", "&gt;");
			data = data.replaceAll("\"", "&quot;");
			data = data.replaceAll("'", "&apos;");
			return data;
		}
		return data;
	}
	
	/**判断输入的url是否正确*/
	public static boolean isUrl(String str){
		String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]" ;
		Pattern patt = Pattern. compile(regex );
		Matcher matcher = patt.matcher(str);
		boolean isMatch = matcher.matches();
		System.out.println( "判断输入的URL地址不正确："+isMatch );
		return isMatch;
	}
	
	private static String nodeInfos = "<%1$s>%2$s</%3$s>";

	public static String getNodeInfos() {
		return nodeInfos;
	}

	private static String nodeInfo = "<%1$s/>";

	public static String getNodeInfo() {
		return nodeInfo;
	}

	/**
	 * 根据字段，拼接该字段对应的xml节点
	 * 
	 * @param info
	 * @return
	 * 
	 *         例子一： 如：传入EP_NAME=计划 返回<EP_NAME>计划</EP_NAME>
	 * 
	 *         例子二： 如：传入EP_NAME= 返回<EP_NAME/>
	 */
	public static String manageNode(String info) {
		String result = "";
		if (info != null) {
			String[] infos = info.split("=");
			if (infos != null) {
				if (infos.length == 2) {
					result = String.format(getNodeInfos(), infos[0],
							infos[1], infos[0]);
				} else if (infos.length == 1) {
					result = String.format(getNodeInfo(), infos[0]);
				}
			}
		}
		return result;
	}

}
