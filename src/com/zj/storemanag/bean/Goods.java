package com.zj.storemanag.bean;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.TimeUtil;

public class Goods implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String VEND_NAME;//供应商
	private String MATERIAL;//物料
	private String MESSAGE;//物料描述
	private String ENTRY_QNT;
	private String ENTRY_QNTSpecified;
//	private String ENTRY_UOM;//单位
	private String unit;
	private String IsSelected;
	private String IsSelectedSpecified;
//	private String PLANT;//工厂
	private String factory;
	private String RFID;
//	private String STGE_LOC;//库位
	private String store;
	private String TotalCount;
	private String TotalCountSpecified;
	private String otherField1;//采购单
	private String otherField2;//采购文件的项目编号
	private String state;//0为选中，1为选中
	private String isInit;//为1时初始化过 否则表示为初始化过
	
	private String proof;
	private String year;
	
	private String updateTime;//用于排序
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVEND_NAME() {
		return VEND_NAME;
	}
	public void setVEND_NAME(String vEND_NAME) {
		VEND_NAME = vEND_NAME;
	}
	public String getMATERIAL() {
		return MATERIAL;
	}
	public void setMATERIAL(String mATERIAL) {
		MATERIAL = mATERIAL;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	public String getENTRY_QNT() {
		return ENTRY_QNT;
	}
	public void setENTRY_QNT(String eNTRY_QNT) {
		ENTRY_QNT = eNTRY_QNT;
	}
	public String getENTRY_QNTSpecified() {
		return ENTRY_QNTSpecified;
	}
	public void setENTRY_QNTSpecified(String eNTRY_QNTSpecified) {
		ENTRY_QNTSpecified = eNTRY_QNTSpecified;
	}
	
	public String getIsSelected() {
		return IsSelected;
	}
	public void setIsSelected(String isSelected) {
		IsSelected = isSelected;
	}
	public String getIsSelectedSpecified() {
		return IsSelectedSpecified;
	}
	public void setIsSelectedSpecified(String isSelectedSpecified) {
		IsSelectedSpecified = isSelectedSpecified;
	}
	public String getRFID() {
		return RFID;
	}
	public void setRFID(String rFID) {
		RFID = rFID;
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
	public String getTotalCount() {
		if(StrUtil.isNotEmpty(TotalCount)){
			return TotalCount;
		}else{
			return "0";
		}
	}
	public void setTotalCount(String totalCount) {
		TotalCount = totalCount;
	}
	public String getTotalCountSpecified() {
		return TotalCountSpecified;
	}
	
	public String getOtherField1() {
		return otherField1;
	}
	public void setOtherField1(String otherField1) {
		this.otherField1 = otherField1;
	}
	public String getOtherField2() {
		return otherField2;
	}
	public void setOtherField2(String otherField2) {
		this.otherField2 = otherField2;
	}
	public void setTotalCountSpecified(String totalCountSpecified) {
		TotalCountSpecified = totalCountSpecified;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getIsInit() {
		return isInit;
	}
	public void setIsInit(String isInit) {
		this.isInit = isInit;
	}
	
	public String getProof() {
		return proof;
	}
	public void setProof(String proof) {
		this.proof = proof;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Object[] getSaveParams() {
		return new Object[]{VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified,unit,IsSelected,IsSelectedSpecified,factory,RFID,store,TotalCount,TotalCountSpecified,otherField1,otherField2,state,isInit, TimeUtil.getSystemTimeStr()};
	}
	public Object[] getUpdateParams(boolean sort) {
		if(sort){
			return new Object[]{VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified,unit,IsSelected,IsSelectedSpecified,factory,RFID,store,TotalCount,TotalCountSpecified,otherField1,otherField2,state,TimeUtil.getSystemTimeStr(),id};
		}else{
			return new Object[]{VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified,unit,IsSelected,IsSelectedSpecified,factory,RFID,store,TotalCount,TotalCountSpecified,otherField1,otherField2,state,id};
		}
	}
}
