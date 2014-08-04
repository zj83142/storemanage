package com.zj.storemanag.activity;

import log.Log;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.bean.StoreHistoryOrder;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.TimeUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.view.TakeTimeDialog;
import com.zj.storemanag.view.custom_autotv.FactoryStoreView;

/**
 * 出入库查询
 * @author zhoujing
 * 2014-5-26 下午3:28:20
 */
public class OutInStoreActivity extends BaseActivity {
	
	private TakeTimeDialog myDialog;
	
	private TextView startEt;
	private TextView endEt;
	private EditText martialEt;
	private FactoryStoreView factoryStoreView;
	private EditText supplierEt;
	private EditText custormEt;
	private EditText moveTypeEt;
	private EditText spetailEt;
	
	private View currentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outinstore);
		
		initView();
	}
	
	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("出入库查询");
		
		startEt = (TextView) findViewById(R.id.outin_star_time_et);
		endEt = (TextView) findViewById(R.id.outin_end_time_et);
		startEt.setText(TimeUtil.getCurrentDate());
		endEt.setText(TimeUtil.getCurrentDate());
		startEt.setOnClickListener(this);
		endEt.setOnClickListener(this);
		martialEt = (EditText) findViewById(R.id.outin_material_et);
		factoryStoreView = (FactoryStoreView) findViewById(R.id.factorystoreview);
		factoryStoreView.setFactoryStoreTitle("工厂：", "库位：");
		supplierEt = (EditText) findViewById(R.id.outin_supplier_et);
		
		custormEt = (EditText) findViewById(R.id.outin_custorm_et);
		moveTypeEt = (EditText) findViewById(R.id.outin_move_type_et);
		spetailEt = (EditText) findViewById(R.id.outin_special_et);

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		case R.id.outin_star_time_et:
		case R.id.outin_star_time_btn:
			// 开始时间
			myDialog = new TakeTimeDialog(OutInStoreActivity.this,
					R.style.modifydialog, startEt);
			myDialog.show();
			break;
		case R.id.outin_end_time_et:
		case R.id.outin_end_time_btn:
			// 结束时间
			myDialog = new TakeTimeDialog(OutInStoreActivity.this,
					R.style.modifydialog, endEt);
			myDialog.show();
			break;
		case R.id.outin_query_btn:
			//查询
			queryStoreHistory();
			break;
		default:
			break;
		}
	}
	
	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.POP_SELECT:
				if (msg.obj != null
						&& msg.obj.getClass().isInstance(new PopItem())) {
					PopItem item = (PopItem) msg.obj;
					if (currentView != null) {
						((EditText) currentView).setText(item.getName());
					}
				}
				break;
			default:
				break;
			}
		};
	};

	private String xml;
	private void queryStoreHistory() {
		String start = startEt.getText().toString();
		String end = endEt.getText().toString();
		if(!StrUtil.isNotEmpty(start)){
			MyTip.showToast(OutInStoreActivity.this, "请输入开始时间！");
			return;
		}
		if(!StrUtil.isNotEmpty(end)){
			MyTip.showToast(OutInStoreActivity.this, "请输入结束时间！");
			return;
		}
		StoreHistoryOrder storeHistoryOrder = new StoreHistoryOrder();
		storeHistoryOrder.setUserId(UrlUtil.safeXml(getUser().getUserId()));
		storeHistoryOrder.setbDate(UrlUtil.safeXml(start));
		storeHistoryOrder.seteDate(UrlUtil.safeXml(end));
		storeHistoryOrder.setMatnr(UrlUtil.safeXml(martialEt.getText().toString()));
		storeHistoryOrder.setWerk(UrlUtil.safeXml(StrUtil.slipValue(factoryStoreView.getFactory())));
		storeHistoryOrder.setLgort(UrlUtil.safeXml(StrUtil.slipValue(factoryStoreView.getStore())));
		storeHistoryOrder.setVendor(UrlUtil.safeXml(supplierEt.getText().toString()));
		storeHistoryOrder.setCustom(UrlUtil.safeXml(custormEt.getText().toString()));
		storeHistoryOrder.setBwart(UrlUtil.safeXml(moveTypeEt.getText().toString()));
		storeHistoryOrder.setSobkz(UrlUtil.safeXml(spetailEt.getText().toString()));
		xml = storeHistoryOrder.getSendParam(storeHistoryOrder);
		if(!StrUtil.isNotEmpty(xml)){
			MyTip.showToast(OutInStoreActivity.this, "查询语句不能为空！");
			return;
		}
		Log.i("zj", "查询出入库凭证报表xml:          "+xml);
		Intent it = new Intent(OutInStoreActivity.this, OutInStoreListActivity.class);
		it.putExtra("xml", xml);
		startActivity(it);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		clearViewData();
	}

	private void clearViewData(){
		startEt.setText(TimeUtil.getCurrentDate());
		endEt.setText(TimeUtil.getCurrentDate());
		martialEt.setText("");
		factoryStoreView.setFactory("");
		factoryStoreView.setStore("");
		supplierEt.setText("");
		custormEt.setText("");
		moveTypeEt.setText("");
		spetailEt.setText("");
	}

}
