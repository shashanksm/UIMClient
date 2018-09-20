package io.automation.beans;

public class CancellationDataBean {
	
	private String orderNumber;
	private String orderType;
	private String orderTypeCode1;
	private String orderTypeCode2;
	private String previousCtn;
	private String currentCtn;
	private String previousSim;
	private String currentSim;
	private String previousAID;
	private String currentAID;
	
	public CancellationDataBean(String orderNumber, String orderType, String orderTypeCode1, String orderTypeCode2,
			String previousCtn, String currentCtn, String previousSim, String currentSim, String previousAID,
			String currentAID) {
		super();
		this.orderNumber = orderNumber;
		this.orderType = orderType;
		this.orderTypeCode1 = orderTypeCode1;
		this.orderTypeCode2 = orderTypeCode2;
		this.previousCtn = previousCtn;
		this.currentCtn = currentCtn;
		this.previousSim = previousSim;
		this.currentSim = currentSim;
		this.previousAID = previousAID;
		this.currentAID = currentAID;
	}

	public CancellationDataBean() {
		super();
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderTypeCode1() {
		return orderTypeCode1;
	}

	public void setOrderTypeCode1(String orderTypeCode1) {
		this.orderTypeCode1 = orderTypeCode1;
	}

	public String getOrderTypeCode2() {
		return orderTypeCode2;
	}

	public void setOrderTypeCode2(String orderTypeCode2) {
		this.orderTypeCode2 = orderTypeCode2;
	}

	public String getPreviousCtn() {
		return previousCtn;
	}

	public void setPreviousCtn(String previousCtn) {
		this.previousCtn = previousCtn;
	}

	public String getCurrentCtn() {
		return currentCtn;
	}

	public void setCurrentCtn(String currentCtn) {
		this.currentCtn = currentCtn;
	}

	public String getPreviousSim() {
		return previousSim;
	}

	public void setPreviousSim(String previousSim) {
		this.previousSim = previousSim;
	}

	public String getCurrentSim() {
		return currentSim;
	}

	public void setCurrentSim(String currentSim) {
		this.currentSim = currentSim;
	}

	public String getPreviousAID() {
		return previousAID;
	}

	public void setPreviousAID(String previousAID) {
		this.previousAID = previousAID;
	}

	public String getCurrentAID() {
		return currentAID;
	}

	public void setCurrentAID(String currentAID) {
		this.currentAID = currentAID;
	}
	
	
}
