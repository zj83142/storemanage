package com.zj.storemanag.bean;

public class BaseInfo {
	
	private String id;
	private String value;//值
	private String name;//名字
	private String type;//类型，1：库位 2：工厂
	private String memo;//库位 f
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Object[] getSaveParams() {
		return new Object[]{value,name,type,memo};
	}

}
