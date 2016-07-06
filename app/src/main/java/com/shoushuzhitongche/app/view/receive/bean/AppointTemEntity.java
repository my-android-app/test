package com.shoushuzhitongche.app.view.receive.bean;

import java.util.List;

public class AppointTemEntity {
	private List<AppointEntity> processingList;
	private List<AppointEntity> doneList;
	public List<AppointEntity> getProcessingList() {
		return processingList;
	}
	public void setProcessingList(List<AppointEntity> processingList) {
		this.processingList = processingList;
	}
	public List<AppointEntity> getDoneList() {
		return doneList;
	}
	public void setDoneList(List<AppointEntity> doneList) {
		this.doneList = doneList;
	}
	
}
