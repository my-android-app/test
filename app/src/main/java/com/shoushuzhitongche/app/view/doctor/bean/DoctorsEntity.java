package com.shoushuzhitongche.app.view.doctor.bean;

import java.io.Serializable;

public class DoctorsEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/**医生id*/
	private String id;
	/**医生姓名*/
	private String name;
	private String mTitle; // 主任医师
	private String aTitle; // 教授
	/**医院名称*/
	private String hpName;
	private String hospital;//特殊
	/**医院科室编号*/
	private String hpDeptId;
	private String hpDeptName; // 口腔科
	private String desc; // 擅长描述
	private String imageUrl;// 图片路径
	private String actionUrl;//预约网页
	
	/**详情地址*/
	private String doctorInfoUrl;
	
	/**是否是预约专家*/
	private String isContracted;
	private String bookingUrl;//部门到医生用的
	public String getId()
	{
		return id;
	}
	public void setId( String id )
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName( String name )
	{
		this.name = name;
	}
	public String getmTitle()
	{
		return mTitle;
	}
	public void setmTitle( String mTitle )
	{
		this.mTitle = mTitle;
	}
	public String getaTitle()
	{
		return aTitle;
	}
	public void setaTitle( String aTitle )
	{
		this.aTitle = aTitle;
	}
	public String getHpName()
	{
		return hpName;
	}
	public void setHpName( String hpName )
	{
		this.hpName = hpName;
	}
	
	public String getHpDeptName()
	{
		return hpDeptName;
	}
	public void setHpDeptName( String hpDeptName )
	{
		this.hpDeptName = hpDeptName;
	}
	public String getDesc()
	{
		return desc;
	}
	public void setDesc( String desc )
	{
		this.desc = desc;
	}
	public String getImageUrl()
	{
		return imageUrl;
	}
	public void setImageUrl( String imageUrl )
	{
		this.imageUrl = imageUrl;
	}
	public String getHpDeptId() {
		return hpDeptId;
	}
	public void setHpDeptId(String hpDeptId) {
		this.hpDeptId = hpDeptId;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getHospital()
	{
		return hospital;
	}
	public void setHospital( String hospital )
	{
		this.hospital = hospital;
	}
	public String getBookingUrl()
	{
		return bookingUrl;
	}
	public void setBookingUrl( String bookingUrl )
	{
		this.bookingUrl = bookingUrl;
	}
	public String getIsContracted()
	{
		return isContracted;
	}
	public void setIsContracted( String isContracted )
	{
		this.isContracted = isContracted;
	}
	public String getDoctorInfoUrl() {
		return doctorInfoUrl;
	}
	public void setDoctorInfoUrl(String doctorInfoUrl) {
		this.doctorInfoUrl = doctorInfoUrl;
	}
	
}
