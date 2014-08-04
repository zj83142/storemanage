package com.zj.storemanag.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import log.Log;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class NfcTools {
	
	private Context context;
	private TextView infoTv;
	private Tag myTag;
	private boolean isWrite = false;
	
	public static final String MIME_TEXT_PLAIN = "text/plain";
	public void readInfo(Intent intent, Tag mytag, TextView infoTv, boolean isWrite, Context context) {
		this.context = context;
		this.myTag = mytag;
		this.infoTv = infoTv;
		this.isWrite = isWrite;
		if(myTag == null){
			showToast("NFC标签不TAG不能为空！");
			return;
		}
		if(infoTv == null){
			return;
		}
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)) {
				readNfcInfo(mytag);
			} else {
				Log.d("zj", "Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
			String[] techList = mytag.getTechList();
			String searchedTech = Ndef.class.getName();
			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					readNfcInfo(mytag);
					break;
				}
			}
		} else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			showToast("声明成功！");
		}
	}
	
	public void readNfcInfo(Tag myTag) {
		new NdefReaderTask().execute(myTag);
	}

	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

		@Override
		protected String doInBackground(Tag... params) {
			Tag tag = params[0];

			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				// NDEF is not supported by this Tag.
				showToast("没有匹配的TAG！");
				return null;
			}
			NdefMessage ndefMessage = ndef.getCachedNdefMessage();
			NdefRecord[] records = ndefMessage.getRecords();
			if(records == null){
				showToast("没有获取到NDEF信息！");
				return null;
			}
			for (NdefRecord ndefRecord : records) {
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN
						&& Arrays.equals(ndefRecord.getType(),
								NdefRecord.RTD_TEXT)) {
					try {
						return readText(ndefRecord);
					} catch (UnsupportedEncodingException e) {
						Log.e("zj", "Unsupported Encoding", e);
					}
				}
			}
			return null;
		}

		private String readText(NdefRecord record)
				throws UnsupportedEncodingException {
			byte[] payload = record.getPayload();
			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8"
					: "UTF-16";
			int languageCodeLength = payload[0] & 0063;
			return new String(payload, languageCodeLength + 1, payload.length
					- languageCodeLength - 1, textEncoding);
		}

		@Override
		protected void onPostExecute(String result) {
			
			if (result != null) {
				if(isWrite){
					//读写功能
					if (judgeNfcInfo(result)) {
						if (infoTv != null) {
							infoTv.setText(result);
						}
					} else {
						String info = createNfcInfo();
						if (write(info, myTag)) {
							if (infoTv != null) {
								infoTv.setText(info);
							}
						} else {
							showToast("读入错误！");
						}
					}
				}else{
					//只读功能
					if (!judgeNfcInfo(result)) {
						infoTv.setText("");
						showToast("此卡片未绑定数据！");
					}else{
						if (infoTv != null) {
							infoTv.setText(result);
						}
					}
				}
			}
		}
	}

	/***
	 * 判断NFC读出的数据是否符合要求
	 * 
	 * @param info
	 * @return
	 */
	public boolean judgeNfcInfo(String info) {
		if(info.length() == 32){
			return true;
		}
		return false;
	}

	/***
	 * 定义生成的rfid
	 * 
	 * @return
	 */
	public String createNfcInfo() {
		String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", ""); //计划批次号
		return uuid;
	}
	private boolean write(String text, Tag tag) {
		try {
			NdefRecord[] records = { createRecord(text) };
			NdefMessage message = new NdefMessage(records);
			int size = message.toByteArray().length;
			Ndef ndef = Ndef.get(tag);

			if (ndef != null) {
				// 允许对标签进行IO操作
				ndef.connect();
				if (!ndef.isWritable()) {
					showToast("NFC Tag是只读的！");
					return false;
				}
				if (ndef.getMaxSize() < size) {
					showToast("NFC Tag的空间不足");
					return false;
				}
				// 向标签写入数据
				ndef.writeNdefMessage(message);
				return true;
			} else {
				// 获取可以格式化和向标签写入数据NdefFormatable对象
				NdefFormatable format = NdefFormatable.get(tag);
				// 向非NDEF格式或未格式化的标签写入NDEF格式数据
				if (format != null) {
					try {
						// 允许对标签进行IO操作
						format.connect();
						format.format(message);
						return true;
					} catch (Exception e) {
						showToast("写入NDEF格式数据失败！");
						return false;
					}
				} else {
					showToast("NFC标签不支持NDEF格式！");
					return false;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			showToast(e.getMessage());
			return false;
		}
	}

	private NdefRecord createRecord(String text)
			throws UnsupportedEncodingException {
		String lang = "en";
		byte[] textBytes = text.getBytes();
		byte[] langBytes = lang.getBytes("US-ASCII");
		int langLength = langBytes.length;
		int textLength = textBytes.length;
		byte[] payload = new byte[1 + langLength + textLength];

		// set status byte (see NDEF spec for actual bits)
		payload[0] = (byte) langLength;

		// copy langbytes and textbytes into payload
		System.arraycopy(langBytes, 0, payload, 1, langLength);
		System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

		NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_TEXT, new byte[0], payload);

		return recordNFC;
	}
	
	private void showToast(String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

}
