package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.zj.storemanag.bean.MaterailDetail;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class GetMaterialDetailService {

	public Object getMaterialDetail(String url, String methodName,
			String userID, String matCode, Context context) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userID", userID);
		paramMap.put("matCode", matCode);
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><ZMATERIALDETAIL><BSTMA>12</BSTMA><BSTMI>0</BSTMI><MAKTX>物料描述</MAKTX><MATNR>物料号</MATNR><MEINS>台</MEINS><MFRNR>制造商号码</MFRNR><MFRPN>制造商零件号</MFRPN><NORMT>工业标准描述</NORMT><STPRS>234.4</STPRS></ZMATERIALDETAIL></Toolkit>";
		} else {
			result = RequestService.postRequest(url, methodName,
					UrlUtil.encodeRequestParam(paramMap));
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

	private Object parserResult(String str, Context context) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		MaterailDetail materailDetail = null;
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
					} else if (name.equalsIgnoreCase("ZMATERIALDETAIL")) {
						materailDetail = new MaterailDetail();
					} else if (name.equalsIgnoreCase("BSTMA")) {
						materailDetail.setBSTMA(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("BSTMI")) {
						materailDetail.setBSTMI(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MAKTX")) {
						String message = StrUtil.filterStr(parser.nextText());
						materailDetail.setMAKTX(message);
					} else if (name.equalsIgnoreCase("MATNR")) {
						materailDetail.setMATNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MEINS")) {
						//计量单位
						String unitValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(unitValue)){
							//单位
							String unitName = baseInfoDao.getNameByCode(unitValue, "3");
							if(StrUtil.isNotEmpty(unitName)){
								materailDetail.setUnit(StrUtil.filterStr(unitName));
							}else{
								materailDetail.setUnit(unitValue);
							}
						}
					} else if (name.equalsIgnoreCase("MFRNR")) {
						materailDetail.setMFRNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MFRPN")) {
						materailDetail.setMFRPN(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("NORMT")) {
						materailDetail.setNORMT(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("STPRS")) {
						materailDetail.setSTPRS(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("Toolkit")) {
						if (materailDetail != null) {
							return new Object[] { "ok", materailDetail };
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
