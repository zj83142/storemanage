package com.zj.storemanag.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;

public class TakeTimeDialog extends Dialog {
	private DatePicker datePicker;
	private Button setBtn;
	private Button cancelBtn;
	private TextView dateET;
	private String[] names = new String[] { "month", "year", "day"};
	private LinearLayout.LayoutParams lp;

	public TakeTimeDialog(Context context, int theme, TextView dateET) {
		super(context, theme);
		this.dateET = dateET;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taketimedialog);
		initView();
	}

	private void initView() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setBtn = (Button) findViewById(R.id.taketime_set);
		cancelBtn = (Button) findViewById(R.id.taketime_cancel);
		setBtn.setOnClickListener(new MyOnclickListener());
		cancelBtn.setOnClickListener(new MyOnclickListener());
		datePicker = (DatePicker) findViewById(R.id.datepicker);
		lp = new LinearLayout.LayoutParams(120,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < names.length; i++) {
			setEditViewStyle(names[i]);
		}
	}

	private void setEditViewStyle(String str) {
		ViewGroup childpicker = (ViewGroup) findViewById(Resources.getSystem()
				.getIdentifier(str, "id", "android"));
		childpicker.setLayoutParams(lp);
		EditText mornthEt = (EditText) childpicker.getChildAt(1);
		mornthEt.setTextSize(18);
	}

	private final class MyOnclickListener implements
			android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.taketime_set:
				StringBuffer sb = new StringBuffer();
				sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
						datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
				dateET.setText(sb);
				break;
			case R.id.taketime_cancel:
				// dateET.setText("");
				break;

			default:
				break;
			}
			TakeTimeDialog.this.dismiss();
		}
	}
}
