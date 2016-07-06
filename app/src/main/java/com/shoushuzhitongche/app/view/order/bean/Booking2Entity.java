package com.shoushuzhitongche.app.view.order.bean;

import java.io.Serializable;

public class Booking2Entity implements Serializable {

	private String id;
	private String refNo;
	private String doctorName;
	private String expectedDoctor;
	private String patientId;
	private String patientName;
	private String statusTitle;
	private String statusCode;
	private String travelType;
	private String detail;
	private String dateCreated;
	private String actionUrl;
	private String payUrl;
	
	public String getId() {
		return id;
	}
	public String getRefNo() {
		return refNo;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public String getExpectedDoctor() {
		return expectedDoctor;
	}
	public String getPatientId() {
		return patientId;
	}
	public String getPatientName() {
		return patientName;
	}
	public String getStatusTitle() {
		return statusTitle;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public String getTravelType() {
		return travelType;
	}
	public String getDetail() {
		return detail;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public void setExpectedDoctor(String expectedDoctor) {
		this.expectedDoctor = expectedDoctor;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public void setStatusTitle(String statusTitle) {
		this.statusTitle = statusTitle;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	
	
}
