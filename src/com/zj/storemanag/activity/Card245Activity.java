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
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.bean.SavePrintCard;
import com.zj.storemanag.bean.SaveRFIDinfo;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.OrderDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.NfcTools;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.view.custom_autotv.AutoTipAdapter;
import com.zj.storemanag.view.custom_autotv.AutoTipTextView;

public class Card245Activity extends BaseActivity {

	private AutoTipTextView materailEt;
	private List<String> materailLs = new ArrayList<String>();
	private AutoTipAdapter materailAdapter;
	private AutoTipTextView cwTv;
	private List<String> cwLs = new ArrayList<String>();
	private AutoTipAdapter cwAdapter;
	private AutoTipTextView proofEt;
	private AutoTipTextView yearEt;
	private List<String> proofNumLs = new ArrayList<String>();
	private List<String> proofYearLs = new ArrayList<String>();
	private AutoTipAdapter proofNumAdapter;
	private AutoTipAdapter proofYearAdapter;
	
	
	private CheckBox projectCb;
	private Button findBtn;
	private TextView unitTv;
	private TextView despTv;
	private TextView dateTv;
	private Button dateBtn;
	private TextView maxKcTv;
	private TextView minKcTv;
	private TextView standerTv;
	private TextView numberTv;
	private TextView useUnitTv;
	private TextView userFactoryTv;
	private TextView userStroeTv;
	private TextView supperTv;
	private TextView rfidEt;
	private Button confirmBtn;
	private Button printBtn;
	private Button updateBtn;

