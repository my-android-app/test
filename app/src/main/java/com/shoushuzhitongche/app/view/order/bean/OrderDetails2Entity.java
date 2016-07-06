package com.shoushuzhitongche.app.view.order.bean;

import java.io.Serializable;
import java.util.List;

public class OrderDetails2Entity implements Serializable {

	private Booking2Entity booking;
	private PaysEntity notPays;
	private List<PaysEntity> pays;
	
	
	public Booking2Entity getBooking() {
		return booking;
	}
	public List<PaysEntity> getPays() {
		return pays;
	}
	public void setBooking(Booking2Entity booking) {
		this.booking = booking;
	}
	public void setPays(List<PaysEntity> pays) {
		this.pays = pays;
	}
	public PaysEntity getNotPays() {
		return notPays;
	}
	public void setNotPays(PaysEntity notPays) {
		this.notPays = notPays;
	}
	public OrderDetails2Entity(Booking2Entity booking, PaysEntity notPays,
			List<PaysEntity> pays) {
		super();
		this.booking = booking;
		this.notPays = notPays;
		this.pays = pays;
	}
	

	
	
}
