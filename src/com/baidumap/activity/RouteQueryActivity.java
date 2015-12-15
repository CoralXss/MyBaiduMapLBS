package com.baidumap.activity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidumap.activity.R;
import com.baidumap.model.RouteResultModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class RouteQueryActivity extends BaseActivity implements OnClickListener, OnGetRoutePlanResultListener{
	private TextView tv_bus, tv_car, tv_walk;
	private EditText edt_startPace, edt_destination;
	private Button btn_search_route;
	
	private RoutePlanSearch routePlanSearch;  // 搜索模块，也可去掉地图模块独立使用
	private RouteLine route = null;           //路线
	
	double latitude;
	double longtitude;
	String address;
	String stPlace;  //起始点
	String enPlace;	
	String city;
	private ArrayList<String> routeList;         //路线节点描述
	private ArrayList<RouteLine> routeLineList;  //可以设置地图上的路线
	
//	private ArrayList<RouteResultModel> routeResultList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_query);
		
//		initLeftButton();
		
		if (getIntent() != null) {
			latitude = getIntent().getDoubleExtra("latitude", 0);
			longtitude = getIntent().getDoubleExtra("longtitude", 0);
			stPlace = getIntent().getStringExtra("address");			
			enPlace = getIntent().getStringExtra("enPlace");	
			city = getIntent().getStringExtra("city");
		}
		
		if (enPlace != null) {
			search();
		}
		
		initView();
		initData();
	}


	@Override
	protected void initView() {
		//初始化搜索模块，注册事件监听
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(this);
		
		tv_bus = (TextView)findViewById(R.id.tv_bus);
		tv_car = (TextView)findViewById(R.id.tv_car);
		tv_walk = (TextView)findViewById(R.id.tv_walk);
		
		edt_startPace = (EditText)findViewById(R.id.edt_startPace);
		edt_destination = (EditText)findViewById(R.id.edt_destination);
		if (!"".equals(getIntent().getStringExtra("enPlace"))) {
			edt_destination.setText(enPlace);
		}
		
		btn_search_route = (Button)findViewById(R.id.btn_search_route);
		
		tv_bus.setOnClickListener(this);
		tv_car.setOnClickListener(this);
		tv_walk.setOnClickListener(this);
		btn_search_route.setOnClickListener(this);		
		
		edt_startPace.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				stPlace = edt_startPace.getText().toString();   
			}
		});
	}


	@Override
	protected void initData() {
		tv_bus.setSelected(true);
		tv_car.setSelected(false);
		tv_walk.setSelected(false);
		
		routeList = new ArrayList<String>();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_bus:   Log.e("hh", "bus click");
			tv_bus.setSelected(true);
			tv_car.setSelected(false);
			tv_walk.setSelected(false);
			break;
		case R.id.tv_car:   Log.e("hh", "car click");
			tv_car.setSelected(true);
			tv_bus.setSelected(false);
			tv_walk.setSelected(false);
			break;			
		case R.id.tv_walk:  Log.e("hh", "walk click");
			tv_walk.setSelected(true);
			tv_car.setSelected(false);
			tv_bus.setSelected(false);
		case R.id.btn_search_route:    Log.e("hh", "search" + "stPlace = "+stPlace); 
			search();
			break;
		default:
			break;
		}
	}
	
	protected void search() {
		getDialogLoading(true).showDialog();
		
		enPlace = edt_destination.getText().toString();
		//设置起始点，对于tranist search 来说，城市名无意义
		PlanNode stNode = PlanNode.withCityNameAndPlaceName(address, stPlace);
		PlanNode enNode = PlanNode.withCityNameAndPlaceName(address, enPlace);

		// 实际使用中请对起点终点城市进行正确的设定
		if (tv_bus.isSelected()) {    Log.e("hh", "bus search");
			routePlanSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(city).to(enNode));
		} else if (tv_car.isSelected()) {
			routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
		} else if (tv_walk.isSelected()) {
			routePlanSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
		}	
		getDialogLoading(true).closeDialog();
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {   //查询自驾路线  
		if (result == null && result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteQueryActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			//输入起始点有歧义，通过以下接口来获得建议路线
			result.getSuggestAddrInfo();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			List<DrivingRouteLine> routeLines = result.getRouteLines();   //获取所有路线
			List<RouteNode> routeNodes = routeLines.get(0).getWayPoints();
			
			
			Log.e("hh", routeNodes.get(0).getTitle() + routeNodes.get(0).getLocation().latitude+","+routeNodes.get(0).getLocation().longitude);
		}
	}


	List<TransitRouteLine> routeLines;
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {   //查询公交路线  
		//Log.e("hh", "baidu bus search" + result.error);
		
		if (result == null && result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteQueryActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			//输入起始点有歧义，通过以下接口来获得建议路线
			result.getSuggestAddrInfo();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			if (routeList != null) {
				routeList.clear();
			}
			
			routeLines = result.getRouteLines(); 
			for (TransitRouteLine routeLine: routeLines) {
				StringBuffer sb = new StringBuffer();
				List<TransitStep> steps = routeLine.getAllStep();   
				for (int i = 0; i < steps.size(); i++) {					
					sb.append(steps.get(i).getInstructions());
				}
				routeList.add(sb.toString());			
			}
			
			Log.e("hh", result.toString());
			Log.e("hh", result.getRouteLines().toString());
			
//			RouteResultModel resultModel = new RouteResultModel(stPlace, enPlace, routeList, routeLines);
			RouteResultModel resultModel = new RouteResultModel(stPlace, enPlace, routeList, result);
			
			Intent intent = new Intent(RouteQueryActivity.this, RouteListActivity.class);
			
			MyApplication app = MyApplication.getInstance();
			app.setResultModel(resultModel);   //全局的路线
			
//			intent.putExtra("result", result.getRouteLines().toString());
			
			intent.putStringArrayListExtra("routeList", routeList);
//			intent.putExtra("stPlcae", stPlace);
//			intent.putExtra("enPlace", enPlace);
//			intent.putExtra("resultModel", resultModel);
			startActivityAnimation(intent);   
			//finish();
		}
	}


	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {   //查询步行路线
		 
	}	

}
