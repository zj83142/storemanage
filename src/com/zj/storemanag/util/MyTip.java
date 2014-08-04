package com.zj.storemanag.util;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.storemanag.R;
import com.zj.storemanag.adapter.PopAdapter;
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.commen.ParamsUtil;

public class MyTip {

	public static ProgressDialog myDialog;

	/**
	 * 提示框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param messge
	 *            消息
	 * @param handler
	 *            消息处理
	 */
	public static void showDialog(Context context, String title, String messge,
			final Handler handler, final int flag) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(messge)
//				.setCancelable(false)
				.setNegativeButton(
						context.getResources().getString(R.string.cancel_str),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sendHandlerMsg(handler, flag, false);
								dialog.cancel();
							}
						})
				.setNeutralButton(
						context.getResources().getString(R.string.confirm_str),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sendHandlerMsg(handler, flag, true);
								dialog.cancel();
							}
						}).show();
	}

	public static void sendHandlerMsg(Handler handler, int flag, boolean select) {
		Message messages = handler.obtainMessage();
		Bundle bl = new Bundle();
		bl.putBoolean("select", select);
		messages.setData(bl);
		messages.what = flag;
		handler.sendMessage(messages);
	}

	/**
	 * toast 提示
	 * 
	 * @param str
	 *            提示内容
	 * @param context
	 */
	public static void showToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示ProgressDialog
	 * 
	 * @param context
	 * @param title
	 * @param msg
	 */
	public static void showProgress(Context context, String title, String msg) {
		myDialog = new ProgressDialog(context);
		myDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		myDialog.setTitle(title);
		myDialog.setMessage(msg);
		// myDialog.setCancelable(false);
		myDialog.show();
	}

	/** 取消ProgressDialog */
	public static void cancelProgress() {
		if (myDialog != null) {
			myDialog.cancel();
		}
	}

	/**
	 * 显示pop
	 * 
	 * @param context
	 *            上下文
	 * @param nameList
	 *            选项
	 * @param posView
	 *            pop在哪个控件下
	 * @param handler
	 *            选择后发送handler消息
	 */
	public static void showPopView(Context context,
			final List<PopItem> nameList, View posView, final Handler handler) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popView = inflater.inflate(R.layout.pop, null);
		popView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		popView.setAnimation(AnimationUtils.loadAnimation(context,
				R.anim.slide_in_up));
		final PopupWindow pw = new PopupWindow(popView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.setTouchable(true);
		pw.update();
		ListView fillerNameList = (ListView) popView
				.findViewById(R.id.byname_list);
		PopAdapter popAdapter = new PopAdapter(context, nameList);
		fillerNameList.setAdapter(popAdapter);
		fillerNameList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				PopItem item = (PopItem) nameList.get(arg2);
				Message msg = handler.obtainMessage();
				msg.what = ParamsUtil.POP_SELECT;
				msg.obj = item;
				handler.sendMessage(msg);
				pw.dismiss();
			}
		});
		pw.showAsDropDown(posView);
	}

	/**
	 * 显示pop
	 * 
	 * @param context
	 *            上下文
	 * @param nameList
	 *            选项
	 * @param posView
	 *            pop在哪个控件下
	 * @param handler
	 *            选择后发送handler消息
	 */
	public static void showPopView(Context context,
			final List<PopItem> nameList, final TextView textView,
			final Handler handler) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popView = inflater.inflate(R.layout.pop, null);
		popView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		popView.setAnimation(AnimationUtils.loadAnimation(context,
				R.anim.slide_in_up));
		final PopupWindow pw = new PopupWindow(popView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.setTouchable(true);
		pw.update();
		ListView fillerNameList = (ListView) popView
				.findViewById(R.id.byname_list);
		PopAdapter popAdapter = new PopAdapter(context, nameList);
		fillerNameList.setAdapter(popAdapter);
		fillerNameList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				PopItem item = (PopItem) nameList.get(arg2);
				textView.setText(item.getName());
				textView.setTag(item.getType());
				// Message msg = handler.obtainMessage();
				// msg.what = ParamsUtil.POP_SELECT;
				// msg.obj = item;
				// handler.sendMessage(msg);
				pw.dismiss();
			}
		});
		pw.showAsDropDown(textView);
	}

	/**
	 * 显示pop
	 * 
	 * @param context
	 *            上下文
	 * @param nameList
	 *            选项
	 * @param posView
	 *            pop在哪个控件下
	 * @param handler
	 *            选择后发送handler消息, handler!= null 发送消息
	 */
	public static void myPopView(Context context, final List<PopItem> nameList,
			final TextView textView, final Handler handler) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popView = inflater.inflate(R.layout.pop, null);
		popView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		popView.setAnimation(AnimationUtils.loadAnimation(context,
				R.anim.slide_in_up));
		final PopupWindow pw = new PopupWindow(popView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pw.setFocusable(true);
		pw.setOutsideTouchable(true);
		pw.setTouchable(true);
		pw.update();
		ListView fillerNameList = (ListView) popView
				.findViewById(R.id.byname_list);
		PopAdapter popAdapter = new PopAdapter(context, nameList);
		fillerNameList.setAdapter(popAdapter);
		fillerNameList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				PopItem item = (PopItem) nameList.get(arg2);
				textView.setText(item.getName());
				textView.setTag(item.getType());
				if (handler != null) {
					Message msg = handler.obtainMessage();
					msg.what = ParamsUtil.POP_SELECT;
					msg.obj = item;
					handler.sendMessage(msg);
				}
				pw.dismiss();
			}
		});
		pw.showAsDropDown(textView);
	}

}
