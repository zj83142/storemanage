package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class InitRFIDService {

	public Object initRFID(String url, String methodName, String xml) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("xml", xml);
		// String result = RequestService.postRequest(url,
		// methodName,UrlUtil.encodeRequestParam(paramMap));
		String result = null;
		if(ParamsUtil.APP == 0){
			result = "<Toolkit><InitResult><result>0</result></InitResult></Toolkit>";
		}else{
			result = RequestService.postRequest(url, methodName,UrlUtil.encodeRequestParam(paramMap));
		}
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
					} else if (name.equalsIgnoreCase("result")) {
						String result = parser.nextText();
						if(result.equalsIgnoreCase("0")){
							result = "初始化成功";
						}else if(result.equalsIgnoreCase("1")){
							result = "之前已经初始，不能重复";
						}else if(result.equalsIgnoreCase("2")){
							result = "此标签已经使用";
						}
						return new Object[]{ "ok", result };
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
