package com.zj.storemanag.bean;

import java.util.List;

import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class MaterialDoc {
	
	private String type;//入库类型
	private String Msg;//订单号，可为空
	
	private String GM_CODE;//货物移库类型编码/事物代码
	private String DOC_DATE;//凭证中的凭证日期. 格式：yyyyMMdd
	private String PSTNG_DATE;//凭证记帐日期.格式：yyyyMMdd
	
	private String GMI_IN_OUT;//1：入库   -1：出库
	private String COSTCENTER;//成本中心
	private String CUSTOMER;//客户的帐户编号
	private String ENTRY_QNT;//数量
	private String MATERIAL;//物料
	private String MOVE_PLANT;//收货工厂/发货工厂
	private String MOVE_STLOC;//收货/发货库存地点
	private String MOVE_TYPE;//货物移动类型
	private String MVT_IND;//移动标识
	private String ORDERID;//订单号(维修订单号？)
	private String PAR_COMPCO;//分公司代码
	private String PLANT;//工厂
	private String PO_ITEM;//采购文件的项目编号
	private String PO_NUMBER;//采购订单号
	private String SPEC_STOCK;//-特殊库存标识
	private String STGE_LOC;//库存地点
	private String VAL_WBS_ELEM;//WBS元素.目的WBS 415+Q
	private String VENDOR;//供应商帐户号
	private String WBS_ELEM;//源WBS 适用于移动类型415,415+Q
	

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMsg() {
		return Msg;
	}


	public void setMsg(String msg) {
		Msg = msg;
	}


	public String getGM_CODE() {
		return GM_CODE;
	}


	public void setGM_CODE(String gM_CODE) {
		GM_CODE = gM_CODE;
	}


	public String getDOC_DATE() {
		return DOC_DATE;
	}


	public void setDOC_DATE(String dOC_DATE) {
		DOC_DATE = dOC_DATE;
	}


	public String getPSTNG_DATE() {
		return PSTNG_DATE;
	}


	public void setPSTNG_DATE(String pSTNG_DATE) {
		PSTNG_DATE = pSTNG_DATE;
	}


	public String getGMI_IN_OUT() {
		return GMI_IN_OUT;
	}


	public void setGMI_IN_OUT(String gMI_IN_OUT) {
		GMI_IN_OUT = gMI_IN_OUT;
	}


	public String getCOSTCENTER() {
		return COSTCENTER;
	}


	public void setCOSTCENTER(String cOSTCENTER) {
		COSTCENTER = cOSTCENTER;
	}


	public String getCUSTOMER() {
		return CUSTOMER;
	}


	public void setCUSTOMER(String cUSTOMER) {
		CUSTOMER = cUSTOMER;
	}


	public String getENTRY_QNT() {
		return ENTRY_QNT;
	}


	public void setENTRY_QNT(String eNTRY_QNT) {
		ENTRY_QNT = eNTRY_QNT;
	}


	public String getMATERIAL() {
		return MATERIAL;
	}


	public void setMATERIAL(String mATERIAL) {
		MATERIAL = mATERIAL;
	}


	public String getMOVE_PLANT() {
		return MOVE_PLANT;
	}


	public void setMOVE_PLANT(String mOVE_PLANT) {
		MOVE_PLANT = mOVE_PLANT;
	}


	public String getMOVE_STLOC() {
		return MOVE_STLOC;
	}


	public void setMOVE_STLOC(String mOVE_STLOC) {
		MOVE_STLOC = mOVE_STLOC;
	}


	public String getMOVE_TYPE() {
		return MOVE_TYPE;
	}


	public void setMOVE_TYPE(String mOVE_TYPE) {
		MOVE_TYPE = mOVE_TYPE;
	}


	public String getMVT_IND() {
		return MVT_IND;
	}


	public void setMVT_IND(String mVT_IND) {
		MVT_IND = mVT_IND;
	}


	public String getORDERID() {
		return ORDERID;
	}


	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}


	public String getPAR_COMPCO() {
		return PAR_COMPCO;
	}


	public void setPAR_COMPCO(String pAR_COMPCO) {
		PAR_COMPCO = pAR_COMPCO;
	}


	public String getPLANT() {
		return PLANT;
	}


	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}


	public String getPO_ITEM() {
		return PO_ITEM;
	}


	public void setPO_ITEM(String pO_ITEM) {
		PO_ITEM = pO_ITEM;
	}


	public String getPO_NUMBER() {
		return PO_NUMBER;
	}


	public void setPO_NUMBER(String pO_NUMBER) {
		PO_NUMBER = pO_NUMBER;
	}


	public String getSPEC_STOCK() {
		return SPEC_STOCK;
	}


	public void setSPEC_STOCK(String sPEC_STOCK) {
		SPEC_STOCK = sPEC_STOCK;
	}


	public String getSTGE_LOC() {
		return STGE_LOC;
	}


	public void setSTGE_LOC(String sTGE_LOC) {
		STGE_LOC = sTGE_LOC;
	}


	public String getVAL_WBS_ELEM() {
		return VAL_WBS_ELEM;
	}


	public void setVAL_WBS_ELEM(String vAL_WBS_ELEM) {
		VAL_WBS_ELEM = vAL_WBS_ELEM;
	}


	public String getVENDOR() {
		return VENDOR;
	}


	public void setVENDOR(String vENDOR) {
		VENDOR = vENDOR;
	}


	public String getWBS_ELEM() {
		return WBS_ELEM;
	}


	public void setWBS_ELEM(String wBS_ELEM) {
		WBS_ELEM = wBS_ELEM;
	}


	public String getSendXml(int flag, List<Goods> goodsLs) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<Toolkit>");
		stringBuffer.append("<Oprations>");
		stringBuffer.append("<type>"+UrlUtil.safeXml(getType())+"</type>");
		stringBuffer.append("<Msg>"+UrlUtil.safeXml(getMsg())+"</Msg>");
		stringBuffer.append("</Oprations>");
		stringBuffer.append("<BAPI2017_GM_CODE>");
		stringBuffer.append("<GM_CODE>"+UrlUtil.safeXml(getGM_CODE())+"</GM_CODE>");
		stringBuffer.append("</BAPI2017_GM_CODE>");
		stringBuffer.append("<BAPI2017_GM_HEAD>");
		stringBuffer.append("<DOC_DATE>"+UrlUtil.safeXml(getDOC_DATE())+"</DOC_DATE>");
		stringBuffer.append("<PSTNG_DATE>"+UrlUtil.safeXml(getPSTNG_DATE())+"</PSTNG_DATE>");
		stringBuffer.append("</BAPI2017_GM_HEAD>");
		
		if (goodsLs != null)
			for (Goods temp : goodsLs) {
				stringBuffer.append("<BAPI2017_GM_ITEM>");
				stringBuffer.append("<GMI_IN_OUT>"+UrlUtil.safeXml(getGMI_IN_OUT())+"</GMI_IN_OUT>");
				stringBuffer.append("<COSTCENTER>"+UrlUtil.safeXml(getCOSTCENTER())+"</COSTCENTER>");
				stringBuffer.append("<CUSTOMER>"+UrlUtil.safeXml(getCUSTOMER())+"</CUSTOMER>");
				stringBuffer.append("<ENTRY_QNT>"+UrlUtil.safeXml(temp.getENTRY_QNT())+"</ENTRY_QNT>");
				stringBuffer.append("<MATERIAL>"+UrlUtil.safeXml(temp.getMATERIAL())+"</MATERIAL>");
				stringBuffer.append("<MOVE_PLANT>"+UrlUtil.safeXml(StrUtil.slipValue(getMOVE_PLANT()))+"</MOVE_PLANT>");
				stringBuffer.append("<MOVE_STLOC>"+UrlUtil.safeXml(StrUtil.slipValue(getMOVE_STLOC()))+"</MOVE_STLOC>");
				stringBuffer.append("<MOVE_TYPE>"+UrlUtil.safeXml(getMOVE_TYPE())+"</MOVE_TYPE>");
				stringBuffer.append("<MVT_IND>"+UrlUtil.safeXml(getMVT_IND())+"</MVT_IND>");
				
				stringBuffer.append("<ORDERID>"+UrlUtil.safeXml(getORDERID())+"</ORDERID>");
				stringBuffer.append("<PAR_COMPCO>"+UrlUtil.safeXml(getPAR_COMPCO())+"</PAR_COMPCO>");
				if(flag == ParamsUtil.interior){
					stringBuffer.append("<PLANT>"+UrlUtil.safeXml(StrUtil.slipValue(getPLANT()))+"</PLANT>");
					stringBuffer.append("<STGE_LOC>"+UrlUtil.safeXml(StrUtil.slipValue(getSTGE_LOC()))+"</STGE_LOC>");
				}else{
					stringBuffer.append("<PLANT>"+UrlUtil.safeXml(StrUtil.slipValue(temp.getFactory()))+"</PLANT>");
					stringBuffer.append("<STGE_LOC>"+UrlUtil.safeXml(StrUtil.slipValue(temp.getStore()))+"</STGE_LOC>");
				}
				if(flag == ParamsUtil.external){
					stringBuffer.append("<PO_ITEM>"+UrlUtil.safeXml(temp.getOtherField2())+"</PO_ITEM>");
				}else{
					stringBuffer.append("<PO_ITEM>"+UrlUtil.safeXml(getPO_ITEM())+"</PO_ITEM>");
				}
				stringBuffer.append("<PO_NUMBER>"+UrlUtil.safeXml(getPO_NUMBER())+"</PO_NUMBER>");
				stringBuffer.append("<SPEC_STOCK>"+UrlUtil.safeXml(getSPEC_STOCK())+"</SPEC_STOCK>");
				stringBuffer.append("<VAL_WBS_ELEM>"+UrlUtil.safeXml(getVAL_WBS_ELEM())+"</VAL_WBS_ELEM>");
				stringBuffer.append("<VENDOR>"+UrlUtil.safeXml(getVENDOR())+"</VENDOR>");
				stringBuffer.append("<WBS_ELEM>"+UrlUtil.safeXml(getWBS_ELEM())+"</WBS_ELEM>");
				stringBuffer.append("</BAPI2017_GM_ITEM>");
			}
		stringBuffer.append("</Toolkit>");
		return stringBuffer.toString();
	}
	
}
