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
		areaGroupList.add("����");
		areaGroupList.add("����");
		areaGroupList.add("��ɽ");
		areaGroupList.add("����");
		
		LinkedList<String> linklist1 = new LinkedList<String>();
//		linklist1.add("");
//		LinkedList<String> linklist2 = new LinkedList<String>();
//		linklist2.add("1������");
//		linklist2.add("3������");
//		linklist2.add("5������");
		LinkedList<String> linklist3 = new LinkedList<String>();
		linklist3.add("����");
		linklist3.add("�ʸ�");
		linklist3.add("����");
		linklist3.add("÷��");
		linklist3.add("��ǿ");
		LinkedList<String> linklist4 = new LinkedList<String>();
		linklist4.add("����");
		linklist4.add("ǰ��");
		linklist4.add("�߿�");
		LinkedList<String> linklist5 = new LinkedList<String>();
		linklist5.add("����");
		linklist5.add("����");
		linklist5.add("����������");
		linklist5.add("�°�");
		linklist5.add("����");
		
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
		priceList.add("����");
		priceList.add("100Ԫ����");
		priceList.add("100-150Ԫ");
		priceList.add("150-200Ԫ");
		priceList.add("200-250Ԫ");
		priceList.add("250-300Ԫ");
		priceList.add("300Ԫ����");
	}
	
	protected void initDistanceList() {
		distanceList.add("����");
		distanceList.add("1000��");
		distanceList.add("2000��");
		distanceList.add("3000��");
	}
	
	protected void initTypeList() {
		typeList.add("����");
		typeList.add("����");
		typeList.add("�������");
		typeList.add("�������(����)");
		typeList.add("��λ����");
	}
}
