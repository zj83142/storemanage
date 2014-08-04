package com.zj.storemanag.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.adapter.LogAdapter;
import com.zj.storemanag.bean.HsLog;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.util.MyTip;

/***
 * 操作日志查询结果列表
 * @author zhoujing
 * 2014-5-27 上午9:54:33
 */
public class OperationListActivity extends BaseActivity {
	
	private List<HsLog> list = new ArrayList<HsLog>();
	private LogAdapter adapter;
	private ListView listView;
	
	private String start;
	private String end;
	private String userNum;
	private String type;
	private String desp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.operation_list);

		initView();
	}
	
	private void initView() {

		listView = (ListView) findViewById(R.id.detail_info_listview);
		adapter = new LogAdapter(OperationListActivity.this, list, handler);
		listView.setAdapter(adapter);
		
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("操作日志");
		Intent it = getIntent();
		start = it.getStringExtra("start");
		end = it.getStringExtra("end");
		userNum = it.getStringExtra("userNum");
		type = it.getStringExtra("type");
		desp = it.getStringExtra("desp");
		
		if (isConnectInternet()) {
			MyTip.showProgress(OperationListActivity.this, "查询数据", "正在查询操作日志信息，请稍候……");
			AsyncService.GetLogList getLogList = AsyncService.getInstance().new GetLogList();
			getLogList.execute(handler, ParamsUtil.GETLOGLIST,getPreference("url"),getUser().getUserId(), type,
					desp, start, end);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;

		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.GETLOGLIST:
				setLogListResult(msg.obj);
				break;

			default:
				break;
			}
		};
	};

	protected void setLogListResult(Object obj) {
		if(obj != null){
			Object[] result = (Object[]) obj;
			String flag = (String) result[0];
			if(flag.equalsIgnoreCase("ok")){
				if(list != null && list.size() > 0){
					list.clear();
				}
				List<HsLog> tempList = (List<HsLog>) result[1];
				list.addAll(tempList);
				adapter.notifyDataSetChanged();
				
			}else if(flag.equalsIgnoreCase("error")){
				MyTip.showToast(OperationListActivity.this, (String)result[1]);
			}
		}
		MyTip.cancelProgress();
		
	}

}
