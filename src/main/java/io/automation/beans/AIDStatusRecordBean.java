package io.automation.beans;

public class AIDStatusRecordBean extends RecordBean {
	private String value;
	private String adminstate;
	private String id;
	private String lastmodifieduser;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAdminstate() {
		return adminstate;
	}
	public void setAdminstate(String adminstate) {
		this.adminstate = adminstate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLastmodifieduser() {
		return lastmodifieduser;
	}
	
	public void setLastmodifieduser(String lastmodifieduser) {
		this.lastmodifieduser = lastmodifieduser;
	}
	
	public AIDStatusRecordBean(String value, String adminstate, String id, String lastmodifieduser) {
		super();
		this.value = value;
		this.adminstate = adminstate;
		this.id = id;
		this.lastmodifieduser = lastmodifieduser;
	}
	
	public AIDStatusRecordBean() {
		super();
	}
	
	
	
}
