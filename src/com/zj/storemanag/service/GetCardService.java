package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.zj.storemanag.bean.Card;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class GetCardService {

	/** 要 求： material：物料号；matCode：凭证号；matYear：凭证年；userID：加密 */
	public Object getCard245(String url, String methodName, String material,
			String matCode, String matYear, String cardType, String userID,Context context) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("material", material);
		paramMap.put("matCode", matCode);
		paramMap.put("matYear", matYear);
		paramMap.put("cardType", cardType);
		paramMap.put("userID", userID);
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><ZRPMWLKP><LGPBE>库位号（10位编号）</LGPBE><BUDAT>记账日期</BUDAT><KOSTL>成本中心</KOSTL><LGOBE>使用单位/工厂</LGOBE><LGORT>库位编号</LGORT><MABST>100</MABST><MAKTX>物料描述</MAKTX><MATNR>物料号</MATNR><MBLNR>物料凭证</MBLNR><MENGE>12</MENGE><MINBE>0</MINBE><NAME2>供应商</NAME2><NORMT>工业标准描述</NORMT><POSKI>WBS</POSKI><POST1>项目名称</POST1><STPRS>1212.3</STPRS><WERKS>使用单位(工厂/车间)编号</WERKS><PSPNR/></ZRPMWLKP></Toolkit>";
		} else {
			result = RequestService.postRequest(url, methodName, UrlUtil.encodeRequestParam(paramMap));
		}
		if (StrUtil.isNotEmpty(result)) {
			if (result.equalsIgnoreCase("sign error")) {
				return new Object[] { "error", "签名不一致" };
			}
			return parserResult(result, context);
		} else {
			return new Object[] { "error", "当前网络不稳定，数据请求失败，请稍后重试！" };
		}
	}
	
	
	/** 要 求： material：物料号；rPlant：工厂； stoc：库位；cardType：卡片类型（1，3） */
	public Object getCard13(String url, String methodName, String material,
			String rPlant, String stoc, String cardType, String userID,Context context) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("material", material);
		paramMap.put("rPlant", rPlant);
		paramMap.put("stoc", stoc);
		paramMap.put("cardType", cardType);
		paramMap.put("userID", userID);
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><ZRPMWLKP><LGPBE /><BUDAT>00000000</BUDAT><KOSTL /><LGOBE>设备动力一热电库</LGOBE><LGORT>2083</LGORT><MABST>0</MABST><MAKTX>冷却风扇BF2-9Q6(已经删除)</MAKTX><MATNR>2502010121</MATNR><MBLNR /><MENGE>0</MENGE><MINBE>0</MINBE><NAME2 /><NORMT /><POSKI /><POST1 /><STPRS>2970.00</STPRS><WERKS>1100</WERKS><PSPNR>0</PSPNR><ENTRY_UOM /></ZRPMWLKP></Toolkit>";
		} else {
			result = RequestService.postRequest(url, methodName, UrlUtil.encodeRequestParam(paramMap));
		}
		if (StrUtil.isNotEmpty(result)) {
			if (result.equalsIgnoreCase("sign error")) {
				return new Object[] { "error", "签名不一致" };
			}
			return parserResult(result, context);
		} else {
			return new Object[] { "error", "当前网络不稳定，数据请求失败，请稍后重试！" };
		}
	}

	private Object parserResult(String str,Context context) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		BaseInfoDao baseInfoDao = new BaseInfoDao(context);
		Card card = null;
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
					} else if (name.equalsIgnoreCase("ZRPMWLKP")) {
						card = new Card();
					} else if (name.equalsIgnoreCase("LGPBE")) {
						card.setLGPBE(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("BUDAT")) {
						card.setBUDAT(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("KOSTL")) {
						card.setKOSTL(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("LGOBE")) {
						card.setLGOBE(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("LGORT")) {
						card.setLGORT(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MABST")) {
						card.setMABST(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MAKTX")) {
						String message = StrUtil.filterStr(parser.nextText());
						card.setMAKTX(message);
					} else if (name.equalsIgnoreCase("MATNR")) {
						card.setMATNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MBLNR")) {
						card.setMBLNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MENGE")) {
						card.setMENGE(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MINBE")) {
						card.setMINBE(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("NAME2")) {
						card.setNAME2(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("NORMT")) {
						card.setNORMT(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("POSKI")) {
						card.setPOSKI(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("STPRS")) {
						card.setSTPRS(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("WERKS")) {
						card.setWERKS(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("PSPNR")) {
						card.setPSPNR(StrUtil.filterStr(parser.nextText()));
					}else if (name.equalsIgnoreCase("ENTRY_UOM")) {
						//查询
						String unitValue = StrUtil.filterStr(parser.nextText());
						String unit = "";
						String unitName = "";
						if(StrUtil.isNotEmpty(unitValue)){
							unitName = baseInfoDao.getNameByCode(unitValue, "3");
						}
						unit = unitValue +"_" + unitName;
						card.setENTRY_UOM(unit);
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("Toolkit")) {
						if (card != null) {
							return new Object[] { "ok", card };
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
