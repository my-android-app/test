package com.shoushuzhitongche.app.view.localdata.bean;

import java.util.List;
import java.util.Map;

import com.shoushuzhitongche.app.view.doctor.bean.DisNavsEntity;

public class LocalDataEntity {
	private Map<String,String> academicTitle;
	
	private Map<String,String> clinicalTitle;
	
	private Map<String,String> gender;
	
	private Map<String,String> bookingTravelType;
	
	//接口地址
	private Map<String ,String> url;

	//城市
	private List<ProvinceEntity> city;
	
	private List<DisNavsEntity> dept;
	
	public Map<String, String> getAcademicTitle() {
		return academicTitle;
	}

	public void setAcademicTitle(Map<String, String> academicTitle) {
		this.academicTitle = academicTitle;
	}

	public Map<String, String> getClinicalTitle() {
		return clinicalTitle;
	}

	public void setClinicalTitle(Map<String, String> clinicalTitle) {
		this.clinicalTitle = clinicalTitle;
	}

	public Map<String, String> getGender() {
		return gender;
	}

	public void setGender(Map<String, String> gender) {
		this.gender = gender;
	}

	public Map<String, String> getBookingTravelType() {
		return bookingTravelType;
	}

	public void setBookingTravelType(Map<String, String> bookingTravelType) {
		this.bookingTravelType = bookingTravelType;
	}

	public List<ProvinceEntity> getCity() {
		return city;
	}

	public void setCity(List<ProvinceEntity> city) {
		this.city = city;
	}

	public Map<String, String> getUrl() {
		return url;
	}

	public void setUrl(Map<String, String> url) {
		this.url = url;
	}

	public List<DisNavsEntity> getDept() {
		return dept;
	}

	public void setDept(List<DisNavsEntity> dept) {
		this.dept = dept;
	}
	
	
}
