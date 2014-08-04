package com.zj.storemanag.bean;

import java.io.Serializable;

public class ProofInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//凭证信息
	private String MATCODE;//物料凭证编号
	private String MATYEAR;//物料凭证的年份
	
	private String FIELD;//参数中的字段
	private String ID;//消息，消息类
	private String lOG_MSG_NO;//应用日志：内部邮件序列号
	private String lOG_NO;//应用程序日志：日志号
	private String MESSAGE;//消息文本.
	private String MESSAGE_V1;//消息,消息变量1
	private String MESSAGE_V2;//消息,消息变量2
	private String MESSAGE_V3;//消息,消息变量3
	private String MESSAGE_V4;//消息,消息变量4
	private String NUMBER;//消息编号
	private String PARAMETER;//参数中的行
	private String ROW;//消息,消息变量4
	private String SYSTEM;//引发消息的逻辑系统-
	private String TYPE;//消息类型：S 成功，E错误，W 警告，I 信息，A中止
	public String getMATCODE() {
		return MATCODE;
	}
	public void setMATCODE(String mATCODE) {
		MATCODE = mATCODE;
	}
	public String getMATYEAR() {
		return MATYEAR;
	}
	public void setMATYEAR(String mATYEAR) {
		MATYEAR = mATYEAR;
	}
	public String getFIELD() {
		return FIELD;
	}
	public void setFIELD(String fIELD) {
		FIELD = fIELD;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getlOG_MSG_NO() {
		return lOG_MSG_NO;
	}
	public void setlOG_MSG_NO(String lOG_MSG_NO) {
		this.lOG_MSG_NO = lOG_MSG_NO;
	}
	public String getlOG_NO() {
		return lOG_NO;
	}
	public void setlOG_NO(String lOG_NO) {
		this.lOG_NO = lOG_NO;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	public String getMESSAGE_V1() {
		return MESSAGE_V1;
	}
	public void setMESSAGE_V1(String mESSAGE_V1) {
		MESSAGE_V1 = mESSAGE_V1;
	}
	public String getMESSAGE_V2() {
		return MESSAGE_V2;
	}
	public void setMESSAGE_V2(String mESSAGE_V2) {
		MESSAGE_V2 = mESSAGE_V2;
	}
	public String getMESSAGE_V3() {
		return MESSAGE_V3;
	}
	public void setMESSAGE_V3(String mESSAGE_V3) {
		MESSAGE_V3 = mESSAGE_V3;
	}
	public String getMESSAGE_V4() {
		return MESSAGE_V4;
	}
	public void setMESSAGE_V4(String mESSAGE_V4) {
		MESSAGE_V4 = mESSAGE_V4;
	}
	public String getNUMBER() {
		return NUMBER;
	}
	public void setNUMBER(String nUMBER) {
		NUMBER = nUMBER;
	}
	public String getPARAMETER() {
		return PARAMETER;
	}
	public void setPARAMETER(String pARAMETER) {
		PARAMETER = pARAMETER;
	}
	public String getROW() {
		return ROW;
	}
	public void setROW(String rOW) {
		ROW = rOW;
	}
	public String getSYSTEM() {
		return SYSTEM;
	}
	public void setSYSTEM(String sYSTEM) {
		SYSTEM = sYSTEM;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	

}
