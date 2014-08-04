package com.zj.storemanag.commen;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import log.Log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat")
public class CrashHandler implements Thread.UncaughtExceptionHandler {

	// 系统规模人的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler 实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// 程序的context对象
	private Context mContext;

	/**
	 * 保证只有一个CrashHandler实例
	 */
	private CrashHandler() {
	}

	/**
	 * 获取CrashHandler实例，单例模式
	 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 */
	public void init(Context context) {
		this.mContext = context;
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				Log.i("zj","-----uncaughtException--->" + e.toString());
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}

	}

	/**
	 * 自定义错误处理，收集错误信息 发送错误报告等操作
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息；否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			public void run() {
				Looper.prepare();
				Looper.loop();
			}
		}.start();
		Log.i("zj","--获取错误的信息---getErrorInfo--->" + getErrorInfo(ex));
		return true;
	}

	/**
	 * 获取错误的信息
	 * 
	 * @param arg1
	 * @return
	 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		Log.i("zj","--获取错误的信息---getErrorInfo--->" + error);
		return error;
	}

}
