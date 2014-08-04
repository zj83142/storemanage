package com.zj.storemanag.activity.inorout;

import java.util.ArrayList;
import java.util.List;

import log.Log;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.activity.AddEquipInfoActivity;
import com.zj.storemanag.activity.EquipDetailActivity;
import com.zj.storemanag.adapter.GoodsAdapter;
import com.zj.storemanag.bean.Goods;
import com.zj.storemanag.bean.MaterialDoc;
import com.zj.storemanag.bean.RFIDinfo;
import com.zj.storemanag.bean.SaveRFIDinfo;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.GoodsDao;
import com.zj.storemanag.dao.OrderDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.service.GetPmOrderService;
import com.zj.storemanag.service.GetPoOrderService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.NfcTools;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.view.CustomKeyboardView;
import com.zj.storemanag.view.custom_autotv.AutoTipAdapter;
import com.zj.storemanag.view.custom_autotv.AutoTipTextView;

/***
 * 工程技改
 * 
 * @author zhoujing 2014-7-25 上午10:12:38
 */
public class StockOperationActivity extends BaseActivity {

	private BaseInfoView baseInfoView;

	private Activity context;

	private int flag;

	private Button addBtn;

	private ListView listView;
	private GoodsAdapter adapter;
	private List<Goods> list;

	private LinearLayout orderSearchLayout;
	private TextView orderSearchTitleTv;
	private AutoTipTextView orderSearchAuto;
	private Button searchBtn;

	private EditText rfidEt;

	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	boolean writeMode;
	Tag mytag;
	private boolean isWrite = false;
	private static String TAG = "zj";

	private CustomKeyboardView keyBoard;

