package com.zj.storemanag.bean;

import java.io.Serializable;

/**
 * 查询到的出入库实体类
 * @author zhoujing
 * 2014-5-16 上午9:49:08
 */
public class StoreHistoryBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String AUFNR;//订单号
	private String BLDAT;//凭证中的凭证日期
	private String BTEXT;//库存类型文本
	private String BUDAT;//凭证记帐日期
	
	private String BWART;//移动类型(库存管理)
	private String BWTAR;//评估类型
	private String CHARG;//批次编号
	private String CPUTM;//输入时间
	
	private String DMBTR;//按本位币计的金额
	private String EBELN;//采购凭证号
	private String ERFME;//输入单位
	private String ERFMG;//以条目单位的数量
	
	private String KOSTL;//成本中心
	private String KZBEW;//移动标识
	private String KZVBR;//消耗记帐
	private String KZZUG;//收货标识
	
	private String LGORT;//库存地点
	private String LIFNR;//供应商或债权人帐户号
	private String MAKTX;//物料描述
	private String MATNR;//物料号码
	
	private String MBLNR;//物料凭证编号
	private String MENGE;//数量
	private String MJAHR;//物料凭证的年份
	private String NAME1;//名称
	
	private String POSID;//期间内的相关日
	private String POSKI;
	private String PS_PSP_PNR;
	private String SOBKZ;//特殊库存标识
	private String VGART;//业务处理/事件类型
	
	private String WERKS;//工厂
	private String ZEILE;//物料凭证中的项目
	public String getAUFNR() {
		return AUFNR;
	}
	public void setAUFNR(String aUFNR) {
		AUFNR = aUFNR;
	}
	public String getBLDAT() {
		return BLDAT;
	}
	public void setBLDAT(String bLDAT) {
		BLDAT = bLDAT;
	}
	public String getBTEXT() {
		return BTEXT;
	}
	public void setBTEXT(String bTEXT) {
		BTEXT = bTEXT;
	}
	public String getBUDAT() {
		return BUDAT;
	}
	public void setBUDAT(String bUDAT) {
		BUDAT = bUDAT;
	}
	public String getBWART() {
		return BWART;
	}
	public void setBWART(String bWART) {
		BWART = bWART;
	}
	public String getBWTAR() {
		return BWTAR;
	}
	public void setBWTAR(String bWTAR) {
		BWTAR = bWTAR;
	}
	public String getCHARG() {
		return CHARG;
	}
	public void setCHARG(String cHARG) {
		CHARG = cHARG;
	}
	public String getCPUTM() {
		return CPUTM;
	}
	public void setCPUTM(String cPUTM) {
		CPUTM = cPUTM;
	}
	public String getDMBTR() {
		return DMBTR;
	}
	public void setDMBTR(String dMBTR) {
		DMBTR = dMBTR;
	}
	public String getEBELN() {
		return EBELN;
	}
	public void setEBELN(String eBELN) {
		EBELN = eBELN;
	}
	public String getERFME() {
		return ERFME;
	}
	public void setERFME(String eRFME) {
		ERFME = eRFME;
	}
	public String getERFMG() {
		return ERFMG;
	}
	public void setERFMG(String eRFMG) {
		ERFMG = eRFMG;
	}
	public String getKOSTL() {
		return KOSTL;
	}
	public void setKOSTL(String kOSTL) {
		KOSTL = kOSTL;
	}
	public String getKZBEW() {
		return KZBEW;
	}
	public void setKZBEW(String kZBEW) {
		KZBEW = kZBEW;
	}
	public String getKZVBR() {
		return KZVBR;
	}
	public void setKZVBR(String kZVBR) {
		KZVBR = kZVBR;
	}
	public String getKZZUG() {
		return KZZUG;
	}
	public void setKZZUG(String kZZUG) {
		KZZUG = kZZUG;
	}
	public String getLGORT() {
		return LGORT;
	}
	public void setLGORT(String lGORT) {
		LGORT = lGORT;
	}
	public String getLIFNR() {
		return LIFNR;
	}
	public void setLIFNR(String lIFNR) {
		LIFNR = lIFNR;
	}
	public String getMAKTX() {
		return MAKTX;
	}
	public void setMAKTX(String mAKTX) {
		MAKTX = mAKTX;
	}
	public String getMATNR() {
		return MATNR;
	}
	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}
	public String getMBLNR() {
		return MBLNR;
	}
	public void setMBLNR(String mBLNR) {
		MBLNR = mBLNR;
	}
	public String getMENGE() {
		return MENGE;
	}
	public void setMENGE(String mENGE) {
		MENGE = mENGE;
	}
	public String getMJAHR() {
		return MJAHR;
	}
	public void setMJAHR(String mJAHR) {
		MJAHR = mJAHR;
	}
	public String getNAME1() {
		return NAME1;
	}
	public void setNAME1(String nAME1) {
		NAME1 = nAME1;
	}
	public String getPOSID() {
		return POSID;
	}
	public void setPOSID(String pOSID) {
		POSID = pOSID;
	}
	public String getPOSKI() {
		return POSKI;
	}
	public void setPOSKI(String pOSKI) {
		POSKI = pOSKI;
	}
	public String getPS_PSP_PNR() {
		return PS_PSP_PNR;
	}
	public void setPS_PSP_PNR(String pS_PSP_PNR) {
		PS_PSP_PNR = pS_PSP_PNR;
	}
	public String getSOBKZ() {
		return SOBKZ;
	}
	public void setSOBKZ(String sOBKZ) {
		SOBKZ = sOBKZ;
	}
	public String getVGART() {
		return VGART;
	}
	public void setVGART(String vGART) {
		VGART = vGART;
	}
	public String getWERKS() {
		return WERKS;
	}
	public void setWERKS(String wERKS) {
		WERKS = wERKS;
	}
	public String getZEILE() {
		return ZEILE;
	}
	public void setZEILE(String zEILE) {
		ZEILE = zEILE;
	}
}
