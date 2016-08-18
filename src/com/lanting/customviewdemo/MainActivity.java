package com.lanting.customviewdemo;

import com.lanting.customviewdemo.myview.CustomTopBar;
import com.lanting.customviewdemo.myview.CustomTopBar.callBackListener;
import com.lanting.customviewdemo.myview.MyRecordBtn;
import com.lanting.customviewdemo.myview.MyRecordBtn.RecordCallBackListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView tv_status;
	MyRecordBtn myRecordBtn;
	CustomTopBar customTopBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		tv_status = (TextView) findViewById(R.id.main_text);
		myRecordBtn = (MyRecordBtn) findViewById(R.id.main_myView);
		customTopBar = (CustomTopBar) findViewById(R.id.topbar);
		setMyRecordBtnLis();
		setTopBarLis();
	}

	// ����ActionBar�ļ����¼�
	private void setTopBarLis() {
		customTopBar.setCallBackListener(new callBackListener() {

			@Override
			public void rightClick(View view) {
				Toast.makeText(MainActivity.this, "��������Ҳ��ͼƬ",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void leftClick(View view) {

			}
		});
	}

	// ���ûص�����
	private void setMyRecordBtnLis() {
		myRecordBtn.setRecordCallBackListener(new RecordCallBackListener() {
			@Override
			public void releaseListener(boolean isInside) {
				tv_status.setText("�ͷţ�" + isInside);
			}

			@Override
			public void pressListener() {
				tv_status.setText("����");
			}

			@Override
			public void outsideListener() {
				tv_status.setText("�ⲿ");
			}

			@Override
			public void insideListener() {
				tv_status.setText("�ڲ�");
			}
		});
	}
}
