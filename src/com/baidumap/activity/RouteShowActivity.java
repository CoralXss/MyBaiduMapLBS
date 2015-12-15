package com.baidumap.activity;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidumap.activity.R;
import com.baidumap.model.RouteResultModel;

import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;

public class RouteShowActivity extends BaseActivity {
	
	private RouteResultModel resultModel;
	
	private List<TransitRouteLine> routeLineList;
	private String stPlace;
	private String enPlace;
	private int index;
	private SearchResult result;
	
	private MapView mapView;
	private BaiduMap baiduMap;
	
	private RouteLine routeLine;
	private OverlayManager overlayManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_show);
		setTitle("ÏêÇé");
		initLeftButton();
		
		resultModel = MyApplication.getInstance().getResultModel();   Log.e("hh show", resultModel.getEnPlace());
		stPlace = resultModel.getStPlace();
		enPlace = resultModel.getEnPlace();
//		result = resultModel.getResult();
//		routeLineList = (List<TransitRouteLine>) resultModel.getRouteLineList();
		
		result = resultModel.getResult();
		if (result instanceof TransitRouteResult) {
			routeLineList = ((TransitRouteResult) result).getRouteLines();
		}
		
		if (getIntent() != null) {
			index = getIntent().getIntExtra("index", 0);			
		}
		
		initView();
		initData();
	}

	@Override
	protected void initView() {
		mapView = (MapView)findViewById(R.id.mapView);
		baiduMap = mapView.getMap();
		
		Log.e("hh index", index+"");
		Log.e("hh show", ""+(routeLineList==null));  //Îª¿Õ
		
		routeLine = routeLineList.get(index);  
		TransitRouteOverlay overlay = new TransitRouteOverlay(baiduMap);
//		if (result instanceof TransitRouteResult) {
//			overlay.setData(((TransitRouteResult) result).getRouteLines().get(index));
//		}	
		overlay.setData(routeLineList.get(index));
		overlay.addToMap();
		overlay.zoomToSpan();
	}

	@Override
	protected void initData() {

	}

}
