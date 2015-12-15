package com.baidumap.activity;

import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.ac;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidumap.activity.R;
import com.baidumap.util.Constant;
import com.baidumap.util.MapUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.PopupWindow.OnDismissListener;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnClickListener{
	private static final String TAG = "MainActivity";
	
	private EditText edt_search;
	private ImageView img_yuyin;
	private Button btn_biggger, btn_smaller;
	private TextView tv_road_condition, tv_all_view;
	private ImageView img_layer;
	private TextView tv_neighbor, tv_route, tv_navigation, tv_mine;
	
	private MapView bmapView;
	private BaiduMap baiduMap;
	private UiSettings uiSettings;  //��ͼ�����ؼ����Ƿ���ʾָ�����
	private LocationClient locationClient;  //��λͼ��
	private boolean isFirstLoc = true;
//	private LatLng latlng;  //��ǰ��λ���ڵصľ�γ�ȣ���������ҳ��
	private double latitude;
	private double longtitude;
	private String address; //��ǰ��λ��ַ
	private String city;
	
	private PopupWindow heatMapPopWindow;
	private boolean isOpenPop = false;
	
	boolean isOpen = false;
	boolean isOpenTraffic = false;
	
	private SDKReceiver sdkReceiver;
	
	class SDKReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			String action = intent.getAction();
			Log.e(TAG, "SDKReceiver action = " + action);
			if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(ctx, "Key ����", Toast.LENGTH_SHORT).show();
			} else if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(ctx, "�������", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//ע��㲥���������Ƿ�key����
		if (sdkReceiver == null) {
			sdkReceiver = new SDKReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		registerReceiver(sdkReceiver, filter);
		
		initView();
		initData();
		
	}
	

	@Override
	protected void initView() {
		bmapView = (MapView)findViewById(R.id.bmapView);
		bmapView.showZoomControls(false);  //����ʾ���ſؼ�
		baiduMap = bmapView.getMap();
		
		//������λͼ��
		baiduMap.setMyLocationEnabled(true);
		locationClient = new LocationClient(getApplicationContext());
		locationClient.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation location) {
				// map view ���ٺ��ڴ����½��յ�λ��
				if (location == null || bmapView == null) {
					return;
				}
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				baiduMap.setMyLocationData(locData);
				if (isFirstLoc) {
					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(),
							location.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					baiduMap.animateMapStatus(u);
				}
				
				//��ȡ��γ��
				latitude = location.getLatitude();
				longtitude = location.getLongitude();
				address = location.getAddrStr();
				city = location.getCity();
				
				StringBuffer sb = new StringBuffer(256);
	            sb.append("time : ");
	            sb.append(location.getTime() + location.getAddrStr() + location.getCity());
	            sb.append("("+location.getLatitude()+", "+location.getLongitude()+")");
	            Log.e(TAG, "loca = " + sb.toString());
			}
		});
		
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);               // ��gps
		option.setCoorType("bd09ll");          // ������������
		option.setScanSpan(0);                 //����һ�ζ�λ��Ч
		option.setIsNeedAddress(true);         //������ʾλ����Ϣ
		
		locationClient.setLocOption(option);
		locationClient.start();
		
		
		btn_biggger = (Button)findViewById(R.id.btn_biggger);
		btn_smaller = (Button)findViewById(R.id.btn_smaller);
		
		tv_road_condition = (TextView)findViewById(R.id.tv_road_condition);
		img_layer = (ImageView)findViewById(R.id.img_layer);
		tv_all_view = (TextView)findViewById(R.id.tv_all_view);
		
		tv_neighbor = (TextView)findViewById(R.id.tv_neighbor);
		tv_route = (TextView)findViewById(R.id.tv_route);  
		tv_mine = (TextView)findViewById(R.id.tv_mine);
		
		
		btn_biggger.setOnClickListener(this);
		btn_smaller.setOnClickListener(this);
		
		tv_road_condition.setOnClickListener(this);
		img_layer.setOnClickListener(this);
		tv_all_view.setOnClickListener(this);
		
		tv_neighbor.setOnClickListener(this);
		tv_route.setOnClickListener(this);
		tv_mine.setOnClickListener(this);
		
	}

	@Override
	protected void initData() {
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_biggger:   //��ͼ�Ŵ�����10�����������[ʧ�ܣ�ÿ�ΰ�ť�¼�ֻ��һ����Ч]
			MapUtil.performZoom(MainActivity.this, baiduMap, 3);
			break;
		case R.id.btn_smaller:
			MapUtil.performZoom(MainActivity.this, baiduMap, -3);
			break;
		case R.id.tv_road_condition:  //·��(�򿪽�ͨͼ��)
			isOpenTraffic = !isOpenTraffic;
			baiduMap.setTrafficEnabled(isOpenTraffic);   
			break;
		case R.id.img_layer:          //ͼ��(����popwindow)			
			isOpenPop = !isOpenPop;   			
			if (isOpenPop) {				
				openPopWindow(img_layer);
			} else {
				if (heatMapPopWindow != null) {
					heatMapPopWindow.dismiss(); // �ر�PopupWindow
				}
			}			
			break;	  
		case R.id.tv_all_view:        //ȫ��(���ȫ�����ڵ�ͼ�ϱ�ע��ǰλ�ã�Ȼ�󵯳�infowindow����ʾ��ǰ��ȫ�������popwindow�ͽ����ͼ)
			showPano();
			break;
		case R.id.tv_neighbor:
			gotoActivity(PoiSearchActivity.class);
			break;
		case R.id.tv_route:
			gotoActivity(RouteQueryActivity.class);
			break;
		case R.id.tv_mine:
			gotoActivity(MyLBSRentActivity.class);
			break;
		default:
			break;
		}
	}
	
	protected void setScreenWidth() {
		DisplayMetrics metric = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(metric);  
		Constant.SCREEN_WIDTH = metric.widthPixels;     // ��Ļ��ȣ����أ�  
		Constant.SCREEN_HEIGHT = metric.heightPixels;   // ��Ļ�߶ȣ����أ�
	}
	
	
	//��ͼ��popWindow
	protected void openPopWindow(View img_layer) {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_window_layer, null);
		heatMapPopWindow = new PopupWindow(view);
		heatMapPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		heatMapPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		heatMapPopWindow.setFocusable(false);
		heatMapPopWindow.setOutsideTouchable(true);
		//�������ñ���ͼƬ������Ϊ���ú󣬲��ܳɹ�ͨ����ť����popwindow�ĵ�������ʧ
		heatMapPopWindow.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.linearlayout_bg2));
