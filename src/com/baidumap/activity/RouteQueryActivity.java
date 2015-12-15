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
	
	private RoutePlanSearch routePlanSearch;  // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	private RouteLine route = null;           //·��
	
	double latitude;
	double longtitude;
	String address;
	String stPlace;  //��ʼ��
	String enPlace;	
	String city;
	private ArrayList<String> routeList;         //·�߽ڵ�����
	private ArrayList<RouteLine> routeLineList;  //�������õ�ͼ�ϵ�·��
	
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
		//��ʼ������ģ�飬ע���¼�����
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
		//������ʼ�㣬����tranist search ��˵��������������
		PlanNode stNode = PlanNode.withCityNameAndPlaceName(address, stPlace);
		PlanNode enNode = PlanNode.withCityNameAndPlaceName(address, enPlace);

		// ʵ��ʹ�����������յ���н�����ȷ���趨
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
	public void onGetDrivingRouteResult(DrivingRouteResult result) {   //��ѯ�Լ�·��  
		if (result == null && result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteQueryActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			//������ʼ�������壬ͨ�����½ӿ�����ý���·��
			result.getSuggestAddrInfo();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			List<DrivingRouteLine> routeLines = result.getRouteLines();   //��ȡ����·��
			List<RouteNode> routeNodes = routeLines.get(0).getWayPoints();
			
			
			Log.e("hh", routeNodes.get(0).getTitle() + routeNodes.get(0).getLocation().latitude+","+routeNodes.get(0).getLocation().longitude);
		}
	}


	List<TransitRouteLine> routeLines;
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {   //��ѯ����·��  
		//Log.e("hh", "baidu bus search" + result.error);
		
		if (result == null && result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(RouteQueryActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			//������ʼ�������壬ͨ�����½ӿ�����ý���·��
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
			app.setResultModel(resultModel);   //ȫ�ֵ�·��
			
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
	public void onGetWalkingRouteResult(WalkingRouteResult result) {   //��ѯ����·��
		 
	}	

}
