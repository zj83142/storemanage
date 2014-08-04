package com.zj.storemanag.activity;

import java.util.ArrayList;
import java.util.List;

import log.Log;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.adapter.Card245Adapter;
import com.zj.storemanag.bean.Card245;
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.BaseInfoDao;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;

public class Card245LsActivity extends BaseActivity {

	private Card245Adapter adapter;
	private ListView listView;
	private List<Card245> list;
	
	private int page = 1;
	
	private TextView factoryTv;
	private TextView storeTv;
	private TextView typeTv;
	private TextView isPrintTv;
	
	private Button searchBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card245_ls);
		initView();
		
		getApp().setPosition(-1);
		getCardList("0","","","");
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("卡片二四五");
		searchBtn = (Button) findViewById(R.id.top_right_btn);
		searchBtn.setVisibility(View.VISIBLE);
		searchBtn.setText("搜索");
		searchBtn.setOnClickListener(this);
		
		listView = (ListView) findViewById(R.id.card245_listview);
		list = new ArrayList<Card245>();
		adapter = new Card245Adapter(Card245LsActivity.this, list);
		listView.setAdapter(adapter);
		factoryTv = (TextView) findViewById(R.id.card245_ls_factory);
		storeTv = (TextView) findViewById(R.id.card245_ls_store);
		typeTv = (TextView) findViewById(R.id.card245_ls_type);
		isPrintTv = (TextView) findViewById(R.id.card245_ls_print);
		factoryTv.setOnClickListener(this);
		storeTv.setOnClickListener(this);
		typeTv.setOnClickListener(this);
		isPrintTv.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Card245 card245 = list.get(arg2);
				Intent it = new Intent(Card245LsActivity.this,
						Card245Activity.class);
				it.putExtra("position", arg2);
				it.putExtra("materail", card245.getMateral());
				it.putExtra("proof", card245.getMatCode());
				it.putExtra("year", card245.getMatYear());
				it.putExtra("facotry", card245.getFactory());
				it.putExtra("store", card245.getStore());
				startActivity(it);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (isConnectInternet()) {
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
						if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
							if (isConnectInternet() && adapter != null && !adapter.equals("")) {
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
								String type = StrUtil.filterStr(typeTv.getText().toString());
								if(type.equalsIgnoreCase("类型")){
									type = "";
								}
								getCardList(isPrint,type,factory,store);
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
	
	private void getCardList(String isPrint, String moveType, String factory, String store) {
		MyTip.showProgress(Card245LsActivity.this, "加载", "数据加载中，请稍后……");
		AsyncService.Card245Ls card245Ls = AsyncService.getInstance().new Card245Ls();
		card245Ls.execute(handler, ParamsUtil.CARD245LS, getApp().getUrl(), isPrint, moveType, factory,store,String.valueOf(page), pageSize, Card245LsActivity.this);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.CARD245LS:
				setCard245LsResult(msg.obj);
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
					String type = StrUtil.filterStr(typeTv.getText().toString());
					if(type.equalsIgnoreCase("类型")){
						type = "";
					}
					list.clear();
					adapter.notifyDataSetChanged();
					page = 1;
					getCardList(isPrint,type,factory,store);
				}
				break;
			default:
				break;
			}
		};
	};

	protected void setCard245LsResult(Object obj) {
		if(obj != null){
			Object[] result = (Object[]) obj;
			String info = (String) result[0];
			if(info.equalsIgnoreCase("ok")){
				List<Card245> tempLs = (List<Card245>)result[1];
				if(tempLs != null){
					list.addAll(tempLs);
					adapter.notifyDataSetChanged();
				}
			}else{
				MyTip.showToast(Card245LsActivity.this, (String) result[1]);
			}
		}
		MyTip.cancelProgress();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		case R.id.top_right_btn:
			Intent it = new Intent(Card245LsActivity.this, Card245Activity.class);
			startActivity(it);
			break;
		case R.id.card245_ls_factory:
			setFactoryPopData(factoryTv);
			break;
		case R.id.card245_ls_store:
			// 库位
			String factory = factoryTv.getText().toString();
			if (StrUtil.isNotEmpty(factory)&& !factory.equalsIgnoreCase("工厂")) {
				String factoryValue = StrUtil.slipValue(factory);
				setStorePopData(factoryValue, storeTv);
			} else {
				MyTip.showToast(Card245LsActivity.this, "请先输入工厂信息！");
			}
			break;
		case R.id.card245_ls_type:
			getTypeLs(typeTv);
			break;
		case R.id.card245_ls_print:
			setPrintLs(isPrintTv);
			break;
		default:
			break;
		}
	}
	
	private void getTypeLs(TextView typeTv2) {
		List<PopItem> popLs = new ArrayList<PopItem>();
		popLs.add(new PopItem("1", "101", "101", false, ""));
		popLs.add(new PopItem("2", "201", "201", false, ""));
		popLs.add(new PopItem("3", "221", "221", false, ""));
		popLs.add(new PopItem("4", "261", "261", false, ""));
		popLs.add(new PopItem("5", "301", "301", false, ""));
		popLs.add(new PopItem("6", "311", "311", false, ""));
		popLs.add(new PopItem("7", "411", "411", false, ""));
		popLs.add(new PopItem("8", "415", "415", false, ""));
		popLs.add(new PopItem("9", "521", "521", false, ""));
		popLs.add(new PopItem("10", "522", "522", false, ""));
		popLs.add(new PopItem("11", "902", "902", false, ""));
		popLs.add(new PopItem("12", "913", "913", false, ""));
		if (popLs != null && popLs.size() > 0) {
			MyTip.myPopView(Card245LsActivity.this, popLs, typeTv2, handler);
		} else {
			Log.i("zj", "没有查询到工厂信息");
		}
	}

	private void setPrintLs(TextView currentView) {
		List<PopItem> popLs = new ArrayList<PopItem>();
		popLs.add(new PopItem("0", "0", "未打印", false, ""));
		popLs.add(new PopItem("1", "1", "已打印", false, ""));
		if (popLs != null && popLs.size() > 0) {
			MyTip.myPopView(Card245LsActivity.this, popLs, currentView, handler);
		} else {
			Log.i("zj", "没有查询到工厂信息");
		}
	}

	
	private void setFactoryPopData(TextView currentView) {
		List<PopItem> popLs = BaseInfoDao.getInstance(Card245LsActivity.this)
				.findLsByType("1");
		if (popLs != null && popLs.size() > 0) {
			MyTip.myPopView(Card245LsActivity.this, popLs, currentView, handler);
			storeTv.setText("库位");
		} else {
			Log.i("zj", "没有查询到工厂信息");
		}
	}

	private void setStorePopData(String factory, TextView currentView) {
		List<PopItem> popLs = BaseInfoDao.getInstance(Card245LsActivity.this).findStoreLs(
				factory);
		if (popLs != null && popLs.size() > 0) {
			MyTip.myPopView(Card245LsActivity.this, popLs, currentView, handler);
		} else {
			Log.i("zj", "没有查询到库位信息");
		}
	}
}
