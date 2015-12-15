package com.baidumap.adapter;

import java.util.List;

import com.baidumap.activity.R;
import com.baidumap.adapter.PoiSearchListAdapter.ViewHolder;
import com.baidumap.model.RentInfoModel;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RentListAdapter extends BaseAdapter {
	private Context ctx;
	private List<RentInfoModel> list;
	
	public RentListAdapter(Context ctx, List<RentInfoModel> list) {
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
		return arg0;
	}

	@Override
	public View getView(int pos, View v, ViewGroup group) {
		ViewHolder holder = null;
		
		if (v == null) {
			v = LayoutInflater.from(ctx).inflate(R.layout.adapter_rent_list_item, null);
			holder = new ViewHolder();
			holder.tv_house_name = (TextView)v.findViewById(R.id.tv_house_name);
			holder.tv_house_addr = (TextView)v.findViewById(R.id.tv_house_addr);
			holder.tv_house_price = (TextView)v.findViewById(R.id.tv_house_price);
			holder.tv_house_distance = (TextView)v.findViewById(R.id.tv_house_distance);
			holder.tv_house_leaseType = (TextView)v.findViewById(R.id.tv_house_type);
			holder.img_logo = (ImageView)v.findViewById(R.id.img_logo);
			holder.tv_house_area = (TextView)v.findViewById(R.id.tv_house_area);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		holder.tv_house_name.setText(list.get(pos).getTitle());
		holder.tv_house_addr.setText("地址:"+list.get(pos).getAddress());
		double dis = Double.parseDouble(list.get(pos).getDistance());
		if (dis > 1000) {
			holder.tv_house_distance.setText("距离当前位置:" + (dis / 1000) + "千米");
		} else {
			holder.tv_house_distance.setText("距离当前位置:" + dis + "米");
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(list.get(pos).getPrice())
		  .append("<font color='")		  
		  .append("#ff0000")
		  .append("'>")
		  .append("元/晚")
		  .append("</font>");
		holder.tv_house_price.setText(Html.fromHtml(sb.toString()));
		holder.tv_house_leaseType.setText("出租类型:"+list.get(pos).getRent_type());
		holder.tv_house_area.setText(list.get(pos).getHouse_area());
		Picasso.with(ctx).load(list.get(pos).getImg_logo()).into(holder.img_logo);
		
		return v;
	}
	
	class ViewHolder {
		TextView tv_house_addr;
		TextView tv_house_name;
		TextView tv_house_distance;
		TextView tv_house_price;
		TextView tv_house_leaseType;
		ImageView img_logo;
		TextView tv_house_area;
	}

}
