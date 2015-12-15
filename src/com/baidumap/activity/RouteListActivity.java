package com.baidumap.activity;

import java.util.ArrayList;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidumap.activity.R;
import com.baidumap.adapter.RouteListAdapter;
import com.baidumap.model.RouteResultModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RouteListActivity extends BaseActivity {
	
	private ListView lv_route_list;
	private RouteResultModel resultModel;
	
	private ArrayList<String> routeList;
//	private ArrayList<RouteLine> routeLineList;
	private String stPlace;
	private String enPlace;
	
	private SearchResult result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_list);
		setTitle("路线列表");
		initLeftButton();
		
		resultModel = MyApplication.getInstance().getResultModel();
//		routeList = resultModel.getRouteList();
		stPlace = resultModel.getStPlace();
		enPlace = resultModel.getEnPlace();
		
		Log.e("hh list", resultModel.getEnPlace());
		if (getIntent() != null) {
			//resultModel = (RouteResultModel) getIntent().getSerializableExtra("resultModel");
			
	//		result = resultModel.getResult();
			
			routeList = getIntent().getStringArrayListExtra("routeList");
//			Log.e("hh list", routeList.get(0));
//			stPlace = getIntent().getStringExtra("stPlcae");
//			enPlace = getIntent().getStringExtra("enPlace");
		}
		
		initView();
		initData();
	}

	@Override
	protected void initView() {
		lv_route_list = (ListView)findViewById(R.id.lv_route_list);		
	}

	@Override
	protected void initData() {
		RouteListAdapter adapter = new RouteListAdapter(RouteListActivity.this, routeList);
		lv_route_list.setAdapter(adapter);
		
		lv_route_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
				Intent intent = new Intent(RouteListActivity.this, RouteShowActivity.class);
				//intent.putExtra("resultModel", resultModel);
				intent.putExtra("index", pos);
				startActivityAnimation(intent);
				finish();
			}
		});
	}

}
