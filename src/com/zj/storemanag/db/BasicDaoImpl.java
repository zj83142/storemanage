package com.zj.storemanag.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BasicDaoImpl implements IBasicDao{
	public DBOpenHelper dbOpenHelper;
	public SQLiteDatabase sqlDataBase;  //数据库操作类
	
	public BasicDaoImpl(Context context){
		dbOpenHelper = DBOpenHelper.getInstance(context);
		sqlDataBase= dbOpenHelper.getWritableDatabase();
	}
	
	/**
	 * 关闭cursor
	 * @param c
	 */
	public void closeCursor(Cursor c) {
		if (c != null && !c.isClosed()) {
			c.close();
			c = null;
		}
	}

	
}
