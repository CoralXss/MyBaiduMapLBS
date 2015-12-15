package com.baidumap.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidumap.activity.R;
import com.baidumap.adapter.RentListAdapter;
import com.baidumap.model.RentInfoModel;
import com.baidumap.util.Constant;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FragmentRentList extends Fragment {
	private View view;
	private ListView lv_rent_list;
	private ProgressBar progressBar;
	private List<RentInfoModel> rentList;
	private RentListAdapter adapter;
	
	double latitude;
	double longtitude;
	
	public Handler getmHandler() {
		return mHandler;
	}
	
	public FragmentRentList() {
		
	}
	
	public FragmentRentList(List<RentInfoModel> rentList) {
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
//				initSearchFlag = true;
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					Log.e("json", "list: "+json.toString());
					parser(json);
					
					setListViewData();
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}
	};
			
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragmet_rent_list, container, false);
		
		if (getArguments() != null) {
			latitude = getArguments().getDouble("latitude");
			longtitude = getArguments().getDouble("longtitude");
		}
		
		lv_rent_list = (ListView)view.findViewById(R.id.lv_rent_list);
		progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
		
		
		return view;
	}
	
	protected void setListViewData() {
		if (rentList != null) {
			adapter = new RentListAdapter(getActivity(), rentList);
			lv_rent_list.setAdapter(adapter);
		}
	}
	
	
	/*
	 * 解析返回数据
	 */
	private void parser(JSONObject json) {

		rentList = new ArrayList<RentInfoModel>();

		try {
			//app.getListActivity().totalItem = json.getInt("total");

			JSONArray jsonArray = json.getJSONArray("contents");    
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

					JSONArray locArray = jsonObject2.getJSONArray("location");
					double latitude = locArray.getDouble(1);
					double longitude = locArray.getDouble(0);
					content.setLatitude(latitude);
					content.setLongitude(longitude);

					float results[] = new float[1];

						Location.distanceBetween(
								this.latitude,
								this.longtitude, latitude,
								longitude, results);
						
					content.setDistance((int) results[0]+"");					
					content.setRent_type(jsonObject2.getString("rent_type"));
					content.setImg_logo(jsonObject2.getString("img_url"));
					content.setHouse_area(jsonObject2.getString("house_area"));
					
					rentList.add(content);
					
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
