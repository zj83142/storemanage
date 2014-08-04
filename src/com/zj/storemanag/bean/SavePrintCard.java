package com.zj.storemanag.bean;

import java.util.List;

import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;

public class SavePrintCard {

	private String Num = "Num";// 卡片号
	private String PR_MATERIAL = "PR_MATERIAL";// 物料号
	private String PR_STGE_LOC2 = "PR_STGE_LOC2";// 仓位号
	private String PR_MESSAGE = "PR_MESSAGE";// 物料描述
	private String PR_ENTRY_UOM = "PR_ENTRY_UOM";// 计量单位
	private String PR_PLANT2 = "PR_PLANT2";// 存储点描述
	private String KC_H = "KC_H";// 最高库存
	private String KC_L = "KC_L";// 最低库存
	private String StandPrice = "StandPrice";// 标准价格
	private String Count = "Count";// 收货数量
	private String nAME2 = "nAME2";// 供应商
	private String Create_ID = "Create_ID";// 保管员
	private String InTime = "InTime";// 入库时间
	private String PR_PLANT = "PR_PLANT";
	private String PR_STGE_LOC = "PR_STGE_LOC";

	public String getNum() {
		return Num;
	}

	public void setNum(String num) {
		Num = getNum() + "=" + num;
	}

	public String getPR_MATERIAL() {
		return PR_MATERIAL;
	}

	public void setPR_MATERIAL(String pR_MATERIAL) {
		PR_MATERIAL = getPR_MATERIAL() + "=" + pR_MATERIAL;
	}

	public String getPR_STGE_LOC2() {
		return PR_STGE_LOC2;
	}

	public void setPR_STGE_LOC2(String pR_STGE_LOC2) {
		PR_STGE_LOC2 = getPR_STGE_LOC2() + "=" + pR_STGE_LOC2;
	}

	public String getPR_MESSAGE() {
		return PR_MESSAGE;
	}

	public void setPR_MESSAGE(String pR_MESSAGE) {
		PR_MESSAGE =  pR_MESSAGE;
	}

	public String getPR_ENTRY_UOM() {
		return PR_ENTRY_UOM;
	}

	public void setPR_ENTRY_UOM(String pR_ENTRY_UOM) {
		PR_ENTRY_UOM = getPR_ENTRY_UOM() + "=" + pR_ENTRY_UOM;
	}

	public String getPR_PLANT2() {
		return PR_PLANT2;
	}

	public void setPR_PLANT2(String pR_PLANT2) {
		PR_PLANT2 = getPR_PLANT2() + "=" + pR_PLANT2;
	}

	public String getKC_H() {
		return KC_H;
	}

	public void setKC_H(String kC_H) {
		KC_H = getKC_H() + "=" + kC_H;
	}

	public String getKC_L() {
		return KC_L;
	}

	public void setKC_L(String kC_L) {
		KC_L = getKC_L() + "=" + kC_L;
	}

	public String getStandPrice() {
		return StandPrice;
	}

	public void setStandPrice(String standPrice) {
		StandPrice = getStandPrice() + "=" + standPrice;
	}

	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = getCount() + "=" + count;
	}

	public String getnAME2() {
		return nAME2;
	}

	public void setnAME2(String nAME2) {
		this.nAME2 = getnAME2() + "=" + nAME2;
	}

	public String getCreate_ID() {
		return Create_ID;
	}

	public void setCreate_ID(String create_ID) {
		Create_ID = getCreate_ID() + "=" + create_ID;
	}

	public String getInTime() {
		return InTime;
	}

	public void setInTime(String inTime) {
		InTime = getInTime() + "=" + inTime;
	}
	public String getPR_PLANT() {
		return PR_PLANT;
	}
	public void setPR_PLANT(String pR_PLANT) {
		PR_PLANT = getPR_PLANT() + "=" + pR_PLANT;
	}

	public String getPR_STGE_LOC() {
		return PR_STGE_LOC;
	}

	public void setPR_STGE_LOC(String pR_STGE_LOC) {
		PR_STGE_LOC = getPR_STGE_LOC() + "=" + pR_STGE_LOC;
	}

	private List<SavePrintCard> saveTemps = null;

	public List<SavePrintCard> getSaveTemps() {
		return saveTemps;
	}

	public void setSaveTemps(List<SavePrintCard> saveTemps) {
		this.saveTemps = saveTemps;
	}

	// public String getSendParam() {
	// StringBuffer stringBuffer = new StringBuffer();
	// stringBuffer.append("<Toolkit>");
	// if (saveTemps != null)
	// for (SavePrintCard temp : saveTemps) {
	// stringBuffer.append("<RFIDinfo>");
	// stringBuffer.append(UrlUtil.manageNode(temp.getPR_MATERIAL()));
	// stringBuffer.append(UrlUtil.manageNode(temp.getPR_ENTRY_UOM()));
	// stringBuffer.append(UrlUtil.manageNode(temp.getPR_MESSAGE()));
	// stringBuffer.append(UrlUtil.manageNode(temp.getPR_PLANT()));
	// stringBuffer.append(UrlUtil.manageNode(temp.getPR_RFID_NO()));
	// stringBuffer.append(UrlUtil.manageNode(temp.getPR_PLANT2()));
	// stringBuffer.append(UrlUtil.manageNode(temp.getPR_STGE_LOC()));
	// stringBuffer.append(UrlUtil.manageNode(temp.getPR_STGE_LOC2()));
	// stringBuffer.append("</RFIDinfo>");
	// }
	// stringBuffer.append("</Toolkit>");
	// return stringBuffer.toString();
	// }

	public String getSavePrintCardInfo(SavePrintCard temp) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<Toolkit>");
		if (temp != null){
			stringBuffer.append("<RFidInitInfo>");
			stringBuffer.append(UrlUtil.manageNode(temp.getNum()));
			stringBuffer.append(UrlUtil.manageNode(temp.getPR_MATERIAL()));
			stringBuffer.append(UrlUtil.manageNode(temp.getPR_STGE_LOC2()));
			String msg = StrUtil.filterStr(temp.getPR_MESSAGE());
			if(!StrUtil.isNotEmpty(msg)){
				msg = "无";
			}
			stringBuffer.append("<PR_MESSAGE>"+temp.getPR_MESSAGE()+"</PR_MESSAGE>");
			stringBuffer.append(UrlUtil.manageNode(temp.getPR_ENTRY_UOM()));
			stringBuffer.append(UrlUtil.manageNode(temp.getPR_PLANT2()));
			stringBuffer.append(UrlUtil.manageNode(temp.getKC_H()));
			stringBuffer.append(UrlUtil.manageNode(temp.getKC_L()));
			stringBuffer.append(UrlUtil.manageNode(temp.getStandPrice()));
			stringBuffer.append(UrlUtil.manageNode(temp.getCount()));
			stringBuffer.append(UrlUtil.manageNode(temp.getnAME2()));
			stringBuffer.append(UrlUtil.manageNode(temp.getCreate_ID()));
			stringBuffer.append(UrlUtil.manageNode(temp.getInTime()));
			stringBuffer.append(UrlUtil.manageNode(temp.getPR_PLANT()));
			stringBuffer.append(UrlUtil.manageNode(temp.getPR_STGE_LOC()));
			stringBuffer.append("</RFidInitInfo>");
		}
		stringBuffer.append("</Toolkit>");
		return stringBuffer.toString();
	}

}
