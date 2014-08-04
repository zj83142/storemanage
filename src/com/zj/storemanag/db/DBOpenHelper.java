package com.zj.storemanag.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	// private static final String NAME = "eq.db";

	public static String NAME = "store.db";
	public static int VERSION = 1;
	private static DBOpenHelper instance = null;

	public synchronized static DBOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DBOpenHelper(context);
		}
		return instance;
	}

	public DBOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	String createUserTable = "create table users(_id integer primary key autoincrement, userId, loginName, loginPwd, loginPwdMd5, loginTime)";
	/**
	 * 创建用户信息表 表名： users 字段：_id, userId, loginName, loginPwd, loginPwdMd5,
	 * loginTime
	 * 
	 * @param db
	 */
	private void createUserTable(SQLiteDatabase db) {
		db.execSQL("drop table if exists users");
		db.execSQL(createUserTable);
	}

	private String createBaseInfoStr = "create table baseInfo(_id integer primary key autoincrement, code, name, type, memo)";
	/**
	 * 基础数据信息 表名： baseInfo 字段：_id, code, name, type, memo 备注： id, 编码, 名称，类型， 备注
	 * 
	 * @param db
	 */
	private void createBaseInfoTable(SQLiteDatabase db) {
		db.execSQL("drop table if exists baseInfo");
		db.execSQL(createBaseInfoStr);
	}

	// 获取采购订单（清货）清单eq_temp
	// VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified,unit,IsSelected,IsSelectedSpecified,factory,RFID,store,TotalCount,TotalCountSpecified,otherField1,otherField2,state,isInit
	private String createEqTempTableStr = "create table eq_temp(_id integer primary key autoincrement,VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified,unit,IsSelected,IsSelectedSpecified,factory,RFID,store,TotalCount,TotalCountSpecified,otherField1,otherField2,state,isInit, proof, year, updateTime)";
	/**
	 * （清货）清单 eq_temp
	 *  表名： eq_temp
	 *  字段：_id, VEND_NAME,MATERIAL,MESSAGE,ENTRY_QNT,ENTRY_QNTSpecified,ENTRY_UOM,IsSelected,IsSelectedSpecified,PLANT,RFID,STGE_LOC,TotalCount,TotalCountSpecified,state
	 */
	private void createEQtempTable(SQLiteDatabase db) {
		db.execSQL("drop table if exists eq_temp");
		db.execSQL(createEqTempTableStr);
	}
	
	private String createOrderStr = "create table orderTemp(_id integer primary key autoincrement,orderNum, time, memo)";
	/**
	 * 保存订单号
	 * 表名：orderTemp
	 * 字段：_id, orderNum, time, memo
	 */
	private void createOrderTable(SQLiteDatabase db){
		db.execSQL("drop table if exists orderTemp");
		db.execSQL(createOrderStr);
	}
	
	private String createImgStr = "create table img(_id integer primary key autoincrement, material, factory, store, filepath, userId, state, cw, desp, unit)";
	/**
	 * 保存物料图片
	 * _id, material, factory, store, path, userId, state
	 */
	private void createImgTable(SQLiteDatabase db){
		db.execSQL("drop table if exists img");
		db.execSQL(createImgStr);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	private void createTables(SQLiteDatabase db) {
		createUserTable(db);
		createBaseInfoTable(db);
		createEQtempTable(db);
		createOrderTable(db);
		createImgTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		createTables(db);
	}

}
