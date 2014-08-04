package com.zj.storemanag.commen;

import log.Log;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.CVersion;
import com.zj.storemanag.bean.User;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.TimeUtil;

/***
 * 4500020228
 * 
 * @author zhoujing 2014-5-27 下午4:13:49
 */
public class BaseActivity extends FragmentActivity implements OnClickListener {

	public static final String MIME_TEXT_PLAIN = "text/plain";

	public MyApplication app;

	public SharedPreferences spf;

	public int request_Code = 1;
	public int result_Code = 0;

	public static final String pageSize = "8";// 分页加载数据，每页条数

	public MyApplication getApp() {
		if (app == null) {
			return (MyApplication) getApplication();
		}
		return app;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		app = (MyApplication) getApplication();

		spf = getSharedPreferences("spf_Info", Context.MODE_PRIVATE);

		initAddress();
	}

	public void setFlag(int flag) {
		savePreferences("FLAG", flag);
		getApp().setFLAG(flag);
	}

	public int getFlag() {
		if (getApp().getFLAG() != 0) {
			return getApp().getFLAG();
		} else {
			return getPreferenceInt("FLAG");
		}
	}

	public void setUser(User user) {
		getApp().setUser(user);
		user.setLoginTime(TimeUtil.getSystemTimeStr());
		savePreferences("userId", user.getUserId());
		savePreferences("userIdMD5", user.getPwdMD5());
		savePreferences("userName", user.getUserName());
		savePreferences("pwd", user.getPwd());
		savePreferences("loginTime", user.getLoginTime());
	}

	public User getUser() {
		User user = getApp().getUser();
		if (user != null) {
			return user;
		} else {
			user = new User();
			String userId = getPreference("userId");
			if (StrUtil.isNotEmpty(userId)) {
				user.setUserId(StrUtil.filterStr(getPreference("userId")));
				user.setUserIdMD5(StrUtil.filterStr(getPreference("userIdMD5")));
				user.setUserName(StrUtil.filterStr(getPreference("userName")));
				user.setPwd(StrUtil.filterStr(getPreference("pwd")));
				user.setLoginTime(StrUtil.filterStr(getPreference("loginTime")));
				return user;
			}
		}
		return null;
	}

	/** 初始化访问地址 */
	private void initAddress() {
		String url = getPreference("url");
		if (!StrUtil.isNotEmpty(url)) {
			url = ParamsUtil.defaultIP;
		}
		getApp().setUrl(url);
		savePreferences("url", url);
	}

	public String getVersionName() {
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			return packInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("zj", e.toString());
		}
		return null;
	}

	public boolean judgeVersion(CVersion versionInfo) {
		try {
			String oldVersionName = getVersionName();
			String newVersion = versionInfo.getVersionNum();
			if (StrUtil.isNotEmpty(oldVersionName)
					&& !oldVersionName.equals(newVersion)) {
				if (Double.parseDouble(oldVersionName) < Double
						.parseDouble(newVersion)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 判断网络是否正常
	 * 
	 * @return
	 */
	public boolean isConnectInternet() {
		ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) { // 注意，这个判断一定要的哦，要不然会出错
			return networkInfo.isAvailable();
		}
		MyTip.showToast(getApplicationContext(),
				getString(R.string.net_error_tip));
		return false;
	}

	/**
	 * 添加activity
	 * 
	 * @param act
	 */
	public void addThis(Activity act) {
		getApp().actions.add(act);
	}

	/**
	 * 退出程序
	 */
	public void exit() {
		for (Activity temp : getApp().actions) {
			temp.finish();
		}
		getApp().actions.clear();
	}

	/** 保存参数设置 */
	public boolean savePreferences(String key, int value) {
		Editor editor = spf.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	/** 获取参数设置 */
	public int getPreferenceInt(String key) {
		return spf.getInt(key, -1);
	}

	/** 获取参数设置 */
	public String getPreference(String key) {
		return spf.getString(key, "");
	}

	/** 获取参数设置 */
	public Boolean getPreferenceBoolean(String key) {
		return spf.getBoolean(key, false);
	}

	/** 保存参数设置 */
	public boolean savePreferences(String key, String value) {
		Editor editor = spf.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/** 保存参数设置 */
	public boolean savePreferences(String key, Boolean value) {
		Editor editor = spf.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public void clearPwdPreference() {
		savePreferences("userLoginPwd", "");
	}

	public void clearPreference() {
		spf.edit().clear().commit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
