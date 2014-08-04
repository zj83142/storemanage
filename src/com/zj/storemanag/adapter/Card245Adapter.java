package com.zj.storemanag.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.Card245;
import com.zj.storemanag.util.StrUtil;

public class Card245Adapter extends BaseAdapter {
	private List<Card245> list;
	private LayoutInflater inflater;

	public Card245Adapter(Context context, List<Card245> list) {
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
			view = inflater.inflate(R.layout.card245ls_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		final Card245 temp = list.get(position);
		holder.materialTv.setText("物料号：" + StrUtil.filterStr(temp.getMateral()));
		holder.matCodeTv.setText("凭证号：" +StrUtil.filterStr(temp.getMatCode()));
		holder.matYearTv.setText("凭证年：" +StrUtil.filterStr(temp.getMatYear()));
		holder.moveTypeTv.setText("移动类型：" +StrUtil.filterStr(temp.getMoveType()));
		return view;
	}

	class ViewHolder {
		TextView materialTv;
		TextView matCodeTv;
		TextView matYearTv;
		TextView moveTypeTv;

		public ViewHolder(View view) {
			this.materialTv = (TextView) view.findViewById(R.id.card245_item_material);
			this.matCodeTv = (TextView) view.findViewById(R.id.card245_item_matcode);
			this.matYearTv = (TextView) view.findViewById(R.id.card245_item_matyear);
			this.moveTypeTv = (TextView) view.findViewById(R.id.card245_item_movetype);
		}
	}

}
