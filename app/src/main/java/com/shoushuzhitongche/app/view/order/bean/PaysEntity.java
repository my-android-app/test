package com.shoushuzhitongche.app.view.order.bean;

import java.io.Serializable;
public class PaysEntity implements Serializable {

	private String id;
	private String refNo;
	private String orderTypeText;
	private String orderType;
	private String finalAmount;
	private String dateClosed;
	private String needPay;
	private String payed;
	private String isPaid;
	private String actionUrl;
	public String getId() {
		return id;
	}
	public String getRefNo() {
		return refNo;
	}
	public String getOrderTypeText() {
		return orderTypeText;
	}
	public String getOrderType() {
		return orderType;
	}
	public String getFinalAmount() {
		return finalAmount;
	}
	public String getDateClosed() {
		return dateClosed;
	}
	public String getNeedPay() {
		return needPay;
	}
	public String getPayed() {
		return payed;
	}
	public String getIsPaid() {
		return isPaid;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public void setOrderTypeText(String orderTypeText) {
		this.orderTypeText = orderTypeText;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public void setFinalAmount(String finalAmount) {
		this.finalAmount = finalAmount;
	}
	public void setDateClosed(String dateClosed) {
		this.dateClosed = dateClosed;
	}
	public void setNeedPay(String needPay) {
		this.needPay = needPay;
	}
	public void setPayed(String payed) {
		this.payed = payed;
	}
	public void setIsPaid(String isPaid) {
		this.isPaid = isPaid;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	
	
}
