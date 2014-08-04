package com.zj.storemanag.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.activity.AboutUsActivity;
import com.zj.storemanag.activity.Card13LsActivity;
import com.zj.storemanag.activity.Card245LsActivity;
import com.zj.storemanag.activity.MaterialQueryActivity;
import com.zj.storemanag.activity.OperationActivity;
import com.zj.storemanag.activity.OutInStoreActivity;
import com.zj.storemanag.activity.RFIDQueryActivity;
import com.zj.storemanag.activity.TempRfidLsActivity;
import com.zj.storemanag.commen.MyApplication;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.util.MyTip;

public class ManageFragment extends Fragment {
	
	private MyApplication myApp;
	
	private Intent it;
	
	private SharedPreferences spf;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApplication) getActivity().getApplication();
		spf = getActivity().getSharedPreferences("spf_Info", Context.MODE_PRIVATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.manage_fragment, container, false);
		TextView materialFindTv = (TextView) view.findViewById(R.id.manage_material_find);
		TextView rfidFindTv = (TextView) view
				.findViewById(R.id.manage_rfid_find);
		TextView outInStoreFindTv = (TextView) view
				.findViewById(R.id.manage_out_in_store_find);
		TextView operationFindTv = (TextView) view
				.findViewById(R.id.manage_operation_find);
//		TextView checkTv = (TextView) view.findViewById(R.id.manage_check);
//		TextView handInitTv = (TextView) view
//				.findViewById(R.id.manage_hand_init);
		TextView tempRfidUpdateTv = (TextView) view
				.findViewById(R.id.manage_temp_rfid_update);
		TextView card245Tv = (TextView) view.findViewById(R.id.manage_card_245);
		TextView card13Tv = (TextView) view.findViewById(R.id.manage_card_13);
		TextView aboutSystemTv = (TextView) view.findViewById(R.id.manage_about_system);
		Button exitBtn = (Button) view.findViewById(R.id.manage_exit);

		materialFindTv.setOnClickListener(new MyClick());
		rfidFindTv.setOnClickListener(new MyClick());

		outInStoreFindTv.setOnClickListener(new MyClick());
		operationFindTv.setOnClickListener(new MyClick());

//		checkTv.setOnClickListener(new MyClick());
//		handInitTv.setOnClickListener(new MyClick());
		tempRfidUpdateTv.setOnClickListener(new MyClick());

		card245Tv.setOnClickListener(new MyClick());
		card13Tv.setOnClickListener(new MyClick());
		aboutSystemTv.setOnClickListener(new MyClick());
		exitBtn.setOnClickListener(new MyClick());

		return view;

	}

	private class MyClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.manage_material_find:
				// 物料查询
				it = new Intent(getActivity(), MaterialQueryActivity.class);
				startActivity(it);
				break;
			case R.id.manage_rfid_find:
				// RFID查询
				it = new Intent(getActivity(), RFIDQueryActivity.class);
				startActivity(it);
				break;
			case R.id.manage_out_in_store_find:
				// 出入库查询
				it = new Intent(getActivity(), OutInStoreActivity.class);
				startActivity(it);
				break;
			case R.id.manage_operation_find:
				// 操作日志查询
				it = new Intent(getActivity(), OperationActivity.class);
				startActivity(it);
				break;
//			case R.id.manage_check:
//				// 盘点
//
//				break;
//			case R.id.manage_hand_init:
//				// 手工初始化
//
//				break;
			case R.id.manage_temp_rfid_update:
				// 临时RFID更新
				it = new Intent(getActivity(), TempRfidLsActivity.class);
				startActivity(it);
				break;
			case R.id.manage_card_245:
				// 卡片二四五
				it = new Intent(getActivity(), Card245LsActivity.class);
				startActivity(it);
				break;
			case R.id.manage_card_13:
				// 旧卡一三
				it = new Intent(getActivity(), Card13LsActivity.class);
				startActivity(it);
				break;
			case R.id.manage_about_system:
				// 关于系统
				it = new Intent(getActivity(), AboutUsActivity.class);
				startActivity(it);
				break;
			case R.id.manage_exit:
				// 退出
				MyTip.showDialog(getActivity(), "退出", "是否确定退出程序", handler, ParamsUtil.EXIT);
				break;

			default:
				break;
			}

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.EXIT:
				boolean isExit = msg.getData().getBoolean("select");
				if(isExit){
					exit();
				}
				break;

			default:
				break;
			}
		};
	};

	public void exit() {
		saveLogout();
		for (Activity temp : myApp.actions) {
			temp.finish();
		}
		myApp.actions.clear();
	}
	
	/** 保存参数设置 */
	public boolean saveLogout() {
		Editor editor = spf.edit();
		editor.putBoolean("isLogin", false);
		return editor.commit();
	}

}
