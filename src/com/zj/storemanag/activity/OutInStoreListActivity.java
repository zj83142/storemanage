package com.zj.storemanag.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.adapter.StoreHistoryAdapter;
import com.zj.storemanag.bean.StoreHistoryBean;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.service.BaseAsync;
import com.zj.storemanag.service.GetStoreHistoryService;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;

/**
 * 出入库查询结果列表显示
 * @author zhoujing
 * 2014-5-26 下午3:28:32
 */
public class OutInStoreListActivity extends BaseActivity {
	
	private List<StoreHistoryBean> list;
	private ListView listView;
	private StoreHistoryAdapter adapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.outinstore_list);

		initView();
	}
	private String xml;
	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("出入库信息");
		xml = getIntent().getStringExtra("xml");
		if(StrUtil.isNotEmpty(xml)){
			MyTip.showProgress(OutInStoreListActivity.this, "查询数据", "正在查询出入库数据，请稍候……");
			new GetStoreHistoryAsynck().execute();
		}
		
		list = new ArrayList<StoreHistoryBean>();
		listView = (ListView) findViewById(R.id.detail_info_listview);
		adapter = new StoreHistoryAdapter(OutInStoreListActivity.this, list);
		listView.setAdapter(adapter);
	}
	
	
	/**
	 * 查询出入库凭证报表
	 * @author zhoujing
	 * 2014-5-15 下午5:47:06
	 */
	private class GetStoreHistoryAsynck extends BaseAsync{

		@Override
		protected Object doInBackground(Object... params) {
			return new GetStoreHistoryService().getStoreHistory(getApp().getUrl(), "GetMB51",
					getUser().getUserId(), xml, OutInStoreListActivity.this);
		}
		
		@Override
		protected void onPostExecute(Object obj) {
			// TODO Auto-generated method stub
			super.onPostExecute(obj);
			list.clear();
			if(obj != null){
				Object[] result = (Object[]) obj;
				String flag = (String)result[0];
				if(flag.equalsIgnoreCase("ok")){
					List<StoreHistoryBean> tempLs = (List<StoreHistoryBean>) result[1];
					if(tempLs != null && tempLs.size() > 0){
						list.addAll(tempLs);
					}
				}
			}
			adapter.notifyDataSetChanged();
			MyTip.cancelProgress();
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;

		default:
			break;
		}
	}

}
