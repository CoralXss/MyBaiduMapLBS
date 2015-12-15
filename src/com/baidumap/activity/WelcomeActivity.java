package com.baidumap.activity;

import java.util.ArrayList;

import com.baidumap.activity.R;
import com.baidumap.adapter.MyViewPagerAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class WelcomeActivity extends BaseActivity {
	private static final String TAG = "WelcomeActivity";
	
	private ViewPager viewPager;
	private PagerTitleStrip pagerTitle;
	private ImageView img_page0, img_page1, img_page2;
	
	private ArrayList<View> viewList = null;
	
	private int currentIndex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		initView();
		
		initData();
		
	}

	@Override
	protected void initView() {
		viewPager = (ViewPager)findViewById(R.id.viewPager);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		pagerTitle = (PagerTitleStrip)findViewById(R.id.pagerTitle);
		img_page0 = (ImageView)findViewById(R.id.img_page0);
		img_page1 = (ImageView)findViewById(R.id.img_page1);
		img_page2 = (ImageView)findViewById(R.id.img_page2);
		
	}

	@Override
	protected void initData() {
		viewList = new ArrayList<View>();		
		View view0 = LayoutInflater.from(this).inflate(R.layout.view_page0, null);
		View view1 = LayoutInflater.from(this).inflate(R.layout.view_page1, null);
		View view2 = LayoutInflater.from(this).inflate(R.layout.view_page2, null);		
		viewList.add(view0);
		viewList.add(view1);
		viewList.add(view2);
		
		MyViewPagerAdapter adapter = new MyViewPagerAdapter(this, viewList);
		viewPager.setAdapter(adapter);
		
		//第三个界面的按钮
		view2.findViewById(R.id.btn_enterMain).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
	
    class MyOnPageChangeListener implements OnPageChangeListener {
		
		@Override
		public void onPageSelected(int index) {
			//右滑设置左边未选中的点，左滑 设置右边的点
			switch (index) {
			case 0:
				img_page0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				img_page1.setImageDrawable(getResources().getDrawable(R.drawable.page));
				break;
			case 1:
				img_page1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				img_page0.setImageDrawable(getResources().getDrawable(R.drawable.page));				
				img_page2.setImageDrawable(getResources().getDrawable(R.drawable.page));			
				break;
			case 2:
				img_page2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				img_page1.setImageDrawable(getResources().getDrawable(R.drawable.page));								
				break;
			default:
				break;
			}
			currentIndex = index;
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	}

}
