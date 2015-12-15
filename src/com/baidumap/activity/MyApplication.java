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
		
		//在使用SDK各组件之前初始化 context 信息，传入ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
	}
/*	
	protected void initMapManager(Context context) {
		if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }
		if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), "BMapManager  初始化错误!",
                    Toast.LENGTH_LONG).show();
        }
	}
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetPermissionState(int iError) {
            // 非零值表示key验证未通过
            if (iError != 0) {
                // 授权Key错误：
                Toast.makeText(MyApplication.getInstance().getApplicationContext(),
                        "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: " + iError, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), "key认证成功", Toast.LENGTH_LONG)
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
	 * 设置手机网络类型，wifi，cmwap，ctwap，用于联网参数选择
	 * @return
	 */
	static String setNetworkType() {
		String networkType = "wifi";
		ConnectivityManager manager = (ConnectivityManager)mInstance
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
			// 当前网络不可用
			return "";
		}

		String info = netWrokInfo.getExtraInfo();
		if ((info != null)
				&& ((info.trim().toLowerCase().equals("cmwap"))
						|| (info.trim().toLowerCase().equals("uniwap"))
						|| (info.trim().toLowerCase().equals("3gwap")) || (info
						.trim().toLowerCase().equals("ctwap")))) {
			// 上网方式为wap
			if (info.trim().toLowerCase().equals("ctwap")) {
				// 电信
				networkType = "ctwap";
			} else {
				networkType = "cmwap";
			}

		}
		return networkType;
	}
}
