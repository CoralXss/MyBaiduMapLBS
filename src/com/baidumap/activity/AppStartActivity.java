package com.baidumap.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.baidumap.activity.R;
import com.baidumap.util.Constant;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

//�ж��Ƿ��һ�ν���app����һ�ν������ʾ��ӭ���棬������ʾ
public class AppStartActivity extends BaseActivity {
	private static final String TAG = "AppStartActivity";
	
	private boolean isFirst = false;  //�Ƿ��һ�ν���
	
	private Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_start);
		
		enter();
	}
	
	//�ж��Ƿ��һ�ν���app
	protected void enter() {
		final SharedPreferences sp = getSharedPreferences(Constant.FIRST, 0);
		final Boolean isFirst = sp.getBoolean(Constant.KEY_ISFIRST, true);  //Ĭ��ֵΪtrue
		
		timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				if(isFirst) {   //��һ�ε�½
					sp.edit().putBoolean(Constant.KEY_ISFIRST, false).commit();
					Log.e(TAG, "first enter");
					Intent intent = new Intent(AppStartActivity.this, WelcomeActivity.class);
					startActivity(intent);
					finish();
					
				} else {  //���ǵ�һ�ε�¼��ֱ�ӵ���ҳ
					Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		};
		timer.schedule(task, 1000);

	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void initData() {
		
	}

}
