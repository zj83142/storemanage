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
import com.zj.storemanag.bean.StoreBean;
import com.zj.storemanag.util.StrUtil;

public class StoreAdapter extends BaseAdapter {
	private List<StoreBean> list;
	private LayoutInflater inflater;
	
	public StoreAdapter(Context context, List<StoreBean> list) {
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
			view = inflater.inflate(R.layout.store_item, null);
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
		final StoreBean temp = list.get(position);
		
		holder.materailTv.setText(StrUtil.filterStr(temp.getMATNR()));
		holder.despTv.setText(StrUtil.filterStr(temp.getMAKTX()));
		holder.fxgjkcTv.setText(StrUtil.filterStr(temp.getLABST()));
		holder.xzzjkcTv.setText(StrUtil.filterStr(temp.getEINME()));
		holder.xzjskcTv.setText(StrUtil.filterStr(temp.getKLABS()));
		holder.unitTv.setText(StrUtil.slipName(temp.getUnit()));
		holder.factoryTv.setText(StrUtil.filterStr(temp.getFactory()));
		holder.storeTv.setText(StrUtil.filterStr(temp.getStore()));
		holder.cwTv.setText(StrUtil.filterStr(temp.getLGPBE()));
		return view;
	}

	class ViewHolder {
		LinearLayout layout;
		TextView materailTv;
		TextView despTv;
		TextView fxgjkcTv;
		TextView xzzjkcTv;
		TextView xzjskcTv;
		TextView unitTv;
		TextView factoryTv;
		TextView storeTv;
		TextView cwTv;

		public ViewHolder(View view) {
			this.layout = (LinearLayout) view.findViewById(R.id.layout);
			this.materailTv = (TextView) view.findViewById(R.id.store_item_materail);
			this.despTv = (TextView) view.findViewById(R.id.store_item_desp);
			this.fxgjkcTv = (TextView) view.findViewById(R.id.store_item_fxgjkc);
			this.xzzjkcTv = (TextView) view.findViewById(R.id.store_item_xzzjkc);
			this.xzjskcTv = (TextView) view.findViewById(R.id.store_item_xzjskc);
			this.unitTv = (TextView) view.findViewById(R.id.store_item_unit);
			this.factoryTv = (TextView) view.findViewById(R.id.store_item_factory);
			this.storeTv = (TextView) view.findViewById(R.id.store_item_store);
			this.cwTv = (TextView) view.findViewById(R.id.store_item_cw);
		}
	}

}
