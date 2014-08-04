package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.zj.storemanag.bean.StoreHistoryBean;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.util.Base64;
import com.zj.storemanag.util.MD5Util;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.util.ZipUtil;

public class GetStoreHistoryService {

	public Object getStoreHistory(String url, String methodName,
			String userId, String xml,Context context) {
		
		String data = "userID="+UrlUtil.urlEncode(userId)+"&xml="+UrlUtil.urlEncode(Base64.encode(ZipUtil.compress(xml)));
		String sign = MD5Util.MD5((data + ParamsUtil.key).getBytes());
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><ZTMPMATKCMB51><AUFNR /><BLDAT>20140429</BLDAT><BTEXT /><BUDAT>20140429</BUDAT><BWART>311</BWART><BWTAR /><CHARG>4114030012</CHARG><CPUTM>103119</CPUTM><DMBTR>0.00</DMBTR><EBELN /><ERFME>TON</ERFME><ERFMG>49.960</ERFMG><KOSTL /><KZBEW /><KZVBR /><KZZUG /><LGORT>3000</LGORT><LIFNR /><MAKTX>连铸坯 20 转炉 150×150×12000</MAKTX><MATNR>1710020301</MATNR><MBLNR>4907600502</MBLNR><MENGE>49.960</MENGE><MJAHR>2014</MJAHR><NAME1>杭钢股份公司生产工厂</NAME1><POSID /><POSKI /><PS_PSP_PNR>0</PS_PSP_PNR><SOBKZ /><VGART>WA</VGART><WERKS>2000</WERKS><ZEILE>1</ZEILE></ZTMPMATKCMB51><ZTMPMATKCMB51><AUFNR /><BLDAT>20140429</BLDAT><BTEXT /><BUDAT>20140429</BUDAT><BWART>311</BWART><BWTAR /><CHARG>4114030012</CHARG><CPUTM>103119</CPUTM><DMBTR>0.00</DMBTR><EBELN /><ERFME>TON</ERFME><ERFMG>49.960</ERFMG><KOSTL /><KZBEW /><KZVBR /><KZZUG /><LGORT>2041</LGORT><LIFNR /><MAKTX>连铸坯 20 转炉 150×150×12000</MAKTX><MATNR>1710020301</MATNR><MBLNR>4907600502</MBLNR><MENGE>49.960</MENGE><MJAHR>2014</MJAHR><NAME1>杭钢股份公司生产工厂</NAME1><POSID /><POSKI /><PS_PSP_PNR>0</PS_PSP_PNR><SOBKZ /><VGART>WA</VGART><WERKS>2000</WERKS><ZEILE>2</ZEILE></ZTMPMATKCMB51></Toolkit>";
		} else {
			result = RequestService.postRequest(url, methodName, sign, data);
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
		List<StoreHistoryBean> list = new ArrayList<StoreHistoryBean>();
		BaseInfoDao baseInfoDao = BaseInfoDao.getInstance(context);
		String factoryValue = null;
		StoreHistoryBean bean = null;
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
					} else if (name.equalsIgnoreCase("ZTMPMATKCMB51")) {
						bean = new StoreHistoryBean();
					} else if (name.equalsIgnoreCase("AUFNR")) {
						bean.setAUFNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("BLDAT")) {
						bean.setBLDAT(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("BTEXT")) {
						bean.setBTEXT(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("BUDAT")) {
						bean.setBUDAT(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("BWART")) {
						bean.setBWART(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("BWTAR")) {
						bean.setBWTAR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("CHARG")) {
						bean.setCHARG(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("CPUTM")) {
						bean.setCPUTM(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("DMBTR")) {
						bean.setDMBTR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("EBELN")) {
						bean.setEBELN(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("ERFME")) {
						//计量单位
						String unitValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(unitValue)){
							//查询
							String unitName = baseInfoDao.getNameByCode(unitValue, "3");
							bean.setERFME(StrUtil.filterStr(unitName));
						}
					} else if (name.equalsIgnoreCase("ERFMG")) {
						bean.setERFMG(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("KOSTL")) {
						bean.setKOSTL(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("KZBEW")) {
						bean.setKZBEW(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("KZVBR")) {
						bean.setKZVBR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("KZZUG")) {
						bean.setKZZUG(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("LGORT")) {
						//库存地点
						String storeValue = StrUtil.filterStr(parser.nextText());
						bean.setLGORT(storeValue);
					} else if (name.equalsIgnoreCase("LIFNR")) {
						bean.setLIFNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MAKTX")) {
						String message = StrUtil.filterStr(parser.nextText());
						bean.setMAKTX(message);
					} else if (name.equalsIgnoreCase("MATNR")) {
						bean.setMATNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MBLNR")) {
						bean.setMBLNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MENGE")) {
						bean.setMENGE(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MJAHR")) {
						bean.setMJAHR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("NAME1")) {
						bean.setNAME1(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("POSID")) {
						bean.setPOSID(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("POSKI")) {
						bean.setPOSKI(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("PS_PSP_PNR")) {
						bean.setPS_PSP_PNR(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("SOBKZ")) {
						bean.setSOBKZ(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("VGART")) {
						bean.setVGART(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("WERKS")) {
						bean.setWERKS(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("ZEILE")) {
						bean.setZEILE(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("Toolkit")) {
						if (list != null && list.size() > 0) {
							return new Object[] { "ok", list };
						}
					}else if (name.equalsIgnoreCase("ZTMPMATKCMB51")) {
						if(bean != null){
							list.add(bean);
							bean = null;
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
