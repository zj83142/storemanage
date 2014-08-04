package com.zj.storemanag.dao;

import java.util.ArrayList;
import java.util.List;

import log.Log;

import android.content.Context;
import android.database.Cursor;

import com.zj.storemanag.bean.Goods;
import com.zj.storemanag.bean.RFIDinfo;
import com.zj.storemanag.db.BasicDaoImpl;
import com.zj.storemanag.util.StrUtil;

/**
 * // 获取采购订单（清货）清单 eq_temp
 * VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified
 * ,unit,IsSelected,IsSelectedSpecified
 * ,factory,RFID,store,TotalCount,TotalCountSpecified
 * ,otherField1,otherField2,state,isInit
 * 
 * @author zhoujing 2014-5-28 下午2:06:42
 */
public class GoodsDao extends BasicDaoImpl {

	public static GoodsDao instance;

	public GoodsDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static GoodsDao getInstance(Context context) {
		if (instance == null) {
			instance = new GoodsDao(context);
		}
		return instance;
	}

	/**
	 * 获取采购订单（清货）清单eq_temp
	 * VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified
	 * ,unit
	 * ,IsSelected,IsSelectedSpecified,factory,RFID
	 * ,store
	 * ,TotalCount,TotalCountSpecified,otherField1,otherField2,state
	 * 
	 * @param info
	 * @return
	 */
	public boolean save(Goods good) {
		try {
			String sql = null;
			if (good == null)
				return false;
			// 查询数据是否存在，如果存在更新，不存在保存
			sql = "select _id from eq_temp where MATERIAL=? and factory=? and store=?";
			Cursor cursor = sqlDataBase.rawQuery(
					sql,
					new String[] { StrUtil.filterStr(good.getMATERIAL()),
							StrUtil.filterStr(good.getFactory()),
							StrUtil.filterStr(good.getStore()) });
			if (cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex("_id"));
				good.setId(id);
				return updateGoods(good, true);
			}
			String sqlStr = "insert into eq_temp(VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified,unit,IsSelected,IsSelectedSpecified,factory,RFID,store,TotalCount,TotalCountSpecified,otherField1,otherField2,state,isInit, updateTime)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			sqlDataBase.execSQL(sqlStr, good.getSaveParams());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "保存用户信息报错：" + e.getMessage());
		}
		return false;
	}
	
	/**
	 * 根据物料号、工厂号、库位号 返回good
	 * @param materail
	 * @param factory
	 * @param store
	 * @return
	 */
	public Goods getRfidInfoByOrder(String materail, String factory, String store){
		String sql = null;
		try {
			sql = "select _id from eq_temp where MATERIAL=? and factory=? and store=?";
			Cursor cursor = sqlDataBase.rawQuery(
					sql,
					new String[] { StrUtil.filterStr(materail),
							StrUtil.filterStr(factory),
							StrUtil.filterStr(store) });
			while (cursor.moveToNext()) {
				Goods temp = new Goods();
				temp.setId(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("_id"))));
				temp.setMATERIAL(StrUtil.filterStr(materail));
				temp.setMESSAGE(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("MESSAGE"))));
				temp.setENTRY_QNT(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("ENTRY_QNT"))));
				temp.setUnit(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("unit"))));
				temp.setFactory(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("factory"))));
				temp.setRFID(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("RFID"))));
				temp.setStore(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("store"))));
				temp.setState(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("state"))));
				return temp;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("zj", "getRfidInfoByOrder查询物料信息报错", e);
		}
		return null;
	}
	
	/**
	 * 判断查询到的数据是否存在，如果存在返回已存在数据，如果不存在返回传入的原始数据
	 * @param rfidInfo
	 * @return
	 */
	public Goods judgeExist(RFIDinfo rfidInfo) {
		String materail = StrUtil.filterStr(rfidInfo.getPR_MATERIAL());
		String factory = StrUtil.filterStr(rfidInfo.getFactory());
		String store = StrUtil.filterStr(rfidInfo.getStore());
		if(StrUtil.isNotEmpty(materail) && StrUtil.isNotEmpty(factory) && StrUtil.isNotEmpty(store)){
			Goods good = getRfidInfoByOrder(rfidInfo.getPR_MATERIAL(), rfidInfo.getFactory(), rfidInfo.getStore());
			if(good != null){
				  return good;
			}
		}
		return null;
	}

	/**
	 * 获取采购订单（清货）清单eq_temp
	 * VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified
	 * ,unitName,unitValue
	 * ,IsSelected,IsSelectedSpecified,planName,planValue,RFID
	 * ,stgeLocName,stgeLocVaule
	 * ,TotalCount,TotalCountSpecified,otherField1,otherField2,state
	 * 
	 * @param info
	 * @return
	 */
	public List<Goods> findLs() {
		List<Goods> list = new ArrayList<Goods>();
		Cursor cursor = null;
		try {
			String sqlStr = "SELECT * from eq_temp order by updateTime desc";
			cursor = sqlDataBase.rawQuery(sqlStr, null);
			while (cursor.moveToNext()) {
				Goods temp = getGoodsByCursor(cursor);
				if (temp != null) {
					list.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "查询临时数据：" + e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return list;
	}

	private Goods getGoodsByCursor(Cursor cursor) {
		Goods temp = new Goods();
		temp.setId(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("_id"))));
		temp.setVEND_NAME(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("VEND_NAME"))));
		temp.setMATERIAL(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("MATERIAL"))));
		temp.setMESSAGE(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("MESSAGE"))));
		temp.setENTRY_QNT(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("ENTRY_QNT"))));
		temp.setENTRY_QNTSpecified(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("ENTRY_QNTSpecified"))));
		temp.setUnit(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("unit"))));
		temp.setIsSelected(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("IsSelected"))));
		temp.setIsSelectedSpecified(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("IsSelectedSpecified"))));
		temp.setFactory(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("factory"))));
		temp.setRFID(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("RFID"))));
		temp.setStore(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("store"))));
		temp.setTotalCount(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("TotalCount"))));
		temp.setTotalCountSpecified(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("TotalCountSpecified"))));
		temp.setOtherField1(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("otherField1"))));
		temp.setOtherField2(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("otherField2"))));
		temp.setState(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("state"))));
		temp.setProof(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("proof"))));
		temp.setYear(StrUtil.filterStr(cursor.getString(cursor
				.getColumnIndex("year"))));
		return temp;
	}

	public boolean updateStateAll(String proof, String year) {
		try {
			String sql = "update eq_temp set state=?, proof=?, year=?";
			sqlDataBase.execSQL(sql, new Object[] { "2", proof, year });
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean delectData() {
		try {
			String sql = "DELETE FROM eq_temp";
			sqlDataBase.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "删除临时数据：" + e.getMessage());
		}
		return false;
	}

//	public boolean updateGoodsLs(List<Goods> list) {
//		for (Goods temp : list) {
//			boolean isUpdate = updateGoods(temp, false);
//			if (!isUpdate)
//				return false;
//		}
//		return true;
//	}

	/** 更新temps数据 */
	public boolean updateGoods(Goods good, boolean sort) {
		try {
			String sql = null;
			String id = good.getId();
			if (StrUtil.isNotEmpty(id)) {
				if(sort){
					sql = "UPDATE eq_temp SET VEND_NAME=?,MATERIAL=?,MESSAGE=?,ENTRY_QNT=?,ENTRY_QNTSpecified=?,unit=?,IsSelected=?,IsSelectedSpecified=?,factory=?,RFID=?,store=?,TotalCount=?,TotalCountSpecified=?,otherField1=?,otherField2=?,state=?, updateTime=? WHERE _id=?";
					sqlDataBase.execSQL(sql, good.getUpdateParams(sort));
				}else{
					sql = "UPDATE eq_temp SET VEND_NAME=?,MATERIAL=?,MESSAGE=?,ENTRY_QNT=?,ENTRY_QNTSpecified=?,unit=?,IsSelected=?,IsSelectedSpecified=?,factory=?,RFID=?,store=?,TotalCount=?,TotalCountSpecified=?,otherField1=?,otherField2=?,state=? WHERE _id=?";
					sqlDataBase.execSQL(sql, good.getUpdateParams(sort));
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "修改temps数据", e);
		}
		return false;
	}

	public boolean updateGoodsState(String id, String state) {
		try {
			if (StrUtil.isNotEmpty(id)) {
				String sql = "UPDATE eq_temp set state=? WHERE _id=?";
				sqlDataBase.execSQL(sql, new Object[] { state, id });
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "修改temps数据", e);
		}
		return false;
	}

	/** 更新temps数据 */
	public boolean updateGoodNumber(String id, String number) {
		String sql = null;
		try {
			if (StrUtil.isNotEmpty(id)) {
				if(StrUtil.filterStr(number).equalsIgnoreCase("0")){
					sql = "UPDATE eq_temp set ENTRY_QNT=?, state='0' WHERE _id=?";
					sqlDataBase.execSQL(sql, new Object[] { number, id });
				}else{
					sql = "UPDATE eq_temp set ENTRY_QNT=? WHERE _id=?";
					sqlDataBase.execSQL(sql, new Object[] { number, id });
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "修改temps数据", e);
		}
		return false;
	}

	public void delectDataById(String id) {
		String sql = "DELETE from eq_temp WHERE _id=?";
		sqlDataBase.execSQL(sql, new Object[] { id });
	}

	public List<Goods> getGoodLsByState(String state) {
		List<Goods> list = new ArrayList<Goods>();
		Cursor cursor = null;
		try {
//			delectDataBySelect();
			String sqlStr = "SELECT * from eq_temp where state=? order by updateTime asc";
			cursor = sqlDataBase.rawQuery(sqlStr, new String[] { state });
			while (cursor.moveToNext()) {
				Goods temp = getGoodsByCursor(cursor);
				if (temp != null) {
					list.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "查询临时数据：" + e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return list;
	}
	
	/**查询是否有未处理数据, 返回true表示没有未出入库数据 */
	public boolean getGoodLsExit() {
		Cursor cursor = null;
		try {
//			delectDataBySelect();
			String sqlStr = "SELECT * from eq_temp where state='1'";
			cursor = sqlDataBase.rawQuery(sqlStr, null);
			int count = cursor.getCount();
			if(count == 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "查询临时数据：" + e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return false;
	}

	/**根据rfid查询设备信息*/
	public Goods findGoodByRFID(String rfid) {
		Goods good = null;
		Cursor cursor = null;
		try {
//			delectDataBySelect();
			String sqlStr = "SELECT * from eq_temp where RFID=?";
			cursor = sqlDataBase.rawQuery(sqlStr, new String[] { rfid });
			while (cursor.moveToNext()) {
				return good = getGoodsByCursor(cursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "查询临时数据：" + e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return good;
	}

	/**删除旧数据，保存为新数据。便于排序显示*/
	public void maintenanceOrderUpdate(Goods good) {
		// TODO Auto-generated method stub
		String sql = "delete from eq_temp where _id=?";
		sqlDataBase.execSQL(sql, new Object[]{good.getId()});
		boolean isSave = save(good);
		Log.i("zj", "维修订单数据更新结果："+isSave);
	}

	/**保存或更新good，如果根据物料号、工厂号、库位号查询到数据，更新传入的*/
	public void saveOrUpdate(Goods good) {
		// TODO Auto-generated method stub
		try {
			Goods findGood = findGoodByOrder(good.getMATERIAL(), good.getFactory(), good.getStore());
			if(findGood != null){
				good.setENTRY_QNT(findGood.getENTRY_QNT());
				good.setState(findGood.getState());
				good.setIsInit(findGood.getIsInit());
				String sql = "delete from eq_temp where _id=?";
				sqlDataBase.execSQL(sql, new Object[]{good.getId()});
			}
			boolean isSave = save(good);
			Log.i("zj", "维修订单数据更新结果："+isSave);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	private Goods findGoodByOrder(String material, String factory, String store) throws Exception{
		// TODO Auto-generated method stub
		String sql = "select * from eq_temp where MATERIAL=? and factory=? and store=?";
		Cursor cursor = sqlDataBase.rawQuery(sql, new String[] {material,factory,	store});
		if (cursor.moveToNext()) {
			return getGoodsByCursor(cursor);
		}
		return null;
	}

}
