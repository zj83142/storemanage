package com.zj.storemanag.util;

import java.util.List;

import log.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class WsUtil {

	/**
	 * 调用WS查询数据接口
	 */

	// 命名空间
	private static final String NAMESPACE = "http://tempuri.org/";
	
	// 请求URL
//	private static final String URL = "http://115.196.217.214:1020/BasicOperation.svc";
	private static final String URL = "http://192.168.1.14:8811/Service1.svc?wsdl";
	
	public static SoapObject getDataByWs(String interfaceName, String method, List<String> paramKeys, List<Object> paramValue) {
		// 指定WebService的命名空间和调用的方法名利用SoapObject类
		// 第1个参数表示WebService的命名空间，可以从WSDL文档中找到WebService的命名空间。
		// 第2个参数表示要调用的WebService方法名
		SoapObject request = new SoapObject(NAMESPACE, method);
		// 设置调用方法的参数值，这一步是可选的，如果方法没有参数，可以省略这一步。设置方法的参数值的代码如下
		// 要注意的是，addProperty方法的第1个参数虽然表示调用方法的参数名，但该参数值并不一定与服务端的WebService类中的方法参数名一致，只要设置参数的顺序一致即可。
		
		Log.i("url", "url---------->"+URL);
		Log.i("url", "接口： " + interfaceName + "/" + method);
		if (paramKeys != null && paramKeys.size() > 0) {
			for (int i = 0; i < paramKeys.size(); i++) {
				Log.i("url", "接口请求参数key：   "+paramKeys.get(i) + "value:   " + paramValue.get(i));
				request.addProperty(paramKeys.get(i), paramValue.get(i));
			}
		}
		// request.addProperty("theCityName", "武汉");
		// 生成调用WebService方法的SOAP请求信息。该信息由SoapSerializationEnvelope对象描述
		// 构造方法设置SOAP协议的版本号:该版本号需要根据服务端WebService的版本号设置
		// SoapSerializationEnvelope对象后，不要忘了设置SoapSerializationEnvelope类的bodyOut属性，该属性的值就是在第1步创建的SoapObject对象。
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.bodyOut = request;
		new MarshalBase64().register(envelope); 


		envelope.setOutputSoapObject(request);
		// 创建HttpTransportSE对象。通过HttpTransportSE类的构造方法可以指定WebService的WSDL文档的URL
		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.debug = true; // 使用调试功能


		// web service请求
		try {
			// 使用call方法调用WebService方法
			String soapAction = "http://tempuri.org/" + interfaceName+  "/" + method;
//			String action ="http://tempuri.org/IService1/GetCodeTable";
			transport.call(soapAction, envelope);
			Log.i("url", soapAction+"-----------call---------->"+URL + interfaceName + "/" + method);
			// 使用getResponse方法获得WebService 方法的返回结果

 			if (envelope.bodyIn instanceof SoapObject) {
 				SoapObject resultsRequestSOAP  = (SoapObject) envelope.bodyIn;
				Log.i("interface", "接口返回数据： " + envelope.bodyIn.toString());
				return resultsRequestSOAP;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.e("interface", "接口返回数据报错："+ex.getMessage());
		}
		return null;
	}
}
