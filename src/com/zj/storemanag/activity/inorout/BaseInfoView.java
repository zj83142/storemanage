package com.zj.storemanag.activity.inorout;

import java.util.List;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.MaterialDoc;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.OrderDao;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.view.ProofAndAccontView;
import com.zj.storemanag.view.custom_autotv.AutoTipAdapter;
import com.zj.storemanag.view.custom_autotv.AutoTipTextView;
import com.zj.storemanag.view.custom_autotv.FactoryStoreLineBjView;

public class BaseInfoView extends LinearLayout {
	
	private LinearLayout projectLayout;//工程技改
	private LinearLayout projectPartLayout;//工程技改分 
	private LinearLayout consignLayout;//寄售备出-移库 
	private LinearLayout centerLayout;//按成本中心-內部投料
	private LinearLayout guestLayout;//按客号-外-內
	private LinearLayout companyLayout;//跨（同）公司移库
	private LinearLayout modifyOrderLayout;//按维修订单
	private LinearLayout externalLayout;//外部备件入库
	private LinearLayout internalLayout;//内部备件入库
	
	private ProofAndAccontView proofAndAccontView;
	
	private int flag = 0;
	
	private int showTipCount = 10;//自动提示显示条数
	
	private Context context;
	
	/**工技改*/
	private AutoTipTextView projectWbs;
	/**工程技改分*/
	private AutoTipTextView projectPartWbs;
	private AutoTipTextView projectPartWbsSon;
	/** 寄售备出-移库*/
	private AutoTipTextView consignSuppser;
	private FactoryStoreLineBjView consignFactoryStore;
	/**按成本中心-內部投料 */
	private AutoTipTextView centerAuto;
	private TextView centerMoveType1;
	private TextView centerMoveType2;
	/**按客号-外-內 */
	private AutoTipTextView guestAuto;
	private TextView guestMoveType1;
	private TextView guestMoveType2;
	/**跨（同）公司移库*/
	private FactoryStoreLineBjView companyFactoryStore;
	private TextView companyMoveType1;
	private TextView companyMoveType2;
	/**按维修订单*/
	/**外部备件入库 */
	private TextView orderSuppter;
	/**内部备件入库*/
	private FactoryStoreLineBjView internalFactoryStore;
	private AutoTipTextView internalCenter;
	
