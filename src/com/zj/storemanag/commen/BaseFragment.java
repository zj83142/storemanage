package com.zj.storemanag.commen;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.zj.storemanag.R;
import com.zj.storemanag.util.MyTip;

public class BaseFragment extends Fragment {

	public MyApplication myApp;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myApp = (MyApplication) getActivity().getApplication();
	}

	public OnClickBtnListener onClickBtn;
	
	public interface OnClickBtnListener {

		/** 出入库保存按钮 */
		public void onSaveClick(String xml);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onClickBtn = (OnClickBtnListener) activity;
		} catch (Exception e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnClickBtnListener");
		}
	}
	

	/**
	 * 判断网络是否正常
	 * 
	 * @return
	 */
	public boolean isConnectInternet() {
		ConnectivityManager conManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) { // 注意，这个判断一定要的哦，要不然会出错
			return networkInfo.isAvailable();
		}
		MyTip.showToast(getActivity(), getString(R.string.net_error_tip));
		return false;
	}

	/** 清除控件数据 */
	public void clearViewData() {
		
	};
	

	/** 获取控件数据放在Bundle中 */
	public Bundle getViewBundle() {
		Bundle bundle = new Bundle();
		bundle.putBoolean("skip", true);
		return bundle;
	}

	/** 工程技改（分公司），寄售备出修，寄售备出成这三种是先入库再出库的，这个方法是获取二期调用接口的xml，其他出入库不需要重写这个方法 */
	public String getSendXml() {
		return null;
	}
	
//	public void saveBtnVisiable(){
//		
//	}

}
