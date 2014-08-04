package com.zj.storemanag.bean;

import com.zj.storemanag.util.UrlUtil;

/**
 * 查询出入库信息的条件
 * 
 * @author zhoujing 2014-5-16 上午9:48:43
 */
public class StoreHistoryOrder {

	private String userID = "userID";
	private String bDate = "bDate";
	private String eDate = "eDate";
	private String matnr = "matnr";
	private String werk = "werk";
	private String lgort = "lgort";
	private String vendor = "vendor";
	private String custom = "custom";
	private String bwart = "bwart";
	private String sobkz = "sobkz";

	public String getUserId() {
		return userID;
	}

	public void setUserId(String userID) {
		this.userID = getUserId() + "=" + userID;
	}

	public String getbDate() {
		return bDate;
	}

	public void setbDate(String bDate) {
		this.bDate = getbDate() + "=" + bDate;
	}

	public String geteDate() {
		return eDate;
	}

	public void seteDate(String eDate) {
		this.eDate = geteDate() + "=" + eDate;
	}

	public String getMatnr() {
		return matnr;
	}

	public void setMatnr(String matnr) {
		this.matnr = getMatnr() + "=" + matnr;
	}

	public String getWerk() {
		return werk;
	}

	public void setWerk(String werk) {
		this.werk = getWerk() + "=" + werk;
	}

	public String getLgort() {
		return lgort;
	}

	public void setLgort(String lgort) {
		this.lgort = getLgort() + "=" + lgort;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = getVendor() + "=" + vendor;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = getCustom() + "=" + custom;
	}

	public String getBwart() {
		return bwart;
	}

	public void setBwart(String bwart) {
		this.bwart = getBwart() + "=" + bwart;
	}

	public String getSobkz() {
		return sobkz;
	}

	public void setSobkz(String sobkz) {
		this.sobkz = getSobkz() + "=" + sobkz;
	}

	public String getSendParam(StoreHistoryOrder bean) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<Toolkit>");
		if (bean != null)
			stringBuffer.append("<PARAMS>");
		stringBuffer.append(UrlUtil.manageNode(bean.getUserId()));
		stringBuffer.append(UrlUtil.manageNode(bean.getbDate()));
		stringBuffer.append(UrlUtil.manageNode(bean.geteDate()));
		stringBuffer.append(UrlUtil.manageNode(bean.getMatnr()));
		stringBuffer.append(UrlUtil.manageNode(bean.getWerk()));
		stringBuffer.append(UrlUtil.manageNode(bean.getLgort()));
		stringBuffer.append(UrlUtil.manageNode(bean.getVendor()));
		stringBuffer.append(UrlUtil.manageNode(bean.getCustom()));
		stringBuffer.append(UrlUtil.manageNode(bean.getBwart()));
		stringBuffer.append(UrlUtil.manageNode(bean.getSobkz()));
		stringBuffer.append("</PARAMS>");
		stringBuffer.append("</Toolkit>");
		return stringBuffer.toString();
	}

}
