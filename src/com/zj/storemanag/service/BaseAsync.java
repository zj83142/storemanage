package com.zj.storemanag.service;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class BaseAsync extends AsyncTask<Object, Object, Object>{
	
	public Handler handler = null;
	public int what = 0;
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(handler != null){
			sendHandlerMessage(handler, result, what);
		}
	}
	
	public void sendHandlerMessage(Handler myHandler, Object result, int what) {
		Message msg = myHandler.obtainMessage();
		msg.what = what;
		msg.obj = result;
		myHandler.sendMessage(msg);
	}

}
