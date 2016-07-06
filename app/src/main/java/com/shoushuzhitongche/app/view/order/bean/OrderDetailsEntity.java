package com.shoushuzhitongche.app.view.order.bean;

import java.io.Serializable;
import java.util.List;

public class OrderDetailsEntity implements Serializable {

	private PatientInfoEntity patientInfo;
	private PatientBookingEntity patientBooking;
	private List<SalesOrderEntity> salesOrder;
	public PatientInfoEntity getPatientInfo() {
		return patientInfo;
	}
	public PatientBookingEntity getPatientBooking() {
		return patientBooking;
	}
	public void setPatientInfo(PatientInfoEntity patientInfo) {
		this.patientInfo = patientInfo;
	}
	public void setPatientBooking(PatientBookingEntity patientBooking) {
		this.patientBooking = patientBooking;
	}
	public List<SalesOrderEntity> getSalesOrder() {
		return salesOrder;
	}
	public void setSalesOrder(List<SalesOrderEntity> salesOrder) {
		this.salesOrder = salesOrder;
	}
	
}
