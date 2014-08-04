package com.zj.storemanag.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.util.StrUtil;

public class CustomKeyboardView extends LinearLayout implements OnClickListener {
	private Handler handler;
	
	private static final int KEYBOARDSHOW = 110;
	private TextView tv_record;
	private String info = "";

	public CustomKeyboardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.customkeyboard, this);
		tv_record = (TextView) linearLayout.findViewById(R.id.tv_recordentry);
		Button btn0 = (Button)linearLayout.findViewById(R.id.keyboard_0);
		Button btn1 = (Button)linearLayout.findViewById(R.id.keyboard_1);
		Button btn2 = (Button)linearLayout.findViewById(R.id.keyboard_2);
		Button btn3 = (Button)linearLayout.findViewById(R.id.keyboard_3);
		Button btn4 = (Button)linearLayout.findViewById(R.id.keyboard_4);
		Button btn5 = (Button)linearLayout.findViewById(R.id.keyboard_5);
		Button btn6 = (Button)linearLayout.findViewById(R.id.keyboard_6);
		Button btn7 = (Button)linearLayout.findViewById(R.id.keyboard_7);
		Button btn8 = (Button)linearLayout.findViewById(R.id.keyboard_8);
		Button btn9 = (Button)linearLayout.findViewById(R.id.keyboard_9);
		Button btnOk = (Button)linearLayout.findViewById(R.id.keyboard_ok);
		Button btnCancel = (Button)linearLayout.findViewById(R.id.keyboard_cancel);
		Button btnC = (Button)linearLayout.findViewById(R.id.keyboard_c);
		Button btnDel = (Button)linearLayout.findViewById(R.id.keyboard_del);
		Button btnPoint = (Button)linearLayout.findViewById(R.id.keyboard_point);
		Button btnMinus = (Button)linearLayout.findViewById(R.id.keyboard_minus);
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnC.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		btnPoint.setOnClickListener(this);
		btnMinus.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.keyboard_0:
			setRecordData("0");
			break;
		case R.id.keyboard_1:
			setRecordData("1");
			break;
		case R.id.keyboard_2:
			setRecordData("2");
			break;
		case R.id.keyboard_3:
			setRecordData("3");
			break;
		case R.id.keyboard_4:
			setRecordData("4");
			break;
		case R.id.keyboard_5:
			setRecordData("5");
			break;
		case R.id.keyboard_6:
			setRecordData("6");
			break;
		case R.id.keyboard_7:
			setRecordData("7");
			break;
		case R.id.keyboard_8:
			setRecordData("8");
			break;
		case R.id.keyboard_9:
			setRecordData("9");
			break;
		case R.id.keyboard_ok:
			if(info.equalsIgnoreCase("-")){
				info = "";
			}
			sendHandlerMessage(info);
			break;
		case R.id.keyboard_cancel:
			sendHandlerMessage("");
			break;
		case R.id.keyboard_c:
			setRecordData("c");
			break;
		case R.id.keyboard_del:
			setRecordData("del");
			break;
		case R.id.keyboard_minus:
			setRecordData("-");
			break;
		case R.id.keyboard_point:
			setRecordData(".");
			break;
		default:
			break;
		}

	}
	
	/** 发送handler消息显示Toast提示 */
	public void sendHandlerMessage(String str) {
		Message msg = handler.obtainMessage();
		msg.obj = str;
		msg.what = ParamsUtil.hideKeyboard;
		handler.sendMessage(msg);
	}

	/**清空textview*/
	public void clearTextView() {
		info = "";
		tv_record.setText(info);
	}

	private void setRecordData(String str) {
		Log.i("zj", info.length() + "----str:--->" + str + "----info---->:"
				+ info);
		if (str.equals("c")) {
			info = "";
		} else if (str.equals("del")) {
			if (info.length() <= 0)
				return;
			info = info.substring(0, info.length() - 1);
		} else {
			if (info.indexOf(".") != -1
					&& !str.equals(".")
					&& (info.substring(info.indexOf(".") + 1, info.length()))
							.length() == 3) {
				return;
			}
//			if (info.length() == 8)
//				return;
			if (str.equals("-")) {
				if (info.length() != 0)
					return;
				info = "-";
			} else if (str.equals(".")) {
				if (info.length() == 0) {
					info = "0.";
				} else if (info.indexOf(".") != -1) {
					return;
				} else {
					info += ".";
				}
			} else {
				info += str;
			}
		}

		tv_record.setText(info);
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public String getInfo() {
		String str = tv_record.getText().toString();
		if(StrUtil.isNotEmpty(str)){
			return str;
		}
		return null;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public boolean isShow = false;
	public boolean isShow(){
		return isShow;
	}
	
	public void setShow(boolean isShow){
		this.isShow = isShow;
		if(isShow){
			CustomKeyboardView.this.setVisibility(View.VISIBLE);
		}else{
			CustomKeyboardView.this.setVisibility(View.GONE);
		}
	}

}
