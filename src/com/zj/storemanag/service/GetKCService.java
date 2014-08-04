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

import com.zj.storemanag.bean.StoreBean;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class GetKCService {

	/**查库存信息*/
	public Object getKC(String url, String methodName, String material,
			String werk, String lgort, Context context) {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("material", material);
		paramMap.put("werk", werk);
		paramMap.put("lgort", lgort);
		String result = null;
		if(ParamsUtil.APP == 0){
			result = "<Toolkit><ZTMPMATKC><MATNR>物料编号0</MATNR><MAKTX>物料描述0</MAKTX><WERKS>工厂编号0</WERKS><NAME1/><MTART/><MTBEZ/><LGORT>库位0</LGORT><LGOBE>使用单位、工厂 0</LGOBE><LGPBE>仓位/库位号（10位编号）0</LGPBE><MEINS>计量单位0</MEINS><LABST>1</LABST><KLABS>2</KLABS><EINME>100</EINME><KEINM>11</KEINM></ZTMPMATKC><ZTMPMATKC><MATNR>物料编号1</MATNR><MAKTX>物料描述1</MAKTX><WERKS>工厂编号1</WERKS><NAME1/><MTART/><MTBEZ/><LGORT>库位1</LGORT><LGOBE>使用单位、工厂 1</LGOBE><LGPBE>仓位/库位号（10位编号）1</LGPBE><MEINS>计量单位1</MEINS><LABST>1</LABST><KLABS>2</KLABS><EINME>100</EINME><KEINM>11</KEINM></ZTMPMATKC></Toolkit>";
		}else{
			result = RequestService.postRequest(url, methodName, UrlUtil.encodeRequestParam(paramMap));
		}
		if (StrUtil.isNotEmpty(result)) {
			if (result.equalsIgnoreCase("sign error")) {
				return new Object[] { "error", "签名不一致" };
			}
			return parserResult(result,context);
		} else {
			return new Object[] { "error", "当前网络不稳定，数据请求失败，请稍后重试！" };
		}
	}
	/**查项目库存信息*/
	public Object getKC2(String url, String methodName, String material,
			String werk, String lgort, Context context) {

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("material", material);
		paramMap.put("werk", werk);
		paramMap.put("lgort", lgort);
		paramMap.put("sobkz", "Q");
		String result = null;
		if(ParamsUtil.APP == 0){
			result = "<Toolkit><ZTMPMATKC><MATNR>物料编号0</MATNR><MAKTX>物料描述0</MAKTX><WERKS>工厂编号0</WERKS><NAME1/><MTART/><MTBEZ/><LGORT>库位0</LGORT><LGOBE>使用单位、工厂 0</LGOBE><LGPBE>仓位/库位号（10位编号）0</LGPBE><MEINS>计量单位0</MEINS><LABST>1</LABST><KLABS>2</KLABS><EINME>100</EINME><KEINM>11</KEINM></ZTMPMATKC><ZTMPMATKC><MATNR>物料编号1</MATNR><MAKTX>物料描述1</MAKTX><WERKS>工厂编号1</WERKS><NAME1/><MTART/><MTBEZ/><LGORT>库位1</LGORT><LGOBE>使用单位、工厂 1</LGOBE><LGPBE>仓位/库位号（10位编号）1</LGPBE><MEINS>计量单位1</MEINS><LABST>1</LABST><KLABS>2</KLABS><EINME>100</EINME><KEINM>11</KEINM></ZTMPMATKC></Toolkit>";
		}else{
			result = RequestService.postRequest(url, methodName, UrlUtil.encodeRequestParam(paramMap));
		}
		if (StrUtil.isNotEmpty(result)) {
			if (result.equalsIgnoreCase("sign error")) {
				return new Object[] { "error", "签名不一致" };
			}
			return parserResult(result,context);
		} else {
			return new Object[] { "error", "当前网络不稳定，数据请求失败，请稍后重试！" };
		}
	}

	private Object parserResult(String str, Context context) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		List<StoreBean> list = new ArrayList<StoreBean>();
		BaseInfoDao baseInfoDao = BaseInfoDao.getInstance(context);
		StoreBean bean = null;
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
					} else if (name.equalsIgnoreCase("ZTMPMATKC")) {
						bean = new StoreBean();
					} else if (name.equalsIgnoreCase("MATNR")) {
						bean.setMATNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MAKTX")) {
						String message = StrUtil.filterStr(parser.nextText());
						bean.setMAKTX(message);
					} else if (name.equalsIgnoreCase("WERKS")) {
						//工厂
						factoryValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(factoryValue)){
							String planName = baseInfoDao.getNameByCode(factoryValue, "1");
							bean.setFactory(StrUtil.comStr(planName, factoryValue));
						}
					} else if (name.equalsIgnoreCase("NAME1")) {
						bean.setNAME1(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MTART")) {
						bean.setMTART(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MTBEZ")) {
						bean.setMTBEZ(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("LGORT")) {
						//库位
						String storeValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(storeValue)){
							String storeName = baseInfoDao.getStoreName(storeValue, factoryValue);
							bean.setStore(StrUtil.comStr(storeName, storeValue));
						}
					} else if (name.equalsIgnoreCase("LGOBE")) {
						//使用工厂/单位
						bean.setLGOBE(StrUtil.filterStr(parser.nextText()));
					}else if(name.equalsIgnoreCase("LGPBE")){
						//仓位
						bean.setLGPBE(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MEINS")) {
						//单位
						String unitValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(unitValue)){
							//单位
							String unitName = baseInfoDao.getNameByCode(unitValue, "3");
							bean.setUnit(StrUtil.comStr(unitName, unitValue));
						}
					} else if (name.equalsIgnoreCase("LABST")) {
						bean.setLABST(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("KLABS")) {
						bean.setKLABS(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("EINME")) {
						bean.setEINME(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("KEINM")) {
						bean.setKEINM(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("ZTMPMATKC")) {
						list.add(bean);
						bean = null;
					}else if(name.equalsIgnoreCase("Toolkit")) {
						return new Object[]{"ok", list};
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
