package com.zj.storemanag.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import log.Log;
import android.app.ProgressDialog;
import android.os.Environment;

import com.zj.storemanag.bean.CVersion;

public class DownLoadVersionService 
{
	private ProgressDialog myDialog;// 版本更新Dialog;
	// 得到当前外部存储设备的目录
	String SD_PATH = Environment.getExternalStorageDirectory() + File.separator;

	public Object downLoadVersion(CVersion cVersion, ProgressDialog myDialog) {
		this.myDialog = myDialog;
		myDialog.setCancelable(false);
		boolean isFinish = downLoadNewVersion(cVersion);
		return isFinish;
	}

	public boolean downLoadNewVersion(CVersion obj) {
		if (obj == null) {
			Log.i("zj","------版本下载时解析出的versioninfo为空------>");
			return false;
		}
		boolean result = false;
		String urlStr = obj.getUrlStr();

		InputStream inputStram = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(urlStr)
					.openConnection();
			if (myDialog != null) {
				myDialog.setMax(conn.getContentLength());
			}
			inputStram = conn.getInputStream();
			File file = new File(SD_PATH, "Download");
			if (!file.exists()) {
				file.mkdirs();
			}
			File apkFile = new File(file.getAbsoluteFile(),
					obj.getVersionName() + ".apk");
			// if (apkFile.exists()) {
			// isDownload = NO_UPDATE;
			// return false;
			// }

			// 创建文件夹下载....apk;
			boolean isDown = versionWriteToSdCardFromInputStream(apkFile,
					inputStram, myDialog);
			if (!isDown) {
				result = false;
			} else {
				result = true;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.i("zj","downLoadNewVersion:" + e.toString());
			result = false;
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("zj","downLoadNewVersion:" + e.toString());
			result = false;
		}
		return result;
	}

	public boolean versionWriteToSdCardFromInputStream(File file,
			InputStream input, ProgressDialog myDialog) {
		Log.i("zj","-------apk---->" + file.getAbsolutePath());
		OutputStream out = null;
		try {
			byte[] temp = new byte[1024 * 4];
			out = new FileOutputStream(file);
			int len = 0;
			int total = 0;
			while ((len = input.read(temp)) != -1) {
				out.write(temp, 0, len);
				total += len;
				myDialog.setProgress(total);
			}
			out.flush();
			myDialog.cancel();
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("zj","versionWriteToSdCardFromInputStream:" + e.toString());
			return false;
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

}
