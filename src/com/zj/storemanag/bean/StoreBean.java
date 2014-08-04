package com.zj.storemanag.bean;

import java.io.Serializable;

/**
 * 获取库存  GetKC
 * @author zhoujing
 * 2014-5-16 下午3:31:07
 */
public class StoreBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String MATNR;//物料编号
	private String MAKTX;//物料描述
//	private String WERKS;//工厂编号
	private String factory;
	private String NAME1;//
	
	private String MTART;//物料类型
	private String MTBEZ;//物料类型描述
//	private String LGORT;//库位
	private String store;
	
	private String LGOBE;//使用单位、工厂 
	private String LGPBE;//仓位/库位号（10位编号）
//	private String MEINS;//计量单位
	private String unit;

	private String LABST;//非限估价库存/非限制使用库存
	private String KLABS;//  非限制使用的寄售库存
	private String EINME;//受限制总计库存
	private String KEINM;//限制寄售库存
	public String getMATNR() {
		return MATNR;
	}
	public void setMATNR(String mATNR) {
		MATNR = mATNR;
	}
	public String getMAKTX() {
		return MAKTX;
	}
	public void setMAKTX(String mAKTX) {
		MAKTX = mAKTX;
	}
	public String getNAME1() {
		return NAME1;
	}
	public void setNAME1(String nAME1) {
		NAME1 = nAME1;
	}
	public String getMTART() {
		return MTART;
	}
	public void setMTART(String mTART) {
		MTART = mTART;
	}
	public String getMTBEZ() {
		return MTBEZ;
	}
	public void setMTBEZ(String mTBEZ) {
		MTBEZ = mTBEZ;
	}
	public String getLGOBE() {
		return LGOBE;
	}
	public void setLGOBE(String lGOBE) {
		LGOBE = lGOBE;
	}
	public String getLGPBE() {
		return LGPBE;
	}
	public void setLGPBE(String lGPBE) {
		LGPBE = lGPBE;
	}
	public String getLABST() {
		return LABST;
	}
	public void setLABST(String lABST) {
		LABST = lABST;
	}
	public String getKLABS() {
		return KLABS;
	}
	public void setKLABS(String kLABS) {
		KLABS = kLABS;
	}
	public String getEINME() {
		return EINME;
	}
	public void setEINME(String eINME) {
		EINME = eINME;
	}
	public String getKEINM() {
		return KEINM;
	}
	public void setKEINM(String kEINM) {
		KEINM = kEINM;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}
