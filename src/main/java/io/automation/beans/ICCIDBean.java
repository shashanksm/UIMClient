package io.automation.beans;

import org.joda.time.LocalDateTime;

public class ICCIDBean extends RecordBean {
	private String name;

	private String adminstate;

	private String msisdn;

	private String msisdnstatus;

	private LocalDateTime lastModifiedDate;

	private String lastModifiedUser;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdminstate() {
		return adminstate;
	}

	public void setAdminstate(String adminstate) {
		this.adminstate = adminstate;
	}

	public ICCIDBean(String name, String adminstate, String msisdn, String msisdnstatus,
			LocalDateTime lastModifiedDate, String lastModifiedUser) {
		super();
		this.name = name;
		this.adminstate = adminstate;
		this.msisdn = msisdn;
		this.msisdnstatus = msisdnstatus;
		this.lastModifiedDate = lastModifiedDate;
		this.lastModifiedUser = lastModifiedUser;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getMsisdnstatus() {
		return msisdnstatus;
	}

	public void setMsisdnstatus(String msisdnstatus) {
		this.msisdnstatus = msisdnstatus;
	}

	public LocalDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedUser() {
		return lastModifiedUser;
	}

	public void setLastModifiedUser(String lastModifiedUser) {
		this.lastModifiedUser = lastModifiedUser;
	}

	public ICCIDBean() {
		super();
	}

}
