package com.zj.storemanag.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import log.Log;

import org.xmlpull.v1.XmlPullParser;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Xml;

import com.zj.storemanag.bean.ImgBean;
import com.zj.storemanag.commen.MyApplication;
import com.zj.storemanag.commen.ParamsUtil;
import com.zj.storemanag.dao.ImgDao;
import com.zj.storemanag.util.Base64;
import com.zj.storemanag.util.CompressImgUtil;
import com.zj.storemanag.util.DelFileUtil;
import com.zj.storemanag.util.MD5Util;
import com.zj.storemanag.util.RequestService;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.util.UrlUtil;
import com.zj.storemanag.util.ZipUtil;

public class UploadImgService extends Service{
	
	private MyApplication myApp;
	private ImgDao imgDao;
	private boolean threadDisable;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int INTERVALTIME = 1000;// 间隔时间
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("zj", "---------UploadImgService--------onCreate---");
		
		myApp = (MyApplication) getApplication();
		imgDao = ImgDao.getInstance(UploadImgService.this);
		threadDisable = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (threadDisable) {
					// 轮询处理
					Log.i("zj", "上传图片");
					threadDisable = true;
					List<ImgBean> list = imgDao.findImgLs();
					if (list.size() > 0) {
						Log.i("zj", "上传图片张数："+list.size());
						for (ImgBean temp : list) {
							String filePath = StrUtil.filterStr(temp.getPath());
							if (!StrUtil.isNotEmpty(filePath)) {
								imgDao.delImgBy(StrUtil.filterStr(temp.getId()));
								continue;
							}
							// 判断路径下文件是否存在
							File file = new File(filePath);
							if(file.exists() && file.length() > 0){
								new CompressImgUtil().compressImg(filePath);
								uploadPhotos(myApp.getUrl(), file, temp, imgDao);
							}else{
								imgDao.delImgBy(StrUtil.filterStr(temp.getId()));
							}
							
						}
					}
					try {
						Thread.sleep(INTERVALTIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}


	FileInputStream in = null;
	String param = null;
	String sign = null;

	public void uploadPhotos(String url, File file, ImgBean bean, ImgDao imgDao) {
		try {
			String methodName = "UploadImage";
			Log.i("zj", "上传图片路径："+ bean.getPath());
			in = new FileInputStream(file);
			int length = in.available();
			byte[] buf = new byte[length];
			in.read(buf);
			in.close();
			// 请求数据： userID={0}& MATERIAL={1}& PLANT={2}& STGE_LOC={3}&
			// filetype={4}& size={5}& data={6}& filename={7}
			param = "userID="
					+ UrlUtil.urlEncode(StrUtil.filterStr(bean.getUserId()))
					+ "&MATERIAL="
					+ UrlUtil.urlEncode(StrUtil.filterStr(bean.getMaterial()))
					+ "&PLANT="
					+ UrlUtil.urlEncode(StrUtil.filterStr(bean.getFactory()))
					+ "&STGE_LOC="
					+ UrlUtil.urlEncode(StrUtil.filterStr(bean.getStore()))
					+ "&STGE_LOC2="
					+ UrlUtil.urlEncode(UrlUtil.safeXml(bean.getCw()))
					+ "&MESSAGE="
					+ UrlUtil.urlEncode(UrlUtil.safeXml(bean.getDesp()))
					+ "&ENTRY_UOM="
					+ UrlUtil.urlEncode(UrlUtil.safeXml(bean.getUnit()))
					+ "&filetype=jpg&size="
					+ UrlUtil.urlEncode(String.valueOf(length)) + "&data="
					+ UrlUtil.urlEncode(Base64.encode(ZipUtil.compress2(buf)))
					+ "&filename=" + UrlUtil.urlEncode(file.getName());

			sign = MD5Util.MD5((param + ParamsUtil.key));
			String result = RequestService.postRequest(url, methodName, sign,
					param);
			Log.i("zj", "*********上传照片接口返回信息：***********" + result);
			setImgStateByResult(result, bean);
			in = null;
			param = null;
			sign = null;
			result = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setImgStateByResult(String result, ImgBean bean) {
		if (result != null) {
			if (result.startsWith("<Toolkit>")) {
				String info = parseXmlUpdate(result);
				if(StrUtil.isNotEmpty(info)){
					// 更改照片状态
					imgDao.delImgBy(bean.getId());
					DelFileUtil
							.delFileByPath(StrUtil.filterStr(bean.getPath()));
				}
			}
		}
	}

	/** 解析上传返回数据 */
	public String parseXmlUpdate(String str) {
		InputStream ios = new ByteArrayInputStream(str.getBytes());
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(ios, "UTF-8");
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (name.equalsIgnoreCase("ErrorCode")) {
						return "error";
					} else if (name.equalsIgnoreCase("SuccessCode")) {
						return "ok";
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				default:
					break;
				}
				event = parser.next();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("zj", "解析出错:" + e.toString());
			return "解析登录信息出错";
		}
		return "";
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		threadDisable = false;
	}
}