	public BaseInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.baseinfo_view, this);
		projectLayout = (LinearLayout) layout.findViewById(R.id.project_layout);
		projectPartLayout = (LinearLayout) layout.findViewById(R.id.project_part_layout);
		consignLayout = (LinearLayout) layout.findViewById(R.id.consign_layout);
		
		centerLayout = (LinearLayout) layout.findViewById(R.id.center_layout);
		guestLayout = (LinearLayout) layout.findViewById(R.id.guest_layout);
		companyLayout = (LinearLayout) layout.findViewById(R.id.company_layout);
		modifyOrderLayout = (LinearLayout) layout.findViewById(R.id.modify_order_layout);
		
		externalLayout = (LinearLayout) layout.findViewById(R.id.external_layout);
		internalLayout = (LinearLayout) layout.findViewById(R.id.internal_layout);
		
		proofAndAccontView = (ProofAndAccontView) layout.findViewById(R.id.proof_account);
		
		projectWbs = (AutoTipTextView) layout.findViewById(R.id.project_wbs_auto);
		projectPartWbs = (AutoTipTextView) layout.findViewById(R.id.project_part_wbs_auto);
		projectPartWbsSon = (AutoTipTextView) layout.findViewById(R.id.project_part_wbsson_auto);
		
		consignSuppser = (AutoTipTextView) layout.findViewById(R.id.consign_supper_auto);
		consignSuppser.setInputType(InputType.TYPE_CLASS_NUMBER);
		consignFactoryStore = (FactoryStoreLineBjView) layout.findViewById(R.id.consign_factory_store);
		centerAuto = (AutoTipTextView) layout.findViewById(R.id.center_auto);
		centerAuto.setInputType(InputType.TYPE_CLASS_NUMBER);
		centerMoveType1 = (TextView) layout.findViewById(R.id.center_movetype1);
		centerMoveType2 = (TextView) layout.findViewById(R.id.center_movetype2);
		guestAuto = (AutoTipTextView) layout.findViewById(R.id.guest_no_auto);
		guestAuto.setInputType(InputType.TYPE_CLASS_NUMBER);
		guestMoveType1 = (TextView) layout.findViewById(R.id.guest_movetype1);
		guestMoveType2 = (TextView) layout.findViewById(R.id.guest_movetype2);
		companyFactoryStore = (FactoryStoreLineBjView) layout.findViewById(R.id.company_factory_store);
		companyMoveType1 = (TextView) layout.findViewById(R.id.company_movetype1);
		companyMoveType2 = (TextView) layout.findViewById(R.id.company_movetype2);
		orderSuppter = (TextView) layout.findViewById(R.id.order_supplier_et);
		internalFactoryStore = (FactoryStoreLineBjView) layout.findViewById(R.id.internal_factory_store);
		internalCenter = (AutoTipTextView) layout.findViewById(R.id.internal_center_auto);
		internalCenter.setInputType(InputType.TYPE_CLASS_NUMBER);
		initView();
	}

	private void initView() {
		projectLayout = (LinearLayout) findViewById(R.id.project_layout);
		projectPartLayout = (LinearLayout) findViewById(R.id.project_part_layout);
		consignLayout = (LinearLayout) findViewById(R.id.consign_layout);
		
		centerLayout = (LinearLayout) findViewById(R.id.center_layout);
		centerMoveType1 = (TextView) findViewById(R.id.center_movetype1);
		centerMoveType2 = (TextView) findViewById(R.id.center_movetype2);
		guestLayout = (LinearLayout) findViewById(R.id.guest_layout);
		guestMoveType1 = (TextView) findViewById(R.id.guest_movetype1);
		guestMoveType2 = (TextView) findViewById(R.id.guest_movetype2);
		companyLayout = (LinearLayout) findViewById(R.id.company_layout);
		companyMoveType1 = (TextView) findViewById(R.id.company_movetype1);
		companyMoveType2 = (TextView) findViewById(R.id.company_movetype2);
		modifyOrderLayout = (LinearLayout) findViewById(R.id.modify_order_layout);
		
		externalLayout = (LinearLayout) findViewById(R.id.external_layout);
		internalLayout = (LinearLayout) findViewById(R.id.internal_layout);
		
		proofAndAccontView = (ProofAndAccontView) findViewById(R.id.proof_account);
		
		consignSuppser = (AutoTipTextView) findViewById(R.id.consign_supper_auto);
		consignFactoryStore = (FactoryStoreLineBjView) findViewById(R.id.consign_factory_store);
		centerAuto = (AutoTipTextView) findViewById(R.id.center_auto);
		guestAuto = (AutoTipTextView) findViewById(R.id.guest_no_auto);
		companyFactoryStore = (FactoryStoreLineBjView) findViewById(R.id.company_factory_store);
		orderSuppter = (TextView) findViewById(R.id.order_supplier_et);
		internalFactoryStore = (FactoryStoreLineBjView) findViewById(R.id.internal_factory_store);
		internalCenter = (AutoTipTextView) findViewById(R.id.internal_center_auto);
	}
	
	public MaterialDoc getBaseInfoByFlag(){
		if(flag == ParamsUtil.project){
			return getProjectBaseInfo();
		}else if(flag == ParamsUtil.project_part){
			return getProjectPartBaseInfo();
		}else if(flag == ParamsUtil.consign_midify){
			return getConsignBaseInfo();
		}else if(flag == ParamsUtil.cost_center|| flag == ParamsUtil.internal_feeding){
			return getCenterBaseInfo();
		}else if(flag == ParamsUtil.guest_no_inside || flag == ParamsUtil.guest_no_outside){
			return getGuestBaseInfo();
		}else if(flag == ParamsUtil.company || flag == ParamsUtil.cross_company){
			return getCompanyBaseInfo();
		}else if(flag == ParamsUtil.maintenance_order){
			return getModifytBaseInfo();
		}else if(flag == ParamsUtil.external){
			return getExternalBaseInfo();
		}else if(flag == ParamsUtil.interior){
			return getInternalBaseInfo();
		}
		return null;
	}
	
	/**内部备件*/
	private MaterialDoc getInternalBaseInfo() {
		String factory = StrUtil.filterStr(internalFactoryStore.getFactory());
		String store = StrUtil.filterStr(internalFactoryStore.getStore());
		String center = StrUtil.filterStr(internalCenter.getText().toString());
		if(!StrUtil.isNotEmpty(factory)){
			MyTip.showToast(context, "请输入工厂信息！");
			return null;
		}
		if(!StrUtil.isNotEmpty(store)){
			MyTip.showToast(context, "请输入库位信息！");
			return null;
		}
		if(!StrUtil.isNotEmpty(center)){
			MyTip.showToast(context, "请输入成本中心信息！");
			return null;
		}
		MaterialDoc info = getCommonInfo();
		info.setPLANT(factory);
		info.setSTGE_LOC(store);
		info.setGM_CODE("05");
		info.setGMI_IN_OUT("1");
		info.setMOVE_TYPE("521");//移动类型
		info.setCOSTCENTER(center);
		return info;
	}
	/**外部备件*/
	private MaterialDoc getExternalBaseInfo() {
		String supper = StrUtil.filterStr(orderSuppter.getText().toString());
		MaterialDoc info = getCommonInfo();
		info.setGM_CODE("01");
		info.setVENDOR(supper);
		info.setGMI_IN_OUT("1");
		info.setMOVE_TYPE("101");
		info.setMVT_IND("B");
		return info;
	}
	/**按维修订单*/
	private MaterialDoc getModifytBaseInfo() {
		MaterialDoc info = getCommonInfo();
		info.setMOVE_TYPE("261");
		info.setGMI_IN_OUT("-1");
		info.setGM_CODE("03");
		return info;
	}
	/**同（夸）公司*/
	private MaterialDoc getCompanyBaseInfo() {
		String factory = StrUtil.filterStr(companyFactoryStore.getFactory());
		String store = StrUtil.filterStr(companyFactoryStore.getStore());
		if(!StrUtil.isNotEmpty(factory)){
			MyTip.showToast(context, "请输入接收工厂信息！");
			return null;
		}
		if(!StrUtil.isNotEmpty(store)){
			MyTip.showToast(context, "请输入接收库位信息！");
			return null;
		}
		MaterialDoc info = getCommonInfo();
		info.setMOVE_PLANT(factory);
		info.setMOVE_STLOC(store);
		if(flag == ParamsUtil.company){
			info.setMOVE_TYPE("311");
		}else if(flag == ParamsUtil.cross_company){
			info.setMOVE_TYPE("301");
		}
		info.setGMI_IN_OUT("-1");
		info.setGM_CODE("04");
		return info;
	}
	/**客户号内（外）*/
	private MaterialDoc getGuestBaseInfo() {
		String guest = StrUtil.filterStr(guestAuto.getText().toString());
		if(!StrUtil.isNotEmpty(guest)){
			MyTip.showToast(context, "请输入客户号信息！");
			return null;
		}
		MaterialDoc info = getCommonInfo();
		info.setCUSTOMER(guest);
		if(flag == ParamsUtil.guest_no_inside){
			info.setMOVE_TYPE("913");
		}else if(flag == ParamsUtil.guest_no_outside){
			info.setMOVE_TYPE("902");
		}
		info.setGMI_IN_OUT("-1");
		info.setGM_CODE("03");
		return info;
	}
	/**成本中心  -- 内部投料*/
	private MaterialDoc getCenterBaseInfo() {
		String center = StrUtil.filterStr(centerAuto.getText().toString());
		if(!StrUtil.isNotEmpty(center)){
			MyTip.showToast(context, "请输入成本中心信息！");
			return null;
		}
		MaterialDoc info = getCommonInfo();
		info.setCOSTCENTER(center);
		if(flag == ParamsUtil.cost_center){
			info.setMOVE_TYPE("201");
			info.setGM_CODE("03");
		}else if(flag == ParamsUtil.internal_feeding){
			info.setMOVE_TYPE("522");
			info.setGM_CODE("05");
		}
		info.setGMI_IN_OUT("-1");
		return info;
	}
	/**寄售备出移库*/
	private MaterialDoc getConsignBaseInfo() {
		String supper = StrUtil.filterStr(consignSuppser.getText().toString());
		String factory = StrUtil.filterStr(consignFactoryStore.getFactory());
		String store = StrUtil.filterStr(consignFactoryStore.getStore());
		if(!StrUtil.isNotEmpty(supper)){
			MyTip.showToast(context, "请输入供应商信息！");
			return null;
		}
		if(!StrUtil.isNotEmpty(factory)){
			MyTip.showToast(context, "请输入接收工地信息！");
			return null;		
		}
		if(!StrUtil.isNotEmpty(store)){
			MyTip.showToast(context, "请输入接收库位信息！");
			return null;
		}
		MaterialDoc info = getCommonInfo();
		info.setGM_CODE("04");
		info.setGMI_IN_OUT("1");
		info.setVENDOR(supper);
		info.setMOVE_PLANT(factory);
		info.setMOVE_STLOC(store);
		info.setMOVE_TYPE("411");
		info.setSPEC_STOCK("K");
		return info;
	}
	/**工程技改分*/
	private MaterialDoc getProjectPartBaseInfo() {
		String wbs = StrUtil.filterStr(projectPartWbs.getText().toString());
		String wbsSon = StrUtil.filterStr(projectPartWbsSon.getText().toString());
		if(!StrUtil.isNotEmpty(wbs)){
			MyTip.showToast(context, "请输入制造分公司WBS信息！");
			return null;
		}
		if(!StrUtil.isNotEmpty(wbsSon)){
			MyTip.showToast(context, "请输入分子分公司WBS信息！");
			return null;
		}
		MaterialDoc info = getCommonInfo();
		info.setGM_CODE("04");
		info.setGMI_IN_OUT("-1");
		info.setVAL_WBS_ELEM(wbs);
		info.setWBS_ELEM(wbsSon);
		info.setSPEC_STOCK("Q");
		info.setMOVE_TYPE("415");
		return info;
	}
	/**工程技改*/
	private MaterialDoc getProjectBaseInfo() {
		// 判断输入
		String wbs = StrUtil.filterStr(projectWbs.getText().toString());
		if(!StrUtil.isNotEmpty(wbs)){
			MyTip.showToast(context, "请输入WBS信息！");
			return null;
		}
		MaterialDoc info = getCommonInfo();
		info.setGM_CODE("03");
		info.setGMI_IN_OUT("-1");
		info.setVAL_WBS_ELEM(wbs);
		info.setSPEC_STOCK("Q");
		info.setMOVE_TYPE("221");
		return info;
	}
	
	private MaterialDoc getCommonInfo(){
		MaterialDoc info = new MaterialDoc();
		info.setType(proofAndAccontView.getThingCode());
		info.setDOC_DATE(proofAndAccontView.getProof().replaceAll("-", ""));
		info.setPSTNG_DATE(proofAndAccontView.getAccount().replaceAll("-", ""));
		return info;
	}

	public void setFlag(int flag){
		this.flag = flag;
		proofAndAccontView.setThingCode(context.getResources().getStringArray(R.array.list_thing_code)[flag]);
		if(flag == ParamsUtil.project){
			//工程技改
			projectLayout.setVisibility(View.VISIBLE);
			setWbsAutoTip();
		}else if(flag == ParamsUtil.project_part){
			//工程技改分
			projectPartLayout.setVisibility(View.VISIBLE);
			setWbsAutoTip();
		}else if(flag == ParamsUtil.consign_midify){
			//寄售备出移库
			consignLayout.setVisibility(View.VISIBLE);
			setSupperAutoTip();
		}else if(flag == ParamsUtil.cost_center){
			//成本中心  -- 内部投料
			centerLayout.setVisibility(View.VISIBLE);
			centerMoveType1.setVisibility(View.VISIBLE);
			setCenterAutoTip();
		}else if(flag == ParamsUtil.internal_feeding){
			//成本中心  -- 内部投料
			centerLayout.setVisibility(View.VISIBLE);
			centerMoveType2.setVisibility(View.VISIBLE);
			setCenterAutoTip();
		}else if(flag == ParamsUtil.guest_no_inside){
			//客户号内（外）
			guestLayout.setVisibility(View.VISIBLE);
			guestMoveType1.setVisibility(View.VISIBLE);
			setGuestAutoTip();
		}else if(flag == ParamsUtil.guest_no_outside){
			guestLayout.setVisibility(View.VISIBLE);
			guestMoveType2.setVisibility(View.VISIBLE);
			setGuestAutoTip();
		}else if(flag == ParamsUtil.company){
			//同（夸）公司
			companyLayout.setVisibility(View.VISIBLE);
			companyMoveType1.setVisibility(View.VISIBLE);
		}else if(flag == ParamsUtil.cross_company){
			//同（夸）公司
			companyLayout.setVisibility(View.VISIBLE);
			companyMoveType2.setVisibility(View.VISIBLE);
		}else if(flag == ParamsUtil.maintenance_order){
			//按维修订单
			modifyOrderLayout.setVisibility(View.VISIBLE);
		}else if(flag == ParamsUtil.external){
			//外部备件
			externalLayout.setVisibility(View.VISIBLE);
		}else if(flag == ParamsUtil.interior){
			//内部备件
			internalLayout.setVisibility(View.VISIBLE);
			internalFactoryStore.setFactoryStoreTitle("工厂", "库位");
			setCenterAutoTip();
		}
	}
	
	public void addAutoTipByFlag(){
		if(flag == ParamsUtil.project){
			//工程技改
			addWbsAutoTip();
		}else if(flag == ParamsUtil.project_part){
			//工程技改分
			addWbsAutoTip();
		}else if(flag == ParamsUtil.consign_midify){
			//寄售备出移库
			addSupperAutoTip();
		}else if(flag == ParamsUtil.cost_center){
			//成本中心  -- 内部投料
			addCenterAutoTip();
		}else if(flag == ParamsUtil.internal_feeding){
			//成本中心  -- 内部投料
			addCenterAutoTip();
		}else if(flag == ParamsUtil.guest_no_inside){
			//客户号内（外）
			addGuestAutoTip();
		}else if(flag == ParamsUtil.guest_no_outside){
			addGuestAutoTip();
		}else if(flag == ParamsUtil.interior){
			//内部备件
			addCenterAutoTip();
		}
	}

	private List<String> guestLs;
	private AutoTipAdapter guestAdapter;
	private void setGuestAutoTip() {
		guestLs = OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_guest);
		guestAdapter = new AutoTipAdapter(context, guestLs, showTipCount);
		guestAuto.setAdapter(guestAdapter);
		guestAuto.setThreshold(0);
	}
	
	private void addGuestAutoTip() {
		// TODO Auto-generated method stub
		String guest = StrUtil.filterStr(guestAuto.getText().toString());
		OrderDao.getInstance(context).saveOrder(guest,ParamsUtil.flag_guest);
		guestLs.clear();
		guestLs.addAll(OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_guest));
		guestAdapter.notifyDataSetChanged();
	}


	private List<String> centerLs;
	private AutoTipAdapter centerAdapter;
	private void setCenterAutoTip() {
		centerLs = OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_center);
		centerAdapter = new AutoTipAdapter(context, centerLs, showTipCount);
		centerAuto.setAdapter(centerAdapter);
		centerAuto.setThreshold(0);
	}
	
	private void addCenterAutoTip() {
		String center = StrUtil.filterStr(centerAuto.getText().toString());
		String in_center = StrUtil.filterStr(internalCenter.getText().toString());
		OrderDao.getInstance(context).saveOrder(center, ParamsUtil.flag_center);
		OrderDao.getInstance(context).saveOrder(in_center, ParamsUtil.flag_center);
		centerLs.clear();
		centerLs.addAll(OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_center));
		centerAdapter.notifyDataSetChanged();
	}

	private List<String> supperLs;
	private AutoTipAdapter supperAdapter;
	private void setSupperAutoTip() {
		supperLs = OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_supper);
		supperAdapter = new AutoTipAdapter(context, supperLs, showTipCount);
		consignSuppser.setAdapter(supperAdapter);
		consignSuppser.setThreshold(0);
	}
	
	private void addSupperAutoTip() {
		// TODO Auto-generated method stub
		String supper = StrUtil.filterStr(consignSuppser.getText().toString());
		OrderDao.getInstance(context).saveOrder(supper, ParamsUtil.flag_supper);
		supperAdapter.notifyDataSetChanged();
		supperLs.addAll(OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_supper));
		supperLs.clear();
	}

	private List<String> wbsLs;
	private AutoTipAdapter wbsAdapter;
	private void setWbsAutoTip() {
		wbsLs = OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_wbs);
		wbsAdapter = new AutoTipAdapter(context, wbsLs, showTipCount);
		projectWbs.setAdapter(wbsAdapter);
		projectPartWbs.setAdapter(wbsAdapter);
		projectPartWbs.setThreshold(0); 
	}
	
	private void addWbsAutoTip() {
		// TODO Auto-generated method stub
		String wbs = StrUtil.filterStr(projectWbs.getText().toString());
		String wbs1 = StrUtil.filterStr(projectPartWbs.getText().toString());
		String wbsSon = StrUtil.filterStr(projectPartWbsSon.getText().toString());
		OrderDao.getInstance(context).saveOrder(wbs,ParamsUtil.flag_wbs);
		OrderDao.getInstance(context).saveOrder(wbs1,ParamsUtil.flag_wbs);
		OrderDao.getInstance(context).saveOrder(wbsSon,ParamsUtil.flag_wbs);
		wbsLs.clear();
		wbsLs.addAll(OrderDao.getInstance(context).findOrderLs(ParamsUtil.flag_wbs));
		wbsAdapter.notifyDataSetChanged();
	}

	public void clearViewData() {
		//添加自动提示信息
		addAutoTipByFlag();
		if(flag == ParamsUtil.project){
			//工程技改
			projectWbs.setText("");
		}else if(flag == ParamsUtil.project_part){
			//工程技改分
			projectPartWbs.setText("");
			projectPartWbsSon.setText("");
		}else if(flag == ParamsUtil.consign_midify){
			//寄售备出移库
			consignSuppser.setText("");
			consignFactoryStore.setFactory("");
			consignFactoryStore.setStore("");
		}else if(flag == ParamsUtil.cost_center || flag == ParamsUtil.internal_feeding){
			//成本中心  -- 内部投料
			centerAuto.setText("");
		}else if(flag == ParamsUtil.guest_no_inside || flag == ParamsUtil.guest_no_outside){
			//客户号内（外）
			guestAuto.setText("");
		}else if(flag == ParamsUtil.company || flag == ParamsUtil.cross_company){
			//同（夸）公司
			companyFactoryStore.setFactory("");
			companyFactoryStore.setStore("");
		}else if(flag == ParamsUtil.external){
			//外部备件
			orderSuppter.setText("");
		}else if(flag == ParamsUtil.interior){
			//内部备件
			internalFactoryStore.setFactory("");
			internalFactoryStore.setStore("");
			internalCenter.setText("");
		}
	}

	public void setOrderSupperTv(String str) {
		orderSuppter.setText(str);
	}
	
}
