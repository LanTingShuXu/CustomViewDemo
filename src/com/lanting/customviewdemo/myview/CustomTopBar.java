package com.lanting.customviewdemo.myview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lanting.customviewdemo.R;

/**
 * 自定义标题栏
 * 
 * @author 蓝亭书序
 * 
 */
public class CustomTopBar extends RelativeLayout {

	// --------------点击“返回”的回调接口---------------

	public interface callBackListener {
		public void leftClick(View view);

		public void rightClick(View view);
	}

	// --------------监听事件的函数----------------

	private callBackListener callBackListener;

	public callBackListener getCallBackListener() {
		return callBackListener;
	}

	public void setCallBackListener(callBackListener callBackListener) {
		this.callBackListener = callBackListener;
	}

	private Activity activity;// 当前的页面
	private Context context;

	private String title = "无标题";// 默认显示为无标题
	private String back_name = "  ";// 返回的Activity默认为 返回
	private boolean back_enable = false;// 默认返回按钮不显示
	private TextView tv_title;
	private ImageView imgv_titleLine;
	private ImageView imgv_back;

	private TextView tv_backname;
	private TextView tv_rightname;
	private ImageView imgv_right;

	private Drawable rightDrawable;
	private String right_name;

	private RelativeLayout relativeLayout; // 被嵌套的RelativeLayout

	private static final int ID_tv_title = 1000;
	private static final int ID_tv_back = 1001;
	private static final int ID_img_back = 1002;

	// 1dp的大小
	private float dp_1 = 1;

	// -------------------一系列的构造方法----------------------
	public CustomTopBar(Context context) {
		this(context, null);
	}

