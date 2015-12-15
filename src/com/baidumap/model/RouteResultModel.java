package com.baidumap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.TransitRouteLine;

public class RouteResultModel implements Serializable{
	private String stPlace;  //ÆðÊ¼µã
	private String enPlace;
	private ArrayList<String> routeList;
	private SearchResult result;
	
	private List<TransitRouteLine> routeLineList;
	
	public RouteResultModel(String stPlace, String enPlace,
			ArrayList<String> routeList, List<TransitRouteLine> routeLines) {
		super();
		this.stPlace = stPlace;
		this.enPlace = enPlace;
		this.routeList = routeList;
		this.routeLineList = routeLines;
	}
	
	public RouteResultModel(String stPlace, String enPlace,
			ArrayList<String> routeList, SearchResult result) {
		super();
		this.stPlace = stPlace;
		this.enPlace = enPlace;
		this.routeList = routeList;
		this.result = result;
	}
	public String getStPlace() {
		return stPlace;
	}
	public void setStPlace(String stPlace) {
		this.stPlace = stPlace;
	}
	public String getEnPlace() {
		return enPlace;
	}
	public void setEnPlace(String enPlace) {
		this.enPlace = enPlace;
	}
	public ArrayList<String> getRouteList() {
		return routeList;
	}
	public void setRouteList(ArrayList<String> routeList) {
		this.routeList = routeList;
	}
	public SearchResult getResult() {
		return result;
	}
	public void setResult(SearchResult result) {
		this.result = result;
	}
	public List<TransitRouteLine> getRouteLineList() {
		return routeLineList;
	}
	public void setRouteLineList(List<TransitRouteLine> routeLineList) {
		this.routeLineList = routeLineList;
	}
	
	
	
}
