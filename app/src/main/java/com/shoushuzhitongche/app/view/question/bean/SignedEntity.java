package com.shoushuzhitongche.app.view.question.bean;

import java.util.List;

public class SignedEntity {
	
	private UserDoctorHz userDoctorHz;
	private UserDoctorZz userDoctorZz;
	
	public UserDoctorHz getUserDoctorHz() {
		return userDoctorHz;
	}

	public void setUserDoctorHz(UserDoctorHz userDoctorHz) {
		this.userDoctorHz = userDoctorHz;
	}

	public UserDoctorZz getUserDoctorZz() {
		return userDoctorZz;
	}

	public void setUserDoctorZz(UserDoctorZz userDoctorZz) {
		this.userDoctorZz = userDoctorZz;
	}

	public static class UserDoctorHz{
		private String id;
		private String is_join;
		private String min_no_surgery;
		
		private List<String> travel_duration;
		
		private String fee_min;
		private String fee_max;
		
		private List<String> week_days;
		
		private String patients_prefer;
		private String freq_destination	;
		private String destination_req;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getIs_join() {
			return is_join;
		}
		public void setIs_join(String is_join) {
			this.is_join = is_join;
		}
		public String getMin_no_surgery() {
			return min_no_surgery;
		}
		public void setMin_no_surgery(String min_no_surgery) {
			this.min_no_surgery = min_no_surgery;
		}
		public List<String> getTravel_duration() {
			return travel_duration;
		}
		public void setTravel_duration(List<String> travel_duration) {
			this.travel_duration = travel_duration;
		}
		public String getFee_min() {
			return fee_min;
		}
		public void setFee_min(String fee_min) {
			this.fee_min = fee_min;
		}
		public String getFee_max() {
			return fee_max;
		}
		public void setFee_max(String fee_max) {
			this.fee_max = fee_max;
		}
		public List<String> getWeek_days() {
			return week_days;
		}
		public void setWeek_days(List<String> week_days) {
			this.week_days = week_days;
		}
		public String getPatients_prefer() {
			return patients_prefer;
		}
		public void setPatients_prefer(String patients_prefer) {
			this.patients_prefer = patients_prefer;
		}
		public String getFreq_destination() {
			return freq_destination;
		}
		public void setFreq_destination(String freq_destination) {
			this.freq_destination = freq_destination;
		}
		public String getDestination_req() {
			return destination_req;
		}
		public void setDestination_req(String destination_req) {
			this.destination_req = destination_req;
		}
		
		
	}

	public static class UserDoctorZz{
		private String id;
		private String is_join;
		private String fee;
		private String preferredPatient;
		private String prep_days;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getIs_join() {
			return is_join;
		}
		public void setIs_join(String is_join) {
			this.is_join = is_join;
		}
		public String getFee() {
			return fee;
		}
		public void setFee(String fee) {
			this.fee = fee;
		}
		public String getPreferredPatient() {
			return preferredPatient;
		}
		public void setPreferredPatient(String preferredPatient) {
			this.preferredPatient = preferredPatient;
		}
		public String getPrep_days() {
			return prep_days;
		}
		public void setPrep_days(String prep_days) {
			this.prep_days = prep_days;
		}
		
	}
	
	
}
