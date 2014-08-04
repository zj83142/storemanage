package com.zj.storemanag.dao;

import java.util.ArrayList;
import java.util.List;

import log.Log;

import android.content.Context;
import android.database.Cursor;

import com.zj.storemanag.bean.BaseInfo;
import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.db.BasicDaoImpl;
import com.zj.storemanag.util.StrUtil;

public class BaseInfoDao extends BasicDaoImpl {

	public static BaseInfoDao instance;

	public BaseInfoDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static BaseInfoDao getInstance(Context context) {
		if (instance == null) {
			instance = new BaseInfoDao(context);
		}
		return instance;
	}

	public boolean save(BaseInfo info) {
		try {
			if (info == null)
				return false;

			String sqlStr = "insert into baseInfo(code,name,type,memo)values(?,?,?,?)";
			sqlDataBase.execSQL(sqlStr, info.getSaveParams());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "保存用户信息报错：" + e.getMessage());
		}
		return false;
	}

	/**
	 * 根据类型获取数据
	 * 
	 * @param type
	 *            type=1:库位 type=2:工厂 3：计量单位 4：成本中心
	 * @return
	 */
	public List<PopItem> findLsByType(String type) {
		List<PopItem> list = new ArrayList<PopItem>();
		Cursor cursor = null;
		try {
			String sqlStr = "SELECT * from baseInfo WHERE type=?";
			cursor = sqlDataBase.rawQuery(sqlStr, new String[] { type });
			while (cursor.moveToNext()) {
				PopItem temp = new PopItem();
				temp.setId(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("_id"))));
				temp.setType(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("code"))));
				// temp.setName(StrUtil.filterStr(cursor.getString(cursor.getColumnIndex("name"))));
				temp.setMemo(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("memo"))));
				String code = StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("code")));
				String name = StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("name")));
				if (StrUtil.isNotEmpty(code) && !type.equalsIgnoreCase("4")) {
					temp.setName(code + "_" + name);
				} else {
					if(type.equalsIgnoreCase("4")){
						temp.setName(name);
					}
				}
				list.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "根据用户id查询操作日志列表报错：" + e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return list;
	}

	/**
	 * 根据工厂code查询库位信息
	 * 
	 * @param 工厂code
	 * @return
	 */
	public List<PopItem> findStoreLs(String factory) {
		List<PopItem> list = new ArrayList<PopItem>();
		Cursor cursor = null;
		try {
			String sqlStr = "SELECT * from baseInfo WHERE type=? and memo =?";
			cursor = sqlDataBase
					.rawQuery(sqlStr, new String[] { "2", factory });
			while (cursor.moveToNext()) {
				PopItem temp = new PopItem();
				temp.setId(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("_id"))));
				temp.setType(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("code"))));
				// temp.setName(StrUtil.filterStr(cursor.getString(cursor.getColumnIndex("name"))));
				temp.setMemo(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("memo"))));
				String code = StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("code")));
				String name = StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("name")));
				if (StrUtil.isNotEmpty(code)) {
					temp.setName(code + "_" + name);
				} else {
					temp.setName(name);
				}
				list.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "根据用户id查询操作日志列表报错：" + e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return list;
	}

	/**
	 * 判断基础数据是否存在，如果type == null 查询的是工厂、库位、计量单位 ，如果type != null 查询的是成本中心数据
	 * 
	 * @return
	 */
	public boolean judgeBasicExist(String type) {
		Cursor cursor = null;
		try {
			String sqlStr = "select * from baseInfo";
			if (StrUtil.isNotEmpty(type)) {
				sqlStr += " where type=?";
				cursor = sqlDataBase.rawQuery(sqlStr, new String[] { type });
			} else {
				cursor = sqlDataBase.rawQuery(sqlStr, null);
			}
			int count = cursor.getCount();
			Log.i("zj", "查询到的基础信息条数：" + count);
			if (count > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("error", "根据用户id查询操作日志列表报错：" + e.getMessage());
		} finally {
			closeCursor(cursor);
		}
		return false;
	}

	/** 清除工厂库位信息 */
	public boolean delectData() {
		try {
			String sql = "DELETE FROM baseInfo";
			sqlDataBase.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "删除基础数据错误：" + e.getMessage());
		}
		return false;
	}

	/**
	 * 查询名称
	 * 
	 * @param value
	 * @param type
	 *            类型
	 * @return code,name,type,memo
	 */
	public String getNameByCode(String code, String type) {
		String name = "";
		String sql = "select name from baseInfo where code=? and type=?";
		Cursor cursor = null;
		try {
			cursor = sqlDataBase.rawQuery(sql, new String[] { code, type });
			while (cursor.moveToNext()) {
				name = StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("name")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "查询编码对应的名称", e);
		}finally{
			closeCursor(cursor);
		}
		return name;
	}
	
	/**根据库位编码和工厂编码查询库位名称*/
	public String getStoreName(String code, String factory) {
		String name = "";
		String sql = "select name from baseInfo where code=? and type='2' and memo=?";
		Cursor cursor = null;
		try {
			cursor = sqlDataBase.rawQuery(sql, new String[] { code, factory });
			while (cursor.moveToNext()) {
				name = StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("name")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "查询编码对应的名称", e);
		}finally{
			closeCursor(cursor);
		}
		return name;
	}

	public String getValueByName(String name, String type) {
		String sql = "select name from baseInfo where name=? and type=?";
		Cursor cursor = null;
		try {
			cursor = sqlDataBase.rawQuery(sql, new String[] { name, type });
			while (cursor.moveToNext()) {
				String value = cursor.getString(cursor.getColumnIndex("value"));
				return StrUtil.filterStr(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "查询编码对应的名称", e);
		}
		return null;
	}

}
