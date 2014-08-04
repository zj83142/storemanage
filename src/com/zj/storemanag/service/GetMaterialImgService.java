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


public class GetMaterialImgService {

	public Object getMaterialImg(String url, String methodName,String userId, String material,
			String factory, String store) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userID", userId);
		paramMap.put("material", material);
		paramMap.put("plant", factory);
		paramMap.put("stgeLoc", store);
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><MATERIALIMAGE><PR_IMAGE_NAME>test.jpg</PR_IMAGE_NAME><PR_IMAGE_SIZE>2245272</PR_IMAGE_SIZE><PR_IMAGE_TYPE>jpg</PR_IMAGE_TYPE><PR_IMAGE_FILE_PATH>../Upload/UploadFiles/1500_1501/test.jpg</PR_IMAGE_FILE_PATH></MATERIALIMAGE></Toolkit>";
		} else {
			result = RequestService.postRequest(url, methodName,
					UrlUtil.encodeRequestParam(paramMap));
		}
		if (StrUtil.isNotEmpty(result)) {
			if (result.equalsIgnoreCase("sign error")) {
				return new Object[] { "error", "签名不一致" };
			}
			return resultParser(result);
		} else {
			return new Object[] { "error", "当前网络不稳定，数据请求失败，请稍后重试！" };
		}
	}
	
	private Object resultParser(String str) {
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
					if (name.equalsIgnoreCase("PR_IMAGE_FILE_PATH")) {
						return  parser.nextText();
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
		return null;
	}

}
