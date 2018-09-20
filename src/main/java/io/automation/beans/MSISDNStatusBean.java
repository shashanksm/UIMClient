package io.automation.beans;

public class MSISDNStatusBean extends RecordBean {
	private String name;
	private String tnAdminstate;
	private String tcAdminstate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTnAdminstate() {
		return tnAdminstate;
	}
	public void setTnAdminstate(String tnAdminstate) {
		this.tnAdminstate = tnAdminstate;
	}
	public String getTcAdminstate() {
		return tcAdminstate;
	}
	public void setTcAdminstate(String tcAdminstate) {
		this.tcAdminstate = tcAdminstate;
	}
	public MSISDNStatusBean(String name, String tnAdminstate, String tcAdminstate) {
		super();
		this.name = name;
		this.tnAdminstate = tnAdminstate;
		this.tcAdminstate = tcAdminstate;
	}
	public MSISDNStatusBean() {
		super();
		
	}
	
}
