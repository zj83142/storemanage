package com.zj.storemanag.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import log.Log;

import com.zj.storemanag.commen.ParamsUtil;

public class RequestService {

	// static String url =
	// "http://223.4.97.56:9000/Library/WebInitPage.aspx?Source=InterfacePage";

	// post请求，无文件长度大小限制
	public static String postRequest(String rootUrl, String service,
			String requestParams) {
		HttpURLConnection con = null;
		try {
			Log.i("zj", "postRequest:--------->" + requestParams);
			if (requestParams == null) {
				requestParams = "";
			}
			byte[] data = requestParams.toString().getBytes();
			String sign = MD5Util.MD5(requestParams + ParamsUtil.key);
			rootUrl = "http://"+rootUrl + ParamsUtil.defaulServiceName;
			String urlStr = rootUrl + "&sign=" + sign + "&service=" + service;
			Log.i("zj", "----发送请求的urlStr-------------->" + urlStr);
			URL url = new URL(urlStr);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setConnectTimeout(60 * 1000);
			con.setReadTimeout(0);
			con.setDoOutput(true);// 打开向外输出
			con.setDoInput(true);  
			con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");// 内容类型
			con.setRequestProperty("Charset", "UTF-8");
			//----------------------------------
			con.setRequestProperty("Accept-Encoding", "identity");
			System.setProperty("http.keepAlive", "false"); 
			
			OutputStream outStream = con.getOutputStream();
			if (data != null) {
				outStream.write(data);// 写入数据
			}
			outStream.flush();// 刷新内存
			outStream.close();
			// 状态码是不成功
    			if (con.getResponseCode() == 200) {
				InputStream is = con.getInputStream();
				String result = new String(readStream(is));
				String sign2 = result.substring(0, 32);
				result = result.substring(32);
				if (MD5Util.MD5((result + ParamsUtil.key)).equalsIgnoreCase(
						sign2)) {
					String xml = ZipUtil.decompress(Base64.decode(result));
					Log.i("zj", "接口返回xml:----->" + xml);
					return xml;
				} else {
					Log.i("zj", "两个签名不一样");
					return "sign error";
				}
			}
		} catch (Exception e) {
  			if(e != null){
				e.printStackTrace();
			}
			Log.i("zj", e.toString());
			return null;
		}finally{
			if(con != null){
				con.disconnect();
			}
		}
		return null;
	}
	
	// post请求，无文件长度大小限制
	/**
	 * 
	 * @param rootUrl url
	 * @param methodName  接口名
	 * @param sign  签名
	 * @param info  压缩数据
	 * @return
	 */
	public static String postRequest(String rootUrl, String methodName, String sign, String info) {
		HttpURLConnection con = null;
		try {
			byte[] data = info.getBytes();
			rootUrl = "http://"+rootUrl + ParamsUtil.defaulServiceName;
			String urlStr = rootUrl + "&sign="
					+ sign + "&service=" + methodName;
			if(!StrUtil.isNotEmpty(urlStr)) return null;
			Log.i("zj","----发送请求的urlStr-------------->" + urlStr);
			URL url = new URL(urlStr);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setConnectTimeout(30 * 1000);
			con.setReadTimeout(0);
			con.setDoOutput(true);// 打开向外输出
			con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");// 内容类型
			con.setRequestProperty("Charset", "UTF-8");
			//------------------------------------------------
			con.setRequestProperty("Accept-Encoding", "identity");
			System.setProperty("http.keepAlive", "false"); 
			
			OutputStream outStream = con.getOutputStream();
			outStream.write(data);// 写入数据
			outStream.flush();// 刷新内存
			outStream.close();
			Log.i("zj","----发送数据--返回的-->状态码" + con.getResponseCode());
			// 状态码是不成功
			if (con.getResponseCode() == 200) {
				Log.i("zj","---e----->");
				InputStream is = con.getInputStream();
				String result = new String(readStream(is));
				String sign2 = result.substring(0, 32);
				result = result.substring(32);
				if (MD5Util.MD5((result + ParamsUtil.key).getBytes()).equals(sign2)) {
					String xml = ZipUtil.decompress(Base64.decode(result));
					Log.i("zj", "接口返回xml:----->" + xml);
					return xml;
				} else {
					Log.i("zj","两个签名不一样");
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("zj",e.toString());
			return null;
		}finally{
			if(con != null){
				con.disconnect();
			}
		}
		return null;
	}

	private static byte[] readStream(InputStream is) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = is.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		is.close();
		return outStream.toByteArray();
	}
}
