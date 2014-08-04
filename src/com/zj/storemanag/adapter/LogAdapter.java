package com.zj.storemanag.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.Goods;
import com.zj.storemanag.bean.HsLog;
import com.zj.storemanag.util.StrUtil;

public class LogAdapter extends BaseAdapter {
	private List<HsLog> list;
	private LayoutInflater inflater;
	private Handler handler;
	private static final int SELECT_ITEM = 111;

	public LogAdapter(Context context, List<HsLog> list, Handler handler) {
		this.list = list;
		this.handler = handler;
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
			view = inflater.inflate(R.layout.log_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		final HsLog temp = list.get(position);
		holder.timeTv.setText("时间：" + StrUtil.filterStr(temp.getHS_DATE()));
//		holder.userTv.setText("用户" +StrUtil.filterStr(temp.getHS_USER()));
		holder.typeTv.setText("类型：" +StrUtil.filterStr(temp.getHS_TYPE()));
		holder.summerTv.setText("摘要：" +StrUtil.filterStr(temp.getHS_SUMMER()));
		holder.detailTv.setText("详情：" +StrUtil.filterStr(temp.getHS_DETAIL()));
		return view;
	}

	class ViewHolder {
		TextView timeTv;
		TextView userTv;
		TextView typeTv;
		TextView summerTv;
		TextView detailTv;

		public ViewHolder(View view) {
			this.timeTv = (TextView) view.findViewById(R.id.log_item_time);
//			this.userTv = (TextView) view.findViewById(R.id.log_item_user);
			this.typeTv = (TextView) view.findViewById(R.id.log_item_type);
			this.summerTv = (TextView) view.findViewById(R.id.log_item_summer);
			this.detailTv = (TextView) view.findViewById(R.id.log_item_detail);
		}
	}

}
