package com.zj.storemanag.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.Goods;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.util.StrUtil;

public class QueryStockActivity extends BaseActivity {
	
	private EditText rfidEt;
	private EditText materialEt;
	private EditText factoryEt;
	private EditText locationEt;
	private EditText despEt;
	private TextView unitEt;
	private EditText fxgjkcEt;
	private EditText xzjskcEt;
	private EditText totalStoreEt;
	private EditText fxjsckEtk;
	private EditText stockCwEt;
	private EditText xmkcEt;

	private Goods goods;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_stock);
		
		initView();
		
	}
	
	private void initView(){
		goods = (Goods) getIntent().getSerializableExtra("goods");
		
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("库存查询");
		
		rfidEt = (EditText) findViewById(R.id.query_stock_rfid_et);
		materialEt = (EditText) findViewById(R.id.query_stock_material_et);
		factoryEt = (EditText) findViewById(R.id.query_stock_factory_et);
		locationEt = (EditText) findViewById(R.id.query_stock_store_location_et);
		despEt = (EditText) findViewById(R.id.query_stock_material_desp_et);
		unitEt = (TextView) findViewById(R.id.query_stock_store_unit_et);
		fxgjkcEt = (EditText) findViewById(R.id.query_stock_fxgjkc_et);
		xzjskcEt = (EditText) findViewById(R.id.query_stock_xzjskc_et);
		totalStoreEt = (EditText) findViewById(R.id.query_stock_total_store_et);
		fxjsckEtk = (EditText) findViewById(R.id.query_stock_fxjskc_et);
		stockCwEt = (EditText) findViewById(R.id.query_stock_cw_et);
		xmkcEt = (EditText) findViewById(R.id.query_stock_xmcw_et);
		
		rfidEt.setText(StrUtil.filterStr(goods.getRFID()));
		materialEt.setText(StrUtil.filterStr(goods.getMATERIAL()));
		factoryEt.setText(StrUtil.filterStr(goods.getFactory()));
		locationEt.setText(StrUtil.filterStr(goods.getStore()));
		despEt.setText(StrUtil.filterStr(goods.getMESSAGE()));
		unitEt.setText(StrUtil.filterStr(goods.getUnit()));
		
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;

		default:
			break;
		}
	}

}
