package com.baidumap.activity;


import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidumap.activity.R;
import com.baidumap.adapter.PoiSearchListAdapter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class PoiSearchActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener{
	private AutoCompleteTextView edt_search_name;
	private Button btn_search;
	private ArrayAdapter<String> suggestionAddapter;
	
	private TextView tv_title;
	private ListView lv_poi_result;
	
	private MapView mapView;
	private BaiduMap baiduMap;
	
	private PoiSearch poiSearch;
	private SuggestionSearch suggestionSearch;
	
	double latitude;
	double longtitude;
	String address;
	String city;
	String search_text;
	
	private List<String> uids;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_search);
		
		if (getIntent() != null) {
			latitude = getIntent().getDoubleExtra("latitude", 0);
			longtitude = getIntent().getDoubleExtra("longtitude", 0);
			city = getIntent().getStringExtra("city");
		}
		
		initLeftButton();
		
		initView();
		initData();
	}

	@Override
	protected void initView() {
		mapView = (MapView)findViewById(R.id.mapView);
		baiduMap = mapView.getMap();
		
		//初始化搜索模块，注册搜索监听事件
		poiSearch = PoiSearch.newInstance();
		poiSearch.setOnGetPoiSearchResultListener(this);
		suggestionSearch = SuggestionSearch.newInstance();
		suggestionSearch.setOnGetSuggestionResultListener(this);
		
		tv_title = (TextView)findViewById(R.id.tv_title);
		lv_poi_result = (ListView)findViewById(R.id.lv_poi_result);
		
		edt_search_name = (AutoCompleteTextView)findViewById(R.id.edt_search_name);
		suggestionAddapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
		edt_search_name.setAdapter(suggestionAddapter);
//		edt_search_name.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
//				if (cs.length() < 0) {
//					return;
//				}
//				
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				//使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
//				suggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(edt_search_name.getText().toString()).city(city));
//			}
//		});
		
		
		btn_search = (Button)findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("搜索".equals(btn_search.getText().toString())) {
//					poiSearch.searchInCity((new PoiCitySearchOption())
//					.city(city)
//					.keyword(edt_search_name.getText().toString())
//					.pageNum(10));
					search_text = edt_search_name.getText().toString();
					poiSearch.searchNearby((new PoiNearbySearchOption())
							.location(new LatLng(latitude, longtitude))
							.keyword(search_text)
							.pageCapacity(10)
							.radius(1000));   //周边检索要设置半径
					
					//点击搜索，显示ListView
					tv_title.setVisibility(View.VISIBLE); 
					lv_poi_result.setVisibility(View.VISIBLE);
					mapView.setVisibility(View.GONE);
					//btn_search.setVisibility(View.GONE);
					btn_search.setText("地图");
					edt_search_name.setVisibility(View.GONE);
				} else {
//					poiSearch.searchInCity((new PoiCitySearchOption())
//					.city(city)
//					.keyword(edt_search_name.getText().toString())
//					.pageNum(10));
					search_text = edt_search_name.getText().toString();
					poiSearch.searchNearby((new PoiNearbySearchOption())
							.location(new LatLng(latitude, longtitude))
							.keyword(search_text)
							.pageCapacity(10)
							.radius(1000));   //周边检索要设置半径
					
					//点击搜索，显示ListView
					tv_title.setVisibility(View.GONE); 
					lv_poi_result.setVisibility(View.GONE);
					mapView.setVisibility(View.VISIBLE);
					//btn_search.setVisibility(View.VISIBLE);
					btn_search.setText("搜索");
					edt_search_name.setVisibility(View.VISIBLE);
				}

			}
		});
		
	}

	@Override
	protected void initData() {
		//设置地图中心点坐标
		LatLng latLng = new LatLng(latitude, longtitude);
		MapStatus status = new MapStatus.Builder().target(latLng).zoom(18).build();
		//定义MapS
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(status);
		//改变地图状态
		baiduMap.setMapStatus(mapStatusUpdate);
	}
	
	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(PoiSearchActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			baiduMap.clear();
			PoiOverlay overlay = new PoiOverlay(baiduMap);  
			
			if (lv_poi_result.getVisibility() == View.VISIBLE) {
				List<PoiInfo> poiInfos = result.getAllPoi();   //获取所有poi检索的结果
				Log.e("poi", "poi num ="+result.getTotalPoiNum());
				
				setTitle(search_text);
				PoiSearchListAdapter adapter = new PoiSearchListAdapter(PoiSearchActivity.this, poiInfos);
				lv_poi_result.setAdapter(adapter);
				
				uids = new ArrayList<String>();
				for (PoiInfo poiInfo: poiInfos) {
					//uids.add(poiInfo.uid);  //根据uid获取检索详情
					//uid是POI检索中获取的POI ID信息
					//poiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poiInfo.uid));
					
					StringBuffer sb = new StringBuffer();
					sb.append(poiInfo.address).append(poiInfo.city).append(poiInfo.hasCaterDetails).append(poiInfo.name);
					Log.e("poi", "addr ="+sb.toString());
				}
			} else {
				overlay.setData(result);
				overlay.addToMap();
				overlay.zoomToSpan();
			}
			
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
			StringBuffer sb = new StringBuffer();
			sb.append("在");
			for (CityInfo cityInfo: result.getSuggestCityList()) {
				sb.append(cityInfo.city).append(", ");
			}
			sb.append("找到结果");
			Toast.makeText(PoiSearchActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
			return;
		}
	}
	
	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
	        //详情检索失败
			Toast.makeText(PoiSearchActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
			return;
	    } else {
	        //检索成功
	    	Log.e("poi", "addr = " + result.getAddress());
	    }
	}
	
	@Override
	public void onGetSuggestionResult(SuggestionResult result) {
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
		poiSearch.destroy();
		suggestionSearch.destroy();
	}

	

	
}
