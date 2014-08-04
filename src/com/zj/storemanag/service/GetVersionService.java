package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.zj.storemanag.bean.CVersion;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class GetVersionService {

	public Object getVersion(String url, String methodName, String userID,  String version) {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userID", userID);
		paramMap.put("version", version);
		String result = RequestService.postRequest(url, methodName, UrlUtil.encodeRequestParam(paramMap));
		if (StrUtil.isNotEmpty(result)) {
			return parseXmlUpdateVersion(result);
		}
		return null;
	}
	
	/** 解析更新接口返回数据 */
	public Object parseXmlUpdateVersion(String str) {
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		XmlPullParser parser = Xml.newPullParser();
		CVersion cVersion = null;
		try {
			parser.setInput(ios, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (name.equals("HS_APK_UPDATE")) {
						cVersion = new CVersion();
					} else if (cVersion != null) {
						if (name.equalsIgnoreCase("HAU_FILE_PATH")) {
							cVersion.setUrlStr(parser.nextText());
						} else if (name.equalsIgnoreCase("HAU_NAME")) {
							cVersion.setVersionName(parser.nextText());
						} else if (name.equalsIgnoreCase("HAU_VERSION")) {
							cVersion.setVersionNum(parser.nextText());
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
			Log.e("zj", "解析检测版本更新接口报错"+e.getMessage());
		}
		return cVersion;
	}

}
