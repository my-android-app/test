package com.shoushuzhitongche.app.view.home.bean;

import java.io.Serializable;
import java.util.List;

import com.shoushuzhitongche.app.view.doctor.bean.DoctorsEntity;

public class HomeEntity implements Serializable{
	private String[] doctors;
	private List<BannerEntity> banners;
	private String publicWelfareUrl;
	private String doctorUrl;
	private String joinUrl;


	public List<BannerEntity> getBanners() {
		return banners;
	}

	public void setBanners(List<BannerEntity> banners) {
		this.banners = banners;
	}

	public String getPublicWelfareUrl() {
		return publicWelfareUrl;
	}

	public void setPublicWelfareUrl(String publicWelfareUrl) {
		this.publicWelfareUrl = publicWelfareUrl;
	}

	public String getDoctorUrl() {
		return doctorUrl;
	}

	public void setDoctorUrl(String doctorUrl) {
		this.doctorUrl = doctorUrl;
	}

	public String getJoinUrl() {
		return joinUrl;
	}

	public void setJoinUrl(String joinUrl) {
		this.joinUrl = joinUrl;
	}

	public String[] getDoctors() {
		return doctors;
	}

	public void setDoctors(String[] doctors) {
		this.doctors = doctors;
	}
	
	
}
