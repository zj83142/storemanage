package com.zj.storemanag.activity;

import java.io.File;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.ImgBean;
import com.zj.storemanag.bean.RFIDinfo;
import com.zj.storemanag.bean.StoreBean;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.ImgDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.NfcTools;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.Utils;
import com.zj.storemanag.view.NetImageView;

public class RFIDQueryActivity extends BaseActivity {

	private EditText rfidEt;

	private String rfidStr;

	private RFIDinfo rfidInfo;

	private TextView materialEt;
	private TextView factoryEt;
	private TextView storeEt;
	private TextView despEt;// 物料描述
	private TextView unitEt;// 单位
	private TextView fxgfkcEt;// 非限估计库存
	private TextView xzjskcEt;// 限制寄售库存
	private TextView totalStoreEt;// 总计库存
	private TextView fxjskcEt;// 非限寄售库存
	private TextView cwEt;// 库位
	private TextView xmcwEt;// 项目库存

	private boolean skip = false;

	private TextView titleTv;

	private StoreBean storeBean;
	private LinearLayout rfidQueryLayout;

	private NetImageView netImageView;

	NfcAdapter mAdapter;
	PendingIntent mPendingIntent;
	boolean writeMode;
	Tag mytag;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;

	private boolean isWrite = false;
	
	private Button photoBtn;
	
	private Activity context;
	
