package com.zj.storemanag.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.util.MyTip;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.TimeUtil;
import com.zj.storemanag.view.TakeTimeDialog;

/**
 * 操作日志查询
 * @author zhoujing
 * 2014-5-27 上午9:54:13
 */
public class OperationActivity extends BaseActivity {
	
	private TakeTimeDialog myDialog;
	
	private TextView startEt;
	private TextView endEt;
	private EditText userNumEt;
	private TextView typeEt;
	private EditText despEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.operation);
		initView();
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("查询操作日志");
		startEt = (TextView) findViewById(R.id.operation_star_time_et);
		endEt = (TextView) findViewById(R.id.operation_end_time_et);
		userNumEt = (EditText) findViewById(R.id.operation_user_num_et);
		typeEt = (TextView) findViewById(R.id.operation_type_et);
		typeEt.setOnClickListener(this);
		despEt = (EditText) findViewById(R.id.operation_desp_et);
		startEt.setOnClickListener(this);
		endEt.setOnClickListener(this);
		startEt.setText(TimeUtil.getCurrentDate());
		endEt.setText(TimeUtil.getCurrentDate());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.top_back_btn:
			finish();
			break;
		case R.id.operation_star_time_et:
		case R.id.operation_star_time_btn:
			// 开始时间
			myDialog = new TakeTimeDialog(OperationActivity.this,
					R.style.modifydialog, startEt);
			myDialog.show();
			break;
		case R.id.operation_end_time_et:
		case R.id.operation_end_time_btn:
			// 结束时间
			myDialog = new TakeTimeDialog(OperationActivity.this,
					R.style.modifydialog, endEt);
			myDialog.show();
			break;
		case R.id.operation_type_et:
		case R.id.operation_type_btn:
			// 操作类型
			List<PopItem> nameList = getTypes();
			if (nameList != null && nameList.size() > 0) {
				MyTip.showPopView(OperationActivity.this, nameList, typeEt, handler);
			}
			break;
		case R.id.operation_query_btn:
			// 查询
			skipList();
			break;
		default:
			break;
		}
	}

	private List<PopItem> getTypes() {
		List<PopItem> list = new ArrayList<PopItem>();
		String[] types = getResources().getStringArray(R.array.operation_type);
		for(int i = 0; i < types.length; i++){
			PopItem temp = new PopItem();
			temp.setId(String.valueOf(i));
			temp.setName(types[i]);
			list.add(temp);
		}
		return list;
	}

	private void skipList() {
		String start = startEt.getText().toString();
		String end = endEt.getText().toString();
		String userNum = userNumEt.getText().toString();
		String type = typeEt.getText().toString();
		String desp = despEt.getText().toString();
		Intent it = new Intent(OperationActivity.this, OperationListActivity.class);
		it.putExtra("start", StrUtil.filterStr(start));
		it.putExtra("end", StrUtil.filterStr(end));
		it.putExtra("userNum", StrUtil.filterStr(userNum));
		it.putExtra("type", StrUtil.filterStr(type));
		it.putExtra("desp", StrUtil.filterStr(desp));
		startActivity(it);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.POP_SELECT:
				if (msg.obj != null
						&& msg.obj.getClass().isInstance(new PopItem())) {
					PopItem item = (PopItem) msg.obj;
					typeEt.setText(item.getName());
				}
				break;

			default:
				break;
			}
		};
	};
	
	
}
