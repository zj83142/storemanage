package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.zj.storemanag.bean.Card13;
import com.zj.storemanag.bean.Card245;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class GetCardLsService {

	public Object getCard245Ls(String url, String methodName, String isPrint,
			String moveType, String plant, String stgeLoc, String page,
			String pageSize, Context context) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("isPrint", isPrint);
		paramMap.put("moveType", moveType);
		paramMap.put("plant", plant);
		paramMap.put("stgeLoc", stgeLoc);
		paramMap.put("page", page);
		paramMap.put("pageSize", pageSize);
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><BAPI2017_GM_ITEM><MATERIAL>1102010064</MATERIAL><PLANT>1200</PLANT><STGE_LOC>1208</STGE_LOC><MOVE_TYPE>101</MOVE_TYPE><MAT_CODE>3464623920</MAT_CODE><MAT_YEAR>2014</MAT_YEAR></BAPI2017_GM_ITEM><BAPI2017_GM_ITEM><MATERIAL>1102010064</MATERIAL><PLANT>1200</PLANT><STGE_LOC>1208</STGE_LOC><MOVE_TYPE>101</MOVE_TYPE><MAT_CODE>4039428310</MAT_CODE><MAT_YEAR>2014</MAT_YEAR></BAPI2017_GM_ITEM></Toolkit>";
		} else {
			result = RequestService.postRequest(url, methodName,
					UrlUtil.encodeRequestParam(paramMap));
		}
		if (StrUtil.isNotEmpty(result)) {
			if (result.equalsIgnoreCase("sign error")) {
				return new Object[] { "error", "签名不一致" };
			}
			return card245Parser(result, context);
		} else {
			return new Object[] { "error", "当前网络不稳定，数据请求失败，请稍后重试！" };
		}
	}

	public Object getCard13Ls(String url, String methodName, String isPrint,
			String plant, String stgeLoc, String page, String pageSize,
			Context context) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("isPrint", isPrint);
		paramMap.put("plant", plant);
		paramMap.put("stgeLoc", stgeLoc);
		paramMap.put("page", page);
		paramMap.put("pageSize", pageSize);
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><HS_RFID><PR_RFID_NO>1111</PR_RFID_NO><PR_MATERIAL>6601000064</PR_MATERIAL><PR_PLANT>1100</PR_PLANT><PR_STGE_LOC>1107</PR_STGE_LOC><PR_MESSAGE>连杆5L-609-03-01</PR_MESSAGE><PR_ENTRY_UOM>件        </PR_ENTRY_UOM></HS_RFID><HS_RFID><PR_RFID_NO>111111111</PR_RFID_NO><PR_MATERIAL>5203130397</PR_MATERIAL><PR_PLANT>杭钢</PR_PLANT><PR_STGE_LOC>废钢</PR_STGE_LOC><PR_MESSAGE>(燃气)电磁阀组G3/4 LNJ201.9</PR_MESSAGE><PR_ENTRY_UOM/></HS_RFID></Toolkit>";
		} else {
			result = RequestService.postRequest(url, methodName,
					UrlUtil.encodeRequestParam(paramMap));
		}
		if (StrUtil.isNotEmpty(result)) {
			if (result.equalsIgnoreCase("sign error")) {
				return new Object[] { "error", "签名不一致" };
			}
			return card13Parser(result, context);
		} else {
			return new Object[] { "error", "当前网络不稳定，数据请求失败，请稍后重试！" };
		}
	}

	private Object card13Parser(String str, Context context) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		List<Card13> list = new ArrayList<Card13>();
		BaseInfoDao baseInfoDao = BaseInfoDao.getInstance(context);
		Card13 bean = null;
		String factoryValue = null;
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
					} else if (name.equalsIgnoreCase("HS_RFID")) {
						bean = new Card13();
					} else if (name.equalsIgnoreCase("PR_RFID_NO")) {
						bean.setRfid(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("PR_MATERIAL")) {
						bean.setMaterial(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("PR_PLANT")) {
						// 工厂
						factoryValue = StrUtil
								.filterStr(parser.nextText());
						if (StrUtil.isNotEmpty(factoryValue)) {
							String planName = baseInfoDao.getNameByCode(
									factoryValue, "1");
							bean.setFactory(StrUtil
									.comStr(planName, factoryValue));
						}
					} else if (name.equalsIgnoreCase("PR_STGE_LOC")) {
						// 库位
						String storeValue = StrUtil
								.filterStr(parser.nextText());
						if (StrUtil.isNotEmpty(storeValue)) {
							String storeName = baseInfoDao.getStoreName(storeValue, factoryValue);
							bean.setStore(StrUtil.comStr(storeName, storeValue));
						}
					} else if (name.equalsIgnoreCase("PR_MESSAGE")) {
						String message = StrUtil.filterStr(parser.nextText());
						bean.setMessage(message);
					} else if (name.equalsIgnoreCase("PR_ENTRY_UOM")) {
						String unitValue = StrUtil.filterStr(parser.nextText());
						if (StrUtil.isNotEmpty(unitValue)) {
							String unitName = baseInfoDao.getNameByCode(
									unitValue, "3");
							bean.setUnit(StrUtil.comStr(unitName, unitValue));
						}
					}else if(name.equalsIgnoreCase("PR_STGE_LOC2")){
						bean.setCw(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("HS_RFID")) {
						list.add(bean);
						bean = null;
					} else if (name.equalsIgnoreCase("Toolkit")) {
						return new Object[] { "ok", list };
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

	private Object card245Parser(String str, Context context) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		List<Card245> list = new ArrayList<Card245>();
		BaseInfoDao baseInfoDao = BaseInfoDao.getInstance(context);
		Card245 bean = null;
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
					} else if (name.equalsIgnoreCase("BAPI2017_GM_ITEM")) {
						bean = new Card245();
					} else if (name.equalsIgnoreCase("MATERIAL")) {
						bean.setMateral(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("PLANT")) {
						// 工厂
						String plantValue = StrUtil
								.filterStr(parser.nextText());
						if (StrUtil.isNotEmpty(plantValue)) {
							String planName = baseInfoDao.getNameByCode(
									plantValue, "1");
							bean.setFactory(StrUtil
									.comStr(planName, plantValue));
						}
					} else if (name.equalsIgnoreCase("STGE_LOC")) {
						// 库位
						String storeValue = StrUtil
								.filterStr(parser.nextText());
						if (StrUtil.isNotEmpty(storeValue)) {
							String storeName = baseInfoDao.getNameByCode(
									storeValue, "2");
							bean.setFactory(StrUtil.comStr(storeName,
									storeValue));
						}
					} else if (name.equalsIgnoreCase("MOVE_TYPE")) {
						bean.setMoveType(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MAT_CODE")) {
						bean.setMatCode(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MAT_YEAR")) {
						bean.setMatYear(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("BAPI2017_GM_ITEM")) {
						list.add(bean);
						bean = null;
					} else if (name.equalsIgnoreCase("Toolkit")) {
						return new Object[] { "ok", list };
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
