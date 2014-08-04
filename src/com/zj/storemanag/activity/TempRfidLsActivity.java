package com.zj.storemanag.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.adapter.TempRfidAdapter;
import com.zj.storemanag.bean.RFIDinfo;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.service.AsyncService;
import com.zj.storemanag.service.AsyncService.Card13Ls;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;

/**
 * 更新临时RFID
 * 
 * @author zhoujing 2014-5-12 上午9:52:50
 */
public class TempRfidLsActivity extends BaseActivity {

	private ListView listView;
	private TempRfidAdapter adapter;
	private List<RFIDinfo> list;

	private int page = 1;

	private EditText cwEt;

	private Button tempBtn;
	private Button updateBtn;

	private int flag = 0;// 0：临时rfid 1：更新rfid

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temp_rfid_list);

		getApp().setPosition(-1);

		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("标签更新");

		tempBtn = (Button) findViewById(R.id.rfid_temp_btn);
		updateBtn = (Button) findViewById(R.id.rfid_update_btn);
		tempBtn.setOnClickListener(this);
		updateBtn.setOnClickListener(this);

		cwEt = (EditText) findViewById(R.id.temp_rfid_cw_et);

		list = new ArrayList<RFIDinfo>();
		listView = (ListView) findViewById(R.id.temp_rfid_listview);
		adapter = new TempRfidAdapter(TempRfidLsActivity.this, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent it = new Intent(TempRfidLsActivity.this,
						UpdateTempRfidActivity.class);
				it.putExtra("rfid", list.get(arg2));
				it.putExtra("position", arg2);
				getApp().setPosition(arg2);
				startActivity(it);
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (isConnectInternet()) {
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
						if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
							if(StrUtil.isNotEmpty(materail)){
								list.clear();
							}
							searchTempRfidLs();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}
		});
		searchTempRfidLs();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int position = getApp().getPosition();
		if (position != -1) {
			list.remove(position);
			adapter.notifyDataSetChanged();
			getApp().setPosition(-1);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			TempRfidLsActivity.this.finish();
			break;
		case R.id.temp_rfid_search_btn:
			materail = StrUtil.filterStr(cwEt.getText().toString());
			if(StrUtil.isNotEmpty(materail)){
				list.clear();
			}
			searchTempRfidLs();
			break;
		case R.id.rfid_temp_btn:
			// 临时标签
			flag = 0;
			setBtnBackground();
			break;
		case R.id.rfid_update_btn:
			// 更新标签
			flag = 1;
			setBtnBackground();
			break;
		default:
			break;
		}
	}

	private void setBtnBackground() {
		if (flag == 0) {
			tempBtn.setBackgroundResource(R.color.blue_dark);
			updateBtn.setBackgroundResource(R.color.blue);
		} else {
			tempBtn.setBackgroundResource(R.color.blue);
			updateBtn.setBackgroundResource(R.color.blue_dark);
		}
		list.clear();
		searchTempRfidLs();
	}

	private String materail;

	private void searchTempRfidLs() {
		// 搜索临时RFID
		MyTip.showProgress(TempRfidLsActivity.this, "查询数据", "正在查询标签,请稍候……");
		materail = StrUtil.filterStr(cwEt.getText().toString());

		AsyncService.GetTempRfidLs getTempRfidLs = AsyncService.getInstance().new GetTempRfidLs();
		getTempRfidLs.execute(handler, ParamsUtil.GETTEMPRFIDLS, getApp()
				.getUrl(), getUser().getUserId(), materail, flag + "", String
				.valueOf(page), pageSize, TempRfidLsActivity.this);

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.GETTEMPRFIDLS:
				setTempRfidLsResult(msg.obj);
				break;
			default:
				break;
			}
		};
	};

	/** 设置Rfid结合结果 */
	protected void setTempRfidLsResult(Object obj) {
		if (obj != null) {
			Object[] result = (Object[]) obj;
			String flag = (String) result[0];
			if (flag.equalsIgnoreCase("ok")) {
				List<RFIDinfo> tempLs = (List<RFIDinfo>) result[1];
				if (tempLs != null) {
					list.addAll(tempLs);
					adapter.notifyDataSetChanged();
				}
			} else {
				MyTip.showToast(TempRfidLsActivity.this, (String) result[1]);
			}
		} else {
			MyTip.showToast(TempRfidLsActivity.this, "没有查询到数据！");
		}
		MyTip.cancelProgress();
	}

}
