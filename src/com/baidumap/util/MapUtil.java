package com.baidumap.util;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapViewLayoutParams;

public class MapUtil {
	
	public static void performZoom(Context ctx, BaiduMap baiduMap, float zoomLevel) {
		if (zoomLevel > baiduMap.getMaxZoomLevel()) {
			Toast.makeText(ctx, "�ѷŴ�����߼���", Toast.LENGTH_SHORT).show();
			return;
		}
		if (zoomLevel < baiduMap.getMinZoomLevel()) {
			Toast.makeText(ctx, "����С����ͼ���", Toast.LENGTH_SHORT).show();
			return;
		}
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(zoomLevel);
		baiduMap.animateMapStatus(mapStatusUpdate);
		
	}

}