/*		heatMapPopWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				isOpenPop = false;
			}
		});  */
//		wanfapopWindow.setAnimationStyle(R.style.PopupAnimation);  //���ô򿪺͹رյĶ���
		
		view.findViewById(R.id.tv_satellite).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			}
		});
		view.findViewById(R.id.tv_2d).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			}
		});
		
		final Button btn = (Button) view.findViewById(R.id.btn_open_heat_map);
		view.findViewById(R.id.btn_open_heat_map).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isOpen = !isOpen;
				if (isOpen) {
					btn.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.pic_toogle_selected));
				} else {
					btn.setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.pic_toogle_normal));
				}
				baiduMap.setBaiduHeatMapEnabled(isOpen);
			}
		});
		
		int[] location = new int[2];
		int tH = 0;
		if (img_layer != null && img_layer instanceof ImageView) {
			Rect rect = new Rect();
			img_layer.getLocalVisibleRect(rect);   //�ֲ�
			img_layer.getGlobalVisibleRect(rect);  //ȫ��
			
			img_layer.getLocationInWindow(location);
			tH = img_layer.getHeight();
			
			img_layer.getLocationOnScreen(location);
		}
		heatMapPopWindow.showAtLocation(img_layer, Gravity.CENTER_HORIZONTAL | Gravity.TOP, location[0] - 100, location[1] + tH);
		heatMapPopWindow.update();
	}

	
	//��ȫ��
	protected void showPano() {
		//����Maker�����  
		LatLng point = new LatLng(latitude, longtitude);  
		//����Markerͼ��  
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pic_camera);  
		//����MarkerOption�������ڵ�ͼ�����Marker  
		OverlayOptions option = new MarkerOptions()  
		    .position(point)  
		    .icon(bitmap);  
		//�ڵ�ͼ�����Marker������ʾ  
		baiduMap.addOverlay(option);
/*		
		View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_window_pano, null);
		PanoramaView panoramaView = (PanoramaView) v.findViewById(R.id.panoramaView);
		panoramaView.setPanorama(longtitude, latitude);
		
		//����InfoWindow , ���� view�� �������꣬ y ��ƫ���� 
		InfoWindow mInfoWindow = new InfoWindow(v, point, -47);  
		//��ʾInfoWindow  
		baiduMap.showInfoWindow(mInfoWindow);   */
	}
	
	//Activity��ת
	protected void gotoActivity(Class clazz) {
		Intent intent = new Intent(MainActivity.this, clazz);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longtitude", longtitude);
		intent.putExtra("city", city);
		intent.putExtra("address", address);

		startActivityAnimation(intent);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		bmapView.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		bmapView.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		bmapView.onDestroy();
		unregisterReceiver(sdkReceiver);
	}

}
