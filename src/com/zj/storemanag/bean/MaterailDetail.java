package com.zj.storemanag.bean;

public class MaterailDetail {
	private String BSTMA;//最大批量
	private String BSTMI;//最小批量
	private String MAKTX;//物料描述
	private String MATNR;//物料号
//	private String MEINS;//基本计量单位
	private String unit;//计量单位
	private String MFRNR;//制造商号码
	private String MFRPN;//制造商零件号
	private String NORMT;//工业标准描述
	private String STPRS;//标准价格
	public String getBSTMA() {
		return BSTMA;
	}
	public void setBSTMA(String bSTMA) {
		BSTMA = bSTMA;
	}
	public String getBSTMI() {
		return BSTMI;
	}
	public void setBSTMI(String bSTMI) {
		BSTMI = bSTMI;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getMFRNR() {
		return MFRNR;
	}
	public void setMFRNR(String mFRNR) {
		MFRNR = mFRNR;
	}
	public String getMFRPN() {
		return MFRPN;
	}
	public void setMFRPN(String mFRPN) {
		MFRPN = mFRPN;
	}
	public String getNORMT() {
		return NORMT;
	}
	public void setNORMT(String nORMT) {
		NORMT = nORMT;
	}
	public String getSTPRS() {
		return STPRS;
	}
	public void setSTPRS(String sTPRS) {
		STPRS = sTPRS;
	}
}
