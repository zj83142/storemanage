package com.zj.storemanag.bean;

import java.util.List;

import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class SaveRFIDinfo {

	private String PR_MATERIAL = "PR_MATERIAL";// 物料号
	private String PR_ENTRY_UOM = "PR_ENTRY_UOM";// 计量单位
	private String PR_MESSAGE = "PR_MESSAGE";// 物料描述
	private String PR_PLANT = "PR_PLANT";// 工厂/车间代号
	private String PR_RFID_NO = "PR_RFID_NO";// RfID编号
	private String PR_PLANT2 = "PR_PLANT2";// 工厂/车间编号
	private String PR_STGE_LOC = "PR_STGE_LOC";// 库位代号
	private String PR_STGE_LOC2 = "PR_STGE_LOC2";// 库位编号

	public String getPR_MATERIAL() {
		return PR_MATERIAL;
	}

	public void setPR_MATERIAL(String pR_MATERIAL) {
		PR_MATERIAL = getPR_MATERIAL() + "=" + pR_MATERIAL;
	}

	public String getPR_ENTRY_UOM() {
		return PR_ENTRY_UOM;
	}

	public void setPR_ENTRY_UOM(String pR_ENTRY_UOM) {
		PR_ENTRY_UOM = getPR_ENTRY_UOM() + "=" + pR_ENTRY_UOM;
	}

	public String getPR_MESSAGE() {
		return PR_MESSAGE;
	}
	public void setPR_MESSAGE(String pR_MESSAGE) {
		PR_MESSAGE = pR_MESSAGE;
	}


	public String getPR_PLANT() {
		return PR_PLANT;
	}

	public void setPR_PLANT(String pR_PLANT) {
		PR_PLANT = getPR_PLANT() + "=" + pR_PLANT;
	}

	public String getPR_RFID_NO() {
		return PR_RFID_NO;
	}

	public void setPR_RFID_NO(String pR_RFID_NO) {
		PR_RFID_NO = getPR_RFID_NO() + "=" + pR_RFID_NO;
	}

	public String getPR_PLANT2() {
		return PR_PLANT2;
	}

	public void setPR_PLANT2(String PR_PLANT2) {
		PR_PLANT2 = getPR_PLANT2() + "=" + PR_PLANT2;
	}

	public String getPR_STGE_LOC() {
		return PR_STGE_LOC;
	}

	public void setPR_STGE_LOC(String pR_STGE_LOC) {
		PR_STGE_LOC = getPR_STGE_LOC() + "=" + pR_STGE_LOC;
	}

	public String getPR_STGE_LOC2() {
		return PR_STGE_LOC2;
	}

	public void setPR_STGE_LOC2(String pR_STGE_LOC2) {
		PR_STGE_LOC2 = getPR_STGE_LOC2 () + "=" + pR_STGE_LOC2;
	}
	
	private List<SaveRFIDinfo> saveTemps = null;
	public List<SaveRFIDinfo> getSaveTemps() {
		return saveTemps;
	}
	public void setSaveTemps(List<SaveRFIDinfo> saveTemps) {
		this.saveTemps = saveTemps;
	}
	
	public String getSendParam() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<Toolkit>");
		if (saveTemps != null)
			for (SaveRFIDinfo temp : saveTemps) {
				stringBuffer.append("<RFIDinfo>");
				stringBuffer.append(UrlUtil.manageNode(temp.getPR_MATERIAL()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPR_ENTRY_UOM()));
				String msg = StrUtil.filterStr(temp.getPR_MESSAGE());
				if(!StrUtil.isNotEmpty(msg)){
					msg = "无";
				}
				stringBuffer.append("<PR_MESSAGE>"+temp.getPR_MESSAGE()+"</PR_MESSAGE>");
				stringBuffer.append(UrlUtil.manageNode(temp.getPR_PLANT()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPR_RFID_NO()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPR_PLANT2()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPR_STGE_LOC()));
				stringBuffer.append(UrlUtil.manageNode(temp.getPR_STGE_LOC2()));
				stringBuffer.append("</RFIDinfo>");
			}
		stringBuffer.append("</Toolkit>");
		return stringBuffer.toString();
	}

	private SaveRFIDinfo getBean(RFIDinfo rfidInfo){
		SaveRFIDinfo saveRFIDinfo = new SaveRFIDinfo();
		saveRFIDinfo.setPR_MATERIAL(StrUtil.filterStr(rfidInfo.getPR_MATERIAL()));
		saveRFIDinfo.setPR_ENTRY_UOM(StrUtil.slipValue(rfidInfo.getUnit()));
		
		saveRFIDinfo.setPR_MESSAGE(StrUtil.filterStr(rfidInfo.getPR_MESSAGE()));
		saveRFIDinfo.setPR_PLANT(StrUtil.slipValue(rfidInfo.getFactory()));
		saveRFIDinfo.setPR_RFID_NO(StrUtil.filterStr(rfidInfo.getPR_RFID_NO()));
		saveRFIDinfo.setPR_PLANT2(StrUtil.slipValue(rfidInfo.getPR_PLANT2()));
		saveRFIDinfo.setPR_STGE_LOC(StrUtil.slipValue(rfidInfo.getStore()));
		saveRFIDinfo.setPR_STGE_LOC2(StrUtil.slipValue(rfidInfo.getPR_STGE_LOC2()));
		return saveRFIDinfo;
	}
	
	public String getSaveRfidInfo(SaveRFIDinfo saveRFIDinfo) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<Toolkit>");
		if (saveRFIDinfo != null)
			stringBuffer.append("<RFIDinfo>");
			stringBuffer.append(UrlUtil.manageNode(saveRFIDinfo.getPR_MATERIAL()));
			stringBuffer.append(UrlUtil.manageNode(saveRFIDinfo.getPR_ENTRY_UOM()));
			String msg = StrUtil.filterStr(saveRFIDinfo.getPR_MESSAGE());
			if(!StrUtil.isNotEmpty(msg)){
				msg = "无";
			}
			stringBuffer.append("<PR_MESSAGE>"+msg+"</PR_MESSAGE>");
			stringBuffer.append(UrlUtil.manageNode(saveRFIDinfo.getPR_PLANT()));
			stringBuffer.append(UrlUtil.manageNode(saveRFIDinfo.getPR_RFID_NO()));
			stringBuffer.append(UrlUtil.manageNode(saveRFIDinfo.getPR_PLANT2()));
			stringBuffer.append(UrlUtil.manageNode(saveRFIDinfo.getPR_STGE_LOC()));
			stringBuffer.append(UrlUtil.manageNode(saveRFIDinfo.getPR_STGE_LOC2()));
			stringBuffer.append("</RFIDinfo>");
		stringBuffer.append("</Toolkit>");
		return stringBuffer.toString();
	}
	
	public String getSaveRfidInfo(RFIDinfo rfidInfo) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<Toolkit>");
		SaveRFIDinfo saveRFIDinfo = getBean(rfidInfo);
		return getSaveRfidInfo(saveRFIDinfo);
	}

}
