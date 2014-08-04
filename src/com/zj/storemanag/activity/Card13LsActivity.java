package com.zj.storemanag.activity;

import java.util.ArrayList;
import java.util.List;

import log.Log;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.adapter.Card13Adapter;
import com.zj.storemanag.bean.Card13;
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;

public class Card13LsActivity extends BaseActivity {

	private Card13Adapter adapter;
	private ListView listView;
	private List<Card13> list;

	private int page = 1;

	private TextView factoryTv;
	private TextView storeTv;
	private TextView isPrintTv;
	
	private Button searchBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card13_ls);
		getApp().setPosition(-1);
		
		initView();

		// 加载卡片
		getCardList("0", "", "");
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("旧卡");
		
		searchBtn = (Button) findViewById(R.id.top_right_btn);
		searchBtn.setVisibility(View.VISIBLE);
		searchBtn.setText("搜索");
		searchBtn.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.card13_listview);
		list = new ArrayList<Card13>();
		adapter = new Card13Adapter(Card13LsActivity.this, list);
		listView.setAdapter(adapter);
		factoryTv = (TextView) findViewById(R.id.card13_ls_factory);
		storeTv = (TextView) findViewById(R.id.card13_ls_store);
		isPrintTv = (TextView) findViewById(R.id.card13_ls_print);
		factoryTv.setOnClickListener(this);
		storeTv.setOnClickListener(this);
		isPrintTv.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Card13 card13 = list.get(arg2);
				Intent it = new Intent(Card13LsActivity.this,
						Card13Activity.class);
				it.putExtra("position", arg2);
				it.putExtra("material", card13.getMaterial());
				it.putExtra("factory", card13.getFactory());
				it.putExtra("store", card13.getStore());
				startActivity(it);
			}
		});

		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (isConnectInternet()) {
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
						if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
							if (isConnectInternet() && adapter != null
									&& !adapter.equals("")) {
								String isPrint = StrUtil.filterStr((String)isPrintTv.getTag());
								if(!StrUtil.isNotEmpty(isPrint)){
									isPrint = "0";
								}
								String factory = StrUtil.filterStr(factoryTv.getText().toString());
								if(factory.equalsIgnoreCase("工厂")){
									factory = "";
								}
								String store = StrUtil.filterStr(storeTv.getText().toString());
								if(store.equalsIgnoreCase("库位")){
									store = "";
								}
								getCardList(isPrint, factory, store);
							}
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	 @Override
	 protected void onResume() {
		 // TODO Auto-generated method stub
		 super.onResume();
	 	int position = getApp().getPosition();
	 	if(position != -1){
	 		list.remove(position);
	 		adapter.notifyDataSetChanged();
	 	}
	 }
	

	private void getCardList(String isPrint, String factory, String store) {
		MyTip.showProgress(Card13LsActivity.this, "加载", "数据加载中，请稍后……");
		AsyncService.Card13Ls card13Ls = AsyncService.getInstance().new Card13Ls();
		card13Ls.execute(handler, ParamsUtil.CARD13LS, getApp().getUrl(), isPrint,
				factory, store, String.valueOf(page++), pageSize, Card13LsActivity.this);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.CARD13LS:
				setCard13LsResult(msg.obj);
				break;
			case ParamsUtil.POP_SELECT:
				if (msg.obj != null && msg.obj.getClass().isInstance(new PopItem())) {
					String isPrint = StrUtil.filterStr((String)isPrintTv.getTag());
					if(!StrUtil.isNotEmpty(isPrint)){
						isPrint = "0";
					}
					String factory = StrUtil.filterStr(factoryTv.getText().toString());
					if(factory.equalsIgnoreCase("工厂")){
						factory = "";
					}
					String store = StrUtil.filterStr(storeTv.getText().toString());
					if(store.equalsIgnoreCase("库位")){
						store = "";
					}
					list.clear();
					adapter.notifyDataSetChanged();
					page = 1;
					getCardList(isPrint, factory, store);
				}
				break;

			default:
				break;
			}
		};
	};

	protected void setCard13LsResult(Object obj) {
		// list.clear();
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if (info.equalsIgnoreCase("ok")) {
				List<Card13> tempLs = (List<Card13>) result[1];
				if (tempLs != null) {
					list.addAll(tempLs);
					adapter.notifyDataSetChanged();
				}
			} else {
				MyTip.showToast(Card13LsActivity.this, (String) result[1]);
			}
		}
		MyTip.cancelProgress();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		case R.id.top_right_btn:
			Intent it = new Intent(Card13LsActivity.this, Card13Activity.class);
			startActivity(it);
			break;
		case R.id.card13_ls_factory:
			setFactoryPopData(factoryTv);
			break;
		case R.id.card13_ls_store:
			// 库位
			String factory = factoryTv.getText().toString();
			if (StrUtil.isNotEmpty(factory)&& !factory.equalsIgnoreCase("工厂")) {
				String factoryValue = StrUtil.slipValue(factory);
				setStorePopData(factoryValue, storeTv);
			} else {
				MyTip.showToast(Card13LsActivity.this, "请先输入工厂信息！");
			}
			break;
		case R.id.card13_ls_print:
			setPrintLs(isPrintTv);
			break;
		default:
			break;
		}
	}
	
	private void setPrintLs(TextView currentView) {
		List<PopItem> popLs = new ArrayList<PopItem>();
		popLs.add(new PopItem("0", "0", "未打印", false, ""));
		popLs.add(new PopItem("1", "1", "已打印", false, ""));
		if (popLs != null && popLs.size() > 0) {
			MyTip.myPopView(Card13LsActivity.this, popLs, currentView, handler);
		} else {
			Log.i("zj", "没有查询到工厂信息");
		}
	}

	
	private void setFactoryPopData(TextView currentView) {
		List<PopItem> popLs = BaseInfoDao.getInstance(Card13LsActivity.this)
				.findLsByType("1");
		if (popLs != null && popLs.size() > 0) {
			MyTip.myPopView(Card13LsActivity.this, popLs, currentView, handler);
			storeTv.setText("库位");
		} else {
			Log.i("zj", "没有查询到工厂信息");
		}
	}

	private void setStorePopData(String factory, TextView currentView) {
		List<PopItem> popLs = BaseInfoDao.getInstance(Card13LsActivity.this).findStoreLs(
				factory);
		if (popLs != null && popLs.size() > 0) {
			MyTip.myPopView(Card13LsActivity.this, popLs, currentView, handler);
		} else {
			Log.i("zj", "没有查询到库位信息");
		}
	}
}
