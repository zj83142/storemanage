package com.zj.storemanag.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.util.StrUtil;

public class AboutUsActivity extends BaseActivity{
	
	private TextView versionTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutsystem);
		
		initView();
	}
	
	private void initView(){
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("关于我们");
		versionTv = (TextView) findViewById(R.id.about_us_version);
		versionTv.setText("V."+StrUtil.filterStr(getVersionName()));
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
}
