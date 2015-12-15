package com.baidumap.adapter;

import java.util.List;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidumap.activity.R;
import com.baidumap.activity.RouteQueryActivity;
import com.baidumap.adapter.RouteListAdapter.ViewHolder;

import android.app.DownloadManager.Query;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PoiSearchListAdapter extends BaseAdapter {
	private Context ctx;
	private List<PoiInfo> list;
	
	public PoiSearchListAdapter(Context ctx, List<PoiInfo> list) {
		this.ctx = ctx;
		this.list = list;
	}

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
		return 0;
	}

	@Override
	public View getView(final int pos, View v, ViewGroup arg2) {
		ViewHolder holder = null;
		if (v == null) {
			v = LayoutInflater.from(ctx).inflate(R.layout.adapter_poi_detail_list_item, null);
			holder = new ViewHolder();
			holder.tv_poi_name = (TextView)v.findViewById(R.id.tv_poi_name);
			holder.tv_poi_address = (TextView)v.findViewById(R.id.tv_poi_address);
			holder.tv_arrive_here = (TextView)v.findViewById(R.id.tv_arrive_here);
			holder.tv_tel = (TextView)v.findViewById(R.id.tv_tel);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		holder.tv_poi_name.setText((pos+1) + "." + list.get(pos).name);
		holder.tv_poi_address.setText(list.get(pos).address);
		
		holder.tv_tel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + list.get(pos).phoneNum));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				ctx.startActivity(intent);
			}
		});
		
		holder.tv_arrive_here.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ctx, RouteQueryActivity.class);
				intent.putExtra("enPlace", list.get(pos).name);
				ctx.startActivity(intent);
			}
		});

		return v;
	}
	
	class ViewHolder {
		public TextView tv_poi_name;
		public TextView tv_poi_address;
		public TextView tv_arrive_here;
		public TextView tv_tel;
	}

}
