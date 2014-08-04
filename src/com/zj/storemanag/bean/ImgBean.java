package com.zj.storemanag.bean;

public class ImgBean {
	//_id, material, factory, store, path, userId, state
	
	private String id;
	private String material;
	private String factory;
	private String store;
	private String path;
	private String userId;
	private String state;
	
	private String cw;
	private String desp;
	private String unit;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCw() {
		return cw;
	}
	public void setCw(String cw) {
		this.cw = cw;
	}
	public String getDesp() {
		return desp;
	}
	public void setDesp(String desp) {
		this.desp = desp;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Object[] getInsertParams() {
		// TODO Auto-generated method stub
		return new Object[]{material, factory, store, path, userId, state, cw, desp, unit};
	}

}
