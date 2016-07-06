package com.shoushuzhitongche.app.view.order.bean;

import java.io.Serializable;
import java.util.List;

public class PatientInfoEntity implements Serializable {

	private String id;
	private String name;
	private String age;
	private String ageMonth;
	private String birthYear;
	private String birthMonth;
	private String gender;
	private String stateName;
	private String cityName;
	private String diseaseName;
	private String diseaseDetail;
	private String mobile;
	private String dateUpdated;
	
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	public String getDiseaseDetail() {
		return diseaseDetail;
	}
	public void setDiseaseDetail(String diseaseDetail) {
		this.diseaseDetail = diseaseDetail;
	}
	public String getBirthYear()
	{
		return birthYear;
	}
	public void setBirthYear( String birthYear )
	{
		this.birthYear = birthYear;
	}
	public String getBirthMonth()
	{
		return birthMonth;
	}
	public void setBirthMonth( String birthMonth )
	{
		this.birthMonth = birthMonth;
	}
	public String getStateName()
	{
		return stateName;
	}
	public void setStateName( String stateName )
	{
		this.stateName = stateName;
	}
	public String getAgeMonth() {
		return ageMonth;
	}
	public String getMobile() {
		return mobile;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public void setAgeMonth(String ageMonth) {
		this.ageMonth = ageMonth;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	
	
}
