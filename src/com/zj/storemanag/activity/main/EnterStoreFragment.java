package com.zj.storemanag.activity.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.zj.storemanag.R;
import com.zj.storemanag.activity.inorout.StockOperationActivity;
import com.zj.storemanag.commen.ParamsUtil;

public class EnterStoreFragment extends Fragment {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enterstore_fragment, container, false);
        Button externalBtn = (Button)view.findViewById(R.id.enter_external);
        Button interiorBtn = (Button)view.findViewById(R.id.enter_interior);
        externalBtn.setOnClickListener(new MyClick());
        interiorBtn.setOnClickListener(new MyClick());
        return view;
    }
    
    private class MyClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.enter_external:
				//外部备件入库
				skipInfoActivity(ParamsUtil.external);
				break;
			case R.id.enter_interior:
				//内部备件入库
				skipInfoActivity(ParamsUtil.interior);
				break;
			default:
				break;
			}
		}
    }
    
    private void skipInfoActivity(int flag) {
		Intent it = new Intent(getActivity(), StockOperationActivity.class);
		it.putExtra("flag", flag);
		startActivity(it);
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
