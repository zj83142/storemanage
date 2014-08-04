package com.zj.storemanag.bean;

public class CVersion {
	private String urlStr;
	private String versionName;
	private String versionNum;
	public String getUrlStr() {
		return urlStr;
	}
	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}
	@Override
	public String toString() {
		return "VersionInfo [urlStr=" + urlStr + ", versionName=" + versionName
				+ ", versionNum=" + versionNum + "]";
	}
}
