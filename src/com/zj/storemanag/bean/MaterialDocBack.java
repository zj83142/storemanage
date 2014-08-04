package com.zj.storemanag.bean;

import java.util.List;

import com.zj.storemanag.util.UrlUtil;

public class MaterialDocBack {
	
	private String type = "type";//入库类型
	private String Msg = "Msg";//订单号，可为空
	
	private String GM_CODE="GM_CODE";//货物移库类型编码/事物代码
	private String DOC_DATE="DOC_DATE";//凭证中的凭证日期. 格式：yyyyMMdd
	private String PSTNG_DATE="PSTNG_DATE";//凭证记帐日期.格式：yyyyMMdd
	
	private String GMI_IN_OUT = "GMI_IN_OUT";//1：入库   -1：出库
	private String COSTCENTER="COSTCENTER";//成本中心
	private String CUSTOMER="CUSTOMER";//客户的帐户编号
	private String ENTRY_QNT="ENTRY_QNT";//数量
	private String MATERIAL="MATERIAL";//物料
	private String MOVE_PLANT="MOVE_PLANT";//收货工厂/发货工厂
	private String MOVE_STLOC="MOVE_STLOC";//收货/发货库存地点
	private String MOVE_TYPE="MOVE_TYPE";//货物移动类型
	private String MVT_IND="MVT_IND";//移动标识
	private String ORDERID="ORDERID";//订单号(维修订单号？)
	private String PAR_COMPCO="PAR_COMPCO";//分公司代码
	private String PLANT="PLANT";//工厂
	private String PO_ITEM="PO_ITEM";//采购文件的项目编号
	private String PO_NUMBER="PO_NUMBER";//采购订单号
	private String SPEC_STOCK="SPEC_STOCK";//-特殊库存标识
	private String STGE_LOC="STGE_LOC";//库存地点
	private String VAL_WBS_ELEM="VAL_WBS_ELEM";//WBS元素.目的WBS 415+Q
	private String VENDOR="VENDOR";//供应商帐户号
	private String WBS_ELEM="WBS_ELEM";//源WBS 适用于移动类型415,415+Q
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = getType() + "=" + type;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = getMsg() + "=" + msg;
	}
	public String getGM_CODE() {
		return GM_CODE;
	}
	public void setGM_CODE(String gM_CODE) {
		GM_CODE = getGM_CODE() + "=" + gM_CODE;
	}
	public String getDOC_DATE() {
		return DOC_DATE;
	}
	public void setDOC_DATE(String dOC_DATE) {
		DOC_DATE = getDOC_DATE() + "=" + dOC_DATE;
	}
	public String getPSTNG_DATE() {
		return PSTNG_DATE;
	}
	public void setPSTNG_DATE(String pSTNG_DATE) {
		PSTNG_DATE = getPSTNG_DATE() + "=" + pSTNG_DATE;
	}
	public String getGMI_IN_OUT() {
		return GMI_IN_OUT;
	}
	public void setGMI_IN_OUT(String gMI_IN_OUT) {
		GMI_IN_OUT = getGMI_IN_OUT() + "=" +  gMI_IN_OUT;
	}
	public String getCOSTCENTER() {
		return COSTCENTER;
	}
	public void setCOSTCENTER(String cOSTCENTER) {
		COSTCENTER = getCOSTCENTER() + "=" + cOSTCENTER;
	}
	public String getCUSTOMER() {
		return CUSTOMER;
	}
	public void setCUSTOMER(String cUSTOMER) {
		CUSTOMER = getCUSTOMER() + "=" + cUSTOMER;
	}
	public String getENTRY_QNT() {
		return ENTRY_QNT;
	}
	public void setENTRY_QNT(String eNTRY_QNT) {
		ENTRY_QNT = getENTRY_QNT() + "=" + eNTRY_QNT;
	}
	public String getMATERIAL() {
		return MATERIAL;
	}
	public void setMATERIAL(String mATERIAL) {
		MATERIAL = getMATERIAL() + "=" + mATERIAL;
	}
	public String getMOVE_PLANT() {
		return MOVE_PLANT;
	}
	public void setMOVE_PLANT(String mOVE_PLANT) {
		MOVE_PLANT = getMOVE_PLANT() + "=" + mOVE_PLANT;
	}
	public String getMOVE_STLOC() {
		return MOVE_STLOC;
	}
	public void setMOVE_STLOC(String mOVE_STLOC) {
		MOVE_STLOC = getMOVE_STLOC() + "=" + mOVE_STLOC;
	}
	public String getMOVE_TYPE() {
		return MOVE_TYPE;
	}
	public void setMOVE_TYPE(String mOVE_TYPE) {
		MOVE_TYPE = getMOVE_TYPE() + "=" + mOVE_TYPE;
	}
	public String getMVT_IND() {
		return MVT_IND;
	}
	public void setMVT_IND(String mVT_IND) {
		MVT_IND = getMVT_IND() + "=" + mVT_IND;
	}
	public String getORDERID() {
		return ORDERID;
	}
	public void setORDERID(String oRDERID) {
		ORDERID = getORDERID() + "=" + oRDERID;
	}
	public String getPAR_COMPCO() {
		return PAR_COMPCO;
	}
	public void setPAR_COMPCO(String pAR_COMPCO) {
		PAR_COMPCO = getPAR_COMPCO() + "=" + pAR_COMPCO;
	}
	public String getPLANT() {
		return PLANT;
	}
	public void setPLANT(String pLANT) {
		PLANT = getPLANT() + "=" + pLANT;
	}
	public String getPO_ITEM() {
		return PO_ITEM;
	}
	public void setPO_ITEM(String pO_ITEM) {
		PO_ITEM = getPO_ITEM() + "=" + pO_ITEM;
	}
	public String getPO_NUMBER() {
		return PO_NUMBER;
	}
	public void setPO_NUMBER(String pO_NUMBER) {
		PO_NUMBER = getPO_NUMBER() + "=" + pO_NUMBER;
	}
	public String getSPEC_STOCK() {
		return SPEC_STOCK;
	}
	public void setSPEC_STOCK(String sPEC_STOCK) {
		SPEC_STOCK = getSPEC_STOCK() + "=" + sPEC_STOCK;
	}
	public String getSTGE_LOC() {
		return STGE_LOC;
	}
	public void setSTGE_LOC(String sTGE_LOC) {
		STGE_LOC = getSTGE_LOC() + "=" + sTGE_LOC;
	}
	public String getVAL_WBS_ELEM() {
		return VAL_WBS_ELEM;
	}
	public void setVAL_WBS_ELEM(String vAL_WBS_ELEM) {
		VAL_WBS_ELEM = getVAL_WBS_ELEM() + "=" + vAL_WBS_ELEM;
	}
	public String getVENDOR() {
		return VENDOR;
	}
	public void setVENDOR(String vENDOR) {
		VENDOR = getVENDOR() + "=" + vENDOR;
	}
	public String getWBS_ELEM() {
		return WBS_ELEM;
	}
	public void setWBS_ELEM(String wBS_ELEM) {
		WBS_ELEM = getWBS_ELEM() + "=" + wBS_ELEM;
	}
	private List<MaterialDocBack> saveTemps = null;
	public List<MaterialDocBack> getSaveTemps() {
		return saveTemps;
	}
	public void setSaveTemps(List<MaterialDocBack> saveTemps) {
		this.saveTemps = saveTemps;
	}
	public String getSendParam() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<Toolkit>");
		stringBuffer.append("<Oprations>");
		stringBuffer.append(UrlUtil.manageNode(getType()));
		stringBuffer.append(UrlUtil.manageNode(getMsg()));
		stringBuffer.append("</Oprations>");
		stringBuffer.append("<BAPI2017_GM_CODE>");
		stringBuffer.append(UrlUtil.manageNode(getGM_CODE()));
		stringBuffer.append("</BAPI2017_GM_CODE>");
		stringBuffer.append("<BAPI2017_GM_HEAD>");
		stringBuffer.append(UrlUtil.manageNode(getDOC_DATE()));
		stringBuffer.append(UrlUtil.manageNode(getPSTNG_DATE()));
		stringBuffer.append("</BAPI2017_GM_HEAD>");
		
		if (saveTemps != null)
			for (MaterialDocBack temp : saveTemps) {
				stringBuffer.append("<BAPI2017_GM_ITEM>");
				stringBuffer.append(UrlUtil.manageNode(temp.getGMI_IN_OUT()));
				stringBuffer.append(UrlUtil.manageNode(temp.getCOSTCENTER()));
				stringBuffer.append(UrlUtil.manageNode(temp.getCUSTOMER()));
				stringBuffer.append(UrlUtil.manageNode(temp.getENTRY_QNT()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMATERIAL()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMOVE_PLANT()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMOVE_STLOC()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMOVE_TYPE()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMVT_IND()));
				
				stringBuffer.append(UrlUtil.manageNode(temp.getORDERID()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPAR_COMPCO()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPLANT()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPO_ITEM()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPO_NUMBER()));
				stringBuffer.append(UrlUtil.manageNode(temp.getSPEC_STOCK()));
				stringBuffer.append(UrlUtil.manageNode(temp.getSTGE_LOC()));
				stringBuffer.append(UrlUtil.manageNode(temp.getVAL_WBS_ELEM()));
				stringBuffer.append(UrlUtil.manageNode(temp.getVENDOR()));
				stringBuffer.append(UrlUtil.manageNode(temp.getWBS_ELEM()));
				stringBuffer.append("</BAPI2017_GM_ITEM>");
			}
		stringBuffer.append("</Toolkit>");
		return stringBuffer.toString();
	}
	public String getSendXml(int flag, List<Goods> goodsLs) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<Toolkit>");
		stringBuffer.append("<Oprations>");
		stringBuffer.append(UrlUtil.manageNode(getType()));
		stringBuffer.append(UrlUtil.manageNode(getMsg()));
		stringBuffer.append("</Oprations>");
		stringBuffer.append("<BAPI2017_GM_CODE>");
		stringBuffer.append(UrlUtil.manageNode(getGM_CODE()));
		stringBuffer.append("</BAPI2017_GM_CODE>");
		stringBuffer.append("<BAPI2017_GM_HEAD>");
		stringBuffer.append(UrlUtil.manageNode(getDOC_DATE()));
		stringBuffer.append(UrlUtil.manageNode(getPSTNG_DATE()));
		stringBuffer.append("</BAPI2017_GM_HEAD>");
		
		if (saveTemps != null)
			for (MaterialDocBack temp : saveTemps) {
				stringBuffer.append("<BAPI2017_GM_ITEM>");
				stringBuffer.append(UrlUtil.manageNode(temp.getGMI_IN_OUT()));
				stringBuffer.append(UrlUtil.manageNode(temp.getCOSTCENTER()));
				stringBuffer.append(UrlUtil.manageNode(temp.getCUSTOMER()));
				stringBuffer.append(UrlUtil.manageNode(temp.getENTRY_QNT()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMATERIAL()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMOVE_PLANT()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMOVE_STLOC()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMOVE_TYPE()));
				stringBuffer.append(UrlUtil.manageNode(temp.getMVT_IND()));
				
				stringBuffer.append(UrlUtil.manageNode(temp.getORDERID()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPAR_COMPCO()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPLANT()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPO_ITEM()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPO_NUMBER()));
				stringBuffer.append(UrlUtil.manageNode(temp.getSPEC_STOCK()));
				stringBuffer.append(UrlUtil.manageNode(temp.getSTGE_LOC()));
				stringBuffer.append(UrlUtil.manageNode(temp.getVAL_WBS_ELEM()));
				stringBuffer.append(UrlUtil.manageNode(temp.getVENDOR()));
				stringBuffer.append(UrlUtil.manageNode(temp.getWBS_ELEM()));
				stringBuffer.append("</BAPI2017_GM_ITEM>");
			}
		stringBuffer.append("</Toolkit>");
		return stringBuffer.toString();
	}
	
}
