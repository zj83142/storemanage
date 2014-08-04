package com.zj.storemanag.view.custom_autotv;

import java.util.ArrayList;
import java.util.List;

import log.Log;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;

public class FactoryStoreView extends LinearLayout {

	private TextView factoryTv;
	private TextView storeTv;
	private MyAutoTextView factoryAutoTv;
	private MyAutoTextView storeAutoTv;
	private Button factoryBtn;
	private Button storeBtn;

	private List<String> factoryLs = new ArrayList<String>();
	private List<String> storeLs = new ArrayList<String>();
	private AutoTipAdapter factoryAdapter;
	private AutoTipAdapter storeAdapter;

	private String flag = "";
	private Context context;
	
	public FactoryStoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.factory_store_view, this);
		factoryTv = (TextView) linearLayout.findViewById(R.id.view_factory_tv);
		storeTv = (TextView) linearLayout.findViewById(R.id.view_store_tv);
		factoryAutoTv = (MyAutoTextView) linearLayout
				.findViewById(R.id.view_factory_auto);
		storeAutoTv = (MyAutoTextView) linearLayout
				.findViewById(R.id.view_store_auto);
		factoryBtn = (Button) linearLayout.findViewById(R.id.view_factory_btn);
		storeBtn = (Button) linearLayout.findViewById(R.id.view_store_btn);
		initView();
	}

	private void initView() {
		factoryTv = (TextView) findViewById(R.id.view_factory_tv);
		storeTv = (TextView) findViewById(R.id.view_store_tv);
		factoryAutoTv = (MyAutoTextView) findViewById(R.id.view_factory_auto);
		storeAutoTv = (MyAutoTextView) findViewById(R.id.view_store_auto);
		factoryBtn = (Button) findViewById(R.id.view_factory_btn);
		storeBtn = (Button) findViewById(R.id.view_store_btn);
		factoryBtn.setOnClickListener(new MyClick());
		storeBtn.setOnClickListener(new MyClick());

		factoryAdapter = new AutoTipAdapter(context, factoryLs, 10);
		factoryAutoTv.setAdapter(factoryAdapter);
		factoryAutoTv.setThreshold(0);
		getFactoryLs();

		storeAdapter = new AutoTipAdapter(context, storeLs, 10);
		storeAutoTv.setAdapter(storeAdapter);
		storeAutoTv.setThreshold(0);
		factoryAutoTv.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				String factory = null;
				String factoryOld = null;
				if (hasFocus) {
					factoryOld = factoryAutoTv.getText().toString();
				} else {
					factory = factoryAutoTv.getText().toString();
					// 失去焦点 ，根据factory的code查询库位list
					if(StrUtil.isNotEmpty(factory)){
						String factoryValue = StrUtil.slipValue(factory);
						getStoreLs(factoryValue);
					}
					if(!factory.equalsIgnoreCase(factoryOld)){
						setStore("");
					}
				}
			}
		});
	}

	public void getFactoryLs() {
		factoryLs.clear();
		List<PopItem> popLs = BaseInfoDao.getInstance(context)
				.findLsByType("1");
		if (popLs != null && popLs.size() > 0) {
			for (PopItem temp : popLs) {
				factoryLs.add(temp.getName());
			}
		} else {
			Log.i("zj", "没有查询到工厂信息");
		}
	}

	protected void getStoreLs(String factory) {
		storeLs.clear();
		if(!StrUtil.isNotEmpty(factory)){
			return;
		}
		List<PopItem> popLs = BaseInfoDao.getInstance(context).findStoreLs(
				factory);
		if (popLs != null && popLs.size() > 0) {
			for (PopItem temp : popLs) {
				storeLs.add(temp.getName());
			}
		} else {
			Log.i("zj", "没有查询到库位信息");
		}
	}

	private class MyClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.view_factory_btn:
				// 工厂
				setFactory("");
				setStore("");
//				flag = "1";
//				setFactoryPopData(factoryAutoTv);
				break;
			case R.id.view_store_btn:
				setStore("");
				// 库位
//				String factory = factoryAutoTv.getText().toString();
//				if (StrUtil.isNotEmpty(factory)) {
//					flag = "2";
//					String factoryValue = StrUtil.slipValue(factory);
//					setStorePopData(factoryValue, storeAutoTv);
//				} else {
//					MyTip.showToast(context, "请先输入工厂信息！");
//				}
				break;
			default:
				break;
			}
		}
	}


	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.POP_SELECT:
				if (msg.obj != null
						&& msg.obj.getClass().isInstance(new PopItem())) {
					PopItem item = (PopItem) msg.obj;

					if (flag.equalsIgnoreCase("1")) {
						setFactory(item.getName());
					} else if (flag.equalsIgnoreCase("2")) {
						setStore(item.getName());
					}
				}
				break;

			default:
				break;
			}
		};
	};
	

	/** 设置工厂库位title */
	public void setFactoryStoreTitle(String factoryTitle, String storeTitle) {
		factoryTv.setText(StrUtil.filterStr(factoryTitle));
		storeTv.setText(StrUtil.filterStr(storeTitle));
	}

	public String getFactory() {
		return factoryAutoTv.getText().toString();
	}

	public String getStore() {
		return storeAutoTv.getText().toString();
	}

	public void setFactory(String factory) {
		factoryAutoTv.setText(factory);
	}

	public void setStore(String store) {
		storeAutoTv.setText(store);
	}

	public String judgeEntry() {
		String factory = factoryAutoTv.getText().toString();
		if(!StrUtil.isNotEmpty(factory)){
			return "请输入工厂信息！";
		}
		String store = storeAutoTv.getText().toString();
		if(!StrUtil.isNotEmpty(store)){
			return "请输入库位信息！";
		}
		return null;
	}

	/**设置工厂库位不能输入*/
	public void setGone() {
		factoryBtn.setVisibility(View.GONE);
		storeBtn.setVisibility(View.GONE);
		factoryAutoTv.setFocusable(false);
		storeAutoTv.setFocusable(false);
	}
}
