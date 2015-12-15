package com.baidumap.adapter;

import java.util.List;

import com.baidumap.activity.R;
import com.baidumap.model.RouteResultModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RouteListAdapter extends BaseAdapter {
	private Context ctx;
//	private List<Object> list;
	private List<String> list;  
	
//	private List<RouteResultModel> list;
	
//	public RouteListAdapter(Context ctx, List<Object> list) {  
//		this.ctx = ctx;
//		this.list = list;
//	}
	
	public RouteListAdapter(Context ctx, List<String> list) {
		this.ctx = ctx;
		this.list = list;
	}
	
//	public RouteListAdapter(Context ctx, List<RouteResultModel> list) {
//		this.ctx = ctx;
//		this.list = list;
//	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View v, ViewGroup arg2) {
		ViewHolder holder = null;
		if (v == null) {
			v = LayoutInflater.from(ctx).inflate(R.layout.adapter_route_list_item, null);
			holder = new ViewHolder();
			holder.tv_route = (TextView)v.findViewById(R.id.tv_route);
			
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		Object step = list.get(pos);
//		if (step instanceof TransitStep) {
//			holder.tv_route.setText(((TransitStep)list.get(pos)).getInstructions());
//		} else if (step instanceof WalkingStep) {
//			holder.tv_route.setText(((WalkingStep)list.get(pos)).getInstructions());
//		} else if (step instanceof DrivingStep) {
//			holder.tv_route.setText(((DrivingStep)list.get(pos)).getInstructions());
//		}
		holder.tv_route.setText(list.get(pos));
 		
		return v;
	}
	
	class ViewHolder {
		public TextView tv_route;
	}

}