	// card1:设置title为属性 card3:设置title为wbs
	private TextView changeTitle;
	private TextView changeTv;
	private LinearLayout card25Layout;
	private LinearLayout card4Layout;

	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	boolean writeMode;
	Tag mytag;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	
	private boolean isWrite = true;
	private int position = -1;
	private boolean isPrint = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card245);

		initView();
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("卡片二四五");

		materailEt = (AutoTipTextView) findViewById(R.id.card245_matrail_num_et);
		cwTv = (AutoTipTextView) findViewById(R.id.card245_cw_num_et);
		proofEt = (AutoTipTextView) findViewById(R.id.card245_proof_et);
		yearEt = (AutoTipTextView) findViewById(R.id.card245_year_et);
		
		projectCb = (CheckBox) findViewById(R.id.card245_project);
		projectCb.setOnCheckedChangeListener(new MyCbCheck());
		findBtn = (Button) findViewById(R.id.card245_find_btn);

		unitTv = (TextView) findViewById(R.id.card245_unit_et);
		despTv = (TextView) findViewById(R.id.card245_matrail_desp_et);
		changeTitle = (TextView) findViewById(R.id.card245_change_tv);
		changeTv = (TextView) findViewById(R.id.card245_params_et);
		dateTv = (TextView) findViewById(R.id.card245_date_et);
		dateBtn = (Button) findViewById(R.id.card245_date_btn);

		maxKcTv = (TextView) findViewById(R.id.card245_zgkc_et);
		minKcTv = (TextView) findViewById(R.id.card245_zdkc_et);
		standerTv = (TextView) findViewById(R.id.card245_bzj_et);
		numberTv = (TextView) findViewById(R.id.card245_num_et);
		useUnitTv = (TextView) findViewById(R.id.card245_use_unit_et);
		userFactoryTv = (TextView) findViewById(R.id.card245_use_unit_factory);
		userStroeTv = (TextView) findViewById(R.id.card245_use_unit_location);
		supperTv = (TextView) findViewById(R.id.card245_supper_et);

		rfidEt = (TextView) findViewById(R.id.card245_rfid_et);
		confirmBtn = (Button) findViewById(R.id.card245_confirm_btn);
		printBtn = (Button) findViewById(R.id.card245_print_btn);
		updateBtn = (Button) findViewById(R.id.card245_udpate_rfid_btn);

		card25Layout = (LinearLayout) findViewById(R.id.card25_layout);
		card4Layout = (LinearLayout) findViewById(R.id.card4_layout);

		materailLs.addAll(OrderDao.getInstance(Card245Activity.this)
				.findOrderLs(ParamsUtil.flag_materail));
		materailAdapter = new AutoTipAdapter(Card245Activity.this,
				materailLs, 10);
		materailEt.setThreshold(0);
		materailEt.setAdapter(materailAdapter);
		
		proofNumLs.addAll(OrderDao.getInstance(Card245Activity.this)
				.findOrderLs(ParamsUtil.flag_proof_num));
		proofNumAdapter = new AutoTipAdapter(Card245Activity.this,
				proofNumLs, 10);
		proofEt.setThreshold(0);
		proofEt.setBtnGone();
		proofEt.setAdapter(proofNumAdapter);
		
		proofYearLs.addAll(OrderDao.getInstance(Card245Activity.this)
				.findOrderLs(ParamsUtil.flag_proof_year));
		proofYearAdapter = new AutoTipAdapter(Card245Activity.this,
				proofYearLs, 10);
		yearEt.setThreshold(0);
		yearEt.setBtnGone();
		yearEt.setAdapter(proofYearAdapter);
		
		
		cwLs.addAll(OrderDao.getInstance(Card245Activity.this)
				.findOrderLs(ParamsUtil.flag_cw));
		cwAdapter = new AutoTipAdapter(Card245Activity.this,
				cwLs, 10);
		cwTv.setThreshold(0);
		cwTv.setBtnGone();
		cwTv.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		cwTv.setAdapter(cwAdapter);
		
		position = getIntent().getIntExtra("position", -1);
		proof = StrUtil.filterStr(getIntent().getStringExtra("proof"));
		materail = StrUtil.filterStr(getIntent().getStringExtra("materail"));
		factory = StrUtil.filterStr(getIntent().getStringExtra("factory"));
		store = StrUtil.filterStr(getIntent().getStringExtra("store"));
		year = StrUtil.filterStr(getIntent().getStringExtra("year"));
		proofEt.setText(proof);
		materailEt.setText(materail);
		yearEt.setText(year);
		
		if(StrUtil.isNotEmpty(materail) && StrUtil.isNotEmpty(proof) && StrUtil.isNotEmpty(year)){
			// 查询
			queryMaterailInfo();
		}
		
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
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		try {
			mytag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			Log.i("zj", "onNewIntent");
			setIntent(intent);
			new NfcTools().readInfo(intent, mytag,rfidEt, isWrite, Card245Activity.this);
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
		switch (v.getId()) {
		case R.id.top_back_btn:
			if(!isPrint){
				getApp().setPosition(-1);
			}
			finish();
			break;
		case R.id.card245_date_btn:
			MyTip.showPopView(Card245Activity.this, getSelectLs(), dateTv, handler);
			break;
		case R.id.card245_find_btn:
			// 查询
			queryMaterailInfo();
			break;
		case R.id.card245_confirm_btn:
			// 确认 initRFID
			initRfidInfo();
			break;
		case R.id.card245_print_btn:
			// 打印
			printCard();
			break;
		case R.id.card245_udpate_rfid_btn:
			// 更新 UpdateRfID
			if (judgeEntry(true)) {
				if (!isConnectInternet()) {
					return;
				}
				String rfid = rfidEt.getText().toString();
				if (StrUtil.isNotEmpty(rfid) && !rfid.startsWith("T")) {
					MyTip.showProgress(Card245Activity.this, "提交",
							"正在提交数据，请稍候……");
					SaveRFIDinfo saveRFIDinfo = new SaveRFIDinfo();
					saveRFIDinfo.setPR_RFID_NO(UrlUtil.safeXml(rfid));
					saveRFIDinfo.setPR_MATERIAL(UrlUtil.safeXml(materail));
					saveRFIDinfo.setPR_ENTRY_UOM(UrlUtil.safeXml(StrUtil.slipValue(unitTv.getText().toString())));
					saveRFIDinfo.setPR_MESSAGE(UrlUtil.safeXml(despTv
							.getText().toString()));
					saveRFIDinfo.setPR_PLANT(UrlUtil.safeXml(userFactoryTv
							.getText().toString()));
					saveRFIDinfo.setPR_STGE_LOC(UrlUtil.safeXml(userStroeTv
							.getText().toString()));
					String plantStr = UrlUtil.safeXml(useUnitTv.getText().toString())
							+ "    " + UrlUtil.safeXml(userFactoryTv.getText().toString())
							+ "/" + UrlUtil.safeXml(userStroeTv.getText().toString());
					saveRFIDinfo.setPR_PLANT2(UrlUtil.safeXml(plantStr));// 存储点描述
					saveRFIDinfo.setPR_STGE_LOC2(UrlUtil.safeXml(cwTv.getText().toString()));
					String xml = saveRFIDinfo.getSaveRfidInfo(saveRFIDinfo);
					AsyncService.UpdateRfID updateRfID = AsyncService
							.getInstance().new UpdateRfID();
					updateRfID.execute(handler, ParamsUtil.UPDATERFID,
							getApp().getUrl(), xml);
				} else {
					MyTip.showToast(Card245Activity.this, "请扫描卡片获取标签信息");
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
			if(!isPrint){
				getApp().setPosition(-1);
			}
			finish();
		}
		return true;
	}


	/** 打印卡片 */
	private void printCard() {
		String project = "0";
		String cardType = "";
		if (projectCb.isChecked()) {
			project = "1";
			cardType = "四";
		} else {
			String js = dateTv.getText().toString();
			if (StrUtil.isNotEmpty(js)) {
				cardType = "五";
			} else {
				cardType = "二";
			}
		}
		String xml = getCardXml(cardType);
		if (!isConnectInternet())
			return;
		AsyncService.PrintCard printCard = AsyncService.getInstance().new PrintCard();
		printCard.execute(handler, ParamsUtil.PRINT_CARD, getApp().getUrl(),
				getUser().getUserId(), project, xml);
		
		OrderDao.getInstance(Card245Activity.this).saveOrder(
				cwTv.getText().toString(), ParamsUtil.flag_cw);
		cwLs.clear();
		cwLs.addAll(OrderDao.getInstance(Card245Activity.this)
				.findOrderLs(ParamsUtil.flag_cw));
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
				+ "    " + StrUtil.filterStr(userFactoryTv.getText().toString())
				+ "/" + StrUtil.filterStr(userStroeTv.getText().toString());
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
		savePrintCard.setInTime(StrUtil.filterStr(card.getBUDAT()));// 入库时间
		savePrintCard.setPR_PLANT(StrUtil.filterStr(userFactoryTv.getText().toString()));
		savePrintCard.setPR_STGE_LOC(StrUtil.filterStr(userStroeTv.getText().toString()));
		return savePrintCard.getSavePrintCardInfo(savePrintCard);
	}

	private void initRfidInfo() {
		if (judgeEntry(true)) {
			if (!isConnectInternet()) {
				return;
			}
			SaveRFIDinfo saveRFIDinfo = new SaveRFIDinfo();
			saveRFIDinfo.setPR_MATERIAL(UrlUtil.safeXml(materail));
			saveRFIDinfo.setPR_PLANT(UrlUtil.safeXml(userFactoryTv.getText().toString()));
			saveRFIDinfo.setPR_STGE_LOC(UrlUtil.safeXml(userStroeTv.getText().toString()));
			saveRFIDinfo.setPR_RFID_NO(UrlUtil.safeXml(rfidEt.getText()
					.toString()));
			saveRFIDinfo.setPR_ENTRY_UOM(UrlUtil.safeXml(StrUtil.slipValue(unitTv.getText().toString())));
			saveRFIDinfo.setPR_MESSAGE(UrlUtil.safeXml(despTv
					.getText().toString()));
			String plantStr = UrlUtil.safeXml(useUnitTv.getText().toString())
					+ "    " + UrlUtil.safeXml(userFactoryTv.getText().toString())
					+ "/" + UrlUtil.safeXml(userStroeTv.getText().toString());
			saveRFIDinfo.setPR_PLANT2(UrlUtil.safeXml(plantStr));// 存储点描述
			saveRFIDinfo.setPR_STGE_LOC2(UrlUtil.safeXml(cwTv.getText().toString()));
			String xml = saveRFIDinfo.getSaveRfidInfo(saveRFIDinfo);
			AsyncService.TempRFID initRFID = AsyncService.getInstance().new TempRFID();
			initRFID.execute(handler, ParamsUtil.TEMPRFID, getApp().getUrl(),
					getUser().getUserId(), xml);
		}
	}

	private String materail;
	private String proof;
	private String year;

	/** 获取物料信息 */
	private void queryMaterailInfo() {
		if (judgeEntry(false)) {
			if (!isConnectInternet()) {
				return;
			}
			MyTip.showProgress(Card245Activity.this, "查询", "正在查询数据，请稍候……");
			AsyncService.GetWLKP getWLKP = AsyncService.getInstance().new GetWLKP();
			// 13: cardType貌似没用随便传的值
			getWLKP.execute(handler, ParamsUtil.GETCARD245, getApp().getUrl(),
					materail, proof, year, "2", getApp().getUser().getUserId(),
					Card245Activity.this);
		}
	}

	private String factory;
	private String store;

	private boolean judgeEntry(boolean isPrint) {
		materail = materailEt.getText().toString();
		if (!StrUtil.isNotEmpty(materail)) {
			MyTip.showToast(Card245Activity.this, "请输入物料号！");
			return false;
		}
		if (isPrint) {
			if (card == null) {
				MyTip.showToast(Card245Activity.this, "请先输入物料号、凭证号、年份查询信息！");
				return false;
			}
			String userFactory = userFactoryTv.getText().toString();
			String userStore = userStroeTv.getText().toString();
			if (!StrUtil.isNotEmpty(userFactory)) {
				MyTip.showToast(Card245Activity.this, "没有查询到工厂信息，不能初始化！");
				return false;
			}
			if (!StrUtil.isNotEmpty(userStore)) {
				MyTip.showToast(Card245Activity.this, "没有查询到库位信息，不能初始化！");
				return false;
			}
		} else {
			proof = proofEt.getText().toString();
			year = yearEt.getText().toString();
			if (!StrUtil.isNotEmpty(proof)) {
				MyTip.showToast(Card245Activity.this, "请输入凭证号！");
				return false;
			}
			if (!StrUtil.isNotEmpty(year)) {
				MyTip.showToast(Card245Activity.this, "请输入年！");
				return false;
			}
		}

		return true;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.POP_SELECT:
				if (msg.obj != null
						&& msg.obj.getClass().isInstance(new PopItem())) {
					PopItem item = (PopItem) msg.obj;
					dateTv.setText(item.getName());
				}
				break;
			case ParamsUtil.GETCARD245:
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
				MyTip.showToast(Card245Activity.this, "更新成功！");
			} else {
				MyTip.showToast(Card245Activity.this, (String) result[1]);
			}
		}
		MyTip.cancelProgress();
	}

	/** 设置更新RFID结果 */
	protected void setUpdateRfidResult(Object obj) {
		MyTip.cancelProgress();
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("0")) {
				// 调用初始化
				initRfidInfo();
			} else {
				MyTip.showToast(Card245Activity.this, (String) result[1]);
			}
		}
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
			}else{
				getApp().setPosition(-1);
			}
			MyTip.showToast(Card245Activity.this, (String) result[1]);
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
				OrderDao.getInstance(Card245Activity.this).saveOrder(
						materailEt.getText().toString(), ParamsUtil.flag_materail);
				materailLs.clear();
				materailLs.addAll(OrderDao.getInstance(Card245Activity.this)
						.findOrderLs(ParamsUtil.flag_materail));
				materailAdapter.notifyDataSetChanged();
				
				OrderDao.getInstance(Card245Activity.this).saveOrder(
						proofEt.getText().toString(), ParamsUtil.flag_proof_num);
				proofNumLs.clear();
				proofNumLs.addAll(OrderDao.getInstance(Card245Activity.this)
						.findOrderLs(ParamsUtil.flag_proof_num));
				proofNumAdapter.notifyDataSetChanged();
				
				OrderDao.getInstance(Card245Activity.this).saveOrder(
						yearEt.getText().toString(), ParamsUtil.flag_proof_year);
				proofYearLs.clear();
				proofYearLs.addAll(OrderDao.getInstance(Card245Activity.this)
						.findOrderLs(ParamsUtil.flag_proof_year));
				proofYearAdapter.notifyDataSetChanged();
				
			} else {
				MyTip.showToast(Card245Activity.this, (String) result[1]);
			}
		} else {
			MyTip.showToast(Card245Activity.this, "暂无数据返回");
		}
		MyTip.cancelProgress();
	}

	private void setViewsData() {
		if (card == null)
			return;
		cwTv.setText(StrUtil.filterStr(card.getLGPBE()));
		unitTv.setText(StrUtil.slipName(card.getENTRY_UOM()));
		despTv.setText(StrUtil.filterStr(card.getMAKTX()));
		changeTv.setText(StrUtil.filterStr(card.getNORMT()));
		maxKcTv.setText(StrUtil.filterStr(card.getMABST()));
		minKcTv.setText(StrUtil.filterStr(card.getMINBE()));
		standerTv.setText(StrUtil.filterStr(card.getSTPRS()));
		numberTv.setText(StrUtil.filterStr(card.getMENGE()));
		useUnitTv.setText(StrUtil.filterStr(card.getLGOBE()));
		userFactoryTv.setText(StrUtil.filterStr(card.getWERKS()));
		userStroeTv.setText(StrUtil.filterStr(card.getLGORT()));
		supperTv.setText(StrUtil.filterStr(card.getNAME2()));
	}

	private List<PopItem> getSelectLs() {
		List<PopItem> list = new ArrayList<PopItem>();
		list.add(new PopItem("1", null, "无", false, null));
		list.add(new PopItem("2", null, "寄售", false, null));
		list.add(new PopItem("3", null, "寄存", false, null));
		return list;
	}

	/** 选择是否工程 ， 确定显示Card1 或是Card3 */
	private class MyCbCheck implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				changeTitle.setText("WBS:");
				card25Layout.setVisibility(View.GONE);
				card4Layout.setVisibility(View.VISIBLE);
			} else {
				changeTitle.setText("属性:");
				card25Layout.setVisibility(View.VISIBLE);
				card4Layout.setVisibility(View.GONE);
			}
		}
	}
}
