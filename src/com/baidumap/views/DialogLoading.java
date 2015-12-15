package com.baidumap.views;

import com.baidumap.activity.BaseActivity;
import com.baidumap.activity.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

public class DialogLoading {
	private Dialog dialog;
	private Context context;
	
	private boolean isCanClose;  //�Ƿ��ȡ��
	
	public DialogLoading(Context context, boolean isCanClose) {
		this.context = context;
		this.isCanClose = isCanClose;
		
		Dialog dialog = new Dialog(context, R.style.dialog_style);
		dialog.getWindow().setGravity(Gravity.CENTER);
		dialog.setCancelable(isCanClose);
		
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);		
		dialog.setContentView(view);
		
		this.dialog = dialog;		
	}

	public boolean isCanClose() {
		return isCanClose;
	}

	public void setCanClose(boolean isCanClose) {
		this.isCanClose = isCanClose;
	}
	
	//��ʾ�Ի���
	public void showDialog() {
		if (dialog != null && !dialog.isShowing()) {
			if (!((BaseActivity)context).isFinishing()) {  
				dialog.show();
			}
		}
	}
	
	//�رնԻ���
	public void closeDialog() {
		if (dialog != null && dialog.isShowing()) {
			if (!((BaseActivity)context).isFinishing()) {  
				dialog.dismiss();
			}
		}
	}
	
	/**
     * �Ƿ�������ʾ
     * @return
     */
    public boolean isShowing(){
        return dialog.isShowing();
    }
}
