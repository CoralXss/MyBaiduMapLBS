package com.baidumap.activity;

import com.baidumap.activity.R;
import com.baidumap.views.DialogLoading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {
	public BaseActivity baseActivity;
	private DialogLoading dialogLoading;  //可取消
	private DialogLoading dialogLoadingCannotClose;  //不可取消

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		baseActivity = this;
	}
	
	protected abstract void initView();
	
	protected abstract void initData();
	
	//设置公共标题
	public void setTitle(String title) {
		TextView tv_title = (TextView)findViewById(R.id.tv_title);
		tv_title.setText(title);
	}
	
	public void initLeftButton() {
		ImageView img_left = (ImageView)findViewById(R.id.img_left);
		img_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishActivityAnimation();
			}
		});
	}
	
	//获取对话框  
	public DialogLoading getDialogLoading(boolean isCanClose) {
			if (dialogLoading == null) {
				dialogLoading = new DialogLoading(baseActivity, isCanClose);
			}
			return dialogLoading;
		
	}

	//关闭正在显示的dialog
	public void closeDialog() {
		if (dialogLoading != null && dialogLoading.isShowing()) {
			dialogLoading.closeDialog();
		} 
	}
	
	//启动调用动画
	public void startActivityAnimation(Intent intent) {
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	//结束调用动画
	public void finishActivityAnimation() {
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
}
