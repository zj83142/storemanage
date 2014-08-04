package com.zj.storemanag.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.StoreHistoryBean;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.util.StrUtil;

public class StoreHistoryAdapter extends BaseAdapter {
	private List<StoreHistoryBean> list;
	private LayoutInflater inflater;
	private BaseInfoDao baseInfoDao;
	
	public StoreHistoryAdapter(Context context, List<StoreHistoryBean> list) {
		baseInfoDao = BaseInfoDao.getInstance(context);
		this.list = list;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = inflater.inflate(R.layout.outinstore_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		if((position & 1) == 0){
			holder.layout.setBackgroundResource(R.color.white);
		}else{
			holder.layout.setBackgroundResource(R.color.gray);
		}
		final StoreHistoryBean temp = list.get(position);
		holder.materialNumTv.setText(StrUtil.filterStr(temp.getMATNR()));
		String factoryValue = StrUtil.filterStr(temp.getWERKS());
		String storeValue = StrUtil.filterStr(temp.getLGORT());
		String factory = StrUtil.filterStr(baseInfoDao.getNameByCode(factoryValue, "1"));
		String store = StrUtil.filterStr(baseInfoDao.getStoreName(storeValue, factoryValue));
		holder.factoryTv.setText(StrUtil.filterStr(factory));
		holder.locationTv.setText(StrUtil.filterStr(store));
		holder.numberTv.setText(StrUtil.filterStr(temp.getERFMG())+StrUtil.slipName(temp.getERFME()));
		holder.despTv.setText(StrUtil.filterStr(temp.getMAKTX()));
		holder.moneyTv.setText(StrUtil.filterStr(temp.getDMBTR()));
		holder.pzDateTv.setText(StrUtil.filterStr(temp.getBLDAT()));
		holder.jzDateTv.setText(StrUtil.filterStr(temp.getBUDAT()));
		holder.wlpzTv.setText(StrUtil.filterStr(temp.getMBLNR()));
		holder.cgddTv.setText(StrUtil.filterStr(temp.getEBELN()));
		holder.ydlxTv.setText(StrUtil.filterStr(temp.getBWART()));
		holder.cbzxTv.setText(StrUtil.filterStr(temp.getKOSTL()));
		holder.tskcTv.setText(StrUtil.filterStr(temp.getSOBKZ()));
		holder.wbsTv.setText(StrUtil.filterStr(temp.getPOSKI()));
		holder.ddhTv.setText(StrUtil.filterStr(temp.getAUFNR()));
		
		return view;
	}

	class ViewHolder {
		LinearLayout layout;
		TextView materialNumTv;
		TextView factoryTv;
		TextView locationTv;
		TextView numberTv;
		TextView despTv;
		TextView moneyTv;
		TextView pzDateTv;
		TextView jzDateTv;
		TextView wlpzTv;
		TextView cgddTv;
		TextView ydlxTv;
		TextView cbzxTv;
		TextView tskcTv;
		TextView wbsTv;
		TextView ddhTv;
		

		public ViewHolder(View view) {
			this.layout = (LinearLayout) view.findViewById(R.id.layout);
			this.materialNumTv = (TextView) view.findViewById(R.id.inout_store_item_marterail);
			this.factoryTv = (TextView) view.findViewById(R.id.inout_store_item_factory);
			this.locationTv = (TextView) view.findViewById(R.id.inout_store_item_store);
			this.numberTv = (TextView) view.findViewById(R.id.inout_store_item_number);
			this.despTv = (TextView) view.findViewById(R.id.inout_store_item_desp);
			
			this.moneyTv = (TextView) view.findViewById(R.id.inout_store_item_money);
			this.pzDateTv = (TextView) view.findViewById(R.id.inout_store_item_pzdate);
			this.jzDateTv = (TextView) view.findViewById(R.id.inout_store_item_jzdate);
			this.wlpzTv = (TextView) view.findViewById(R.id.inout_store_item_wlpz);
			this.cgddTv = (TextView) view.findViewById(R.id.inout_store_item_cgdd);
			
			this.ydlxTv = (TextView) view.findViewById(R.id.inout_store_item_ydlx);
			this.cbzxTv = (TextView) view.findViewById(R.id.inout_store_item_cbzx);
			this.tskcTv = (TextView) view.findViewById(R.id.inout_store_item_tskc);
			this.wbsTv = (TextView) view.findViewById(R.id.inout_store_item_wbs);
			this.ddhTv = (TextView) view.findViewById(R.id.inout_store_item_ddh);
		}
	}

}
