package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.util.Base64;
import com.zj.storemanag.util.MD5Util;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.util.ZipUtil;

public class TempRfIDService {

	public Object tempRfID(String url, String methodName,String userID, String xml) {
//		Map<String, String> paramMap = new HashMap<String, String>();
		// UrlUtil.urlEncode(Base64.encode(ZipUtil.compress(param)))
		String data = "userID=" + userID +"&xml="
				+ UrlUtil.urlEncode(Base64.encode(ZipUtil.compress(xml)));
		String sign = MD5Util.MD5((data + ParamsUtil.key).getBytes());
		String result = null;
		if(ParamsUtil.APP == 0){
			result = "<Toolkit><RFID><PR_RFID_NO>T1405291448350001</PR_RFID_NO></RFID></Toolkit>";
		}else{
			result = RequestService.postRequest(url, methodName, sign, data);
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
		//<Toolkit><RFID><PR_RFID_NO>6ca5c3dbf37d43edbf6c45bf60698eb8</PR_RFID_NO></RFID></Toolkit>
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
					} else if (name.equalsIgnoreCase("Column1")||name.equalsIgnoreCase("PR_RFID_NO")) {
						String result = StrUtil.filterStr(parser.nextText());
						if (StrUtil.isNotEmpty(result)) {
							return new Object[] { "ok", result };
						} else {
							return new Object[] { "error", "更新失败！" };
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
