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

public class UploadService {

	public Object upload(String url, String methodName, String userID,
			String xml) {

		String data = "userID=" + UrlUtil.urlEncode(userID) + "&xml="
				+ UrlUtil.urlEncode(Base64.encode(ZipUtil.compress(xml)));
		String sign = MD5Util.MD5((data + ParamsUtil.key).getBytes());
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><GOODSMVT_RESULT><CODE>ok</CODE><MSG>出库成功</MSG><MATCODE>20140402</MATCODE><MATYEAR>2014</MATYEAR></GOODSMVT_RESULT></Toolkit>";
		} else {
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
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		boolean isError = false;
		Object[] obj = null;
		try {
			parser.setInput(ios, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if(name.equalsIgnoreCase("ErrorCode")){
						isError = true;
					}else if (name.equalsIgnoreCase("GOODSMVT_RESULT")) {
						obj = new Object[4];
					} else if (name.equalsIgnoreCase("MATYEAR")) {
						// 凭证年
						obj[3] = StrUtil.filterStr(parser.nextText());
					} else if (name.equalsIgnoreCase("MATCODE")) {
						// 凭证号
						obj[2] = StrUtil.filterStr(parser.nextText());
					} else if (name.equalsIgnoreCase("MSG")) {
						if(isError){
							return new Object[]{"error","数据提交错误","",""};
						}
						obj[1] = StrUtil.filterStr(parser.nextText());
					} else if (name.equalsIgnoreCase("CODE")) {
						obj[0] = StrUtil.filterStr(parser.nextText());
					}
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
			return new Object[] { "error", "xml解析错误" };
		}
		return obj;
	}
}
