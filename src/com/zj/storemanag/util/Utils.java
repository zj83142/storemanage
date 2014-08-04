package com.zj.storemanag.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import log.Log;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;

public class Utils {
	public static final String root = Environment.getExternalStorageDirectory()+ "/store_manage";
	
	public static final String EQPICTUREPATH = root + "/photo";
	public static final String ZX_PHOTO = root + "/zx_photo";

	// 根据传入的后缀名和系统时间创建文件
	// public static File createFile(String path, String str) {
	// File file = new File(path, getRandomStr() + str);
	// return file;
	// }

	// 根据传入的后缀名和系统时间创建文件
	public static File createFile(String path, String str) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyyMMddHHmmss");
			String date = simpleDateFormat.format(new Date());
			File file = new File(path, date + str);
			System.out.println("创建文件，文件绝对路径：" + file.getAbsolutePath());
			if(!file.exists()){
				boolean isCreate = file.createNewFile();
				if(isCreate){
					return file;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("zj", "创建问价出错："+e.getMessage());
		}
		return null;
	}

	public static void setCurrentTime(String time) {
		Date date = strFormatToDate(time);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		long when = c.getTimeInMillis();
		if (when / 1000 < Integer.MAX_VALUE) {
			// 最关键的一句话
			SystemClock.setCurrentTimeMillis(when);
		}
	}

	public static Date strFormatToDate(String dateMsg) {
		Date res = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (dateMsg != null) {
				if (dateMsg.length() == 10) {
					res = sdf.parse(dateMsg + " 00:00:00");
				} else if (dateMsg.length() == 13) {
					res = sdf.parse(dateMsg + ":00:00");
				} else if (dateMsg.length() == 16) {
					res = sdf.parse(dateMsg + ":00");
				} else {
					res = sdf.parse(dateMsg);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String getRandomStr() {
		// 时间
		Calendar calendar = Calendar.getInstance();
		Date myDate = calendar.getTime();
		Long lon = myDate.getTime();
		// 随机数字
		Long temp = Math.round(Math.random() * 89999 + 10000);
		return String.valueOf(lon) + String.valueOf(temp);
	}

	/** 获取当前系统时间 */
	public static String getSystemDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		// 获取当前时间
		return formatter.format(curDate);
	}

	public static String getSystemDate2() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());
		// 获取当前时间
		return formatter.format(curDate);
	}

	// 判断SDcard状态
	public static boolean judgeSDcardState() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	// 创文件保存路径
	public static void createFiledir() {
		createFileDirByName(root);
		createFileDirByName(EQPICTUREPATH);
		createFileDirByName(ZX_PHOTO);
	}

	private static void createFileDirByName(String fileName) {
		File fileDir = new File(fileName);
		if (!fileDir.exists())
			fileDir.mkdirs();
	}

	/**
	 * Bitmap 保存为文件  保存二维码图片
	 * 
	 * @param bmp
	 * @param filename
	 * @return
	 */
	public static String saveBitmapToFile(Bitmap bmp) {
		String fileName = null;
		CompressFormat format = Bitmap.CompressFormat.JPEG;

		File file = createFile(ZX_PHOTO, ".jpg");
		if(file != null){
			System.out.println("-------saveBitmapToFile-------->"
					+ file.getAbsolutePath());
			int quality = 80;
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
				boolean isResult = bmp.compress(format, quality, stream);
				if (isResult) {
					fileName = file.getAbsolutePath();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// return bmp.compress(format, quality, stream);
			Log.i("zj","保存的抓拍照片名称：" + fileName);
			return fileName;
		}else{
			return null;
		}
	}

	// 得到sdcard可用空间大小
	public int getSDcardStore() {
		String[] availale = null;
		if (!judgeSDcardState())
			return 0;
		File path = Environment.getExternalStorageDirectory();
		// 取得sdcard文件路径
		StatFs statfs = new StatFs(path.getPath());
		// 获取block的SIZE
		long blocSize = statfs.getBlockSize();
		long availaBlock = statfs.getAvailableBlocks();
		availale = filesize(availaBlock * blocSize);
		return Integer.parseInt(availale[0].replace(",", ""));

	}

	// 返回数组，下标1代表大小，下标2代表单位 KB/MB
	String[] filesize(long size) {
		String str = "";
		if (size >= 1024) {
			str = "KB";
			size /= 1024;
			if (size >= 1024) {
				str = "MB";
				size /= 1024;
			}
		}
		DecimalFormat formatter = new DecimalFormat();
		formatter.setGroupingSize(3);
		String result[] = new String[2];
		result[0] = formatter.format(size);
		result[1] = str;
		return result;
	}

	private static String nodeInfos = "<%1$s>%2$s</%3$s>";

	public static String getNodeInfos() {
		return nodeInfos;
	}

	private static String nodeInfo = "<%1$s/>";

	public static String getNodeInfo() {
		return nodeInfo;
	}

	/**
	 * 根据字段，拼接该字段对应的xml节点
	 * 
	 * @param info
	 * @return
	 * 
	 *         例子一： 如：传入EP_NAME=计划 返回<EP_NAME>计划</EP_NAME>
	 * 
	 *         例子二： 如：传入EP_NAME= 返回<EP_NAME/>
	 */
	public static String manageNode(String info) {
		String result = "";
		if (info != null) {
			String[] infos = info.split("=");
			if (infos != null) {
				if (infos.length == 2) {
					result = String.format(Utils.getNodeInfos(), infos[0],
							infos[1], infos[0]);
				} else if (infos.length == 1) {
					result = String.format(Utils.getNodeInfo(), infos[0]);
				}
			}
		}
		return result;
	}
	public static String lastWeek() {
		Date date = new Date(System.currentTimeMillis());
		int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(date));
		int month = Integer.parseInt(new SimpleDateFormat("MM").format(date));
		int day = Integer.parseInt(new SimpleDateFormat("dd").format(date)) - 6;
		if (day < 1) {
			month -= 1;
			if (month == 0) {
				year -= 1;
				month = 12;
			}
			if (month == 4 || month == 6 || month == 9 || month == 11) {
				day = 30 + day;
			} else if (month == 1 || month == 3 || month == 5 || month == 7
					|| month == 8 || month == 10 || month == 12) {
				day = 31 + day;
			} else if (month == 2) {
				if (year == 0 || (year % 4 == 0 && year != 0))
					day = 29 + day;
				else
					day = 28 + day;
			}
		}
		String y = year + "";
		String m = "";
		String d = "";
		if (month < 10)
			m = "0" + month;
		else
			m = month + "";
		if (day < 10)
			d = "0" + day;
		else
			d = day + "";
		return y + "-" + m + "-" + d;
	}

	/**判断照片是否存在*/
	public static boolean judgePhotoIsExist(String path) {
		File file = new File(EQPICTUREPATH,path);
		return file.exists();
	}

}
