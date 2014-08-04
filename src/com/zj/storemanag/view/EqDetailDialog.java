package com.zj.storemanag.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.zj.storemanag.R;
import com.zj.storemanag.bean.Goods;

public class EqDetailDialog extends Dialog {

	private Context context;
	private Handler handler;
	private Goods goods;
	private Button modifyBtn;
	private Button closeBtn;

	public EqDetailDialog(Context context, int theme, Goods goods,
			Handler handler) {
		super(context, theme);
		this.context = context;
		this.handler = handler;
		this.goods = goods;
		this.handler = handler;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eq_detail);
		initView();
	}

	private void initView() {
		modifyBtn = (Button) findViewById(R.id.eq_detail_update_btn);
		closeBtn = (Button) findViewById(R.id.eq_detail_close_btn);
		modifyBtn.setOnClickListener(new MyClick());
		closeBtn.setOnClickListener(new MyClick());
	}

	private final class MyClick implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.eq_detail_update_btn:
				// 修改

				break;
			case R.id.eq_detail_close_btn:
				EqDetailDialog.this.cancel();
				break;
			default:
				break;
			}
		}
	}
}
