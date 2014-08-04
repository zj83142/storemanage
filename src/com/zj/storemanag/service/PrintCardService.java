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

public class PrintCardService {

	public Object printCard(String url, String methodName, String userID,
			String isProject, String xml) {
		//UserIDEncdoe={0}&isProject={1}&xml={2}
		String data = "userID="+UrlUtil.urlEncode(userID)+"&isProject="+UrlUtil.urlEncode(isProject)+"&xml="
				+ UrlUtil.urlEncode(Base64.encode(ZipUtil.compress(xml)));
		String sign = MD5Util.MD5((data + ParamsUtil.key).getBytes());
		String result = null;
		if(ParamsUtil.APP == 0){
			result = "<Toolkit><SUCCESS><SuccessCode>1</SuccessCode><Msg>打印成功，请查看打印机是否打印</Msg></SUCCESS></Toolkit>";
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
		// <Toolkit><UpdateResult><result>0</result></UpdateResult></Toolkit>
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		String flag = "error";
		String msg = "暂无数据";
		try {
			parser.setInput(ios, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (name.equalsIgnoreCase("SUCCESS")) {
						flag = "ok";
					} else if (name.equalsIgnoreCase("Msg")) {
						msg = parser.nextText();
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
		return new Object[]{flag, msg};
	}


}
