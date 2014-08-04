package com.zj.storemanag.activity;

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
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.Card;
import com.zj.storemanag.bean.SavePrintCard;
import com.zj.storemanag.bean.SaveRFIDinfo;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.OrderDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.NfcTools;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.TimeUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.view.TakeTimeDialog;
import com.zj.storemanag.view.custom_autotv.AutoTipAdapter;
import com.zj.storemanag.view.custom_autotv.AutoTipTextView;

public class Card13Activity extends BaseActivity {

	private AutoTipTextView materailEt;
	private List<String> materailLs = new ArrayList<String>();
	private AutoTipAdapter materalAdapter;
	private AutoTipTextView cwTv;
	private List<String> cwLs = new ArrayList<String>();
	private AutoTipAdapter cwAdapter;

	private CheckBox projectCb;
	private EditText factoryEt;
	private EditText storeEt;
	private Button findBtn;
	private TextView unitTv;
	private TextView despTv;
	private TextView dateEt;
	private Button dateBtn;
	private TextView maxKcTv;
	private TextView minKcTv;
	private TextView standerTv;
	private TextView numberTv;
	private TextView useUnitTv;
	private TextView supperTv;
	private TextView rfidEt;
	private Button scanBtn;
	private Button confirmBtn;
	private Button printBtn;
	private Button updateBtn;

	private TakeTimeDialog timeDialog;

	// card1:设置title为属性 card3:设置title为wbs
	private TextView changeTitle;
	private TextView changeTv;
	private LinearLayout card1Layout;
	private LinearLayout card3Layout;

	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	boolean writeMode;
	Tag mytag;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;

	private boolean isWrite = true;

	private int position = -1;

	private boolean isPrint = false;
	