	private String in_factory;
	private String in_store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stock_operation);

		context = this;
		addThis(context);
		flag = getIntent().getIntExtra("flag", -1);
		if (flag == -1)
			return;
		setFlag(flag);

		initView();

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
			Log.i(TAG, "onCreate " + e.getMessage());
		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		try {
			mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Log.i(TAG, "onNewIntent");
			setIntent(intent);
			new NfcTools().readInfo(intent, mytag, rfidEt, isWrite, context);
		} catch (Exception e) {
			Log.i(TAG, "Exception onNewIntent " + e.getMessage());
		}
	}

	@TargetApi(10)
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause() called");
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this);
		}
	}

	@TargetApi(10)
	@Override
	public void onResume() {
		super.onResume();
		initDate();
		Log.d(TAG, "onResume() called");
		if (mAdapter != null)
			mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
					mTechLists);
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText(getResources().getStringArray(R.array.list_operation)[flag]);
		baseInfoView = (BaseInfoView) findViewById(R.id.baseinfo);
		baseInfoView.setFlag(flag);

		orderSearchLayout = (LinearLayout) findViewById(R.id.order_search_layout);
		orderSearchTitleTv = (TextView) findViewById(R.id.order_search_title_tv);
		orderSearchAuto = (AutoTipTextView) findViewById(R.id.order_search_et);
		searchBtn = (Button) findViewById(R.id.order_search_btn);
		searchBtn.setOnClickListener(this);

		addBtn = (Button) findViewById(R.id.top_right_btn);
		addBtn.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.equip_listview);
		list = new ArrayList<Goods>();
		// flag: 适配器类型，用于区分是否有采购数量
		adapter = new GoodsAdapter(context, list, handler, flag);
		listView.setAdapter(adapter);
		if (flag == ParamsUtil.interior) {
			addBtn.setVisibility(View.VISIBLE);
		} else if (flag == ParamsUtil.external) {
			orderSearchTitleTv.setText("订单号：");
			orderSearchLayout.setVisibility(View.VISIBLE);
			setOrderAutoTip();
		} else if (flag == ParamsUtil.maintenance_order) {
			orderSearchLayout.setVisibility(View.VISIBLE);
			setOrderAutoTip();
		}
		// 自定义键盘
		keyBoard = (CustomKeyboardView) findViewById(R.id.layout_custom_keyboard);
		keyBoard.setHandler(handler);

		rfidEt = (EditText) findViewById(R.id.rfid_et);
		rfidEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (flag == ParamsUtil.external) {
					// 采购订单入库不需要扫描rfid
					return;
				} else if (flag == ParamsUtil.interior) {
					// 内部备件入库需要先填写基础信息
					MaterialDoc info = baseInfoView.getBaseInfoByFlag();
					if (info != null) {
						in_factory = info.getPLANT();
						in_store = info.getSTGE_LOC();
					} else {
						return;
					}
				}
				String rfid = rfidEt.getText().toString();
				if (StrUtil.isNotEmpty(rfid) && rfid.length() == 32) {
					MyTip.showProgress(context, "搜索", "正在搜索数据，请稍候...");
					AsyncService.GetRFIDInfo getRFIDInfo = AsyncService
							.getInstance().new GetRFIDInfo();
					getRFIDInfo.execute(handler, ParamsUtil.GETRFIDINFO,
							app.getUrl(), app.getUser().getUserId(), rfid,
							context);
				}
			}
		});
	}

	private void initDate() {
		list.clear();
		list.addAll(GoodsDao.getInstance(context).findLs());
		adapter.notifyDataSetChanged();
	}

	private Goods currentGoods = null;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.update_list:
				// 更新列表
				initDate();
				break;
			case ParamsUtil.EQ_DETAIL:
				// 详情
				if (msg.obj != null
						&& msg.obj.getClass().isInstance(new Goods())) {
					Goods good = (Goods) msg.obj;
					skipDetail(good);
				}
				break;
			case ParamsUtil.GETRFIDINFO:
				// 获取RFID信息
				setRFIDInfoResult(msg.obj);
				break;
			case ParamsUtil.hideKeyboard:
				// 隐藏键盘
				keyBoard.setShow(false);
				if (msg.obj == null) {
					return;
				}
				String info = StrUtil.filterStr((String) msg.obj);
				if (!StrUtil.isNotEmpty(info)) {
					info = "0";
				}
				System.out.println("录入数据-----------------》" + info);
				// 保存数量
				if (currentGoods != null) {
					GoodsDao.getInstance(context).updateGoodNumber(
							currentGoods.getId(), info);
					currentGoods = null;
					initDate();
				}
				break;
			case ParamsUtil.showKeyboard:
				if (!keyBoard.isShow()) {
					keyBoard.clearTextView();
					keyBoard.setShow(true);
				}
				if (msg.obj != null
						&& msg.obj.getClass().isInstance(new Goods())) {
					currentGoods = (Goods) msg.obj;
				}
				break;
			case ParamsUtil.UPLOAD:
				// 设置上传结果
				setUploadResult(msg.obj);
				break;
			case ParamsUtil.EXIT_OPERATION:
				// 是否退出，是退出
				if (msg.getData().getBoolean("select")) {
					finish();
				}
				break;
			case ParamsUtil.IS_GOON:
				// 是否继续操作，否退出
				if (!msg.getData().getBoolean("select")) {
					finish();
				}
				break;
			case ParamsUtil.IsTEMPRFID:
				if (msg.obj != null
						&& msg.obj.getClass().isInstance(new Goods())) {
					currentGoods = (Goods) msg.obj;
					if(app.getFLAG() == ParamsUtil.maintenance_order){
						String store = StrUtil.filterStr(currentGoods.getStore());
						if(!StrUtil.isNotEmpty(store)){
							MyTip.showToast(context, "设备信息不完善，请选择设备信息！");
							return;
						}
					}
					updateRfid();
				}
				break;
			case ParamsUtil.TEMPRFID:
				setTempRfidResult(msg.obj);
				break;
			default:
				break;
			}
		};
	};

	protected void setTempRfidResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (StrUtil.isNotEmpty(info) && info.equalsIgnoreCase("ok")) {
				String rfid = (String) result[1];
				currentGoods.setRFID(StrUtil.filterStr(rfid));
				currentGoods.setIsInit("1");// 表示已经初始化过RFID
				currentGoods.setState("1");// 选中
				GoodsDao.getInstance(context).updateGoods(currentGoods, false);
				MyTip.showToast(context, "初始化标签成功！");
				initDate();
			} else {
				MyTip.showToast(context, "初始化标签失败，不能出入库！");
			}
		}
		currentGoods = null;
		MyTip.cancelProgress();
	}

	private void updateRfid() {
		MyTip.showProgress(context, "初始化标签", "正在初始化标签，请稍等……");
		SaveRFIDinfo saveRFIDinfo = new SaveRFIDinfo();
		saveRFIDinfo
				.setPR_MATERIAL(UrlUtil.safeXml(currentGoods.getMATERIAL()));
		saveRFIDinfo.setPR_ENTRY_UOM(UrlUtil.safeXml(StrUtil
				.slipName(currentGoods.getUnit())));
		saveRFIDinfo.setPR_MESSAGE(UrlUtil.safeXml(currentGoods.getMESSAGE()));
		saveRFIDinfo.setPR_RFID_NO(UrlUtil.safeXml(currentGoods.getRFID()));
		saveRFIDinfo.setPR_PLANT(UrlUtil.safeXml(StrUtil.slipValue(currentGoods
				.getFactory())));
		saveRFIDinfo.setPR_STGE_LOC(UrlUtil.safeXml(StrUtil
				.slipValue(currentGoods.getStore())));
		String xml = saveRFIDinfo.getSaveRfidInfo(saveRFIDinfo);
		AsyncService.TempRFID tempRFID = AsyncService.getInstance().new TempRFID();
		tempRFID.execute(handler, ParamsUtil.TEMPRFID, app.getUrl(), app
				.getUser().getUserId(), xml);

	}

	/** 跳转到详情页面 */
	private void skipDetail(Goods good) {
		Intent it = new Intent(context, EquipDetailActivity.class);
		it.putExtra("good", good);
		startActivity(it);
	}

	/** 获取RFID信息结果 */
	protected void setRFIDInfoResult(Object obj) {
		// TODO Auto-generated method stub
		MyTip.cancelProgress();
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("ok") && result[1] != null) {
				RFIDinfo rfidInfo = (RFIDinfo) result[1];
				if (rfidInfo == null) {
					MyTip.showToast(context, "数据查询失败！");
					return;
				}
				Goods good = null;
				// 1、查询到rfid后保存到列表中，如果是维修订单或者采购订单，更新数据
				if (flag == ParamsUtil.maintenance_order) {
					// 维修订单出库，判断扫描到的rfid是否在列表中，如果在,更新库位信息和排序内容，如果rfid!=null and
					// num!=0 设置state='1' and isInit='1'
					good = GoodsDao.getInstance(context).findGoodByRFID(
							rfidInfo.getPR_RFID_NO());
					if (good != null) {
						String number = good.getENTRY_QNT();
						String infoGood = "0";
						if (StrUtil.isNotEmpty(number)
								&& !number.equalsIgnoreCase("0")) {
							infoGood = "1";
						}
						good.setState(infoGood);
						good.setIsInit(infoGood);
						good.setStore(rfidInfo.getStore());
						GoodsDao.getInstance(context).maintenanceOrderUpdate(
								good);
					} else {
						MyTip.showToast(context, "此设备信息不在维修订单中");
					}
				} else {
					if (flag == ParamsUtil.interior) {
						String factory = StrUtil.filterStr(rfidInfo
								.getFactory());
						String store = StrUtil.filterStr(rfidInfo.getStore());
						if (StrUtil.filterStr(in_factory).equalsIgnoreCase(
								factory)
								&& StrUtil.filterStr(in_store)
										.equalsIgnoreCase(store)) {
							// 如果是类型，删除后保存数据
							good = getGoodByRfidInfo(rfidInfo);
							GoodsDao.getInstance(context).saveOrUpdate(good);
						} else {
							MyTip.showToast(context,
									"查询到的设备工厂和库位与基础信息中填写的信息不匹配，不能入库!");
						}
					} else {
						// 如果是类型，删除后保存数据
						good = getGoodByRfidInfo(rfidInfo);
						GoodsDao.getInstance(context).saveOrUpdate(good);
					}
				}
				initDate();
			} else {
				MyTip.showToast(context, "没有查询到与此标签相匹配的数据!");
			}
		} else {
			MyTip.showToast(context, "没有查询到数据！");
		}
	}

	public Goods getGoodByRfidInfo(RFIDinfo rfiDinfo) {
		Goods good = new Goods();
		good.setRFID(StrUtil.filterStr(rfiDinfo.getPR_RFID_NO()));
		good.setMATERIAL(StrUtil.filterStr(rfiDinfo.getPR_MATERIAL()));
		good.setMESSAGE(StrUtil.filterStr(rfiDinfo.getPR_MESSAGE()));
		good.setENTRY_QNT("0");// 数量
		good.setUnit(StrUtil.filterStr(rfiDinfo.getUnit()));
		good.setFactory(StrUtil.filterStr(rfiDinfo.getFactory()));
		good.setStore(StrUtil.filterStr(rfiDinfo.getStore()));
		good.setState("0");
		good.setIsInit("0");
		return good;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(keyBoard.isShow()){
			return;
		}
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			if(keyBoard.isShow()){
				keyBoard.setShow(false);
			}else{
				isExit();
			}
			break;
		case R.id.save_btn:
			// 查询出入库设备列表是否为空，如果为空提示录入需要出入库的设备
			// 不为空，则获取基础信息
			String xml = getSubmitXmlByFlag();
			if (xml != null) {
				if (isConnectInternet()) {
					MyTip.showProgress(context, "提交数据", "数据提交中，请稍等");
					AsyncService.Upload upload = AsyncService.getInstance().new Upload();
					upload.execute(handler, ParamsUtil.UPLOAD,
							getPreference("url"), getUser().getUserId(), xml);
				}
			}
			break;
		case R.id.top_right_btn:
			MaterialDoc info = baseInfoView.getBaseInfoByFlag();
			if (info != null) {
				in_factory = info.getPLANT();
				in_store = info.getSTGE_LOC();
				Intent it = new Intent(context, AddEquipInfoActivity.class);
				it.putExtra("isNew", true);
				it.putExtra("factory", in_factory);
				it.putExtra("store", in_store);
				startActivity(it);
			}
			break;
		case R.id.order_search_btn:
			String order = StrUtil.filterStr(orderSearchAuto.getText().toString());
			if(!StrUtil.isNotEmpty(order)){
				MyTip.showToast(context, "请输入单号查询！");
				return;
			}
			if (!isConnectInternet())
				return;
			GoodsDao.getInstance(context).delectData();
			MyTip.showProgress(context, "加载查询", "正在查询数据中，请稍候……");
			if(flag == ParamsUtil.external){
				//采购订单
				// 清除临时数据
				new GetPoOrder().execute(order);
			}else if(flag == ParamsUtil.maintenance_order){
				//维修订单
				new GetPmOrder().execute(order);
			}
			break;
		default:
			break;
		}
	}

	private String getSubmitXmlByFlag() {
		List<Goods> goodsLs = GoodsDao.getInstance(context).getGoodLsByState(
				"1");
		if (goodsLs == null || goodsLs.size() == 0) {
			MyTip.showToast(context, "请添加需要出入库的设备！");
			return null;
		}
		MaterialDoc info = baseInfoView.getBaseInfoByFlag();
		if (info != null) {
			String order = StrUtil.filterStr(orderSearchAuto.getText().toString());
			if(flag == ParamsUtil.external){
				info.setPO_NUMBER(order);
			}else if(flag == ParamsUtil.maintenance_order){
				info.setORDERID(order);
			}
			return info.getSendXml(flag, goodsLs);
		}
		return null;
	}

	private String proof;
	private String year;

	/** 入库返回值 */
	protected void setUploadResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String isOk = (String) result[0];
			if (isOk.equalsIgnoreCase("ok")) {
				// 判断是否为 入库再出库类型的
				proof = (String) result[2];
				year = (String) result[3];
				if (StrUtil.isNotEmpty(proof) && StrUtil.isNotEmpty(year)) {
					GoodsDao.getInstance(context).updateStateAll(proof, year);
				}
				rfidEt.setText("");
				orderSearchAuto.setText("");
				baseInfoView.clearViewData();
				GoodsDao.getInstance(context).delectData();
				MyTip.showToast(context, "数据提交成功");
				MyTip.cancelProgress();
				String msg = "凭证号：" + proof + "\n" + "凭证年：" + year + "\n"
						+ "数据提交成功，是否继续操作？";
				MyTip.showDialog(context, "操作成功", msg, handler,
						ParamsUtil.IS_GOON);
			} else {
				String errorInfo = "数据提交错误";
				if (StrUtil.isNotEmpty((String) result[1])) {
					errorInfo = (String) result[1];
				}
				MyTip.showToast(context, errorInfo);
				MyTip.cancelProgress();
			}
		} else {
			MyTip.showToast(context, "数据提交失败");
			MyTip.cancelProgress();
		}
		initDate();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(keyBoard.isShow()){
				keyBoard.setShow(false);
			}else{
				isExit();
			}
		}
		return true;
	}

	private void isExit() {
		boolean goodsLs = GoodsDao.getInstance(context).getGoodLsExit();
		if (goodsLs) {
			finish();
		} else {
			MyTip.showDialog(context, "退出", "还有数据未处理，是否退出操作？", handler,
					ParamsUtil.EXIT_OPERATION);
		}
	}
	
	private class GetPoOrder extends AsyncTask<Object, Object, Object> {
		
		String order;

		@Override
		protected Object doInBackground(Object... params) {
			// String url, String UserIDEncdoe, String OrderID, Context context
			this.order = (String) params[0];
			return new GetPoOrderService().getPoOrder(app.getUrl(), "GetPoOrder", app
					.getUser().getUserId(), order, context);
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			baseInfoView.setOrderSupperTv("");
			if (result != null) {
				Object[] info = (Object[]) result;
				String flag = (String) info[0];
				if (flag.equalsIgnoreCase("OK")) {
					baseInfoView.setOrderSupperTv(StrUtil.filterStr((String) info[1]));
					handler.sendEmptyMessage(ParamsUtil.update_list);
					
					addOrderAutoTip();
				} else {
					MyTip.showToast(context, StrUtil.filterStr((String) info[1]));
				}
			}
			MyTip.cancelProgress();
		}
	}
	
	private class GetPmOrder extends AsyncTask<Object, Object, Object> {
		
		String order;

		@Override
		protected Object doInBackground(Object... params) {
			// String url, String UserIDEncdoe, String OrderID, Context context
			this.order = (String) params[0];
			return new GetPmOrderService().getPmOrder(app.getUrl(), "GetPmOrder", app .getUser().getUserId(), order, context);
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			baseInfoView.setOrderSupperTv("");
			if (result != null) {
				Object[] info = (Object[]) result;
				String flag = (String) info[0];
				if (flag.equalsIgnoreCase("OK")) {
					handler.sendEmptyMessage(ParamsUtil.update_list);
					
					addOrderAutoTip();
				} else {
					MyTip.showToast(context, StrUtil.filterStr((String) info[1]));
				}
			}
			MyTip.cancelProgress();
		}
	}
	
	private int showTipCount = 10;//自动提示显示条数
	private List<String> orderLs;
	private AutoTipAdapter orderAdapter;
	
	private void setOrderAutoTip(){
		if(flag == ParamsUtil.external){
			orderLs = OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_orderNum);
		}else if(flag == ParamsUtil.maintenance_order){
			orderLs = OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_modifyOrder);
		}
		orderAdapter = new AutoTipAdapter(context, orderLs, showTipCount);
		orderSearchAuto.setAdapter(orderAdapter);
		orderSearchAuto.setThreshold(0);
		orderSearchAuto.setInputType(InputType.TYPE_CLASS_NUMBER);
	}
	
	public void addOrderAutoTip(){
		String order = StrUtil.filterStr(orderSearchAuto.getText().toString());
		orderLs.clear();
		if(flag == ParamsUtil.external){
			OrderDao.getInstance(context).saveOrder(order,ParamsUtil.flag_orderNum);
			orderLs.addAll(OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_orderNum));
		}else if(flag == ParamsUtil.maintenance_order){
			OrderDao.getInstance(context).saveOrder(order,ParamsUtil.flag_modifyOrder);
			orderLs.addAll(OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_modifyOrder));
		}
		orderAdapter.notifyDataSetChanged();
	}

}
