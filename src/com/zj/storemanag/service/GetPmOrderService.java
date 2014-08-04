package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

import com.zj.storemanag.bean.Goods;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.dao.GoodsDao;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class GetPmOrderService {

	public Object getPmOrder(String url, String methodName,
			String userID, String repairNo, Context context) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userID", userID);
		paramMap.put("repairNo", repairNo);
		String result = null;
		if (ParamsUtil.APP == 0) {
			result = "<Toolkit><Good><MATERIAL>2503020010</MATERIAL><MESSAGE>检修箱250*500*600</MESSAGE><ENTRY_QNT>0</ENTRY_QNT><ENTRY_QNTSpecified/><ENTRY_UOM>Z09</ENTRY_UOM><IsSelected>False</IsSelected><IsSelectedSpecified/><PLANT>1100</PLANT><TotalCount>10.000</TotalCount><TotalCountSpecified/></Good></Toolkit>";
		} else {
			result = RequestService.postRequest(url, methodName,
					UrlUtil.encodeRequestParam(paramMap));
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

	private Object parserResult(String str,Context context) {
		XmlPullParser parser = Xml.newPullParser();
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		BaseInfoDao baseInfoDao = BaseInfoDao.getInstance(context);
		GoodsDao goodsDao = GoodsDao.getInstance(context);
		Goods good = null;
//		String supper = null;
		String factoryValue = null;
		boolean isSave = false;
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
					} else if (name.equalsIgnoreCase("Good")) {
						good = new Goods();
					}
//					else if(name.equalsIgnoreCase("VEND_NAME")){
//						supper = parser.nextText();
//					} 
					else if (name.equalsIgnoreCase("MATERIAL")) {
						good.setMATERIAL(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("MESSAGE")) {
						String message = StrUtil.filterStr(parser.nextText());
						good.setMESSAGE(message);
					} else if (name.equalsIgnoreCase("ENTRY_QNT")) {
//						good.setENTRY_QNT(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("ENTRY_QNTSpecified")) {
						good.setENTRY_QNTSpecified(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("ENTRY_UOM")) {
						String unitValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(unitValue)){
							//查询
							String unitName = baseInfoDao.getNameByCode(unitValue, "3");
							good.setUnit(StrUtil.comStr(unitName, unitValue));
						}
					} else if (name.equalsIgnoreCase("IsSelected")) {
						good.setIsSelected(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("IsSelectedSpecified")) {
						good.setIsSelectedSpecified(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("PLANT")) {
						factoryValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(factoryValue)){
							String planName = baseInfoDao.getNameByCode(factoryValue, "1");
							good.setFactory(StrUtil.comStr(planName, factoryValue));
						}
					} else if (name.equalsIgnoreCase("RFID")) {
						good.setRFID(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("STGE_LOC")) {
						String storeValue = StrUtil.filterStr(parser.nextText());
						if(StrUtil.isNotEmpty(storeValue)){
							String storeName = baseInfoDao.getStoreName(storeValue, factoryValue);
							good.setStore(StrUtil.comStr(storeName, storeValue));
						}
					} else if (name.equalsIgnoreCase("TotalCount")) {
						String number = StrUtil.filterStr(parser.nextText());
						good.setTotalCount(number);
						good.setENTRY_QNT(number);
					} else if (name.equalsIgnoreCase("TotalCountSpecified")) {
						good.setTotalCountSpecified(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("otherField1")) {
						good.setOtherField1(StrUtil.filterStr(parser.nextText()));
					} else if (name.equalsIgnoreCase("otherField2")) {
						good.setOtherField2(StrUtil.filterStr(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:
					if (name.equalsIgnoreCase("Good")) {
//						if(StrUtil.isNotEmpty(supper)){
//							good.setVEND_NAME(supper);
//						}
						isSave = goodsDao.save(good);
						if(!isSave){
							return  new Object[]{"error", "数据保存失败!"};
						}
						good = null;
					}else if(name.equalsIgnoreCase("Toolkit")) {
						if(isSave){
							return new Object[]{"ok", "数据保存成功!"};
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
