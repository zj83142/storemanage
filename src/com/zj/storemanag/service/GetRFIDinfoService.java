package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.zj.storemanag.bean.RFIDinfo;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class GetRFIDinfoService {
	public Object getRFIDinfo(String url, String methodName, String userID,
			String rfid, Context context) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userID", userID);
		paramMap.put("rfid", rfid);
		String result = null;
		if(ParamsUtil.APP == 0){
			result = "<Toolkit><RFIDinfo><PR_RFID_NO>1111</PR_RFID_NO><PR_MATERIAL>2503020010</PR_MATERIAL><PR_PLANT>1100</PR_PLANT><PR_STGE_LOC>1501</PR_STGE_LOC><PR_MESSAGE>连杆5L-609-03-01</PR_MESSAGE><PR_ENTRY_UOM>2X</PR_ENTRY_UOM><PR_PLANT2/><PR_STGE_LOC2/><PR_IMAGE_NAME>test.jpg</PR_IMAGE_NAME><PR_IMAGE_SIZE>2245272</PR_IMAGE_SIZE><PR_IMAGE_TYPE>jpg</PR_IMAGE_TYPE><PR_IMAGE_FILE_PATH>../Upload/UploadFiles/1500_1501/test.jpg</PR_IMAGE_FILE_PATH></RFIDinfo></Toolkit>";
		}else{
			result = RequestService.postRequest(url, methodName, UrlUtil.encodeRequestParam(paramMap));
		}
//		result = "<Toolkit><RFIDinfo><PR_RFID_NO>1111</PR_RFID_NO><PR_MATERIAL>2503020010</PR_MATERIAL><PR_PLANT>1100</PR_PLANT><PR_STGE_LOC>1501</PR_STGE_LOC><PR_MESSAGE>连杆5L-609-03-01</PR_MESSAGE><PR_ENTRY_UOM>2X</PR_ENTRY_UOM><PR_PLANT2/><PR_STGE_LOC2/><PR_IMAGE_NAME>test.jpg</PR_IMAGE_NAME><PR_IMAGE_SIZE>2245272</PR_IMAGE_SIZE><PR_IMAGE_TYPE>jpg</PR_IMAGE_TYPE><PR_IMAGE_FILE_PATH>../Upload/UploadFiles/1500_1501/test.jpg</PR_IMAGE_FILE_PATH></RFIDinfo></Toolkit>";
		if (StrUtil.isNotEmpty(result)) {
			if (result.equalsIgnoreCase("sign error")) {
				return new Object[] { "error", "签名不一致" };
			}
			return parserResult(result, context);
		} else {
			return new Object[] { "error", "当前网络不稳定，数据请求失败，请稍后重试！" };
		}
	}

	private Object parserResult(String str, Context context) {
		// <Toolkit><RFIDinfo><PR_MATERIAL>物料1</PR_MATERIAL><PR_ENTRY_UOM>台</PR_ENTRY_UOM><PR_MESSAGE>物料描述1</PR_MESSAGE><PR_PLANT>1001</PR_PLANT><PR_RFID_NO>20140401</PR_RFID_NO><PR_PLANT2>100110101</PR_PLANT2><PR_STGE_LOC>x333</PR_STGE_LOC><PR_STGE_LOC2>x333101010</PR_STGE_LOC2></RFIDinfo></Toolkit>
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		RFIDinfo info = null;
		String factoryValue = null;
		BaseInfoDao baseInfoDao = BaseInfoDao.getInstance(context);
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
					} else if (name.equalsIgnoreCase("RFIDinfo")) {
						info = new RFIDinfo();
					} else if (name.equalsIgnoreCase("PR_MATERIAL")) {
						info.setPR_MATERIAL(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("PR_ENTRY_UOM")) {
						String unitValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(unitValue)){
							//单位
							String unitName = baseInfoDao.getNameByCode(unitValue, "3");
							info.setUnit(StrUtil.comStr(unitName, unitValue));
						}
					} else if (name.equalsIgnoreCase("PR_MESSAGE")) {
						String message = StrUtil.filterStr(parser.nextText());
						info.setPR_MESSAGE(message);
					} else if (name.equalsIgnoreCase("PR_PLANT")) {
						factoryValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(factoryValue)){
							String planName = baseInfoDao.getNameByCode(factoryValue, "1");
							info.setFactory(StrUtil.comStr(planName, factoryValue));
						}
					} else if (name.equalsIgnoreCase("PR_RFID_NO")) {
						info.setPR_RFID_NO(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("PR_PLANT2")) {
						info.setPR_PLANT2(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("PR_STGE_LOC")) {
						//库位
						String storeValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(storeValue)){
							String storeName = baseInfoDao.getStoreName(storeValue, factoryValue);
							info.setStore(StrUtil.comStr(storeName, storeValue));
						}
					} else if (name.equalsIgnoreCase("PR_STGE_LOC2")) {
						info.setPR_STGE_LOC2(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("RFIDinfo")) {
						if (info != null) {
							return new Object[] { "ok", info };
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