	private Activity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card13);

		context = this;
		initView();
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("旧卡");

		materailEt = (AutoTipTextView) findViewById(R.id.card13_matrail_num_et);
		cwTv = (AutoTipTextView) findViewById(R.id.card13_cw_num_et);

		materailEt.setInputType(InputType.TYPE_CLASS_NUMBER);
		projectCb = (CheckBox) findViewById(R.id.card13_project);
		projectCb.setOnCheckedChangeListener(new MyCbCheck());
		factoryEt = (EditText) findViewById(R.id.card13_factory_et);
		storeEt = (EditText) findViewById(R.id.card13_store_et);
		findBtn = (Button) findViewById(R.id.card13_find_btn);

		unitTv = (TextView) findViewById(R.id.card13_unit_et);
		despTv = (TextView) findViewById(R.id.card13_matrail_desp_et);
		changeTitle = (TextView) findViewById(R.id.card13_change_tv);
		changeTv = (TextView) findViewById(R.id.card13_params_et);
		dateEt = (TextView) findViewById(R.id.card13_date_et);
		dateEt.setText(TimeUtil.getCurrentDate());
		dateEt.setOnClickListener(this);
		dateBtn = (Button) findViewById(R.id.card13_date_btn);

		maxKcTv = (TextView) findViewById(R.id.card13_zgkc_et);
		minKcTv = (TextView) findViewById(R.id.card13_zdkc_et);
		standerTv = (TextView) findViewById(R.id.card13_bzj_et);
		numberTv = (TextView) findViewById(R.id.card13_num_et);
		useUnitTv = (TextView) findViewById(R.id.card13_use_unit_et);
		supperTv = (TextView) findViewById(R.id.card13_supper_et);

		rfidEt = (TextView) findViewById(R.id.card13_rfid_et);
		// scanBtn = (Button) findViewById(R.id.card13_rfid_scan_btn);
		confirmBtn = (Button) findViewById(R.id.card13_confirm_btn);
		printBtn = (Button) findViewById(R.id.card13_print_btn);
		updateBtn = (Button) findViewById(R.id.card13_udpate_rfid_btn);

		card1Layout = (LinearLayout) findViewById(R.id.card1_layout);
		card3Layout = (LinearLayout) findViewById(R.id.card3_layout);

		materailLs.addAll(OrderDao.getInstance(context)
				.findOrderLs(ParamsUtil.flag_materail));
		materalAdapter = new AutoTipAdapter(context, materailLs, 10);
		materailEt.setThreshold(0);
		materailEt.setAdapter(materalAdapter);

		cwLs.addAll(OrderDao.getInstance(context).findOrderLs(
				ParamsUtil.flag_cw));
		cwAdapter = new AutoTipAdapter(context, cwLs, 10);
		cwTv.setThreshold(0);
		cwTv.setBtnGone();
		cwTv.setAdapter(cwAdapter);
		cwTv.setInputType(InputType.TYPE_CLASS_NUMBER);

		Intent it = getIntent();
		position = it.getIntExtra("position", -1);
		String material = StrUtil.filterStr(it.getStringExtra("material"));
		String factory = StrUtil.filterStr(it.getStringExtra("factory"));
		String store = StrUtil.filterStr(it.getStringExtra("store"));
		materailEt.setText(material);
		factoryEt.setText(StrUtil.slipValue(factory));
		storeEt.setText(StrUtil.slipValue(store));

		if (StrUtil.isNotEmpty(material) && StrUtil.isNotEmpty(factory)
				&& StrUtil.isNotEmpty(store)) {
			// 查询
			queryMaterailInfo();
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
		try {
			mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Log.i("zj", "onNewIntent");
			setIntent(intent);
			new NfcTools().readInfo(intent, mytag, rfidEt, isWrite,
					context);
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
			if (!isPrint) {
				getApp().setPosition(-1);
			}
			finish();
			break;
		case R.id.card13_find_btn:
			// 查询
			queryMaterailInfo();
			break;
		case R.id.card13_date_et:
		case R.id.card13_date_btn:
			// 时间选择
			timeDialog = new TakeTimeDialog(context,
					R.style.modifydialog, dateEt);
			timeDialog.show();
			break;
		case R.id.card13_confirm_btn:
			// 确认 InitRFID
			initRfidInfo();
			break;
		case R.id.card13_print_btn:
			// 打印 PrintCard
			printCard();
			break;
		case R.id.card13_udpate_rfid_btn:
			// 更新 UpdateRfID
			if (judgeEntry()) {
				if (!isConnectInternet()) {
					return;
				}
				String rfid = rfidEt.getText().toString();
				if (StrUtil.isNotEmpty(rfid) && !rfid.startsWith("T")) {
					MyTip.showProgress(context, "提交",
							"正在提交数据，请稍候……");
					SaveRFIDinfo saveRFIDinfo = new SaveRFIDinfo();
					saveRFIDinfo.setPR_MATERIAL(UrlUtil.safeXml(materail));
					saveRFIDinfo.setPR_PLANT(UrlUtil.safeXml(factory));
					saveRFIDinfo.setPR_STGE_LOC(UrlUtil.safeXml(store));
					saveRFIDinfo.setPR_MESSAGE(UrlUtil.safeXml(despTv
							.getText().toString()));
					saveRFIDinfo.setPR_ENTRY_UOM(UrlUtil.safeXml(StrUtil.slipValue(unitTv.getText().toString())));
					saveRFIDinfo.setPR_RFID_NO(UrlUtil.safeXml(rfid));
					String plantStr = UrlUtil.safeXml(useUnitTv.getText()
							.toString())
							+ "    "
							+ UrlUtil.safeXml(factoryEt.getText().toString())
							+ "/"
							+ UrlUtil.safeXml(storeEt.getText().toString());
					saveRFIDinfo.setPR_PLANT2(UrlUtil.safeXml(plantStr));// 存储点描述
					saveRFIDinfo.setPR_STGE_LOC2(UrlUtil.safeXml(cwTv
							.getText().toString()));
					String xml = saveRFIDinfo.getSaveRfidInfo(saveRFIDinfo);
					AsyncService.UpdateRfID updateRfID = AsyncService
							.getInstance().new UpdateRfID();
					updateRfID.execute(handler, ParamsUtil.UPDATERFID, getApp()
							.getUrl(), xml);
				} else {
					MyTip.showToast(context, "请先扫描标签");
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isPrint) {
				getApp().setPosition(-1);
			}
			finish();
		}
		return true;
	}

	private void initRfidInfo() {
		if (judgeEntry()) {
			if (!isConnectInternet()) {
				return;
			}
			SaveRFIDinfo saveRFIDinfo = new SaveRFIDinfo();
			saveRFIDinfo.setPR_MATERIAL(UrlUtil.safeXml(materail));
			saveRFIDinfo.setPR_PLANT(UrlUtil.safeXml(factory));
			saveRFIDinfo.setPR_STGE_LOC(UrlUtil.safeXml(store));
			saveRFIDinfo.setPR_RFID_NO(UrlUtil.safeXml(rfidEt.getText()
					.toString()));
			saveRFIDinfo.setPR_ENTRY_UOM(UrlUtil.safeXml(StrUtil.slipValue(unitTv.getText().toString())));
			saveRFIDinfo.setPR_MESSAGE(UrlUtil.safeXml(despTv.getText()
					.toString()));
			String plantStr = UrlUtil.safeXml(useUnitTv.getText().toString())
					+ "    " + UrlUtil.safeXml(factoryEt.getText().toString())
					+ "/" + UrlUtil.safeXml(storeEt.getText().toString());
			saveRFIDinfo.setPR_PLANT2(UrlUtil.safeXml(plantStr));// 存储点描述
			saveRFIDinfo.setPR_STGE_LOC2(UrlUtil.safeXml(cwTv.getText()
					.toString()));
			String xml = saveRFIDinfo.getSaveRfidInfo(saveRFIDinfo);
			AsyncService.TempRFID initRFID = AsyncService.getInstance().new TempRFID();
			initRFID.execute(handler, ParamsUtil.TEMPRFID, getApp().getUrl(),
					getUser().getUserId(), xml);
		}
		MyTip.cancelProgress();
	}

	/** 打印卡片 */
	private void printCard() {
		String project = "0";
		String cardType = "一";
		if (projectCb.isChecked()) {
			project = "1";
			cardType = "三";
		}
		String xml = getCardXml(cardType);
		if (!isConnectInternet())
			return;
		AsyncService.PrintCard printCard = AsyncService.getInstance().new PrintCard();
		printCard.execute(handler, ParamsUtil.PRINT_CARD, getApp().getUrl(),
				getUser().getUserId(), project, xml);

		OrderDao.getInstance(context).saveOrder(
				cwTv.getText().toString(), ParamsUtil.flag_cw);
		cwLs.clear();
		cwLs.addAll(OrderDao.getInstance(context).findOrderLs(
				ParamsUtil.flag_cw));
		cwAdapter.notifyDataSetChanged();

	}

	private String getCardXml(String cardType) {
		SavePrintCard savePrintCard = new SavePrintCard();
		savePrintCard.setNum(cardType);
		savePrintCard.setPR_MATERIAL(StrUtil.filterStr(materailEt.getText()
				.toString()));// 物料号
		savePrintCard.setPR_STGE_LOC2(StrUtil.filterStr(cwTv.getText()
				.toString()));// 仓位号
		savePrintCard.setPR_MESSAGE(StrUtil.filterStr(despTv.getText()
				.toString()));// 物料描述
		savePrintCard.setPR_ENTRY_UOM(StrUtil.slipValue(unitTv.getText()
				.toString()));// 计量单位
		String plantStr = StrUtil.filterStr(useUnitTv.getText().toString())
				+ "    " + StrUtil.filterStr(factoryEt.getText().toString()) + "/"
				+ StrUtil.filterStr(storeEt.getText().toString());
		savePrintCard.setPR_PLANT2(StrUtil.filterStr(plantStr));// 存储点描述
		savePrintCard.setKC_H(StrUtil.filterStr(maxKcTv.getText().toString()));// 最高库存
		savePrintCard.setKC_L(StrUtil.filterStr(minKcTv.getText().toString()));// 最低库存
		savePrintCard.setStandPrice(StrUtil.filterStr(standerTv.getText()
				.toString()));// 标准价格
		savePrintCard
				.setCount(StrUtil.filterStr(numberTv.getText().toString()));// //
																			// 收货数量
		savePrintCard
				.setnAME2(StrUtil.filterStr(supperTv.getText().toString()));// 供应商
		savePrintCard.setCreate_ID(StrUtil.filterStr(getApp().getUser()
				.getUserId()));// 保管员
		savePrintCard.setInTime(StrUtil.filterStr(dateEt.getText().toString()
				.replaceAll("-", "")));// 入库时间
		savePrintCard.setPR_PLANT(StrUtil.filterStr(factoryEt.getText()
				.toString()));
		savePrintCard.setPR_STGE_LOC(StrUtil.filterStr(storeEt.getText()
				.toString()));
		return savePrintCard.getSavePrintCardInfo(savePrintCard);
	}

	private String materail;
	private String factory;
	private String store;

	/** 获取物料信息 */
	private void queryMaterailInfo() {
		// TODO Auto-generated method stub
		if (judgeEntry()) {
			if (!isConnectInternet()) {
				return;
			}
			MyTip.showProgress(context, "查询", "正在查询数据，请稍候……");
			AsyncService.GetWLKP2 getWLKP2 = AsyncService.getInstance().new GetWLKP2();
			// 13: cardType貌似没用随便传的值
			getWLKP2.execute(handler, ParamsUtil.GETCARD13, getApp().getUrl(),
					materail, factory, store, "1", getUser().getUserId(),
					context);
		}
	}

	private boolean judgeEntry() {
		materail = materailEt.getText().toString();
		factory = factoryEt.getText().toString();
		store = storeEt.getText().toString();
		if (!StrUtil.isNotEmpty(materail)) {
			MyTip.showToast(context, "请填写物料号！");
			return false;
		}
		if (!StrUtil.isNotEmpty(factory)) {
			MyTip.showToast(context, "请填写工厂号！");
			return false;
		}
		if (!StrUtil.isNotEmpty(store)) {
			MyTip.showToast(context, "请填写库位号！");
			return false;
		}
		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.GETCARD13:
				setMaterailResult(msg.obj);
				break;
			case ParamsUtil.TEMPRFID:
				setInitRfidResult(msg.obj);
				break;
			case ParamsUtil.UPDATERFID:
				setUpdateRfidResult(msg.obj);
				break;
			case ParamsUtil.PRINT_CARD:
				setPrintCardResult(msg.obj);
				break;

			default:
				break;
			}
		};
	};

	/** 设置初始化RFID结果 */
	protected void setInitRfidResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("ok")) {
				// 初始化rfid成功
				MyTip.showToast(context, "更新成功！");
			} else {
				MyTip.showToast(context, (String) result[1]);
			}
		}
		MyTip.cancelProgress();
	}

	/** 设置打印卡片结果 */
	protected void setPrintCardResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("ok")) {
				// 打印卡片成功
				isPrint = true;
				getApp().setPosition(position);
			} else {
				getApp().setPosition(-1);
			}
			MyTip.showToast(context, (String) result[1]);
		}
		MyTip.cancelProgress();
	}

	/** 设置更新RFID结果 */
	protected void setUpdateRfidResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("0")) {
				// 调用初始化
				initRfidInfo();
			} else {
				MyTip.showToast(context, (String) result[1]);
			}
		}
		MyTip.cancelProgress();

	}

	private Card card;

	/** 设置查询物料信息结果 */
	protected void setMaterailResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("ok")) {
				card = (Card) result[1];
				setViewsData();
				OrderDao.getInstance(context).saveOrder(
						materailEt.getText().toString(),
						ParamsUtil.flag_materail);
				materailLs.clear();
				materailLs.addAll(OrderDao.getInstance(context)
						.findOrderLs(ParamsUtil.flag_materail));
				materalAdapter.notifyDataSetChanged();
			} else {
				MyTip.showToast(context, "数据已变更，没有查询到数据！");
			}
		} else {
			MyTip.showToast(context, "暂无数据返回");
		}
		MyTip.cancelProgress();
	}

	private void setViewsData() {
		if (card == null)
			return;
		cwTv.setText(StrUtil.filterStr(card.getLGPBE()));
		unitTv.setText(StrUtil.slipName(card.getENTRY_UOM()));
		despTv.setText(StrUtil.filterStr(card.getMAKTX()));
		if (projectCb.isChecked()) {
			changeTv.setText(StrUtil.filterStr(card.getPOSKI()));
		} else {
			changeTv.setText("");
		}
		maxKcTv.setText(StrUtil.filterStr(card.getMABST()));
		minKcTv.setText(StrUtil.filterStr(card.getMINBE()));
		standerTv.setText(StrUtil.filterStr(card.getSTPRS()));
		numberTv.setText(StrUtil.filterStr(card.getMENGE()));
		useUnitTv.setText(StrUtil.filterStr(card.getLGOBE()));
		supperTv.setText(StrUtil.filterStr(card.getNAME2()));
		String timeStr = card.getBUDAT();
		if (StrUtil.isNotEmpty(timeStr)) {
			timeStr = timeStr.substring(0, 4) + "-" + timeStr.substring(4, 6)
					+ "-" + timeStr.substring(6);
			if (timeStr.equalsIgnoreCase("00000000")) {
				timeStr = TimeUtil.getCurrentDate();
			}
		}
		dateEt.setText(timeStr);
	}

	/** 选择是否工程 ， 确定显示Card1 或是Card3 */
	private class MyCbCheck implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				changeTitle.setText("WBS:");
				card1Layout.setVisibility(View.GONE);
				card3Layout.setVisibility(View.VISIBLE);
			} else {
				changeTitle.setText("属性:");
				card1Layout.setVisibility(View.VISIBLE);
				card3Layout.setVisibility(View.GONE);
			}
		}
	}
}
