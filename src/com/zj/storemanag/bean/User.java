package com.zj.storemanag.bean;

public class User {
	
	private String userId;
	private String userIdMD5;
	private String userName;
	private String pwd;
	private String pwdMD5;
	private String loginTime;
	
	public String getUserId() {
		return userId;
	}
	public String getUserIdMD5() {
		return userIdMD5;
	}
	public void setUserIdMD5(String userIdMD5) {
		this.userIdMD5 = userIdMD5;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPwdMD5() {
		return pwdMD5;
	}
	public void setPwdMD5(String pwdMD5) {
		this.pwdMD5 = pwdMD5;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public Object[] getSaveParams() {
		//user_id, login_name, login_pwd, login_pwd_md5, type, login_time
		return new Object[]{userId, userName, pwd, pwdMD5,loginTime};
	}
}
