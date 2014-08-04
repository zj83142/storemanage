package com.zj.storemanag.util;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;

/**
 * Handler+ExecutorService(线程池)+MessageQueue+缓存模式 下载图片
 * 
 */
// -newFixedThreadPool与cacheThreadPool差不多，也是能reuse就用，但不能随时建新的线程
// -其独特之处:任意时间点，最多只能有固定数目的活动线程存在，此时如果有新的线程要建立，只能放在另外的队列中等待，直到当前的线程中某个线程终止直接被移出池子
// -
// 和cacheThreadPool不同，FixedThreadPool没有IDLE机制（可能也有，但既然文档没提，肯定非常长，类似依赖上层的TCP
// 或UDP IDLE机制之类的），所以FixedThreadPool多数针对一些很稳定很固定的正规并发线程，多用于服务器
// -从方法的源代码看，cache池和fixed 池调用的是同一个底层池，只不过参数不同:
// fixed池线程数固定，并且是0秒IDLE（无IDLE）
// cache池线程数支持0-Integer.MAX_VALUE(显然完全没考虑主机的资源承受能力），60秒IDLE
public class AsyncLoadImage {

	BitmapFactory.Options options = new BitmapFactory.Options();
	
	private static AsyncLoadImage asyncLoadImage;

	public static synchronized AsyncLoadImage getInstance() {
		if (asyncLoadImage == null) {
			asyncLoadImage = new AsyncLoadImage();
		}
		return asyncLoadImage;
	}
	
	// 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同一个图片要多次被访问，比如在ListView时来回滚动)
	public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
	public Map<String, SoftReference<Bitmap>> bmpCache = new HashMap<String, SoftReference<Bitmap>>();
	// ExecutorService线程池;ExecutorService 建立多线程;
	// 建立ExecutorService线程池
	// ExecutorService executorService = Executors.newCachedThreadPool();
	private ExecutorService executorService = Executors.newFixedThreadPool(4);
	private final Handler handler = new Handler();

	public Drawable loadDrawable(final String imgUrl,
			final ImageLoadCallback callback) {
		// 通过containsKey() 判断hashmap中是否包含此键值,这里为false获取true两种状态;
		if (imageCache.containsKey(imgUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imgUrl);
			if (softReference != null) {
				return softReference.get();
				// newCachedThreadPool()
				// -缓存型池子，先查看池中有没有以前建立的线程，如果有，就reuse.如果没有，就建一个新的线程加入池中
				// -缓存型池子通常用于执行一些生存期很短的异步型任务

			}
		}
		// execute(Runnable对象)方法;
		// 其实就是对Runnable对象调用start()方法;
		// 这里为调用线程池的操作;
		executorService.submit(new Runnable() {

			@Override
			public void run() {

				final Drawable drawable = loadImageFromUlr(imgUrl);

				imageCache.put(imgUrl, new SoftReference<Drawable>(drawable));

				handler.post(new Runnable() {

					@Override
					public void run() {
						callback.imageLoaded(drawable);
					}
				});
			}
		});

		return null;

	}

	public Bitmap loadDrawable(final File file,
			final ImageLoadCallback callback, final int scale) {
		// 通过containsKey() 判断hashmap中是否包含此键值,这里为false获取true两种状态;
		if (imageCache.containsKey(file.getName())) {
			SoftReference<Bitmap> softReference = bmpCache.get(file.getName());
			if (softReference != null) {
				return softReference.get();
				// newCachedThreadPool()
				// -缓存型池子，先查看池中有没有以前建立的线程，如果有，就reuse.如果没有，就建一个新的线程加入池中
				// -缓存型池子通常用于执行一些生存期很短的异步型任务

			}
		}
		// execute(Runnable对象)方法;
		// 其实就是对Runnable对象调用start()方法;
		// 这里为调用线程池的操作;
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				options.inSampleSize = scale;// 图片缩放比例
				final Bitmap bitmap = BitmapFactory.decodeFile(
						file.getAbsolutePath(), options);
				bmpCache.put(file.getName(),
						new SoftReference<Bitmap>(bitmap));

				handler.post(new Runnable() {

					@Override
					public void run() {
						callback.imageLoaded(bitmap);
					}
				});
			}
		});

		return null;

	}

	protected Drawable loadImageFromUlr(String url) {
		try {
			return Drawable.createFromStream(new URL(url).openStream(),
					"image.png");
		} catch (MalformedURLException e) {
			// e.printStackTrace();
			return null;
		} catch (IOException e) {
			// e.printStackTrace();
			return null;
		}
		// return null;
	}

	// 对外界开放的回调接口
	public interface ImageLoadCallback {
		// 注意 此方法是用来设置目标对象的图像资源
		public void imageLoaded(Drawable imageDrawable);
		
		public void imageLoaded(Bitmap imageBitmap);
	}

}
