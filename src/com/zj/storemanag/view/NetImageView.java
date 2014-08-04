package com.zj.storemanag.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zj.storemanag.util.AsyncLoadImage;
import com.zj.storemanag.util.AsyncLoadImage.ImageLoadCallback;

public class NetImageView extends ImageView {
	private String mPicUrl;
	
	private AsyncLoadImage asyncLoadImage;
	
	public NetImageView(Context context) {
		super(context);
	}

	public NetImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NetImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setImagePath(File file){
		int scale = 2;
		if(file.length()/1024 >300){
			scale = 4;
		}
		asyncLoadImage = new AsyncLoadImage();
		Bitmap drawable = asyncLoadImage.loadDrawable(file,
				new ImageLoadCallback() {

					@Override
					public void imageLoaded(Bitmap imageDrawable) {
						if (imageDrawable != null) {
							NetImageView.this.setImageBitmap(imageDrawable);
						}
					}

					@Override
					public void imageLoaded(Drawable imageDrawable) {
						
					}
				},scale);
		if (drawable != null) {
			NetImageView.this.setImageBitmap(drawable);
		}
	}
	
	public void setImageUrl(String url) {
		mPicUrl = url;
		loadImage(url);
	}

	private void loadImage(String url) {
		if (NetImageViewCache.getInstance().isBitmapExit(url)) {
			Bitmap bitmap = NetImageViewCache.getInstance().get(url);
			this.setImageBitmap(bitmap);
		}
		else
		{
			new NetImageDownLoad().execute(mPicUrl);
		}
	}

	private byte[] getBytesFromStream(InputStream inputStream) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		while (len != -1) {
			try {
				len = inputStream.read(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (len != -1) {
				baos.write(b, 0, len);
			}
		}

		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return baos.toByteArray();
	}

	private class NetImageDownLoad extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			URL url = null;
			InputStream inputStream = null;
			HttpURLConnection urlConnection = null;
			Bitmap bmp = null;
			try {
				url = new URL(params[0]);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setConnectTimeout(10000);
				inputStream = urlConnection.getInputStream();
				byte[] bt = getBytesFromStream(inputStream);
				bmp = BitmapFactory.decodeByteArray(bt, 0, bt.length);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (null != inputStream) {
					try {
						inputStream.close();
						inputStream = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != urlConnection) {
					urlConnection.disconnect();
					urlConnection = null;
				}
			}
			return bmp;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (null != result) {
				NetImageView.this.setImageBitmap(result);
				//是否缓存图片
				NetImageViewCache.getInstance().put(mPicUrl, result, false);				
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}
