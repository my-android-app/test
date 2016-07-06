package com.shoushuzhitongche.app.view.localdata.bean;

import java.util.List;

public class ProvinceEntity {
	private int id;
	private String state;
	private List<SubCityEntity> subCity;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<SubCityEntity> getSubCity() {
		return subCity;
	}
	public void setSubCity(List<SubCityEntity> subCity) {
		this.subCity = subCity;
	}
	
}
