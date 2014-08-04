package com.zj.storemanag.bean;

import java.io.Serializable;

import com.zj.storemanag.util.StrUtil;

public class RFIDinfo implements Serializable{
	
	/**
	 * Rfid
	 */
	private static final long serialVersionUID = 1L;
	private String PR_MATERIAL;//物料号
//	private String PR_ENTRY_UOM;//计量单位
	private String unit;
	private String PR_MESSAGE;//物料描述
//	private String PR_PLANT;//工厂/车间代号
	private String factory;
	
	private String PR_RFID_NO;//RfID编号
	private String PR_PLANT2;//工厂/车间编号
//	private String PR_STGE_LOC;//库位代号
	private String store;
	private String PR_STGE_LOC2;//库位编号
	public String getPR_MATERIAL() {
		return PR_MATERIAL;
	}
	public void setPR_MATERIAL(String pR_MATERIAL) {
		this.PR_MATERIAL = pR_MATERIAL;
	}
	public String getPR_MESSAGE() {
		return PR_MESSAGE;
	}
	public void setPR_MESSAGE(String pR_MESSAGE) {
		this.PR_MESSAGE = pR_MESSAGE;
	}
	public String getPR_RFID_NO() {
		return PR_RFID_NO;
	}
	public void setPR_RFID_NO(String pR_RFID_NO) {
		this.PR_RFID_NO = pR_RFID_NO;
	}
	public String getPR_PLANT2() {
		return PR_PLANT2;
	}
	public void setPR_PLANT2(String pR_PLANT2) {
		this.PR_PLANT2 = pR_PLANT2;
	}
	public String getPR_STGE_LOC2() {
		return PR_STGE_LOC2;
	}
	public void setPR_STGE_LOC2(String pR_STGE_LOC2) {
		this.PR_STGE_LOC2 = pR_STGE_LOC2;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
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
	
}
