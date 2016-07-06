package com.shoushuzhitongche.app.view.patient.bean;

import java.util.List;

public class PatientTemEntity {
	private List<PatientEntity> hasBookingList;
	private List<PatientEntity> noBookingList;
	public List<PatientEntity> getHasBookingList() {
		return hasBookingList;
	}
	public void setHasBookingList(List<PatientEntity> hasBookingList) {
		this.hasBookingList = hasBookingList;
	}
	public List<PatientEntity> getNoBookingList() {
		return noBookingList;
	}
	public void setNoBookingList(List<PatientEntity> noBookingList) {
		this.noBookingList = noBookingList;
	}
	
}
