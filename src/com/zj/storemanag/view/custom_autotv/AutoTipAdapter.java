package com.zj.storemanag.view.custom_autotv;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.zj.storemanag.R;

public class AutoTipAdapter extends BaseAdapter implements Filterable {

	private Context context;
	private ArrayFilter mFilter;
	private List<String> allList;// 所有item
	private List<String> filterLs;// 过滤后的item
	private final Object mLock = new Object();
	private int maxMathch = 10;// 最多显示多少个选项，负数表示全部

	public AutoTipAdapter(Context context, List<String> allList, int maxMathch) {
		this.context = context;
		this.allList = allList;
		this.maxMathch = maxMathch;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(filterLs == null) return 0;
		return filterLs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if(filterLs == null) return null;
		return filterLs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.auto_tip_item, null);
			holder.tv = (TextView) convertView.findViewById(R.id.simple_item_0);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv.setText(filterLs.get(position));
		return convertView;
	}

	class ViewHolder {
		TextView tv;
	}

	public List<String> getAllItems() {
		return allList;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			// TODO Auto-generated method stub
			FilterResults results = new FilterResults();
			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					List<String> list = new ArrayList<String>(allList);
					results.values = list;
					results.count = list.size();
					return results;
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();
				final int count = allList.size();
				final List<String> newList = new ArrayList<String>(count);
				for (int i = 0; i < count; i++) {
					final String value = allList.get(i);
					final String valueStr = value.toLowerCase();
					if (valueStr.startsWith(prefixString)) {
						newList.add(value);
					}
					if (maxMathch > 0) {
						// 数量限制
						if (newList.size() > maxMathch - 1) {
							break;
						}
					}
				}
				results.values = newList;
				results.count = newList.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			filterLs = (List<String>) results.values;
			if(filterLs == null) return;
			if (results.count > 0) {
				notifyDataSetChanged();
			}
//			else {
//				notifyDataSetInvalidated();
//			}

		}

	}

}
