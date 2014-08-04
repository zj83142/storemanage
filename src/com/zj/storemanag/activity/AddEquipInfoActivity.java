package com.zj.storemanag.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import log.Log;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.Goods;
import com.zj.storemanag.bean.ImgBean;
import com.zj.storemanag.bean.MaterailDetail;
import com.zj.storemanag.bean.RFIDinfo;
import com.zj.storemanag.bean.SaveRFIDinfo;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.GoodsDao;
import com.zj.storemanag.dao.ImgDao;
import com.zj.storemanag.dao.OrderDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.NfcTools;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.util.Utils;
import com.zj.storemanag.view.custom_autotv.AutoTipAdapter;
import com.zj.storemanag.view.custom_autotv.AutoTipTextView;
import com.zj.storemanag.view.custom_autotv.FactoryStoreView;

public class AddEquipInfoActivity extends BaseActivity {
	/** 物料号记忆功能 */
	private AutoTipTextView materialEt;
	private List<String> materailLs = new ArrayList<String>();
	private AutoTipAdapter autoTipAdapter;

	private FactoryStoreView factoryStoreView;
	private TextView despEt;
	private TextView unitEt;
	private EditText numberEt;

	private RFIDinfo rfidInfo;

	private boolean isNew = false;

	private Button photoBtn;

	private LinearLayout rfidLayout;
	private EditText rfidEt;

	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	boolean writeMode;
	Tag mytag;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	
	private String newFactory;//新增时传入的工厂
	private String newStore;//新增时传入的库位
	
