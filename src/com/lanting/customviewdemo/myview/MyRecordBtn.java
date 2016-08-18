package com.lanting.customviewdemo.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义View，此View为圆形的录音按钮，手指按下时按钮会变小，松开时变大。同时，实现了几个接口，用于监听手指是否滑动出去/进来 、手按下和松开
 * 
 * @author 蓝亭书序
 * 
 */
public class MyRecordBtn extends View {
	private Context context;
	// 记录1dp对应的大小，默认是1。具体获取通过get1dp（）获取
	private float dp_1 = 1;
	// 控件默认尺寸
	private float defaultSize;
	// 半径偏移值（手指按下时，半径变小）
	private int radiusOffset = 0;
	// 标志位，true表示手指在控件区域内
	private boolean isInside;

	// 事件回调函数
	private RecordCallBackListener callbackLis = null;

	public MyRecordBtn(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		getDefaultSize();
	}

	public MyRecordBtn(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyRecordBtn(Context context) {
		this(context, null);
	}

	// 获取1dp的尺寸
	private void get1dp() {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		dp_1 = TypedValue.applyDimension(1, TypedValue.COMPLEX_UNIT_DIP,
				metrics);
	}

	// 初始化控件的默认尺寸，默认尺寸为50dp
	private void getDefaultSize() {
		get1dp();
		defaultSize = dp_1 * 80;
	}

	// 设置事件回调监听
	public void setRecordCallBackListener(RecordCallBackListener callbackLis) {
		this.callbackLis = callbackLis;
	}

	// 获取事件回调监听
	public RecordCallBackListener getRecordCallBackListener() {
		return callbackLis;
	}

	/**
	 * 复写测量方法，处理布局参数为wrap_content的情况
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthSpecMode == MeasureSpec.AT_MOST
				&& heightSpecMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension((int) defaultSize, (int) defaultSize);
		} else if (widthSpecMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension((int) defaultSize, heightSpecSize);
		} else if (heightSpecMode == MeasureSpec.AT_MOST) {
			setMeasuredDimension(widthSpecSize, (int) defaultSize);
		} else {
			setMeasuredDimension(widthSpecSize, heightSpecSize);
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 判断手指是否在控件区域内
		isInside = event.getX() > 0 && event.getX() < getMeasuredWidth()
				&& event.getY() > 0 && event.getY() < getMeasuredHeight();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			radiusOffset = (int) (getMeasuredWidth() / 20.0f);
			invalidate();
			pressEvent();
			break;
		case MotionEvent.ACTION_UP:
			radiusOffset = 0;
			invalidate();
			releaseEvent(isInside);
			break;
		case MotionEvent.ACTION_MOVE:
			if (isInside) {
				insideEvent();
			} else {
				outsideEvent();
			}
			break;
		}
		return true;
	}

	/**
	 * 绘制View
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		drawCircle(canvas);
	}

	/**
	 * 绘制圆形
	 * 
	 * @param canvas
	 *            View的画布对象
	 */
	private void drawCircle(Canvas canvas) {
		float radius = getMeasuredWidth() / 2.0f;

		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);

		canvas.drawCircle(radius, radius, radius - radiusOffset, paint);

	}

	/**
	 * 在内部时的回调接口
	 */
	public void insideEvent() {
		if (callbackLis != null) {
			callbackLis.insideListener();
		}
	}

	/**
	 * 在外部时的回调接口
	 */
	public void outsideEvent() {
		if (callbackLis != null) {
			callbackLis.outsideListener();
		}
	}

	/**
	 * 松开时的回调接口
	 */
	public void releaseEvent(boolean isInside) {
		if (callbackLis != null) {
			callbackLis.releaseListener(isInside);
		}
	}

	/**
	 * 按下时的回调接口
	 */
	public void pressEvent() {
		if (callbackLis != null) {
			callbackLis.pressListener();
		}
	}

	/**
	 * 回调（推荐使用VoiceCallBackListenerAdapter，空实现此接口）
	 * 
	 * @author 蓝亭书序
	 * 
	 */
	public interface RecordCallBackListener {
		/**
		 * 在内部时的回调接口
		 */
		public void insideListener();

		/**
		 * 在外部时的回调接口
		 */
		public void outsideListener();

		/**
		 * 松开时的回调接口
		 * 
		 * @param isInside
		 *            是否是在控件区域内释放（如果在之外，那么可能会取消发送）
		 */
		public void releaseListener(boolean isInside);

		/**
		 * 按下时的回调接口
		 */
		public void pressListener();
	}

	/**
	 * RecordCallBackListener的适配器，默认实现为空
	 * 
	 * @author 蓝亭书序
	 * 
	 */
	public static class RecordCallBackListenerAdapter implements
			RecordCallBackListener {

		@Override
		public void insideListener() {
		}

		@Override
		public void outsideListener() {
		}

		@Override
		public void releaseListener(boolean isInside) {
		}

		@Override
		public void pressListener() {
		}
	}
}
