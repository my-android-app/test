package com.shoushuzhitongche.app.view.order.bean;

import java.io.Serializable;
public class OrderItemEntity implements Serializable {

	private String id;
	private String refNo;
	private String status;
	private String statusText;
	private String isDepositPaid;
	private String dateUpdated;
	private String patientId;
	private String name;
	private String dataCreate;
	private String diseaseName;
	private String diseaseDetail;
	private String age;
	private String ageMonth;
	private String actionUrl;
	
	
	private String travelType;
	public String getId() {
		return id;
	}
	public String getRefNo() {
		return refNo;
	}
	public String getStatus() {
		return status;
	}
	public String getIsDepositPaid() {
		return isDepositPaid;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public String getPatientId() {
		return patientId;
	}
	public String getName() {
		return name;
	}
	public String getDataCreate() {
		return dataCreate;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public String getDiseaseDetail() {
		return diseaseDetail;
	}
	public String getAge() {
		return age;
	}
	public String getAgeMonth() {
		return ageMonth;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setIsDepositPaid(String isDepositPaid) {
		this.isDepositPaid = isDepositPaid;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDataCreate(String dataCreate) {
		this.dataCreate = dataCreate;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	public void setDiseaseDetail(String diseaseDetail) {
		this.diseaseDetail = diseaseDetail;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public void setAgeMonth(String ageMonth) {
		this.ageMonth = ageMonth;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getTravelType() {
		return travelType;
	}
	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	
}
