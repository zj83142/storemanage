package com.zj.storemanag.bean;

public class GoodsList {
	
	private String MATERIAL;//物料编号
	private String MESSAGE;//物料描述
	private String PLANT;//工厂/车间
	private String RFID;//RFID号
	
	private String STGE_LOC;//库位
	private String ENTRY_QNT;//数量
	private String ENTRY_UOM;//计量单位
	private String ENTRY_QNTSpecified;
	
	private String IsSelected;
	private String IsSelectedSpecified;
	private String TotalCount;
	private String TotalCountSpecified;
	private String otherField1;
	private String otherField2;
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
	public String getPLANT() {
		return PLANT;
	}
	public void setPLANT(String pLANT) {
		PLANT = pLANT;
	}
	public String getRFID() {
		return RFID;
	}
	public void setRFID(String rFID) {
		RFID = rFID;
	}
	public String getSTGE_LOC() {
		return STGE_LOC;
	}
	public void setSTGE_LOC(String sTGE_LOC) {
		STGE_LOC = sTGE_LOC;
	}
	public String getENTRY_QNT() {
		return ENTRY_QNT;
	}
	public void setENTRY_QNT(String eNTRY_QNT) {
		ENTRY_QNT = eNTRY_QNT;
	}
	public String getENTRY_UOM() {
		return ENTRY_UOM;
	}
	public void setENTRY_UOM(String eNTRY_UOM) {
		ENTRY_UOM = eNTRY_UOM;
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
	public String getTotalCount() {
		return TotalCount;
	}
	public void setTotalCount(String totalCount) {
		TotalCount = totalCount;
	}
	public String getTotalCountSpecified() {
		return TotalCountSpecified;
	}
	public void setTotalCountSpecified(String totalCountSpecified) {
		TotalCountSpecified = totalCountSpecified;
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
	
}
