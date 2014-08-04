package com.zj.storemanag.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.adapter.StoreAdapter;
import com.zj.storemanag.bean.StoreBean;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.OrderDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.view.custom_autotv.AutoTipAdapter;
import com.zj.storemanag.view.custom_autotv.AutoTipTextView;

/**
 * 物料号查询
 * 
 * @author zhoujing 2014-5-15 下午4:06:30
 */
public class MaterialQueryActivity extends BaseActivity {

	private ListView listView;
	private List<StoreBean> list;
	private StoreAdapter adapter;

	/** 物料号记忆功能 */
	private AutoTipTextView materailEt;
	private List<String> materailLs = new ArrayList<String>();
	private AutoTipAdapter autoTipAdapter;
	
	private TextView acountTv;
	private TextView supperAcountTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.material_query);

		initView();
	}


	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("物料查询");
		acountTv = (TextView) findViewById(R.id.acount_tv);
		supperAcountTv = (TextView) findViewById(R.id.acount_js_tv);
		materailEt = (AutoTipTextView) findViewById(R.id.material_num_et);
		materailLs.addAll(OrderDao.getInstance(MaterialQueryActivity.this)
				.findOrderLs(ParamsUtil.flag_materail));
		autoTipAdapter = new AutoTipAdapter(MaterialQueryActivity.this,
				materailLs, 10);
		materailEt.setThreshold(0);
		materailEt.setAdapter(autoTipAdapter);
		materailEt.setInputType(InputType.TYPE_CLASS_NUMBER);
		list = new ArrayList<StoreBean>();
		listView = (ListView) findViewById(R.id.material_query_listview);
		adapter = new StoreAdapter(MaterialQueryActivity.this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent(MaterialQueryActivity.this,
						RFIDQueryActivity.class);
				it.putExtra("material", (Serializable) list.get(arg2));
				it.putExtra("skip", true);
				startActivity(it);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		case R.id.material_find_btn:
			String materialStr = materailEt.getText().toString();
			if (!StrUtil.isNotEmpty(materialStr)) {
				MyTip.showToast(MaterialQueryActivity.this, "请输入物料号");
				return;
			}
			MyTip.showProgress(MaterialQueryActivity.this, "查询", "正在查询数据，请稍候……");
			AsyncService.GetKC getKc = AsyncService.getInstance().new GetKC();
			getKc.execute(handler, ParamsUtil.GETKC, getApp().getUrl(),
					materialStr, "", "", MaterialQueryActivity.this);
			break;
		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.GETKC:
				setKCResult(msg.obj);
				break;

			default:
				break;
			}
		};
	};

	/** 设置库存查询结果 */
	protected void setKCResult(Object obj) {
		list.clear();
		autoTipAdapter.notifyDataSetChanged();
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("ok")) {
				List<StoreBean> tempLs = (List<StoreBean>) result[1];
				if (tempLs != null && tempLs.size() > 0) {
					list.addAll(tempLs);
					adapter.notifyDataSetChanged();
				}
				// 保存物料号
				OrderDao.getInstance(MaterialQueryActivity.this).saveOrder(
						materailEt.getText().toString(), ParamsUtil.flag_materail);
				materailLs.clear();
				materailLs.addAll(OrderDao.getInstance(MaterialQueryActivity.this)
						.findOrderLs(ParamsUtil.flag_materail));
				autoTipAdapter.notifyDataSetChanged();
				//获取库存总量
				getTotalStoreNum();
			} else {
				MyTip.showToast(MaterialQueryActivity.this, "没有查询到该物料号信息！");
			}
		}
		MyTip.cancelProgress();
	}


	private void getTotalStoreNum() {
		if(list != null && list.size() > 0){
			Double total = 0.0;
			Double jsTotal = 0.0;
			for(StoreBean temp : list){
				String LABST = temp.getLABST();
				String KLABS = temp.getKLABS();
				total += Double.parseDouble(LABST);
				jsTotal += Double.parseDouble(KLABS);
			}
			acountTv.setText("库存总量: " + filter(total));
			supperAcountTv.setText("寄售库存："+filter(jsTotal));
		}else{
			acountTv.setText("库存总量: 0");
			supperAcountTv.setText("寄售库存： 0");
		}
	}


	private String filter(Double total) {
		String info = String.valueOf(total);
		if(info.endsWith(".0")){
			return info.replace(".0", "");
		}
		return info;
	}

}
