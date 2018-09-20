package io.automation.beans;

public class LogicalDeviceRecordBean extends RecordBean {
	private String name;
	private String id;
	private String adminstate;
	private String objectstate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAdminstate() {
		return adminstate;
	}
	public void setAdminstate(String adminstate) {
		this.adminstate = adminstate;
	}
	public String getObjectstate() {
		return objectstate;
	}
	public void setObjectstate(String objectstate) {
		this.objectstate = objectstate;
	}
	
	public LogicalDeviceRecordBean(String name, String id, String adminstate, String objectstate) {
		super();
		this.name = name;
		this.id = id;
		this.adminstate = adminstate;
		this.objectstate = objectstate;
	}
	
	public LogicalDeviceRecordBean() {
		super();
	}
	
	
}