	public CustomTopBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomTopBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		get1dp();
		relativeLayout = new RelativeLayout(context);
		setBackgroundColor(Color.parseColor("#cc2c72d0"));
		// 获取所有的属性
		getAttr(context, attrs);
	}

	// 获取1dp的尺寸
	private void get1dp() {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		dp_1 = TypedValue.applyDimension(1, TypedValue.COMPLEX_UNIT_DIP,
				metrics);
	}

	// --------------------------------------------------------

	private void getAttr(Context context, AttributeSet attrs) {

		// 获取所有的属性

		TypedArray typeArray = context.obtainStyledAttributes(attrs,
				R.styleable.CustomTopBar);
		// 自定义属性个数
		int childCount = typeArray.getIndexCount();

		for (int i = 0; i < childCount; i++) {
			// 获取索引ID
			int ID = typeArray.getIndex(i);
			switch (ID) {
			// 如果是自定义的标题
			case R.styleable.CustomTopBar_topbar_title:
				// 获取用户自己输入的标题
				title = typeArray.getString(ID);
				break;
			// 如果是自定义的返回标题
			case R.styleable.CustomTopBar_back_name:
				// 获取用户自己输入的标题
				back_name = typeArray.getString(ID);
				break;
			// 获取是否需要有返回按钮
			case R.styleable.CustomTopBar_backenable:
				// 获取用户自己输入的标题
				back_enable = typeArray.getBoolean(i, false);
				break;
			// 获取是否需要有右上角按钮文字
			case R.styleable.CustomTopBar_right_name:
				// 获取用户自己输入的右上角文字
				right_name = typeArray.getString(ID);
				break;
			// 获取是否需要有右上角按钮图片
			case R.styleable.CustomTopBar_right_img:
				// 获取用户自己输入的右上角的图片
				rightDrawable = typeArray.getDrawable(ID);
				break;
			}
		}
		typeArray.recycle();

		// 初始化我们需要的组件
		tv_title = new TextView(getContext());
		imgv_titleLine = new ImageView(getContext());
		imgv_back = new ImageView(getContext());
		tv_backname = new TextView(getContext());
		tv_rightname = new TextView(getContext());
		imgv_right = new ImageView(getContext());

		// 设置标题
		setTitle();
		setBack();
		setRight();
		relativeLayout.setBackgroundColor(Color.TRANSPARENT);
		addView(relativeLayout);

	}

	/**
	 * 设置右上角的显示内容（文字和图片）
	 * 
	 */
	private void setRight() {

		if (rightDrawable != null) {
			imgv_right.setImageDrawable(rightDrawable);
			imgv_right.setClickable(true);
			LayoutParams layoutParams = new LayoutParams((int) dp_1 * 40,
					(int) dp_1 * 40);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
//			layoutParams.rightMargin = (int) dp_1 * 3;
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
					RelativeLayout.TRUE);

			imgv_right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (callBackListener != null) {
						callBackListener.rightClick(v);
					}
				}
			});

			relativeLayout.addView(imgv_right, layoutParams);
		} else if (right_name != null) {
			tv_rightname.setText(right_name);
			tv_rightname.setTextSize(16);
			tv_rightname.setClickable(true);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			layoutParams.rightMargin = (int) dp_1 * 10;
			layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
					RelativeLayout.TRUE);

			tv_rightname.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (callBackListener != null) {
						callBackListener.rightClick(v);
					}
				}
			});

			relativeLayout.addView(tv_rightname, layoutParams);
		}

	}

	/**
	 * 设置左上角的显示内容（文字和图片）
	 */
	private void setBack() {
		// 为ImageView设置图片
		imgv_titleLine.setImageResource(android.R.color.background_dark);
		LayoutParams lp2 = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 顶部以及底部的margin
		int topMargin = (int) dp_1 * 20;
		lp2.topMargin = topMargin;
		lp2.bottomMargin = topMargin;
		lp2.addRule(RelativeLayout.BELOW, ID_tv_title);
		relativeLayout.addView(imgv_titleLine, lp2);

		// 如果用户需要有返回按钮
		if (back_enable) {
			// 为BackImageView设置图片
			imgv_back.setImageResource(R.drawable.back_1);
			imgv_back.setId(ID_img_back);
			imgv_back.setBackgroundResource(R.drawable.selector_bg_topbar);
			imgv_back.setOnClickListener(backClick);
			LayoutParams lp3 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			int leftMargin = (int) dp_1 * 10;
			// lp3.leftMargin = leftMargin;
			imgv_back.setPadding(leftMargin, 0, 0, 0);
			lp3.addRule(RelativeLayout.CENTER_VERTICAL);
			relativeLayout.addView(imgv_back, lp3);

			// 为控件设置自定义的返回文字 tv_backname.setText(back_name);
			tv_backname.setText(back_name);
			tv_backname.setTextSize(16);
			tv_backname.setId(ID_tv_back);
			// tv_backname.setBackgroundResource(R.drawable.selector_bg_topbar);
			LayoutParams lp4 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					lp3.height);
			lp4.leftMargin = lp3.leftMargin + (int) dp_1 * 30;
			lp4.addRule(RelativeLayout.CENTER_VERTICAL);
			// 设置点击的监听器
			// tv_backname.setOnClickListener(backClick);
			relativeLayout.addView(tv_backname, lp4);
		}

	}

	/**
	 * 设置标题
	 */
	private void setTitle() {
		// 为控件设置自定义标题的内容
		tv_title.setText(title);
		tv_title.setTextSize(20);
		tv_title.setId(ID_tv_title);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
		tv_title.setTextColor(Color.WHITE);
		relativeLayout.addView(tv_title, lp);
	}

	/**
	 * 设置自定义标题
	 * 
	 * @param title
	 *            标题
	 */
	public void setTitle(String title) {
		tv_title.setText(title);
	}

	/**
	 * 设置需要返回的界面。
	 * 
	 * @param activity
	 *            当前界面
	 */
	public CustomTopBar setActivity(Activity activity) {
		this.activity = activity;
		return this;
	}

	/**
	 * 点击返回的监听器
	 * 
	 * @author 李长军
	 * 
	 */
	private OnClickListener backClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 如果都不为空，跳转到指定的界面
			if (activity != null) {
				activity.finish();
			} else {
				((Activity) getContext()).finish();
			}
		}

	};

}
