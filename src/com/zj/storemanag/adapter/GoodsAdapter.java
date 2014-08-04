package com.zj.storemanag.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.adapter.StoreAdapter.ViewHolder;
import com.zj.storemanag.bean.Goods;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.GoodsDao;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;

public class GoodsAdapter extends BaseAdapter {
	private List<Goods> list;
	private LayoutInflater inflater;
	private Handler handler;
	private Context context;
	
	public GoodsAdapter(Context context, List<Goods> list, Handler handler,
			int type) {
		this.context = context;
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
			view = inflater.inflate(R.layout.goods_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		
		if((position & 1) == 0){
			holder.layoutBj.setBackgroundResource(R.color.white);
		}else{
			holder.layoutBj.setBackgroundResource(R.color.gray);
		}
		final Goods temp = list.get(position);
		holder.materialNumTv.setText(StrUtil.filterStr(temp.getMATERIAL()));
		holder.factoryTv.setText(StrUtil.filterStr(temp.getFactory()));
		holder.locationTv.setText(StrUtil.filterStr(temp.getStore()));
		holder.unitTv.setText(StrUtil.slipName(temp.getUnit()));
		holder.despTv.setText(StrUtil.filterStr(temp.getMESSAGE()));
		holder.numberTv.setText(StrUtil.filterStr(temp.getENTRY_QNT()));
		String state = temp.getState();
		if (StrUtil.isNotEmpty(state)) {
			if (state.equalsIgnoreCase("1")) {
				holder.selectBtn.setBackgroundResource(R.drawable.check_press);
			} else {
				holder.selectBtn.setBackgroundResource(R.drawable.check_none);
			}
		} else {
			holder.selectBtn.setBackgroundResource(R.drawable.check_none);
		}
		holder.selectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = StrUtil.filterStr(temp.getENTRY_QNT());
				if (!StrUtil.isNotEmpty(number)) {
					MyTip.showToast(context, "此设备没有录入数量，不能入库");
					return;
				}
				if (number.equalsIgnoreCase("0")) {
					MyTip.showToast(context, "此设备没有录入数量，不能入库");
					return;
				}
				// 判断rfid是否为空，如果为空，初始化rifd
				String rfid = StrUtil.filterStr(temp.getRFID());
				if (!StrUtil.isNotEmpty(rfid)) {
					//判断是否是维修订单
					sendHandlerSelect(temp, ParamsUtil.IsTEMPRFID);
					return;
				}
				if (temp.getState() != null
						&& temp.getState().equalsIgnoreCase("1")) {
					temp.setState("0");
					v.setBackgroundResource(R.drawable.check_none);
				} else {
					temp.setState("1");
					v.setBackgroundResource(R.drawable.check_press);
				}
				GoodsDao.getInstance(context).updateGoodsState(temp.getId(),
						temp.getState());
			}
		});
		holder.numberTv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendHandlerSelect(temp, ParamsUtil.showKeyboard);
			}
		});
		holder.layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendHandlerSelect(temp, ParamsUtil.EQ_DETAIL);
			}

		});
		holder.materialNumTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendHandlerSelect(temp, ParamsUtil.EQ_DETAIL);
			}
		});
		return view;
	}
	
	class ViewHolder {
		LinearLayout layoutBj;
		LinearLayout layout;
		Button selectBtn;
		TextView materialNumTv;
		TextView numberTv;
		TextView unitTv;
		TextView factoryTv;
		TextView locationTv;
		TextView despTv;

		public ViewHolder(View view) {
			
			
			this.layoutBj = (LinearLayout) view.findViewById(R.id.layout_bj);
			this.layout = (LinearLayout) view.findViewById(R.id.goods_item_content_layout);
			this.selectBtn = (Button) view.findViewById(R.id.goods_item_select);
			this.materialNumTv = (TextView) view.findViewById(R.id.goods_item_material);
			this.numberTv = (TextView) view.findViewById(R.id.goods_item_num);
			this.unitTv = (TextView) view.findViewById(R.id.goods_item_unit);
			this.factoryTv = (TextView) view.findViewById(R.id.goods_item_factory);
			this.locationTv = (TextView) view.findViewById(R.id.goods_item_location);
			this.despTv = (TextView) view.findViewById(R.id.goods_item_desp);
		}
	}

	private void sendHandlerSelect(Goods temp, int flag) {
		Message msg = handler.obtainMessage();
		msg.what = flag;
		msg.obj = temp;
		handler.sendMessage(msg);
	}
}
