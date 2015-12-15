package com.baidumap.activity;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidumap.model.RouteResultModel;

public class MyApplication extends Application {
	private static MyApplication mInstance = null;
	
	public BMapManager mBMapManager = null;
	
	private RouteResultModel resultModel;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mInstance = this;
		
//		initMapManager(this);
		
		//��ʹ��SDK�����֮ǰ��ʼ�� context ��Ϣ������ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
	}
/*	
	protected void initMapManager(Context context) {
		if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }
		if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), "BMapManager  ��ʼ������!",
                    Toast.LENGTH_LONG).show();
        }
	}
	
	// �����¼���������������ͨ�������������Ȩ��֤�����
    static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetPermissionState(int iError) {
            // ����ֵ��ʾkey��֤δͨ��
            if (iError != 0) {
                // ��ȨKey����
                Toast.makeText(MyApplication.getInstance().getApplicationContext(),
                        "����AndoridManifest.xml��������ȷ����ȨKey,������������������Ƿ�������error: " + iError, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "key��֤�ɹ�", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }  */

	public static MyApplication getInstance() {
		return mInstance;
	}

	public RouteResultModel getResultModel() {
		return resultModel;
	}

	public void setResultModel(RouteResultModel resultModel) {
		this.resultModel = resultModel;
	}

	/**
	 * �����ֻ��������ͣ�wifi��cmwap��ctwap��������������ѡ��
	 * @return
	 */
	static String setNetworkType() {
		String networkType = "wifi";
		ConnectivityManager manager = (ConnectivityManager)mInstance
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
			// ��ǰ���粻����
			return "";
		}

		String info = netWrokInfo.getExtraInfo();
		if ((info != null)
				&& ((info.trim().toLowerCase().equals("cmwap"))
						|| (info.trim().toLowerCase().equals("uniwap"))
						|| (info.trim().toLowerCase().equals("3gwap")) || (info
						.trim().toLowerCase().equals("ctwap")))) {
			// ������ʽΪwap
			if (info.trim().toLowerCase().equals("ctwap")) {
				// ����
				networkType = "ctwap";
			} else {
				networkType = "cmwap";
			}

		}
		return networkType;
	}
}
