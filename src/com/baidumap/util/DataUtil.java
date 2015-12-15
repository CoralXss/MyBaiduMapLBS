package com.baidumap.util;

import java.util.ArrayList;
import java.util.LinkedList;

import android.util.SparseArray;

public class DataUtil {
	private ArrayList<String> areaGroupList = new ArrayList<String>();
	private SparseArray<LinkedList<String>> areaChildList = new SparseArray<LinkedList<String>>();
	private ArrayList<LinkedList<String>> childItemList = new ArrayList<LinkedList<String>>();
	
	private ArrayList<String> priceList = new ArrayList<String>();
	private ArrayList<String> distanceList = new ArrayList<String>();
	private ArrayList<String> typeList = new ArrayList<String>();   
	
	public DataUtil() {
		
	}
	
	public DataUtil(ArrayList<String> areaGroupList, SparseArray<LinkedList<String>> areaChildList, 
			ArrayList<String> priceList, ArrayList<String> distanceList, ArrayList<String> typeList) {
		
		this.areaGroupList = areaGroupList;
		this.areaChildList = areaChildList;
		this.priceList = priceList;
		this.distanceList = distanceList;
		this.typeList = typeList;
	}
	
	public void initSeaarchData() {
		initAreaList();  
		initPriceList();   
		initDistanceList();  
		initTypeList();
	}
	
	protected void initAreaList() {
		areaGroupList.add("不限");
		areaGroupList.add("福田");
		areaGroupList.add("南山");
		areaGroupList.add("宝安");
		
		LinkedList<String> linklist1 = new LinkedList<String>();
//		linklist1.add("");
//		LinkedList<String> linklist2 = new LinkedList<String>();
//		linklist2.add("1公里内");
//		linklist2.add("3公里内");
//		linklist2.add("5公里内");
		LinkedList<String> linklist3 = new LinkedList<String>();
		linklist3.add("不限");
		linklist3.add("皇岗");
		linklist3.add("景田");
		linklist3.add("梅林");
		linklist3.add("华强");
		LinkedList<String> linklist4 = new LinkedList<String>();
		linklist4.add("不限");
		linklist4.add("前海");
		linklist4.add("蛇口");
		LinkedList<String> linklist5 = new LinkedList<String>();
		linklist5.add("不限");
		linklist5.add("西乡");
		linklist5.add("宝安中心区");
		linklist5.add("新安");
		linklist5.add("福永");
		
		childItemList.add(linklist1);
//		childItemList.add(linklist2);
		childItemList.add(linklist3);
		childItemList.add(linklist4);
		childItemList.add(linklist5);
		
		for (int i = 0; i < areaGroupList.size(); i++) {
			areaChildList.put(i, childItemList.get(i));
		}
	}

	protected void initPriceList() {
		priceList.add("不限");
		priceList.add("100元以下");
		priceList.add("100-150元");
		priceList.add("150-200元");
		priceList.add("200-250元");
		priceList.add("250-300元");
		priceList.add("300元以上");
	}
	
	protected void initDistanceList() {
		distanceList.add("不限");
		distanceList.add("1000米");
		distanceList.add("2000米");
		distanceList.add("3000米");
	}
	
	protected void initTypeList() {
		typeList.add("不限");
		typeList.add("整租");
		typeList.add("单间出租");
		typeList.add("单间出租(隔断)");
		typeList.add("床位出租");
	}
}