	private String pathStr;
	private String urlStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rfid_query);
		Log.i("zj", "----------onCreate----------");
		context = this;
		initView();
	}

	private void initView() {
		titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("标签查询");
		
		photoBtn = (Button) findViewById(R.id.top_right_btn);
		photoBtn.setText("拍照");
		photoBtn.setVisibility(View.VISIBLE);
		photoBtn.setOnClickListener(this);
		
		rfidEt = (EditText) findViewById(R.id.rfid_query_et);

		rfidQueryLayout = (LinearLayout) findViewById(R.id.rfid_query_layout);
		materialEt = (TextView) findViewById(R.id.rfid_query_material_et);
		factoryEt = (TextView) findViewById(R.id.rfid_query_factory_et);
		storeEt = (TextView) findViewById(R.id.rfid_query_store_location_et);
		despEt = (TextView) findViewById(R.id.rfid_query_material_desp_et);
		unitEt = (TextView) findViewById(R.id.rfid_query_store_unit_et);
		fxgfkcEt = (TextView) findViewById(R.id.rfid_query_fxgjkc_et);

		xzjskcEt = (TextView) findViewById(R.id.rfid_query_xzjskc_et);
		totalStoreEt = (TextView) findViewById(R.id.rfid_query_total_store_et);
		fxjskcEt = (TextView) findViewById(R.id.rfid_query_fxjskc_et);
		cwEt = (TextView) findViewById(R.id.rfid_query_cw_et);
		xmcwEt = (TextView) findViewById(R.id.rfid_query_xmcw_et);
		netImageView = (NetImageView) findViewById(R.id.material_img);
		netImageView.setOnClickListener(this);

		skip = getIntent().getBooleanExtra("skip", false);
		if (skip) {
			// 从物料号查询界面跳转进来的
			storeBean = (StoreBean) getIntent()
					.getSerializableExtra("material");
			if (storeBean != null) {
				setViewData();
			}
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
				if (skip) {
					// 采购订单入库不需要扫描rfid
					return;
				}
				String rfid = rfidEt.getText().toString();
				if (StrUtil.isNotEmpty(rfid) && rfid.length() == 32) {
					MyTip.showProgress(context, "查询", "正在查询数据，请稍候……");
					AsyncService.GetRFIDInfo getRFIDInfo = AsyncService.getInstance().new GetRFIDInfo();
					getRFIDInfo.execute(handler, ParamsUtil.GETRFIDINFO,
							getPreference("url"), getUser().getUserId(), rfid,
							context);
				}
			}
		});
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i("zj", "----------onNewIntent----------");
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
		if(out != null && out.length() > 0){
			netImageView.setVisibility(View.VISIBLE);
			netImageView.setImagePath(out);
			String material = StrUtil.filterStr(materialEt.getText().toString());
			String factoryStr = StrUtil.slipValue(factoryEt.getText().toString());
			String storeStr = StrUtil.slipValue(storeEt.getText().toString());
			ImgDao.getInstance(context).updateImgState(material, factoryStr, storeStr);
		}
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
					pathStr = photoPath;
					Log.i("zj", "保存照片：" + photoPath);
					// _id, material, factory, store, path, userId, state
					ImgBean bean = new ImgBean();
					bean.setMaterial(materialEt.getText().toString());
					bean.setFactory(StrUtil.slipValue(factoryEt.getText().toString()));
					bean.setStore(StrUtil.slipValue(storeEt.getText().toString()));
					bean.setPath(photoPath);
					bean.setUserId(getUser().getUserId());
					bean.setState("0");
					
					bean.setDesp(StrUtil.filterStr(despEt.getText().toString()));
					bean.setUnit(StrUtil.filterStr(unitEt.getText().toString()));
					bean.setCw(StrUtil.filterStr(cwEt.getText().toString()));
					// 保存图片到表中
					boolean isResult = ImgDao.getInstance(
							context).save(bean);
					if (isResult) {
//						MyTip.showToast(AddEquipInfoActivity.this, "图片保存成功");
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		// case R.id.rfid_query_scan_btn:
		// // 扫描
		// rfidEt.setText("123456789");
		// break;
		case R.id.top_right_btn:
			if (!judgeMaterial()) {
				// 请先输入
				MyTip.showToast(context, "请查询完整物料信息再拍照！");
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
		case R.id.rfid_query_find_btn:
			// 查询RFIDInfo
			rfidStr = rfidEt.getText().toString();
			if (!StrUtil.isNotEmpty(rfidStr)) {
				MyTip.showToast(context, "请输入标签信息！");
				return;
			}
			MyTip.showProgress(context, "查询", "正在查询数据，请稍候……");
			AsyncService.GetRFIDInfo getRFIDInfo = AsyncService.getInstance().new GetRFIDInfo();
			getRFIDInfo.execute(handler, ParamsUtil.GETRFIDINFO,
					getPreference("url"), getUser().getUserId(), rfidStr,
					context);
			break;
		case R.id.material_img:
			Intent it = new Intent(context, PhotoScanActivity.class);
			it.putExtra("path", pathStr);
			it.putExtra("url", urlStr);
			startActivity(it);
			break;

		default:
			break;
		}
	}
	
	private boolean judgeMaterial() {
		String material = StrUtil.filterStr(materialEt.getText().toString());
		String factoryStr = StrUtil.slipValue(factoryEt.getText().toString());
		String storeStr = StrUtil.slipValue(storeEt.getText().toString());
		String unit = StrUtil.filterStr(unitEt.getText().toString());
		if (StrUtil.isNotEmpty(material) && StrUtil.isNotEmpty(factoryStr)
				&& StrUtil.isNotEmpty(storeStr) && StrUtil.isNotEmpty(unit)) {
			return true;
		}
		return false;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.GETRFIDINFO:
				// 获取RFIDInfo
				setRfidInfoResult(msg.obj);
				break;
			case ParamsUtil.GETKC:
				setKCResult(msg.obj);
				break;
			case ParamsUtil.GETKC2:
				setKC2Result(msg.obj);
				break;
			case ParamsUtil.load_img:
				if (msg.obj != null) {
					netImageView.setVisibility(View.VISIBLE);
					String imgPath = (String) msg.obj;
					if (imgPath.startsWith("../")) {
						imgPath = imgPath.replace("../", "");
					}
					imgPath = "http://" + getApp().getUrl() + "/" + imgPath;
					Log.i("zj", "加载图片路径：" + imgPath);
					// String url =
					// "http://pic23.nipic.com/20120909/5907156_101905268000_2.jpg";
					urlStr = imgPath;
					netImageView.setImageUrl(imgPath);
				} else {
					netImageView.setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}
		}
	};

	private List<StoreBean> list;

	/** 设置库存查询结果 */
	protected void setKCResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("ok")) {
				list = (List<StoreBean>) result[1];
				setViewsData();
				// 调用项目库存接口
				AsyncService.GetKC2 getKc = AsyncService.getInstance().new GetKC2();
				getKc.execute(handler, ParamsUtil.GETKC2, getApp().getUrl(),
						materialStr, factoryStr, storeStr,
						context);
			} else {
				MyTip.showToast(context, "数据已变更，没有查询到数据！");
			}
		} else {
			clearViewDate();
		}
		MyTip.cancelProgress();
	}

	protected void setKC2Result(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("ok")) {
				List<StoreBean> tempLs = (List<StoreBean>) result[1];
				if (tempLs != null && tempLs.size() > 0) {
					xmcwEt.setText(tempLs.get(0).getLABST());
				} else {
					xmcwEt.setText("");
				}
			} else {
				xmcwEt.setText("");
			}
		} else {
			xmcwEt.setText("");
		}

	}

	private void clearViewDate() {
		materialEt.setText("");
		factoryEt.setText("");
		storeEt.setText("");
		despEt.setText("");
		unitEt.setText("");
		fxgfkcEt.setText("");

		xzjskcEt.setText("");
		totalStoreEt.setText("");
		fxjskcEt.setText("");
		cwEt.setText("");
		xmcwEt.setText("");
	}

	private void setViewsData() {
		if (rfidInfo == null) {
			rfidEt.setText("");
			MyTip.showToast(context, "没有查询到物料信息！");
			return;
		}
		materialEt.setText(StrUtil.filterStr(rfidInfo.getPR_MATERIAL()));
		despEt.setText(StrUtil.filterStr(rfidInfo.getPR_MESSAGE()));
		if (list != null && list.size() > 0) {

			StoreBean storeBean = list.get(0);
			factoryEt.setText(StrUtil.filterStr(storeBean.getFactory()));
			storeEt.setText(StrUtil.filterStr(storeBean.getStore()));
			unit = StrUtil.filterStr(storeBean.getUnit());
			unitEt.setText(StrUtil.slipName(unit));
			fxgfkcEt.setText(StrUtil.filterStr(storeBean.getLABST()));
			xzjskcEt.setText(StrUtil.filterStr(storeBean.getKLABS()));
			totalStoreEt.setText(StrUtil.filterStr(storeBean.getEINME()));
			fxjskcEt.setText(StrUtil.filterStr(storeBean.getKEINM()));
			cwEt.setText(StrUtil.filterStr(storeBean.getLGPBE()));
			setMaterialImg();
			getProjectKc();
		}
	}

	private void getProjectKc() {
		// 调用项目库存接口
		AsyncService.GetKC2 getKc = AsyncService.getInstance().new GetKC2();
		getKc.execute(handler, ParamsUtil.GETKC2, getApp().getUrl(),
				materialStr, factoryStr, storeStr, context);
	}

	private String unit;
	/** 物料查询界面跳转过来的 */
	private void setViewData() {
		titleTv.setText("物料详情");

		rfidQueryLayout.setVisibility(View.GONE);
		materialStr = StrUtil.filterStr(storeBean.getMATNR());
		factoryStr = StrUtil.filterStr(storeBean.getFactory());
		storeStr = StrUtil.filterStr(storeBean.getStore());

		materialEt.setText(materialStr);
		factoryEt.setText(StrUtil.filterStr(storeBean.getFactory()));

		storeEt.setText(StrUtil.filterStr(storeBean.getStore()));
		despEt.setText(StrUtil.filterStr(storeBean.getMAKTX()));

		unit = StrUtil.filterStr(storeBean.getUnit());
		unitEt.setText(StrUtil.slipName(storeBean.getUnit()));
		fxgfkcEt.setText(StrUtil.filterStr(storeBean.getLABST()));
		xzjskcEt.setText(StrUtil.filterStr(storeBean.getKLABS()));
		totalStoreEt.setText(StrUtil.filterStr(storeBean.getEINME()));
		fxjskcEt.setText(StrUtil.filterStr(storeBean.getKEINM()));
		cwEt.setText(StrUtil.filterStr(storeBean.getLGPBE()));
		//查询数据库中是否有匹配的图片，如果没有查询接口是否有图片
		setMaterialImg();
		
		getProjectKc();
	}

	private void setMaterialImg() {
		// TODO Auto-generated method stub
		String materialStr = StrUtil.filterStr(materialEt.getText().toString());
		String factoryStr = StrUtil.slipValue(factoryEt.getText().toString());
		String storeStr = StrUtil.slipValue(storeEt.getText().toString());
		String path = ImgDao.getInstance(context).getMaterialImgPath(materialStr, factoryStr, storeStr);
		File file = new File(path);
		if(file.exists()){
			pathStr = path;
			netImageView.setVisibility(View.VISIBLE);
			netImageView.setImagePath(file);
			ImgDao.getInstance(context).updateImgState(materialStr, factoryStr, storeStr);
		}else{
			AsyncService.GetMaterialImg getMaterialImg = AsyncService.getInstance().new GetMaterialImg();
			getMaterialImg.execute(handler, ParamsUtil.load_img, getApp().getUrl(),
					getUser().getUserId(), materialStr,
					factoryStr, StrUtil.slipValue(storeStr));
		}
	}

	String materialStr;
	String factoryStr;
	String storeStr;

	private void setRfidInfoResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("ok")) {
				rfidInfo = (RFIDinfo) result[1];
				materialStr = rfidInfo.getPR_MATERIAL();
				factoryStr = StrUtil.slipValue(rfidInfo.getFactory());
				storeStr = StrUtil.slipValue(rfidInfo.getStore());
				if (StrUtil.isNotEmpty(materialStr)
						&& StrUtil.isNotEmpty(factoryStr)
						&& StrUtil.isNotEmpty(storeStr)) {
					// 根据RFID、工厂、库位获取库存信息
					AsyncService.GetKC getKc = AsyncService.getInstance().new GetKC();
					getKc.execute(handler, ParamsUtil.GETKC, getApp().getUrl(),
							materialStr, factoryStr, storeStr,
							context);
				} else {
					MyTip.cancelProgress();
					MyTip.showToast(context, "没有查询到数据");
				}
			} else {
				MyTip.cancelProgress();
				MyTip.showToast(context, (String) result[1]);
			}
		} else {
			MyTip.showToast(context, "没有查询到物料信息！");
			MyTip.cancelProgress();
		}
	};

}
