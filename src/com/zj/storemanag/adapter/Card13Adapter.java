package com.zj.storemanag.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.Card13;
import com.zj.storemanag.util.StrUtil;

public class Card13Adapter extends BaseAdapter {
	private List<Card13> list;
	private LayoutInflater inflater;

	public Card13Adapter(Context context, List<Card13> list) {
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
			view = inflater.inflate(R.layout.card13ls_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		final Card13 temp = list.get(position);
		holder.materialTv.setText("物料号：" + StrUtil.filterStr(temp.getMaterial()));
		holder.factoryTv.setText("工厂：" +StrUtil.filterStr(temp.getFactory()));
		holder.storeTv.setText("库位：" +StrUtil.filterStr(temp.getStore()));
		holder.despTv.setText("物料描述：" +StrUtil.filterStr(temp.getMessage()));
		holder.cwTv.setText("仓位号：" +StrUtil.filterStr(temp.getCw()));
		return view;
	}

	class ViewHolder {
		TextView materialTv;
		TextView factoryTv;
		TextView storeTv;
		TextView despTv;
		TextView cwTv;

		public ViewHolder(View view) {
			this.materialTv = (TextView) view.findViewById(R.id.card13_item_material);
			this.factoryTv = (TextView) view.findViewById(R.id.card13_item_facotry);
			this.storeTv = (TextView) view.findViewById(R.id.card13_item_store);
			this.despTv = (TextView) view.findViewById(R.id.card13_item_desp);
			this.cwTv = (TextView) view.findViewById(R.id.card13_item_cw); 
		}
	}

}