	private Activity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_equip);
		
		context = this;

		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("录入数据");
		photoBtn = (Button) findViewById(R.id.top_right_btn);
		photoBtn.setText("拍照");
		photoBtn.setOnClickListener(this);

		materialEt = (AutoTipTextView) findViewById(R.id.add_eq_materal);
		materailLs.addAll(OrderDao.getInstance(context)
				.findOrderLs(ParamsUtil.flag_materail));
		autoTipAdapter = new AutoTipAdapter(context,
				materailLs, 10);
		materialEt.setThreshold(0);
		materialEt.setInputType(InputType.TYPE_CLASS_NUMBER);
		materialEt.setAdapter(autoTipAdapter);
		factoryStoreView = (FactoryStoreView) findViewById(R.id.factory_store_view);
		factoryStoreView.setFactoryStoreTitle("工厂：", "库位：");
		despEt = (TextView) findViewById(R.id.add_eq_desp_et);
		unitEt = (TextView) findViewById(R.id.add_eq_unit_et);
		numberEt = (EditText) findViewById(R.id.add_eq_number_et);

		rfidLayout = (LinearLayout) findViewById(R.id.layout_rfid);
		rfidEt = (EditText) findViewById(R.id.add_eq_rfid_et);

		isNew = getIntent().getBooleanExtra("isNew", false);

		if (isNew) {
			// 添加设备
			Intent intent = getIntent();
			newFactory = intent.getStringExtra("factory");
			newStore = intent.getStringExtra("store");
			factoryStoreView.setFactory(StrUtil.filterStr(factory));
			factoryStoreView.setStore(StrUtil.filterStr(store));
			factoryStoreView.setVisibility(View.GONE);
			rfidLayout.setVisibility(View.VISIBLE);
		} else {
			// 录入数量
			rfidInfo = (RFIDinfo) getIntent().getSerializableExtra("rfidInfo");
			if (rfidInfo != null) {
				Goods good = GoodsDao.getInstance(context)
						.judgeExist(rfidInfo);
				if (good != null) {
					materialEt.setText(StrUtil.filterStr(good.getMATERIAL()));
					factoryStoreView.setFactory(StrUtil.filterStr(good
							.getFactory()));
					factoryStoreView
							.setStore(StrUtil.filterStr(good.getStore()));
					despEt.setText(StrUtil.filterStr(good.getMESSAGE()));
					unitEt.setText(StrUtil.filterStr(good.getUnit()));
					numberEt.setText(StrUtil.filterStr(good.getENTRY_QNT()));
				} else {
					materialEt.setText(StrUtil.filterStr(rfidInfo
							.getPR_MATERIAL()));
					factoryStoreView.setFactory(StrUtil.filterStr(rfidInfo
							.getFactory()));
					factoryStoreView.setStore(StrUtil.filterStr(rfidInfo
							.getStore()));
					despEt.setText(StrUtil.filterStr(rfidInfo.getPR_MESSAGE()));
					unitEt.setText(StrUtil.filterStr(rfidInfo.getUnit()));
				}
			}
		}
		int flag = getApp().FLAG;
		if (flag == ParamsUtil.interior) {
			// 内部备件入库,拍照功能
			// factoryStoreView.setGone();
			photoBtn.setVisibility(View.VISIBLE);
		} else if (flag == ParamsUtil.maintenance_order) {
			// 按维修订单
		}

		try {
			mAdapter = NfcAdapter.getDefaultAdapter(this);
			mPendingIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, getClass())
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			// Setup an intent filter for all MIME based dispatches
			IntentFilter ndef = new IntentFilter(
					NfcAdapter.ACTION_TECH_DISCOVERED);
			mFilters = new IntentFilter[] { ndef, };
			// Setup a tech list for all NfcF tags
			mTechLists = new String[][] { new String[] { NfcA.class.getName() } };
		} catch (Exception e) {
			Log.i("zj", "onCreate " + e.getMessage());
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Log.i("zj", "onNewIntent");
		setIntent(intent);
		if(isNew){
			try {
				new NfcTools().readInfo(intent, mytag, rfidEt, isNew,
						context);
			} catch (Exception e) {
				Log.i("zj", "Exception onNewIntent " + e.getMessage());
			}
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

	String photoPath;
	File out;
	public final static int REQUEST_CODE_PHOTO = 1002;

	// 拍照返回的时候 修改那个指标值
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// log.i("zj","返回的请求标志：" + requestCode);
		switch (requestCode) {
		case REQUEST_CODE_PHOTO: // 拍照返回
			try {
				photoPath = getPreference("photoPath");
				Log.i("zj", "PHOTO:" + photoPath);
				File file = new File(photoPath);
				if (file.exists()) {
					Log.i("zj", "保存照片：" + photoPath);
					// _id, material, factory, store, path, userId, state
					ImgBean bean = new ImgBean();
					bean.setMaterial(materialEt.getText().toString());
					bean.setFactory(StrUtil.slipValue(newFactory));
					bean.setStore(StrUtil.slipValue(newStore));
					bean.setPath(photoPath);
					bean.setUserId(getUser().getUserId());
					bean.setState("0");
					bean.setDesp(StrUtil.filterStr(despEt.getText().toString()));
					bean.setUnit(StrUtil.filterStr(unitEt.getText().toString()));
					bean.setCw("");
					// 保存图片到表中
					boolean isResult = ImgDao.getInstance(
							context).save(bean);
					if (isResult) {
//						MyTip.showToast(context, "图片保存成功");
					} else {
						MyTip.showToast(context, "图片保存失败");
					}
				} else {
					MyTip.showToast(context, "保存照片错误，文件不存在！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("zj", "保存照片报错：" + e.getMessage());
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	boolean isSave = false;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
		case R.id.add_eq_save_btn:
			// 保存
			if (judgeEntry()) {
				updateRfid();
			}
			break;
		case R.id.top_right_btn:
			// 先判断物料信息是否存在
			if (!judgeMaterial()) {
				// 请先输入
				MyTip.showToast(context, "请输入完整物料信息再拍照！");
				return;
			}
			// 拍照
			try {
				out = Utils.createFile(Utils.EQPICTUREPATH, ".jpg");
				if (out.exists() && out != null) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					photoPath = out.getAbsolutePath();
					savePreferences("photoPath", photoPath);
					Log.i("zj", "拍照，保存照片路径：" + photoPath);
					Uri uri = Uri.fromFile(out);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(intent, REQUEST_CODE_PHOTO);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("zj", "拍照报错：" + e.getMessage());
			}
			break;
		case R.id.add_eq_materal_find:
			// 查询物料信息
			String materal = materialEt.getText().toString();
			if (!StrUtil.isNotEmpty(materal)) {
				MyTip.showToast(context, "请输入物料号！");
				return;
			}
			if (isConnectInternet()) {
				MyTip.showProgress(context, "查询",
						"正在查询数据，请稍后！");
				AsyncService.GetMaterialDetail getMaterialDetail = AsyncService
						.getInstance().new GetMaterialDetail();
				getMaterialDetail.execute(handler,
						ParamsUtil.GETMATERIALDETAIL, getApp().getUrl(),
						getUser().getUserId(), materal,
						context);
			}
			break;
		default:
			break;
		}
	}

	private boolean judgeMaterial() {
		String material = StrUtil.filterStr(materialEt.getText().toString());
		String factoryStr = StrUtil.slipValue(newFactory);
		String storeStr = StrUtil.slipValue(newStore);
		String unit = StrUtil.filterStr(unitEt.getText().toString());
		if (StrUtil.isNotEmpty(material) && StrUtil.isNotEmpty(factoryStr)
				&& StrUtil.isNotEmpty(storeStr) && StrUtil.isNotEmpty(unit)) {
			return true;
		}
		return false;
	}

	private String factory;
	private String store;
	private String number;
	
	private String scanRfid;

	private boolean judgeEntry() {
		if(app.getFLAG() == ParamsUtil.interior){
			return true;
		}
		factory = factoryStoreView.getFactory();
		store = factoryStoreView.getStore();
		if (!StrUtil.isNotEmpty(factory)) {
			MyTip.showToast(context, "请选择工厂信息！");
			return false;
		}
		if (!StrUtil.isNotEmpty(store)) {
			MyTip.showToast(context, "请选择库位信息！");
			return false;
		}
		number = numberEt.getText().toString();
		if (!StrUtil.isNotEmpty(number)) {
			MyTip.showToast(context, "请输入数量");
			return false;
		} else if (number.equalsIgnoreCase("0")) {
			MyTip.showToast(context, "录入的采购数量不能为0");
			return false;
		}
//		if(isNew){
//			scanRfid = rfidEt.getText().toString();
//			if(!StrUtil.isNotEmpty(scanRfid)){
//				MyTip.showToast(context, "请扫描获取标签信息！");
//				return false;                      
//			}
//		}
		return true;
	}

	private void updateRfid() {
		MyTip.showProgress(context, "提交数据", "正在提交数据，请稍等……");
		SaveRFIDinfo saveRFIDinfo = new SaveRFIDinfo();
		saveRFIDinfo.setPR_MATERIAL(UrlUtil.safeXml(materialEt.getText()
				.toString()));
		saveRFIDinfo.setPR_ENTRY_UOM(UrlUtil.safeXml(StrUtil.slipName(unitEt.getText()
				.toString())));
		saveRFIDinfo.setPR_MESSAGE(UrlUtil.safeXml(despEt.getText()
				.toString()));
		String rfidStr = "";
		if(isNew){
			rfidStr = rfidEt.getText().toString();
		}else{
			if (rfidInfo != null) {
				rfidStr = rfidInfo.getPR_RFID_NO();
			}
		}
		saveRFIDinfo.setPR_RFID_NO(UrlUtil.safeXml(rfidStr));
		if(app.getFLAG() == ParamsUtil.interior){
			saveRFIDinfo.setPR_PLANT(UrlUtil.safeXml(StrUtil.slipValue(newFactory)));
			saveRFIDinfo.setPR_STGE_LOC(UrlUtil.safeXml(StrUtil.slipValue(newStore)));
		}else{
			saveRFIDinfo.setPR_PLANT(UrlUtil.safeXml(StrUtil.slipValue(factoryStoreView
					.getFactory())));
			saveRFIDinfo.setPR_STGE_LOC(UrlUtil.safeXml(StrUtil.slipValue(factoryStoreView
					.getStore())));
		}
		String xml = saveRFIDinfo.getSaveRfidInfo(saveRFIDinfo);
		AsyncService.TempRFID tempRFID = AsyncService.getInstance().new TempRFID();
		tempRFID.execute(handler, ParamsUtil.TEMPRFID, getApp().getUrl(),
				getUser().getUserId(), xml);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.GETMATERIALDETAIL:
				setMaterailDetailResult(msg.obj);
				break;
			case ParamsUtil.TEMPRFID:
				setTempRfidResult(msg.obj);
				break;
			default:
				break;
			}
		};
	};

	private MaterailDetail materailDetail;

	protected void setMaterailDetailResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (StrUtil.isNotEmpty(info) && info.equalsIgnoreCase("ok")) {
				materailDetail = (MaterailDetail) result[1];
				if (materailDetail != null) {
					despEt.setText(StrUtil.filterStr(materailDetail.getMAKTX()));
					unitEt.setText(StrUtil.filterStr(materailDetail.getUnit()));
					// 保存物料号
					OrderDao.getInstance(context).saveOrder(
							materialEt.getText().toString(), ParamsUtil.flag_materail);
					materailLs.clear();
					materailLs.addAll(OrderDao.getInstance(
							context).findOrderLs(ParamsUtil.flag_materail));
					autoTipAdapter.notifyDataSetChanged();
				}
			} else {
				MyTip.showToast(context, (String) result[1]);
			}
		}
		MyTip.cancelProgress();
	}

	protected void setTempRfidResult(Object obj) {
		MyTip.cancelProgress();
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (StrUtil.isNotEmpty(info) && info.equalsIgnoreCase("ok")) {
				ImgDao.getInstance(context).updateImgState(materialEt.getText()
							.toString(), StrUtil.slipValue(newFactory), StrUtil.slipValue(newStore));
				
				//判断更新返回的rfid是否和扫描的rfid匹配，如果不匹配，提示此卡已使用，请重新绑定
				String rfid = (String) result[1];
				scanRfid = StrUtil.filterStr(rfidEt.getText().toString());
				if(isNew && StrUtil.isNotEmpty(scanRfid)){
					if(!scanRfid.equalsIgnoreCase(rfid)){
						if(rfid.startsWith("0")){
							MyTip.showToast(context, "此卡片已使用,请换张卡片！");
							return;
						}else{
							MyTip.showToast(context, "此物料已绑定！");
						}
					}
				}
				Goods good = new Goods();
				good.setRFID(StrUtil.filterStr(rfid));
				good.setMATERIAL(StrUtil.filterStr(materialEt.getText()
						.toString()));
				good.setMESSAGE(StrUtil.filterStr(despEt.getText().toString()));
				good.setENTRY_QNT(StrUtil.filterStr(numberEt.getText()
						.toString()));
				good.setUnit(StrUtil.filterStr(unitEt.getText().toString()));
				if(app.getFLAG() == ParamsUtil.interior){
					good.setFactory(newFactory);
					good.setStore(store);
				}else{
					good.setFactory(StrUtil.filterStr(factoryStoreView.getFactory()));
					good.setStore(StrUtil.filterStr(factoryStoreView.getStore()));
					
				}
				good.setState("1");
				good.setIsInit("1");
				boolean isSave = GoodsDao
						.getInstance(context).save(good);
				if(isSave){
					finish();
				}else{
					MyTip.showToast(context, "数据保存失败");
				}
			} else {
				MyTip.showToast(context, (String) result[1]);
			}
		}
	}
}
