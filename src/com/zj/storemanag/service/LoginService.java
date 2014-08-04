package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.zj.storemanag.bean.User;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class LoginService {

	/** 登录 */
	public Object login(String url, String methodName, String userName,
			String pwd) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("password", pwd);
		paramMap.put("userName", userName);
		String result = null;
		if(ParamsUtil.APP == 0){
			result = "<Toolkit><UR_USERS><USERID>1</USERID><USERIDENCDOE>GWiXxqkVUj7rr46LY6HaEg==</USERIDENCDOE><USERNAME>sa</USERNAME></UR_USERS></Toolkit>";
		}else{
			 result = RequestService.postRequest(url, methodName, UrlUtil.encodeRequestParam(paramMap));
		}
		Log.i("zj", "登录:  接口返回值：  " + result);
		if (StrUtil.isNotEmpty(result)) {
			if (result.equalsIgnoreCase("sign error")) {
				return new Object[] { "error", "签名不一致" };
			}
			return parserResult(result);
		} else {
			return new Object[] { "error", "当前网络不稳定，数据请求失败，请稍后重试！" };
		}
	}

	private Object parserResult(String str) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		User user = null;
		try {
			parser.setInput(ios, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (name.equalsIgnoreCase("Msg")) {
						return new Object[] { "error", parser.nextText() };
					} else if (name.equalsIgnoreCase("UR_USERS")) {
						user = new User();
					} else if (name.equalsIgnoreCase("USERID")) {
						user.setUserId(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("USERIDENCDOE")) {
						user.setUserIdMD5(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("USERNAME")) {
						user.setUserName(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("UR_USERS")) {
						if (user != null) {
							return new Object[] { "ok", user };
						}
					}
					break;
				default:
					break;
				}
				event = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("error", e.toString());
			return new Object[] { "error", "xml解析错误" };
		}
		return null;

	}

}
