package com.shoushuzhitongche.app.view.order.bean;

import java.io.Serializable;

public class PatientBookingEntity implements Serializable {

	private String id;
	private String refNo;
	private String creatorId;
	private String creatorName;
	
	private String doctorName;
	private String hospitalName;
	private String departmentName;
	
	private String status;
	private String statusCode;
	private String travelType;
	private String dateStart;
	private String dateEnd;
	private String detail;
	private String apptDate;
	private String dateConfirm;
	private String remark;
	private String dateCreated;
	private String dateUpdated;
	private String dateNow;
	private String payment;
	private String csExplain;
	private String expected_doctor;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getTravelType() {
		return travelType;
	}
	public void setTravelType(String travelType) {
		this.travelType = travelType;
	}
	public String getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getApptDate() {
		return apptDate;
	}
	public void setApptDate(String apptDate) {
		this.apptDate = apptDate;
	}
	public String getDateConfirm() {
		return dateConfirm;
	}
	public void setDateConfirm(String dateConfirm) {
		this.dateConfirm = dateConfirm;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	public String getDateNow() {
		return dateNow;
	}
	public void setDateNow(String dateNow) {
		this.dateNow = dateNow;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getExpected_doctor() {
		return expected_doctor;
	}
	public void setExpected_doctor(String expected_doctor) {
		this.expected_doctor = expected_doctor;
	}
	public String getCsExplain() {
		return csExplain;
	}
	public void setCsExplain(String csExplain) {
		this.csExplain = csExplain;
	}
}
