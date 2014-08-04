package com.zj.storemanag.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.RFIDinfo;
import com.zj.storemanag.util.StrUtil;

public class TempRfidAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<RFIDinfo> list;
	
	public TempRfidAdapter(Context context, List<RFIDinfo> list){
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			view = inflater.inflate(R.layout.temp_rfid_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		RFIDinfo temp = list.get(position);
		holder.materalTv.setText("物料编号："+StrUtil.filterStr(temp.getPR_MATERIAL()));
		holder.factoryTv.setText("工厂："+StrUtil.filterStr(temp.getFactory()));
		holder.locationTv.setText("库位："+StrUtil.filterStr(temp.getStore()));
		holder.despTv.setText("描述："+StrUtil.filterStr(temp.getPR_MESSAGE()));
		holder.cwTv.setText("库位："+StrUtil.filterStr(temp.getPR_STGE_LOC2()));
		
		return view;
	}
	
	class ViewHolder {
		TextView materalTv;
		TextView factoryTv;
		TextView locationTv;
		TextView despTv;
		TextView cwTv;

		public ViewHolder(View view) {
			this.materalTv = (TextView) view.findViewById(R.id.temp_rfid_materal);
			this.factoryTv = (TextView) view.findViewById(R.id.temp_rfid_factory);
			this.locationTv = (TextView) view.findViewById(R.id.temp_rfid_location);
			this.despTv = (TextView) view.findViewById(R.id.temp_rfid_desp);
			this.cwTv = (TextView) view.findViewById(R.id.temp_rfid_cw);
		}
	}

}
