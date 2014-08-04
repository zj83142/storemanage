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

public class UpdateRfIDService {

	public Object updateRfID(String url, String methodName, String xml) {
//		Map<String, String> paramMap = new HashMap<String, String>();
		// UrlUtil.urlEncode(Base64.encode(ZipUtil.compress(param)))
		String data = "xml="
				+ UrlUtil.urlEncode(Base64.encode(ZipUtil.compress(xml)));
		String sign = MD5Util.MD5((data + ParamsUtil.key).getBytes());
		String result = null;
		if(ParamsUtil.APP == 0){
			result = "<Toolkit><UpdateResult><result>0</result></UpdateResult></Toolkit>";
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
		// <Toolkit><SUCCESS><SuccessCode>0</SuccessCode><Msg>此物料没有进行初始化，请先初始化再更新！</Msg></SUCCESS></Toolkit>
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
					if (name.equalsIgnoreCase("error")) {
						return new Object[] { "error", parser.nextText() };
					} else if (name.equalsIgnoreCase("SuccessCode")) {
						String result = parser.nextText();
						if (result.equalsIgnoreCase("1")) {
							return new Object[] { "1", "更新成功 " };
						}else if(result.equalsIgnoreCase("0")){
							return new Object[]{"0","数据未初始化！"};
						} else if(result.equalsIgnoreCase("2")){
							return new Object[] { "2", "此标签已使用" };
						}else if(result.equalsIgnoreCase("-1")){
							return new Object[] { "-1", "没有查询该记录" };
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
