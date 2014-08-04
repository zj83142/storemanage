package com.zj.storemanag.activity;

import java.util.List;

import log.Log;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.dao.UserDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.TimeUtil;
import com.zj.storemanag.util.UrlUtil;

public class SetActivity extends BaseActivity {

	private EditText defaultUserNameEt;
	private EditText addressEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting);
		initView();
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("系统设置");
		Button backBtn = (Button) findViewById(R.id.top_back_btn);
		backBtn.setOnClickListener(this);
		Button setDefalutBtn = (Button) findViewById(R.id.set_default_username_btn);
		setDefalutBtn.setOnClickListener(this);

		TextView update_dateTv = (TextView) findViewById(R.id.set_cache_update_date_tv);
		String update_info = getPreference("update_info");
		if (StrUtil.isNotEmpty(update_info)) {
			update_dateTv.setText(update_info);
		}
		defaultUserNameEt = (EditText) findViewById(R.id.set_default_login_name_et);
		String defaultLoginName = getPreference("default_login_name");
		if (StrUtil.isNotEmpty(defaultLoginName)) {
			defaultUserNameEt.setText(defaultLoginName);
		}
		addressEt = (EditText) findViewById(R.id.set_service_address_et);
		String url = getPreference("url");
		if (StrUtil.isNotEmpty(url)) {
			addressEt.setText(url);
			getApp().setUrl(url);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		case R.id.set_save_btn:
			// 保存服务地址和默认登录名
			String defaultLoginName = defaultUserNameEt.getText().toString();
			if (!StrUtil.isNotEmpty(defaultLoginName)) {
				MyTip.showToast(SetActivity.this, "请输入默认登录名");
				return;
			}
			String url = addressEt.getText().toString();
			if (!StrUtil.isNotEmpty(url)) {
				MyTip.showToast(SetActivity.this, "请输入服务地址");
				return;
			}
			if (!UrlUtil.isUrl("http://" + url + ParamsUtil.defaulServiceName)) {
				MyTip.showToast(SetActivity.this, "输入的服务地址不正确");
				return;
			}
			savePreferences("default_login_name", defaultLoginName);
			savePreferences("url", url);
			getApp().setUrl(url);
			MyTip.showToast(SetActivity.this, "保存成功");
			finish();
			break;
		case R.id.set_clear_cache_factory_btn:
			// 清除工厂库位缓存
			if (isConnectInternet()) {
				// 1、清除数据
				boolean isClear = BaseInfoDao.getInstance(SetActivity.this)
						.delectData();
				if (!isClear) {
					MyTip.showToast(SetActivity.this, "清除工厂库位缓存失败");
					return;
				}
				// 2、更新数据、保存更新数据时间，更新显示
				MyTip.showProgress(SetActivity.this, "初始化", "正在初始化工厂库位数据，请稍候……");
				AsyncService.GetBaseData getBaseData = AsyncService
						.getInstance().new GetBaseData();
				getBaseData.execute(handler, ParamsUtil.GETBASEDATA,
						getPreference("url"), SetActivity.this);
			}
			break;
		case R.id.set_open_wifi_btn:
			// 设置无线网络
			// 网络设置
			Intent it = new Intent(Settings.ACTION_WIFI_SETTINGS);// 直接进入手机中的wifi网络设置界面
			SetActivity.this.startActivity(it);

			// toggleWiFi(SetActivity.this);
			break;
		case R.id.set_default_username_btn:
			// 选择用户名
			List<PopItem> nameList = UserDao.getInstance(SetActivity.this)
					.getUserLs();
			if (nameList != null && nameList.size() > 0) {
				MyTip.showPopView(SetActivity.this, nameList,
						defaultUserNameEt, handler);
			} else {
				Log.i("zj", "没有查询到登录信息");
			}
			break;
		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.POP_SELECT:
				if (msg.obj != null
						&& msg.obj.getClass().isInstance(new PopItem())) {
					PopItem item = (PopItem) msg.obj;
					defaultUserNameEt.setText(item.getName());
				}
				break;
			case ParamsUtil.GETBASEDATA:
				setBaseInfoResult(msg.obj);
				break;
			default:
				break;
			}
		};
	};

	/** 设置获取工厂库位数据结果 */
	protected void setBaseInfoResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String flag = (String) result[0];
			if (flag.equalsIgnoreCase("ok")) {
				savePreferences("isInit", true);
				// 保存更新工厂库位数据更新时间
				savePreferences("update_info", TimeUtil.getSystemTimeStr());
			}
			MyTip.showToast(SetActivity.this, (String) result[1]);
		} else {
			MyTip.showToast(SetActivity.this, "暂无数据");
		}
		MyTip.cancelProgress();
	}

	/** 打开或关闭wifi */
	private void toggleWiFi(Context context) {
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (!wm.isWifiEnabled()) {
			wm.setWifiEnabled(true);
		} else {
			MyTip.showToast(context, getString(R.string.wifi_state_tip));
		}
	}

}
