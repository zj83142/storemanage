package com.zj.storemanag.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.PopItem;

public class PopAdapter extends BaseAdapter {
	private List<PopItem> list;
	private LayoutInflater inflater;

	public PopAdapter(Context context, List<PopItem> list) {
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
			view = inflater.inflate(R.layout.pop_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		holder.byName.setText(list.get(position).getName());
		return view;
	}

	class ViewHolder {
		TextView byName;

		public ViewHolder(View view) {
			this.byName = (TextView) view.findViewById(R.id.pop_item_name);
		}
	}

}
