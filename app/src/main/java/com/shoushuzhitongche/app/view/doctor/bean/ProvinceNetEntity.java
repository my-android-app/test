package com.shoushuzhitongche.app.view.doctor.bean;

import java.util.List;

public class ProvinceNetEntity{
	private int id;
	private String state;
	private List<CityEntity> subCity;
	public int getId()
	{
		return id;
	}
	public void setId( int id )
	{
		this.id = id;
	}
	public String getState()
	{
		return state;
	}
	public void setState( String state )
	{
		this.state = state;
	}
	public List<CityEntity> getSubCity()
	{
		return subCity;
	}
	public void setSubCity( List<CityEntity> subCity )
	{
		this.subCity = subCity;
	}
	
	
}
