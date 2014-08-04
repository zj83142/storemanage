package com.zj.storemanag.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.zj.storemanag.R;
import com.zj.storemanag.commen.BaseActivity;
import com.zj.storemanag.util.StrUtil;
import com.zj.storemanag.view.NetImageView;

@SuppressLint("NewApi")
public class PhotoScanActivity extends BaseActivity implements ViewFactory {

	private NetImageView netImageView; // 显示大图

	/*** 图片放大缩小 ***/
	private static final int NONE = 0; // 无状态
	private static final int DRAG = 1; // 单点
	private static final int ZOOM = 2; // 多点

	private int mode = NONE; // 默认无状态
	private float oldDist;
	private Matrix matrix = new Matrix();// 坐标变换矩阵（Matrix）;
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private ImageView view;// 图片缩小;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_detial);
		initView();
		initListener();
	}

	private void initListener() {
		netImageView.setOnClickListener(this);
		netImageView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				view = (ImageView) v;
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:// 按下;
					netImageView.setScaleType(ScaleType.MATRIX);
					savedMatrix.set(matrix);
					start.set(event.getX(), event.getY());
					mode = DRAG;
					break;
				case MotionEvent.ACTION_UP:// 松开;
					float downX = event.getX() - start.x;
					float downY = event.getY() - start.y;
					Log.d("Debug", downX + "|" + downY);
					view.setImageMatrix(matrix);
					break;
				case MotionEvent.ACTION_POINTER_UP:
					view.setImageMatrix(matrix);
					mode = NONE;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					if (oldDist > 10f) {
						savedMatrix.set(matrix);
						midPoint(mid, event);
						mode = ZOOM;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG) {
						matrix.set(savedMatrix);
						float scalX = event.getX() - start.x;
						float scalY = event.getY() - start.y;
						matrix.postTranslate(scalX, scalY);
					} else if (mode == ZOOM) {
						float newDist = spacing(event);
						if (newDist > 5f) {
							matrix.set(savedMatrix);
							float scale = newDist / oldDist;
							matrix.postScale(scale, scale, mid.x, mid.y);
						}
					}
					break;
				}
//				 view.setImageMatrix(matrix);
				return true;
			}

			/**
			 * 计算拖动的距离
			 * 
			 * @author ZHZEPHI
			 * @data 2013-2-3 下午6:58:31
			 * @email zzf_soft@163.com
			 * @param event
			 * @return
			 */
			private float spacing(MotionEvent event) {
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				return FloatMath.sqrt(x * x + y * y);
			}

			/**
			 * 计算两点的之间的中间点
			 * 
			 * @author ZHZEPHI
			 * @data 2013-2-3 下午6:58:20
			 * @email zzf_soft@163.com
			 * @param point
			 * @param event
			 */
			private void midPoint(PointF point, MotionEvent event) {
				float x = event.getX(0) + event.getX(1);
				float y = event.getY(0) + event.getY(1);
				point.set(x / 2, y / 2);
			}
		});
	}

	private void initView() {
		TextView titleTv = (TextView) findViewById(R.id.top_title_tv);
		titleTv.setText("图片预览");
		netImageView = (NetImageView) findViewById(R.id.album_imgview);
		String path = StrUtil.filterStr(getIntent().getStringExtra("path"));
		String urlStr =  StrUtil.filterStr(getIntent().getStringExtra("url"));
		//如果path不为空显示file，否则判断url
		if(StrUtil.isNotEmpty(path)){
			File file = new File(path);
			if(file.exists()){
				netImageView.setImagePath(file);
				return;
			}
		}
		if(StrUtil.isNotEmpty(urlStr)){
			netImageView.setImageUrl(urlStr);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_back_btn:// 返回;
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 提醒系统及时回收
		System.gc();
		finish();
	}

	@Override
	public View makeView() {
		return null;
	}

}
