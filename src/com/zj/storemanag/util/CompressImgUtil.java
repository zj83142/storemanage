package com.zj.storemanag.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CompressImgUtil {
	
	public void compressImg(String path){
		File file = new File(path);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 2;
		if (file.exists()) {
			Bitmap bmp = BitmapFactory.decodeFile(path, opts);
			if (bmp != null) {
				compressBmpToFile(bmp, file);
			}
		}
	}
	
	public static void compressBmpToFile(Bitmap bmp, File file) {
		if(file.length() / 1024 <= 300){
			return;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 80;// 个人喜欢从80开始,
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 300) {
			baos.reset();
			options -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
