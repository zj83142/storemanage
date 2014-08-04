package com.zj.storemanag.activity;

import java.util.ArrayList;
import java.util.List;

import log.Log;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.RFIDinfo;
import com.zj.storemanag.bean.SaveRFIDinfo;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.NfcTools;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.view.custom_autotv.FactoryStoreView;

public class UpdateTempRfidActivity extends BaseActivity{
	
	private FactoryStoreView factoryStoreView;
	
	private TextView materalTv;
	private TextView despEt;
	private TextView unitEt;
	private TextView rfidEt;
	
	RFIDinfo rfidInfo;

	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	boolean writeMode;
	Tag mytag;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	
	private boolean isWrite = true;
	
	private int position = -1;
	
	private boolean isUpdate = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.update_temp_rfid);
		rfidInfo = (RFIDinfo) getIntent().getSerializableExtra("rfid");
		if(rfidInfo == null) return;
		initView();
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("更新标签");
		factoryStoreView = (FactoryStoreView) findViewById(R.id.factory_store);
		factoryStoreView.setFactoryStoreTitle("工厂:", "库位：");
		
		materalTv = (TextView) findViewById(R.id.update_rfid_materal);
		despEt = (TextView) findViewById(R.id.update_rfid_material_desp_et);
		unitEt = (TextView) findViewById(R.id.update_rfid_store_unit_et);
		rfidEt = (TextView) findViewById(R.id.update_rfid_rfid_et);
		
		materalTv.setText(StrUtil.filterStr(rfidInfo.getPR_MATERIAL()));
		despEt.setText(StrUtil.filterStr(rfidInfo.getPR_MESSAGE()));
		factoryStoreView.setFactory(StrUtil.filterStr(rfidInfo.getFactory()));
		factoryStoreView.setStore(StrUtil.filterStr(rfidInfo.getStore()));
		unitEt.setText(StrUtil.filterStr(rfidInfo.getUnit()));
		rfidEt.setText(StrUtil.filterStr(rfidInfo.getPR_RFID_NO()));
		
		try {
			mAdapter = NfcAdapter.getDefaultAdapter(this);
			mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			// Setup an intent filter for all MIME based dispatches
			IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
			mFilters = new IntentFilter[] { ndef, };
			// Setup a tech list for all NfcF tags
			mTechLists = new String[][] { new String[] { NfcA.class.getName() } };
		} catch (Exception e) {
			Log.i("zj", "onCreate " + e.getMessage());
		}
		position = getIntent().getIntExtra("position", -1);
		//工厂、库位、单位不能更改
//		factoryStoreView.setGone();
	}

	@Override
	public void onNewIntent(Intent intent) {
		try {
			mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Log.i("zj", "onNewIntent");
			setIntent(intent);
			new NfcTools().readInfo(intent, mytag,rfidEt, isWrite, UpdateTempRfidActivity.this);
		} catch (Exception e) {
			Log.i("zj", "Exception onNewIntent " + e.getMessage());
		}
	}

	@TargetApi(10)
	@Override
	public void onPause() {
		super.onPause();
		Log.d("zj", "onPause() called");
		if (mAdapter != null)
			mAdapter.disableForegroundDispatch(this);
	}

	@TargetApi(10)
	@Override
	public void onResume() {
		super.onResume();
		Log.d("zj", "onResume() called");
		if (mAdapter != null)
			mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
					mTechLists);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			if(!isUpdate){
				getApp().setPosition(-1);
			}
			UpdateTempRfidActivity.this.finish();
			break;
		case R.id.update_rfid_save_btn:
			//更新Rfid按钮
			updateTempRfid();
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(!isUpdate){
				getApp().setPosition(-1);
			}
			UpdateTempRfidActivity.this.finish();
		}
		return true;
	}
	/**更新RFId*/
	private void updateTempRfid() {
		String rfid = rfidEt.getText().toString();
		if(StrUtil.isNotEmpty(rfid)&& !rfid.startsWith("T")){
			MyTip.showProgress(UpdateTempRfidActivity.this, "更新标签", "标签更新中，请稍候……");
			SaveRFIDinfo saveRFIDinfo = new SaveRFIDinfo();
			saveRFIDinfo.setPR_RFID_NO(UrlUtil.safeXml(rfid));
			saveRFIDinfo.setPR_MATERIAL(UrlUtil.safeXml(rfidInfo.getPR_MATERIAL()));
			saveRFIDinfo.setPR_MESSAGE(UrlUtil.safeXml(rfidInfo.getPR_MESSAGE()));
			saveRFIDinfo.setPR_PLANT(UrlUtil.safeXml(StrUtil.slipValue(factoryStoreView.getFactory())));
			saveRFIDinfo.setPR_STGE_LOC(UrlUtil.safeXml(StrUtil.slipValue(factoryStoreView.getStore())));
			saveRFIDinfo.setPR_ENTRY_UOM(UrlUtil.safeXml(StrUtil.slipName(unitEt.getText().toString())));
//			rfidInfo.setPR_RFID_NO(rfid);
			String xml = saveRFIDinfo.getSaveRfidInfo(saveRFIDinfo);
			AsyncService.UpdateRfID updateRfID = AsyncService.getInstance().new UpdateRfID();
			updateRfID.execute(handler, ParamsUtil.UPDATERFID, getApp().getUrl(), xml);
		}else{
			MyTip.showToast(UpdateTempRfidActivity.this, "请先扫描出标签");
		}
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.UPDATERFID:
				setUpdateRfidResult(msg.obj);
				break;
			default:
				break;
			}
		};
	};

	protected void setUpdateRfidResult(Object obj) {
		if(obj != null){
			Object[] result = (Object[])obj;
			String flag = (String) result[0];
			if(flag.equalsIgnoreCase("1")){
				isUpdate = true;
				getApp().setPosition(position);
				finish();
			}else{
				getApp().setPosition(-1);
			}
			if(result[1] != null){
				MyTip.showToast(UpdateTempRfidActivity.this, (String)result[1]);
			}
		}
		MyTip.cancelProgress();
	}

}
