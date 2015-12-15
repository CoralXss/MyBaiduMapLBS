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

//判断是否第一次进入app，第一次进入就显示欢迎界面，否则不显示
public class AppStartActivity extends BaseActivity {
	private static final String TAG = "AppStartActivity";
	
	private boolean isFirst = false;  //是否第一次进入
	
	private Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_start);
		
		enter();
	}
	
	//判断是否第一次进入app
	protected void enter() {
		final SharedPreferences sp = getSharedPreferences(Constant.FIRST, 0);
		final Boolean isFirst = sp.getBoolean(Constant.KEY_ISFIRST, true);  //默认值为true
		
		timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				if(isFirst) {   //第一次登陆
					sp.edit().putBoolean(Constant.KEY_ISFIRST, false).commit();
					Log.e(TAG, "first enter");
					Intent intent = new Intent(AppStartActivity.this, WelcomeActivity.class);
					startActivity(intent);
					finish();
					
				} else {  //不是第一次登录，直接到主页
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
