package com.zj.storemanag.activity;

import java.io.File;

import log.Log;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.Goods;
import com.zj.storemanag.bean.ImgBean;
import com.zj.storemanag.bean.SaveRFIDinfo;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.GoodsDao;
import com.zj.storemanag.dao.ImgDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.util.Utils;
import com.zj.storemanag.view.custom_autotv.FactoryStoreView;

public class EquipDetailActivity extends BaseActivity {

	private TextView materailTv;
	private FactoryStoreView factoryStoreView;
	private TextView despTv;
	private TextView unitTv;
	private EditText recordNumEt;
	private TextView supperTv;
	// private TextView buyNumTv;

	private Button photoBtn;

	private Goods good;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equip_detail);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("设备信息详情");
		photoBtn = (Button) findViewById(R.id.top_right_btn);
		photoBtn.setText("拍照");
		photoBtn.setOnClickListener(this);
		materailTv = (TextView) findViewById(R.id.equip_detail_materal);
		factoryStoreView = (FactoryStoreView) findViewById(R.id.factorystoreview);
		despTv = (TextView) findViewById(R.id.equip_detail_desp_et);
		unitTv = (TextView) findViewById(R.id.equip_detail_unit_et);
		recordNumEt = (EditText) findViewById(R.id.equip_detail_number_et);
		supperTv = (TextView) findViewById(R.id.equip_detail_supper_et);
		// buyNumTv = (TextView) findViewById(R.id.equip_detail_buy_num_et);
		int flag = getApp().FLAG;
		if (flag == ParamsUtil.interior) {
			// 内部备件入库,设置工厂库位只能传入不能更改
			factoryStoreView.setGone();
			photoBtn.setVisibility(View.VISIBLE);
		}else if(flag == ParamsUtil.consign_midify || flag == ParamsUtil.cross_company || flag == ParamsUtil.company){
			factoryStoreView.setFactoryStoreTitle("原工厂：", "原库位：");
		}

		good = (Goods) getIntent().getSerializableExtra("good");
		if (good != null) {
			materailTv.setText(StrUtil.filterStr(good.getMATERIAL()));
			factoryStoreView.setFactory(StrUtil.filterStr(good.getFactory()));
			factoryStoreView.setStore(StrUtil.filterStr(good.getStore()));
			despTv.setText(StrUtil.filterStr(good.getMESSAGE()));
			unitTv.setText(StrUtil.slipName(good.getUnit()));
			String number = StrUtil.filterStr(good.getENTRY_QNT());
			if(number.equalsIgnoreCase("0")){
				number = "";
			}
			recordNumEt.setText(number);
			supperTv.setText(StrUtil.filterStr(good.getVEND_NAME()));
			// buyNumTv.setText(StrUtil.filterStr(good.getTotalCount()));

		} else {
			MyTip.showToast(EquipDetailActivity.this, "数据错误");
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
		case R.id.equip_detail_update_btn:
			//判断录入数据
			String number = StrUtil.filterStr(recordNumEt.getText().toString());
			if(!StrUtil.isNotEmpty(number)){
				MyTip.showToast(EquipDetailActivity.this, "录入的采购数量不能为空");
				return;
			}else if(number.equalsIgnoreCase("0")){
				MyTip.showToast(EquipDetailActivity.this, "录入的采购数量不能为0");
				return;
			}
			if (!isConnectInternet()) {
				return;
			}
			// 更新rfid 保存设备信息
			updateRfid();
			break;
		case R.id.top_right_btn:
			// 先判断物料信息是否存在
			if (!judgeMaterial()) {
				// 请先输入
				MyTip.showToast(EquipDetailActivity.this, "请输入完整物料信息再拍照！");
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
		default:
			break;
		}
	}
	
	private boolean judgeMaterial() {
		String material = StrUtil.filterStr(materailTv.getText().toString());
		String factoryStr = StrUtil.slipValue(factoryStoreView.getFactory());
		String storeStr = StrUtil.slipValue(factoryStoreView.getStore());
		String unit = StrUtil.filterStr(unitTv.getText().toString());
		if (StrUtil.isNotEmpty(material) && StrUtil.isNotEmpty(factoryStr)
				&& StrUtil.isNotEmpty(storeStr) && StrUtil.isNotEmpty(unit)) {
			return true;
		}
		return false;
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
					bean.setMaterial(materailTv.getText().toString());
					bean.setFactory(StrUtil.slipValue(factoryStoreView
							.getFactory()));
					bean.setStore(StrUtil.slipValue(factoryStoreView.getStore()));
					bean.setPath(photoPath);
					bean.setUserId(getUser().getUserId());
					bean.setState("0");
					bean.setDesp(StrUtil.filterStr(despTv.getText().toString()));
					bean.setUnit(StrUtil.slipName(unitTv.getText().toString()));
					bean.setCw("");
					// 保存图片到表中
					boolean isResult = ImgDao.getInstance(
							EquipDetailActivity.this).save(bean);
					if (isResult) {
//						MyTip.showToast(AddEquipInfoActivity.this, "图片保存成功");
					} else {
						MyTip.showToast(EquipDetailActivity.this, "图片保存失败");
					}
				} else {
					MyTip.showToast(EquipDetailActivity.this, "保存照片错误，文件不存在！");
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

	private void updateRfid() {
		MyTip.showProgress(EquipDetailActivity.this, "提交数据", "正在提交数据，请稍等……");
		SaveRFIDinfo saveRFIDinfo = new SaveRFIDinfo();
		saveRFIDinfo.setPR_MATERIAL(UrlUtil.safeXml(good.getMATERIAL()));
		saveRFIDinfo.setPR_ENTRY_UOM(UrlUtil.safeXml(StrUtil.slipName(unitTv.getText().toString())));
		saveRFIDinfo.setPR_MESSAGE(UrlUtil.safeXml(good.getMESSAGE()));
		saveRFIDinfo.setPR_RFID_NO(UrlUtil.safeXml(good.getRFID()));
		saveRFIDinfo.setPR_PLANT(UrlUtil.safeXml(StrUtil.slipValue(factoryStoreView
				.getFactory())));
		saveRFIDinfo.setPR_STGE_LOC(UrlUtil.safeXml(StrUtil.slipValue(factoryStoreView
				.getStore())));
		String xml = saveRFIDinfo.getSaveRfidInfo(saveRFIDinfo);
		AsyncService.TempRFID tempRFID = AsyncService.getInstance().new TempRFID();
		tempRFID.execute(handler, ParamsUtil.TEMPRFID, getApp().getUrl(), getApp()
				.getUser().getUserId(), xml);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
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
				good.setRFID(StrUtil.filterStr(rfid));
				good.setFactory(StrUtil.filterStr(factoryStoreView.getFactory()));
				good.setStore(StrUtil.filterStr(factoryStoreView.getStore()));
				good.setENTRY_QNT(StrUtil.filterStr(recordNumEt.getText().toString()));
				good.setIsInit("1");// 表示已经初始化过RFID
				good.setState("1");// 选中
				GoodsDao.getInstance(EquipDetailActivity.this)
						.updateGoods(good,true);
				
				ImgDao.getInstance(EquipDetailActivity.this).updateImgState(materailTv.getText()
						.toString(), StrUtil.slipValue(factoryStoreView.getFactory()), StrUtil.slipValue(factoryStoreView.getStore()));
				
				finish();
			} else {
				MyTip.showToast(EquipDetailActivity.this, (String) result[1]);
			}
		}
		MyTip.cancelProgress();
	}
}
