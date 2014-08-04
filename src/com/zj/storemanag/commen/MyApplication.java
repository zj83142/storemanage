package com.zj.storemanag.commen;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.zj.storemanag.bean.User;
import com.zj.storemanag.util.Utils;

public class MyApplication extends Application {
	public boolean isDown;
	public boolean isRun;
	
	public User user;
	
	public String url;
	
	public int FLAG = 0;
	
	public int position = -1;
	
	public List<Activity> actions = new ArrayList<Activity>();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		
		Utils.createFiledir();// 创建根目录
		
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getFLAG() {
		return FLAG;
	}

	public void setFLAG(int fLAG) {
		FLAG = fLAG;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	
	

}
