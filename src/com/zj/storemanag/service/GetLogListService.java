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

import com.zj.storemanag.bean.HsLog;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class GetLogListService {

	public Object getLogList(String url, String methodName,
			String userID, String logType, String summer, String startDT,
			String endDT) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userID", userID);
		paramMap.put("logType", logType);
		paramMap.put("summer", summer);
		paramMap.put("startDT", startDT);
		paramMap.put("endDT", endDT);
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><HS_LOG><HS_DATE>2014-04-02 17:52:12</HS_DATE><HS_ID>1</HS_ID><HS_USER/><HS_TYPE>外部采购备件入库</HS_TYPE><HS_SUMMER>外部采购</HS_SUMMER><HS_DETAIL>外部采购订单错误</HS_DETAIL></HS_LOG><HS_LOG><HS_DATE>2014-04-02 17:52:12</HS_DATE><HS_ID>2</HS_ID><HS_USER/><HS_TYPE>外部采购备件入库</HS_TYPE><HS_SUMMER>外部采购</HS_SUMMER><HS_DETAIL>外部采购订单错误</HS_DETAIL></HS_LOG></Toolkit>";
		}else{
			result = RequestService.postRequest(url, methodName, UrlUtil.encodeRequestParam(paramMap));
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
		List<HsLog> list = new ArrayList<HsLog>();
		HsLog hsLog = null;
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
					} else if (name.equalsIgnoreCase("Table")) {
						hsLog = new HsLog();
					} else if (name.equalsIgnoreCase("HS_ID")) {
						hsLog.setHS_ID(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("HS_DATE")) {
						String date = parser.nextText();
						if(StrUtil.isNotEmpty(date)&& date.length()>19){
							date = date.substring(0, 19).replace( "T", " ");
							hsLog.setHS_DATE(StrUtil.filterStr(date));
						}
					} else if (name.equalsIgnoreCase("HS_USER")) {
						hsLog.setHS_USER(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("HS_TYPE")) {
						hsLog.setHS_TYPE(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("HS_SUMMER")) {
						hsLog.setHS_SUMMER(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("HS_DETAIL")) {
						hsLog.setHS_DETAIL(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("Toolkit")) {
						return new Object[] { "ok", list };
					} else if (name.equalsIgnoreCase("Table")) {
						list.add(hsLog);
						hsLog = null;
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
