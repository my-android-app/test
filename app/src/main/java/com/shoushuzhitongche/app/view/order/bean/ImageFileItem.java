package com.shoushuzhitongche.app.view.order.bean;

import java.io.Serializable;

public class ImageFileItem implements Serializable {
	public String id;
	public String absThumbnailUrl;
	public String absFileUrl;
	public String getId()
	{
		return id;
	}
	public void setId( String id )
	{
		this.id = id;
	}
	public String getAbsThumbnailUrl()
	{
		return absThumbnailUrl;
	}
	public void setAbsThumbnailUrl( String absThumbnailUrl )
	{
		this.absThumbnailUrl = absThumbnailUrl;
	}
	public String getAbsFileUrl()
	{
		return absFileUrl;
	}
	public void setAbsFileUrl( String absFileUrl )
	{
		this.absFileUrl = absFileUrl;
	}
	
}
