package com.zj.storemanag.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.zj.storemanag.bean.ImgBean;
import com.zj.storemanag.db.BasicDaoImpl;
import com.zj.storemanag.util.DelFileUtil;
import com.zj.storemanag.util.StrUtil;

public class ImgDao extends BasicDaoImpl {

	public static ImgDao instance;

	public ImgDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static ImgDao getInstance(Context context) {
		if (instance == null) {
			instance = new ImgDao(context);
		}
		return instance;
	}

	public boolean save(ImgBean bean) {
		// 先查询该物料号的图片是否存在，如果存在更新图片路径，且删除原图片
		boolean isSave = false;
		String sql = null;
		Cursor cursor = null;
		try {
			sql = "select _id, filepath from img where material=? and factory=? and store=?";
			cursor = sqlDataBase.rawQuery(
					sql,
					new String[] { bean.getMaterial(), bean.getFactory(),
							bean.getStore() });
			int count = cursor.getCount();
			if (count == 0) {
				// String sqlStr =
				// "insert into eq_temp(VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified,unit,IsSelected,IsSelectedSpecified,factory,RFID,store,TotalCount,TotalCountSpecified,otherField1,otherField2,state,isInit)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				// _id, material, factory, store, path, userId, state
				sql = "insert into img(material, factory, store, filepath, userId, state, cw, desp, unit)values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				sqlDataBase.execSQL(sql, bean.getInsertParams());
			} else {
				while (cursor.moveToNext()) {
					String id = cursor.getString(cursor.getColumnIndex("_id"));
					String path = cursor.getString(cursor
							.getColumnIndex("filepath"));
					isSave = updateImgById(id, path, bean);
				}
			}
			isSave = true;
		} catch (Exception e) {
			e.printStackTrace();
			isSave = false;
		} finally {
			closeCursor(cursor);
		}
		return isSave;
	}

	private boolean updateImgById(String id, String path, ImgBean bean) {
		try {
			String sql = "update img set filepath=?, userId=? where _id=?";
			sqlDataBase.execSQL(sql,
					new Object[] { bean.getPath(), bean.getUserId(), id });
			DelFileUtil.delFileByPath(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** 根据id删除图片 */
	public void delImgBy(String id) {
		if (StrUtil.isNotEmpty(id)) {
			String sql = "delete from img where _id=?";
			sqlDataBase.execSQL(sql, new Object[] { id });
		}
	}

	public String getMaterialImgPath(String material, String factory,
			String store) {
		String sql = null;
		Cursor cursor = null;
		try {
			sql = "select _id, filepath from img where material=? and factory=? and store=?";
			cursor = sqlDataBase.rawQuery(sql, new String[] { material,
					factory, store });
			while (cursor.moveToNext()) {
				String path = StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("filepath")));
				return path;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeCursor(cursor);
		}
		return "";
	}

	/** 更新物料图片状态为1，可以上传 */
	public void updateImgState(String material, String factory, String store) {
		String sql = null;
		Cursor cursor = null;
		try {
			sql = "select _id, filepath from img where material=? and factory=? and store=?";
			cursor = sqlDataBase.rawQuery(sql, new String[] { material,
					factory, store });
			while (cursor.moveToNext()) {
				String id = StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("_id")));
				String path = StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("filepath")));
				if (StrUtil.isNotEmpty(path)) {
					File file = new File(path);
					if (file.exists() && file.length() > 0) {
						sql = "update img set state='1' where _id=?";
						sqlDataBase.execSQL(sql, new Object[] { id });
					} else {
						delImgBy(id);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeCursor(cursor);
		}
	}

	public List<ImgBean> findImgLs() {
		String sql = null;
		Cursor cursor = null;
		List<ImgBean> list = new ArrayList<ImgBean>();
		try {
			sql = "select * from img where state='1'";
			cursor = sqlDataBase.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				//material, factory, store, filepath, userId, state, cw, desp, unit
				ImgBean imgBean = new ImgBean();
				imgBean.setId(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("_id"))));
				imgBean.setMaterial(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("material"))));
				imgBean.setFactory(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("factory"))));
				imgBean.setStore(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("store"))));
				imgBean.setPath(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("filepath"))));
				imgBean.setUserId(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("userId"))));
				imgBean.setState(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("cw"))));
				imgBean.setCw(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("state"))));
				imgBean.setDesp(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("desp"))));
				imgBean.setUnit(StrUtil.filterStr(cursor.getString(cursor
						.getColumnIndex("unit"))));
				// _id, material, factory, store, path, userId, state
				list.add(imgBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeCursor(cursor);
		}
		return list;
	}
}
