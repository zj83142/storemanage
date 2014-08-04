package com.zj.storemanag.activity;

import java.io.File;
import java.util.List;

import log.Log;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zj.storemanag.R;
import com.zj.storemanag.activity.main.MainActivity;
import com.zj.storemanag.bean.CVersion;
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.bean.User;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.dao.UserDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.AESUtil;
import com.zj.storemanag.util.MD5Util;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.TimeUtil;

/**
 * 登录
 * 
 * @author zhoujing 2014-5-27 上午10:10:58
 */
public class LoginActivity extends BaseActivity {

	private EditText userNameEt;
	private EditText pwdEt;
	private Intent it;

	private String userNameStr;
	private String pwdStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		addThis(this);

		if (!BaseInfoDao.getInstance(LoginActivity.this).judgeBasicExist(null)) {
			handler.sendEmptyMessage(ParamsUtil.INITBASICDATE);
		}
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 判断基础数据是否存在
		if (ParamsUtil.APP == 0) {
			userNameEt.setText("sa");
		} else {
			// 获取默认登录名信息
			String defaulUserName = StrUtil
					.filterStr(getPreference("default_login_name"));
			if (StrUtil.isNotEmpty(defaulUserName)) {
				userNameEt.setText(StrUtil.filterStr(defaulUserName));
			} else {
				// 如果没有设置默认登录名称则显示上次登录用户名
				userNameEt
						.setText(StrUtil.filterStr(getPreference("userName")));
			}
		}
	}

	private void initView() {
		Button selectUserBtn = (Button) findViewById(R.id.username_btn);
		selectUserBtn.setOnClickListener(this);
		userNameEt = (EditText) findViewById(R.id.login_username_et);
		pwdEt = (EditText) findViewById(R.id.login_pwd_et);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.login_btn:
			// 登录
			if (BaseInfoDao.getInstance(LoginActivity.this).judgeBasicExist(
					null)) {
				login();
			} else {
				MyTip.showToast(LoginActivity.this, "没有查询到基础数据，需要重新获取！");
				handler.sendEmptyMessage(ParamsUtil.INITBASICDATE);
			}
			break;
		case R.id.login_exit_btn:
			// 退出
			MyTip.showDialog(LoginActivity.this, "提出", "是否确定退出程序", handler,
					ParamsUtil.EXIT);
			break;
		case R.id.login_set_btn:
			// 设置
			it = new Intent(LoginActivity.this, SetActivity.class);
			startActivity(it);
			break;
		case R.id.username_btn:
			// 选择用户名
			List<PopItem> nameList = UserDao.getInstance(LoginActivity.this)
					.getUserLs();
			if (nameList != null && nameList.size() > 0) {
				MyTip.showPopView(LoginActivity.this, nameList, userNameEt,
						handler);
			} else {
				Log.i("zj", "没有查询到登录信息");
			}
			break;
		default:
			break;
		}
	}

	/** 登录 */
	private void login() {
		// TODO Auto-generated method stub
		userNameStr = StrUtil.filterStr(userNameEt.getText().toString());
		pwdStr = StrUtil.filterStr(pwdEt.getText().toString());

		if (!StrUtil.isNotEmpty(userNameStr)) {
			MyTip.showToast(LoginActivity.this, "请输入用户名！");
			return;
		}
		// if (!StrUtil.isNotEmpty(pwdStr)) {
		// MyTip.showToast(LoginActivity.this, "请输入密码！");
		// return;
		// }
		if (!StrUtil.isNotEmpty(pwdStr)) {
			pwdStr = "";
		}
		if (!isConnectInternet())
			return;
		MyTip.showProgress(LoginActivity.this, "登录", "登录中，请稍等……");
		AsyncService.Login login = AsyncService.getInstance().new Login();
		String userName = AESUtil.encrypt(userNameStr);
		String pwd = AESUtil.encrypt(pwdStr);
		login.execute(handler, ParamsUtil.LOGIN, getPreference("url"),
				userName, pwd);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.POP_SELECT:
				if (msg.obj != null
						&& msg.obj.getClass().isInstance(new PopItem())) {
					PopItem item = (PopItem) msg.obj;
					userNameEt.setText(item.getName());
				}
				break;
			case ParamsUtil.EXIT:
				boolean isSelect = msg.getData().getBoolean("select");
				if (isSelect) {
					exit();
				}
				break;
			case ParamsUtil.LOGIN:
				setLoginResult(msg.obj);
				break;
			case ParamsUtil.GETBASEDATA:
				setBaseInfoResult(msg.obj);
				break;
			case ParamsUtil.INITBASICDATE:
				if (!isConnectInternet())
					return;
				MyTip.showProgress(LoginActivity.this, "初始化",
						"正在初始化工厂库位数据，请稍候……");
				AsyncService.GetBaseData getBaseData = AsyncService
						.getInstance().new GetBaseData();
				getBaseData.execute(handler, ParamsUtil.GETBASEDATA, getApp()
						.getUrl(), LoginActivity.this);
				break;

			case ParamsUtil.CHECK_VERSION:
				// 检测更新
				setCheckVersionResult(msg.obj);
				break;
			case ParamsUtil.DOWNLOAD_NEW_VERSION:
				// 下载新版本
				boolean isDownload = msg.getData().getBoolean("select");
				Log.i("zj", "选择是否下载新版本：" + isDownload);
				if (isDownload) {
					// 下载
					if (isConnectInternet()) {
						cVersion.setUrlStr("http://" + getApp().getUrl() + "/"
								+ cVersion.getUrlStr().replace("../", ""));
						ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
						dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						dialog.setTitle("下载新版本");
						dialog.setMessage("正在下载新版本,请稍后...");
						dialog.show();
						AsyncService.DownloadVersion downloadVersion = AsyncService
								.getInstance().new DownloadVersion();
						downloadVersion.execute(handler,
								ParamsUtil.DOWNLOAD_RESULT, cVersion, dialog);
					}
					return;
				} else {
					it = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(it);
					finish();
				}
				break;
			case ParamsUtil.DOWNLOAD_RESULT:
				boolean isDownLoadSuccess = (Boolean) msg.obj;
				if (isDownLoadSuccess) {
					MyTip.showDialog(LoginActivity.this, "提示", "是否安装新版本?",
							handler, ParamsUtil.INSTALL);
					return;
				}
				it = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(it);
				finish();
				break;
			case ParamsUtil.INSTALL:
				boolean isInstall = msg.getData().getBoolean("select");
				if (isInstall) {
					loadIntentSupper();
				} else {
					it = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(it);
					finish();
				}
				break;
			default:
				break;
			}
		};
	};

	String SD_PATH = Environment.getExternalStorageDirectory() + File.separator;

	// 安装新版本;
	public void loadIntentSupper() {
		Intent uploadIntent = new Intent(Intent.ACTION_VIEW);
		uploadIntent.setDataAndType(
				Uri.fromFile(new File(SD_PATH + "Download" + File.separator
						+ cVersion.getVersionName() + ".apk")),
				"application/vnd.android.package-archive");
		startActivity(uploadIntent);
	}

	/** 设置登录接口返回值 */
	protected void setLoginResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			if (result[0] != null) {
				String flag = (String) result[0];
				if (!StrUtil.isNotEmpty(flag)) {
					return;
				}
				if (flag.equalsIgnoreCase("error")) {
					MyTip.showToast(LoginActivity.this, (String) result[1]);
				} else if (flag.equalsIgnoreCase("ok")) {
					User user = (User) result[1];
					user.setPwd(pwdStr);
					user.setPwdMD5(MD5Util.MD5(pwdStr));
					user.setLoginTime(TimeUtil.getSystemTimeStr());
					setUser(user);
					savePreferences("isLogin", true);
					boolean isSave = UserDao.getInstance(LoginActivity.this)
							.saveUser(user);
					if (isSave) {
						// 登录成功，检测版本更新
						updateNewVersion();
						// it = new Intent(LoginActivity.this,
						// MainActivity.class);
						// startActivity(it);
						// finish();
					} else {
						MyTip.showToast(LoginActivity.this, "登录信息报错失败");
					}
				}
			}
		}
		MyTip.cancelProgress();

	}

	private CVersion cVersion;

	protected void setCheckVersionResult(Object obj) {
		MyTip.cancelProgress();
		if (obj != null) {
			cVersion = (CVersion) obj;
			// 有新版本，提示是否下载更新
			boolean isInstall = judgeVersion(cVersion);
			if (isInstall) {
				// 下载新版本
				MyTip.showDialog(LoginActivity.this, "提示", "检测到新版本，是否下载？",
						handler, ParamsUtil.DOWNLOAD_NEW_VERSION);
				return;
			} else {
				MyTip.showToast(LoginActivity.this, "已经是最新版本了");
			}
		}
		it = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(it);
		finish();

	}

	private void updateNewVersion() {
		String versionStr = getVersionName();
		if (isConnectInternet()) {
			AsyncService.UpdateVersion updateVersion = AsyncService
					.getInstance().new UpdateVersion();
			updateVersion.execute(handler, ParamsUtil.CHECK_VERSION, getApp()
					.getUrl(), "MobileUpdate", getUser().getUserId(), versionStr);
		}
	}

	/** 设置获取工厂库位数据结果 */
	protected void setBaseInfoResult(Object obj) {
		MyTip.cancelProgress();
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String flag = (String) result[0];
			if (flag.equalsIgnoreCase("ok")) {
				savePreferences("isInit", true);
				// 保存更新工厂库位数据更新时间
				savePreferences("update_info", TimeUtil.getSystemTimeStr());
				return;
			}
			MyTip.showToast(LoginActivity.this, (String) result[1]);
		} else {
			MyTip.showToast(LoginActivity.this, "暂无数据");
		}
	}

}
