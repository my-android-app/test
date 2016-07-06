package com.shoushuzhitongche.app.view.doctor.bean;

import java.io.Serializable;
import java.util.List;

public class DeptSubCatEntity implements Serializable{
	
	public String id;
	public String nasId;
	public String name;
	
	public String getName()
	{
		return name;
	}
	public void setName( String name )
	{
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNasId() {
		return nasId;
	}
	public void setNasId(String nasId) {
		this.nasId = nasId;
	}

}
