package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import log.Log;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.zj.storemanag.util.Base64;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.WsUtil;
import com.zj.storemanag.util.ZipUtil;

public class GetRfidInfosService {
	
	public Object GetRfidInfos(String userID, Object[] rfids, Object retHead){
		List<String> paramKeys = new ArrayList<String>();
		List<Object> paramValue = new ArrayList<Object>();

		paramKeys.add("userID");
		paramValue.add(userID);
		SoapObject result = WsUtil.getDataByWs("IERPService", "GetRfidInfos ",
				paramKeys, paramValue);
		if (result != null) {
			return getResult(result);
		}
		return null;
	}
	
	private Object getResult(SoapObject resultSoap) {
		try {
			byte[] result = Base64.decode(resultSoap.getProperty("GetRFIDinfoResult").toString());
			String data = ZipUtil.decompress(result);
			Log.i("interface", "Get标签info返回值："+data);
			if(!StrUtil.isNotEmpty(data)){
				return new Object[]{"error","没有获取到数据"};
			}
			XmlPullParser parser = Xml.newPullParser();
			InputStream ios = new ByteArrayInputStream(data.getBytes());
			try {
				parser.setInput(ios, "UTF-8");
				int event = parser.getEventType();
				while (event != XmlPullParser.END_DOCUMENT) {
					String name = parser.getName();
					switch (event) {
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						break;
					case XmlPullParser.END_TAG:
						break;
					default:
						break;
					}
					event = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("error", e.toString());
				return new Object[]{"error","获取标签信息xml解析错误"};
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", e.getMessage());
		}
		return new Object[]{"ok","获取标签信息已完成"};
	}


}
