package com.zj.storemanag.dao;

import java.util.ArrayList;
import java.util.List;

import log.Log;
import android.content.Context;
import android.database.Cursor;

import com.zj.storemanag.bean.PopItem;
import com.zj.storemanag.bean.User;
import com.zj.storemanag.db.BasicDaoImpl;
import com.zj.storemanag.util.StrUtil;

public class UserDao extends BasicDaoImpl{
	
	public static UserDao instance;

	public UserDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public static UserDao getInstance(Context context){
		if(instance == null){
			instance = new UserDao(context);
		}
		return instance;
	}

	public List<PopItem> getUserLs() {
		List<PopItem> list = new ArrayList<PopItem>();
		Cursor cursor = null;
		try {
			String sql = "SELECT * FROM users";
			cursor = sqlDataBase.rawQuery(sql, null);
			while(cursor.moveToNext()){
				PopItem item = new PopItem();
				item.setId(StrUtil.filterStr(cursor.getString(cursor.getColumnIndex("_id"))));
				item.setName(StrUtil.filterStr(cursor.getString(cursor.getColumnIndex("loginName"))));
				item.setMemo(StrUtil.filterStr(cursor.getString(cursor.getColumnIndex("loginPwd"))));
				item.setState(false);
				list.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "查询登录人信息",e);
		}finally{
			closeCursor(cursor);
		}
		return list;
	}

	/**保存登录信息*/
	public boolean saveUser(User user) {
		boolean isSave = false;
		Cursor cursor = null;
		try {
			//users 字段：_id, userId, loginName, loginPwd, loginPwdMd5,loginTime
			String sql = "select _id from users where loginName=? and loginPwd=?";
			cursor = sqlDataBase.rawQuery(sql, new String[]{user.getUserName(), user.getPwd()});
			int count = cursor.getCount();
			if(count == 0){
				//user_id, login_name, login_pwd, login_pwd_md5, login_time
				String sqlStr = "insert into users(userId, loginName, loginPwd, loginPwdMd5,loginTime)values(?, ?, ?, ?, ?)";
				sqlDataBase.execSQL(sqlStr, user.getSaveParams() );
			}
			isSave = true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "保存登录信息报错", e);
		}finally{
			closeCursor(cursor);
		}
		return isSave;
		
	}

}
