package com.zj.storemanag.bean;

public class PopItem {
	
	private String id;
	private String type;
	private String name;
	private boolean state;//状态，是否选择
	private String memo;//备注
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	public PopItem(){
		
	}
	
	public PopItem(String id, String type, String name, boolean state,
			String memo) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.state = state;
		this.memo = memo;
	}
	

}
