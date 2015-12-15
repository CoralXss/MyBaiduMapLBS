package com.baidumap.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidumap.activity.R;
import com.baidumap.adapter.RentListAdapter;
import com.baidumap.model.RentInfoModel;
import com.baidumap.util.Constant;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentRentMap extends Fragment {
	private View view;
	private ProgressBar progressBar;
	private List<RentInfoModel> rentList;
	
	private MapView mapView = null;
	private BaiduMap baiduMap;
	
	double latitude;
	double longtitude;
	
	private RentInfoModel rentInfoModel;
	private LatLng latlng;
	
	public Handler getmHandler() {
		return mHandler;
	}
	
	public FragmentRentMap() {
		
	}
	
	public FragmentRentMap(List<RentInfoModel> rentList) {
		this.rentList = rentList;
	}


	/*
	 * 处理网络请求
	 */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressBar != null) {
				progressBar.setVisibility(View.GONE);
			}
			switch (msg.what) {
			case Constant.MSG_NET_TIMEOUT:
				break;
			case Constant.MSG_NET_STATUS_ERROR:
				break;
			case Constant.MSG_NET_SUCC:
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					Log.e("json", "map: "+json.toString());
					parser(json);

					initMapView();
				}catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
			
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_map, container, false);
		
		if (getArguments() != null) {
			latitude = getArguments().getDouble("latitude");
			longtitude = getArguments().getDouble("longtitude");
		}

		progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
		
		mapView = (MapView)view.findViewById(R.id.mapView);
		mapView.showZoomControls(true);
		baiduMap = mapView.getMap();
		
		
		
		return view;
	}
	
	protected void initMapView() {		
		
		//设置地图中心点坐标
		LatLng latLng = new LatLng(latitude, longtitude);
		MapStatus status = new MapStatus.Builder().target(latLng).zoom(18).build();
		//定义MapS
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(status);
		//改变地图状态
		baiduMap.setMapStatus(mapStatusUpdate);
		
//		initOverlay();
		
		//添加标注
		if (rentList != null) {   
			for (int i = 0; i < rentList.size(); i++) {
				rentInfoModel = rentList.get(i);   Log.e("json", "init map: "+rentInfoModel.getLongitude());
				
				//39.963175, 116.400244
				//latlng = new LatLng(22.528244, 114.059388);
				latlng = new LatLng(rentInfoModel.getLatitude(), rentInfoModel.getLongitude());
				BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);				
				//构建MarkerOption，用于在地图上添加Marker  
				OverlayOptions option = new MarkerOptions().position(latlng).icon(bitmap);  
				//在地图上添加Marker，并显示  
				baiduMap.addOverlay(option);
				
				baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
					
					@Override
					public boolean onMarkerClick(Marker marker) {
						View v = LayoutInflater.from(getActivity()).inflate(R.layout.marker_info, null);
						InfoWindow infoWindow = new InfoWindow(v, latlng, -47);
						TextView tv_title = (TextView)v.findViewById(R.id.tv_title);   tv_title.setText(rentInfoModel.getTitle());
						TextView tv_area = (TextView)v.findViewById(R.id.tv_area);   tv_area.setText(rentInfoModel.getHouse_area());
						TextView tv_tags = (TextView)v.findViewById(R.id.tv_tags);   tv_title.setText(rentInfoModel.getTags());
						ImageView img_logo = (ImageView)v.findViewById(R.id.img_logo); 
						Picasso.with(getActivity()).load(rentInfoModel.getImg_logo()).into(img_logo);
						
						return true;
					}
				});
				
			}
		} 
	}
	
	protected void initOverlay() {
		latlng = new LatLng(22.528244, 114.059388);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);				
		//构建MarkerOption，用于在地图上添加Marker  
		OverlayOptions option = new MarkerOptions().position(latlng).icon(bitmap);  
		//在地图上添加Marker，并显示  
		baiduMap.addOverlay(option);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}
	
	
	/*
	 * 解析返回数据
	 */
	private void parser(JSONObject json) {

		rentList = new ArrayList<RentInfoModel>();

		try {
			//app.getListActivity().totalItem = json.getInt("total");

			JSONArray jsonArray = json.getJSONArray("contents");      Log.e("hh", json.toString());
			if (jsonArray != null && jsonArray.length() <= 0) {
				Toast.makeText(getActivity(), "没有符合要求的数据", Toast.LENGTH_SHORT).show();
			} else {
				rentList.clear();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					RentInfoModel content = new RentInfoModel();
					content.setUid(jsonObject2.getString("uid"));
					content.setTitle(jsonObject2.getString("title"));
					content.setAddress(jsonObject2.getString("address"));
					content.setPrice(jsonObject2.getString("price"));
					content.setTags(jsonObject2.getString("tags"));

					JSONArray locArray = jsonObject2.getJSONArray("location");
					double latitude = locArray.getDouble(1);
					double longitude = locArray.getDouble(0);
					content.setLatitude(latitude);
					content.setLongitude(longitude);

					float results[] = new float[1];

//						Location.distanceBetween(
//								this.latitude,
//								this.longtitude, latitude,
//								longitude, results);
						
					content.setDistance((int) results[0]+"");					
					content.setRent_type(jsonObject2.getString("rent_type"));
					content.setImg_logo(jsonObject2.getString("img_url"));
					content.setHouse_area(jsonObject2.getString("house_area"));
					
					rentList.add(content);					
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

}
