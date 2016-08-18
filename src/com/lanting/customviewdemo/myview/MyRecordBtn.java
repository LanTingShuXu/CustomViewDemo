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
 * �Զ���View����ViewΪԲ�ε�¼����ť����ָ����ʱ��ť���С���ɿ�ʱ���ͬʱ��ʵ���˼����ӿڣ����ڼ�����ָ�Ƿ񻬶���ȥ/���� ���ְ��º��ɿ�
 * 
 * @author ��ͤ����
 * 
 */
public class MyRecordBtn extends View {
	private Context context;
	// ��¼1dp��Ӧ�Ĵ�С��Ĭ����1�������ȡͨ��get1dp������ȡ
	private float dp_1 = 1;
	// �ؼ�Ĭ�ϳߴ�
	private float defaultSize;
	// �뾶ƫ��ֵ����ָ����ʱ���뾶��С��
	private int radiusOffset = 0;
	// ��־λ��true��ʾ��ָ�ڿؼ�������
	private boolean isInside;

	// �¼��ص�����
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

	// ��ȡ1dp�ĳߴ�
	private void get1dp() {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		dp_1 = TypedValue.applyDimension(1, TypedValue.COMPLEX_UNIT_DIP,
				metrics);
	}

	// ��ʼ���ؼ���Ĭ�ϳߴ磬Ĭ�ϳߴ�Ϊ50dp
	private void getDefaultSize() {
		get1dp();
		defaultSize = dp_1 * 80;
	}

	// �����¼��ص�����
	public void setRecordCallBackListener(RecordCallBackListener callbackLis) {
		this.callbackLis = callbackLis;
	}

	// ��ȡ�¼��ص�����
	public RecordCallBackListener getRecordCallBackListener() {
		return callbackLis;
	}

	/**
	 * ��д���������������ֲ���Ϊwrap_content�����
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
		// �ж���ָ�Ƿ��ڿؼ�������
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
	 * ����View
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		drawCircle(canvas);
	}

	/**
	 * ����Բ��
	 * 
	 * @param canvas
	 *            View�Ļ�������
	 */
	private void drawCircle(Canvas canvas) {
		float radius = getMeasuredWidth() / 2.0f;

		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAntiAlias(true);

		canvas.drawCircle(radius, radius, radius - radiusOffset, paint);

	}

	/**
	 * ���ڲ�ʱ�Ļص��ӿ�
	 */
	public void insideEvent() {
		if (callbackLis != null) {
			callbackLis.insideListener();
		}
	}

	/**
	 * ���ⲿʱ�Ļص��ӿ�
	 */
	public void outsideEvent() {
		if (callbackLis != null) {
			callbackLis.outsideListener();
		}
	}

	/**
	 * �ɿ�ʱ�Ļص��ӿ�
	 */
	public void releaseEvent(boolean isInside) {
		if (callbackLis != null) {
			callbackLis.releaseListener(isInside);
		}
	}

	/**
	 * ����ʱ�Ļص��ӿ�
	 */
	public void pressEvent() {
		if (callbackLis != null) {
			callbackLis.pressListener();
		}
	}

	/**
	 * �ص����Ƽ�ʹ��VoiceCallBackListenerAdapter����ʵ�ִ˽ӿڣ�
	 * 
	 * @author ��ͤ����
	 * 
	 */
	public interface RecordCallBackListener {
		/**
		 * ���ڲ�ʱ�Ļص��ӿ�
		 */
		public void insideListener();

		/**
		 * ���ⲿʱ�Ļص��ӿ�
		 */
		public void outsideListener();

		/**
		 * �ɿ�ʱ�Ļص��ӿ�
		 * 
		 * @param isInside
		 *            �Ƿ����ڿؼ��������ͷţ������֮�⣬��ô���ܻ�ȡ�����ͣ�
		 */
		public void releaseListener(boolean isInside);

		/**
		 * ����ʱ�Ļص��ӿ�
		 */
		public void pressListener();
	}

	/**
	 * RecordCallBackListener����������Ĭ��ʵ��Ϊ��
	 * 
	 * @author ��ͤ����
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
