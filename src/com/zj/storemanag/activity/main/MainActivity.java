package com.zj.storemanag.activity.main;

import java.util.ArrayList;

import log.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.activity.LoginActivity;
import com.zj.storemanag.commen.MyApplication;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.GoodsDao;
import com.zj.storemanag.service.UploadImgService;
import com.zj.storemanag.util.MyTip;

public class MainActivity extends FragmentActivity {
	
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	private ImageView ivBottomLine;
	private TextView tvTabActivity, tvTabGroups, tvTabFriends;

	private int currIndex = 0;
	private int bottomLineWidth;
	private int offset = 0;
	private int position_one;
	private int position_two;
	private Resources resources;
	public MyApplication myApp;
	
	public SharedPreferences spf;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		System.out.println("------MainActivity--------onCreate--------");
		// 启动定时上传图片服务
		
		startService(new Intent(getApplicationContext(), UploadImgService.class));
		
		resources = getResources();
		
		myApp = (MyApplication) getApplication();
		
		myApp.actions.add(this);
		
		InitWidth();
		InitTextView();
		InitViewPager();
		spf = getSharedPreferences("spf_Info", Context.MODE_PRIVATE);
		boolean isLogin = spf.getBoolean("isLogin", false);
		if(!isLogin){
			Intent it = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(it);
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 清除本地保存的设备数据goods
		GoodsDao.getInstance(MainActivity.this).delectData();
	}

	private void InitTextView() {
		tvTabActivity = (TextView) findViewById(R.id.tv_tab_activity);
		tvTabGroups = (TextView) findViewById(R.id.tv_tab_groups);
		tvTabFriends = (TextView) findViewById(R.id.tv_tab_friends);

		tvTabActivity.setOnClickListener(new MyOnClickListener(0));
		tvTabGroups.setOnClickListener(new MyOnClickListener(1));
		tvTabFriends.setOnClickListener(new MyOnClickListener(2));
	}

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();
		
		Fragment outFragment = new OutStoreFragment();
		Fragment enterFragment = new EnterStoreFragment();
		Fragment manageFragment = new ManageFragment();
		
		fragmentsList.add(outFragment);
		fragmentsList.add(enterFragment);
		fragmentsList.add(manageFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void InitWidth() {
		ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
		bottomLineWidth = ivBottomLine.getLayoutParams().width;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (int) ((screenW / 3 - bottomLineWidth) / 2);
		Log.i("MainActivity", "offset=" + offset);

		position_one = (int) (screenW / 3);
		position_two = position_one * 2;
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, 0, 0, 0);
					tvTabGroups.setTextColor(resources.getColor(R.color.blue_dark));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, 0, 0, 0);
					tvTabFriends.setTextColor(resources.getColor(R.color.blue_dark));
				}
				tvTabActivity.setTextColor(resources.getColor(R.color.black));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, position_one, 0,
							0);
					tvTabActivity.setTextColor(resources.getColor(R.color.blue_dark));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(position_two, position_one, 0, 0);
					tvTabFriends.setTextColor(resources.getColor(R.color.blue_dark));
				} 
				tvTabGroups.setTextColor(resources.getColor(R.color.black));
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, position_two, 0,
							0);
					tvTabActivity.setTextColor(resources.getColor(R.color.blue_dark));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(position_one, position_two, 0, 0);
					tvTabGroups.setTextColor(resources.getColor(R.color.blue_dark));
				} 
				tvTabFriends.setTextColor(resources.getColor(R.color.black));
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			ivBottomLine.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			MyTip.showDialog(MainActivity.this, "退出", "是否退出系统？", myHandler, ParamsUtil.EXIT);
		}
		return true;
	}
	
	private Handler myHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ParamsUtil.EXIT:
				boolean isExit = msg.getData().getBoolean("select");
				if(isExit){
					//退出系统
					exit();
				}
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			MyTip.showDialog(MainActivity.this, "退出", "是否退出系统？", myHandler, ParamsUtil.EXIT);
			return true;// 不知道返回true或是false有什么区别??
		}
		return super.dispatchKeyEvent(event);
	}
	
	/**
	 * 退出程序
	 */
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(new Intent(getApplicationContext(),
				UploadImgService.class));
	}
}
