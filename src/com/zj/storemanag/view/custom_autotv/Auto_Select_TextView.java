package com.zj.storemanag.view.custom_autotv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import com.zj.storemanag.R;
import com.zj.storemanag.util.StrUtil;

public class Auto_Select_TextView extends LinearLayout{
	private AutoCompleteTextView tv;

	public Auto_Select_TextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.auto_select_layout, this);
		tv = (AutoCompleteTextView) linearLayout
				.findViewById(R.id.auto_select_auto_tv);
	}

	public Auto_Select_TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.auto_select_layout, this);
		tv = (AutoCompleteTextView) linearLayout
				.findViewById(R.id.auto_select_auto_tv);
	}

	public void setText(String str) {
		tv.setText(str);
	}

	public void setAdapter(AutoTipAdapter adapter) {
		tv.setAdapter(adapter);
	}

	public void setThreshold(int threshold) {
		tv.setThreshold(threshold);
	}

	public AutoCompleteTextView getAutoCompleteTextView() {
		return tv;
	}

	public String getText() {
		return StrUtil.filterStr(tv.getText().toString());
	}

}
