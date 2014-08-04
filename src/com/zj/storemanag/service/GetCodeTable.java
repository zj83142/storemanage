package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.zj.storemanag.bean.BaseInfo;
import com.zj.storemanag.util.StrUtil;

public class GetCodeTable {
	
	public Object getCodeTable(String url,String methodName, String tableName){
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tableName", tableName);
		// String result = RequestService.postRequest(url, methodName,UrlUtil.encodeRequestParam(paramMap));
		String result = "<Toolkit><CD_EDUCATION><CODE_VALUE>01</CODE_VALUE><CODE_NAME>小学</CODE_NAME></CD_EDUCATION><CD_EDUCATION><CODE_VALUE>02</CODE_VALUE><CODE_NAME>初中</CODE_NAME></CD_EDUCATION><CD_EDUCATION><CODE_VALUE>03</CODE_VALUE><CODE_NAME>高中</CODE_NAME></CD_EDUCATION><CD_EDUCATION><CODE_VALUE>04</CODE_VALUE><CODE_NAME>中专</CODE_NAME></CD_EDUCATION></Toolkit>";
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
		List<BaseInfo> list = new ArrayList<BaseInfo>();
		BaseInfo info = null;
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
					} else if (name.equalsIgnoreCase("LGORTS")) {
						info = new BaseInfo();
						info.setType("1");
					} else if (name.equalsIgnoreCase("CD_EDUCATION")) {
						info = new BaseInfo();
					} else if (name.equalsIgnoreCase("CODE_VALUE")) {
						info.setValue(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("CODE_NAME")) {
						info.setName(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("Toolkit")) {
						return new Object[] { "ok", list };
					} else if (name.equalsIgnoreCase("CD_EDUCATION")) {
						list.add(info);
						info = null;
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
