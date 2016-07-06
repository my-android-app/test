package com.shoushuzhitongche.app.view.receive.bean;

public class AppointEntity {
	private String  id;
	private String  dateUpdated;
	private String bkType;
	private String  name;
	private String  diseaseName;
	private String diseaseDetail;
	private String travelType;
	private String doctorAccept;
	private String actionUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	public String getTravelType() {
		return travelType;
	}
	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}
	public String getDoctorAccept() {
		return doctorAccept;
	}
	public void setDoctorAccept(String doctorAccept) {
		this.doctorAccept = doctorAccept;
	}
	public String getBkType() {
		return bkType;
	}
	public void setBkType(String bkType) {
		this.bkType = bkType;
	}
	public String getDiseaseDetail() {
		return diseaseDetail;
	}
	public void setDiseaseDetail(String diseaseDetail) {
		this.diseaseDetail = diseaseDetail;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
}
