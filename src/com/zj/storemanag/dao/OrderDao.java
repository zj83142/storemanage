package com.zj.storemanag.dao;

import java.util.ArrayList;
import java.util.List;

import log.Log;

import android.content.Context;
import android.database.Cursor;

import com.zj.storemanag.db.BasicDaoImpl;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.TimeUtil;

/**
 * 采购订单:1
 * 维修订单：2
 * wbs元素：3
 * 物料号：4
 * 成本中心：5
 * 客户号：6
 * 供应商： 7
 * @author Administrator
 *
 */
public class OrderDao extends BasicDaoImpl {

	public static OrderDao instance;

	public OrderDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static OrderDao getInstance(Context context) {
		if (instance == null) {
			instance = new OrderDao(context);
		}
		return instance;
	}

	/**
	 * 保存订单号
	 * 
	 * @param orderNum
	 *            订单号
	 *   @param memo 1:采购订单，  2：维修订单
	 * @return 保存结果
	 */
	public boolean saveOrder(String orderNum, String memo) {
		if(!StrUtil.isNotEmpty(orderNum)){
			return false;
		}
		boolean isSave = false;
		Cursor cursor = null;
		try {
			// orderTemp 字段：_id, orderNum, time, memo
			String sql = "select _id from orderTemp where orderNum=? and memo=?";
			cursor = sqlDataBase.rawQuery(sql, new String[] { orderNum, memo });
			int count = cursor.getCount();
			if (count == 0) {
				sql = "insert into orderTemp(orderNum, time, memo)values(?,?,?)";
				sqlDataBase.execSQL(sql,
						new Object[] { orderNum, TimeUtil.getSystemTimeStr(), memo });
			}else{
				//更新时间
				sql = "update orderTemp set time=? where orderNum=? and memo=?";
				sqlDataBase.execSQL(sql, new Object[]{TimeUtil.getSystemTimeStr(), orderNum, memo});
			}
			isSave = true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "保存订单号信息报错", e);
		}finally{
			closeCursor(cursor);
		}
		return isSave;
	}

	public List<String> findOrderLs(String type) {
		Cursor cursor = null;
		List<String> list = new ArrayList<String>();
		try {
			String currentTime = TimeUtil.getSystemTimeStr();
			String lastWeekTime = TimeUtil.lastWeek();
			String sql = "select orderNum from orderTemp where memo='"+type+"' and time between '"
					+ lastWeekTime + "' and '" + currentTime
					+ "' GROUP BY orderNum ORDER BY time";
			cursor = sqlDataBase.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				String orderNum = StrUtil.filterStr(cursor.getString(cursor.getColumnIndex("orderNum")));
				if(StrUtil.isNotEmpty(orderNum)){
					list.add(orderNum);
				}
			}
			//删除过期的订单号
			sql = "select _id from orderTemp where time < " + lastWeekTime;
			cursor = sqlDataBase.rawQuery(sql, null);
			while(cursor.moveToNext()){
				String id = cursor.getString(cursor.getColumnIndex("_id"));
				sql = "delete from orderTemp where _id=?";
				sqlDataBase.execSQL(sql, new Object[] { id });
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "查询订单号别表报错", e);
		}finally{
			closeCursor(cursor);
		}
		return list;
	}

}
