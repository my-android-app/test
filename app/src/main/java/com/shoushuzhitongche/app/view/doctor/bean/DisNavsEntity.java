package com.shoushuzhitongche.app.view.doctor.bean;

import java.io.Serializable;
import java.util.List;


public class DisNavsEntity implements Serializable{
	public String id;
	public String name;
	public List<DeptSubCatEntity> subCat;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DeptSubCatEntity> getSubCat() {
		return subCat;
	}
	public void setSubCat(List<DeptSubCatEntity> subCat) {
		this.subCat = subCat;
	}
	
}
