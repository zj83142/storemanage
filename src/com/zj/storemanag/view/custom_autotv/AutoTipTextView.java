package com.zj.storemanag.view.custom_autotv;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zj.storemanag.R;
import com.zj.storemanag.util.StrUtil;

public class AutoTipTextView extends LinearLayout {
	private AutoCompleteTextView tv;
	private Button delBtn;

	public AutoTipTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.auto_tip_layout, this);
		tv = (AutoCompleteTextView) linearLayout.findViewById(R.id.auto_tip_auto_tv);
		delBtn = (Button) linearLayout.findViewById(R.id.auto_del_btn);
		delBtn.setOnClickListener(new MyClick());
	}

	public AutoTipTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.auto_tip_layout, this);
		tv = (AutoCompleteTextView) linearLayout.findViewById(R.id.auto_tip_auto_tv);
		delBtn = (Button) linearLayout.findViewById(R.id.auto_del_btn);
		delBtn.setOnClickListener(new MyClick());
	}
	
	public void setBtnGone(){
		delBtn.setVisibility(View.GONE);
	}
	
	
	private class MyClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			tv.setText("");
		}
	}
	
	public void setInputType(int flag){
		tv.setInputType(flag);
	}
	
	public void setText(String str){
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
