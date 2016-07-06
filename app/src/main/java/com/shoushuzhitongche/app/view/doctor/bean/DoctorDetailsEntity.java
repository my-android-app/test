package com.shoushuzhitongche.app.view.doctor.bean;

import java.io.Serializable;

public class DoctorDetailsEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String hospitalId; 
	private String hospitalName; 
	private String mTitle;
	private String aTitle;
	private String imageUrl;
	private String hpDeptId;
	private String hpDeptName; 
	private String isExpteam; 
	private String description;
	private String careerExp;
	private String doctorInfo;
	private String  hpDeptForHospital;
	/**是否是预约专家*/
	private String isContracted;
	
	
	public String getIsContracted() {
		return isContracted;
	}
	public void setIsContracted(String isContracted) {
		this.isContracted = isContracted;
	}
	private String honour[];
	private String reasons[];
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
	public String getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(String hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getaTitle() {
		return aTitle;
	}
	public void setaTitle(String aTitle) {
		this.aTitle = aTitle;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getHpDeptId() {
		return hpDeptId;
	}
	public void setHpDeptId(String hpDeptId) {
		this.hpDeptId = hpDeptId;
	}
	public String getHpDeptName() {
		return hpDeptName;
	}
	public void setHpDeptName(String hpDeptName) {
		this.hpDeptName = hpDeptName;
	}
	public String getIsExpteam() {
		return isExpteam;
	}
	public void setIsExpteam(String isExpteam) {
		this.isExpteam = isExpteam;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCareerExp() {
		return careerExp;
	}
	public void setCareerExp(String careerExp) {
		this.careerExp = careerExp;
	}
	public String[] getHonour() {
		return honour;
	}
	public void setHonour(String[] honour) {
		this.honour = honour;
	}
	public String[] getReasons() {
		return reasons;
	}
	public void setReasons(String[] reasons) {
		this.reasons = reasons;
	}
	public String getDoctorInfo() {
		return doctorInfo;
	}
	public void setDoctorInfo(String doctorInfo) {
		this.doctorInfo = doctorInfo;
	}
	public String getHpDeptForHospital() {
		return hpDeptForHospital;
	}
	public void setHpDeptForHospital(String hpDeptForHospital) {
		this.hpDeptForHospital = hpDeptForHospital;
	}
	
	
}
