package com.zj.storemanag.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.storemanag.R;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.TimeUtil;

/**
 * 显示指标项各内容
 * 
 * @author Administrator
 * 
 */
public class ProofAndAccontView extends LinearLayout {
	
	private TextView thingCodeTv;
	private TextView proofEt;
	private TextView accountEt;
	
	private Context context;
	private TakeTimeDialog timeDialog;
	
	public ProofAndAccontView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout linearLayout = (LinearLayout) inflater.inflate(
				R.layout.left_comment, this);
		thingCodeTv = (TextView) linearLayout.findViewById(R.id.thing_code_tv);
		proofEt = (TextView) linearLayout.findViewById(R.id.proof_date_et);
		accountEt = (TextView) linearLayout.findViewById(R.id.account_date_et);
		initView();
	}

	private void initView() {
		thingCodeTv = (TextView) findViewById(R.id.thing_code_tv);
		proofEt = (TextView) findViewById(R.id.proof_date_et);
		accountEt = (TextView) findViewById(R.id.account_date_et);
		proofEt.setText(TimeUtil.getCurrentDate());
		accountEt.setText(TimeUtil.getCurrentDate());
		proofEt.setOnClickListener(new MyClick());
		accountEt.setOnClickListener(new MyClick());
	}
	
	private class MyClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.proof_date_et:
				// 凭证日期
				timeDialog = new TakeTimeDialog(context, R.style.modifydialog, proofEt);
				timeDialog.show();
				break;
			case R.id.account_date_et:
				// 记账日期
				timeDialog = new TakeTimeDialog(context, R.style.modifydialog, accountEt);
				timeDialog.show();
				break;
			default:
				break;
			}
		}
	}
	
	public void setThingCode(String thingCode){
		thingCodeTv.setText(thingCode);
	}
	
	public String getThingCode(){
		return thingCodeTv.getText().toString();
	}
	
	public String judgeEntry(){
		String proof = getProof();
		if(!StrUtil.isNotEmpty(proof)){
			return "请输入凭证日期！";
		}
		String account = getAccount();
		if(!StrUtil.isNotEmpty(account)){
			return "请输入记账日期！";
		}
		return null;
	}
	
	public String getProof(){
		return proofEt.getText().toString();
	}
	
	public String getAccount(){
		return accountEt.getText().toString();
	}
	
	public void setProof(String proof){
		if(StrUtil.isNotEmpty(proof)){
			proofEt.setText(proof);
		}
	}
	
	public void setAccount(String account){
		if(StrUtil.isNotEmpty(account)){
			accountEt.setText(account);
		}
	}
}
