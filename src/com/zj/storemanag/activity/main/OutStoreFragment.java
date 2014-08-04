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

public class OutStoreFragment extends Fragment {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.outstore_fragment, container, false);
        Button projectBtn = (Button) view.findViewById(R.id.outstore_project);
        Button projectPartBtn = (Button) view.findViewById(R.id.outstore_project_part);
        Button consignMidifyBtn = (Button) view.findViewById(R.id.outstore_consign_midify);
//        Button consignModifyCompleteBtn = (Button) view.findViewById(R.id.outstore_consign_midify_complete);
        Button costCenterBtn = (Button) view.findViewById(R.id.outstore_cost_center);
        Button guestNoOutBtn = (Button) view.findViewById(R.id.outstore_guest_no_outside);
        Button guestNoInBtn = (Button) view.findViewById(R.id.outstore_guest_no_inside);
        Button crossCompanyBtn = (Button) view.findViewById(R.id.outstore_cross_company);
        Button companyBtn = (Button) view.findViewById(R.id.outstore_company);
        Button maintenanceOrderBtn = (Button) view.findViewById(R.id.outstore_maintenance_order);
        Button internalFeedingBtn = (Button) view.findViewById(R.id.outstore_internal_feeding);
        projectBtn.setOnClickListener(new MyClick());
        projectPartBtn.setOnClickListener(new MyClick());
        consignMidifyBtn.setOnClickListener(new MyClick());
//        consignModifyCompleteBtn.setOnClickListener(new MyClick());
        costCenterBtn.setOnClickListener(new MyClick());
        guestNoOutBtn.setOnClickListener(new MyClick());
        guestNoInBtn.setOnClickListener(new MyClick());
        crossCompanyBtn.setOnClickListener(new MyClick());
        companyBtn.setOnClickListener(new MyClick());
        maintenanceOrderBtn.setOnClickListener(new MyClick());
        internalFeedingBtn.setOnClickListener(new MyClick());
        return view;
    }
    
    private class MyClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.outstore_project:
				//工技改处
				skipInfoActivity(ParamsUtil.project);
				break;
			case R.id.outstore_project_part:
				//工技改处-分
				skipInfoActivity(ParamsUtil.project_part);
				break;
			case R.id.outstore_consign_midify:
				//寄售备出-修
				skipInfoActivity(ParamsUtil.consign_midify);
				break;
//			case R.id.outstore_consign_midify_complete:
//				//寄售备出-成
//				skipInfoActivity(ParamsUtil.consign_midify_complete);
//				break;
			case R.id.outstore_maintenance_order:
				//按维修订单
				skipInfoActivity(ParamsUtil.maintenance_order);
				break;
			case R.id.outstore_cost_center:
				//按成本中心
				skipInfoActivity(ParamsUtil.cost_center);
				break;
			case R.id.outstore_guest_no_inside:
				//按客号发-内
				skipInfoActivity(ParamsUtil.guest_no_inside);
				break;
			case R.id.outstore_guest_no_outside:
				//按客号发-外
				skipInfoActivity(ParamsUtil.guest_no_outside);
				break;
			case R.id.outstore_cross_company:
				//跨公司移库
				skipInfoActivity(ParamsUtil.cross_company);
				break;
			case R.id.outstore_company:
				//同公司移库
				skipInfoActivity(ParamsUtil.company);
				break;
			case R.id.outstore_internal_feeding:
				//内部投料
				skipInfoActivity(ParamsUtil.internal_feeding);
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
