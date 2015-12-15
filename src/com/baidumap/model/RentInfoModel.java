package com.baidumap.model;

public class RentInfoModel {
	private String uid;
	private String title;  //Ãû³Æ
	private String address;
	private String price; 
	private String distance;
	private String rent_type;
	private String house_area;  //Ãæ»ý
	private String img_logo;    //Í¼Æ¬url
	private double latitude;
	private double longitude;
	private String tags;
	
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getRent_type() {
		return rent_type;
	}
	public void setRent_type(String rent_type) {
		this.rent_type = rent_type;
	}
	public String getHouse_area() {
		return house_area;
	}
	public void setHouse_area(String house_area) {
		this.house_area = house_area;
	}
	public String getImg_logo() {
		return img_logo;
	}
	public void setImg_logo(String img_logo) {
		this.img_logo = img_logo;
	}
	
	

}
