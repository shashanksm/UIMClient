package io.automation.beans;

public class OrderDetailsBean extends RecordBean {
	
	//Order No.	MSISDN	BAN	Task	Channel of order	Shipping	Order Type	Order Type 1	Order Type Code 2	
	//Prepaid/PostPaid	Cancellation Reason	Cancellation requested by (individual)	Re-creation required	
	//Notes for re-creation

	//ORDER_NUM	CTN	Account number	Channel	CARRIER_PRIO_CD	Order TypeCode1	Order TypeCode 2	
	//ASSET_INTEG_ID	PRODUCT_CLASS_NAME	
	//ACTION_CD
	
	private String orderNumber;
	private String ctn;
	private String accountNumber;
	private String channel;
	private String shipping;
	private String orderTypecode1;
	private String orderTypecode2;
	private String assetIntegrationId;
	private String actionCd;
	
	
	public OrderDetailsBean() {
		super();
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getCtn() {
		return ctn;
	}
	public void setCtn(String ctn) {
		this.ctn = ctn;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getShipping() {
		return shipping;
	}
	public void setShipping(String shipping) {
		this.shipping = shipping;
	}
	public String getOrderTypecode1() {
		return orderTypecode1;
	}
	public void setOrderTypecode1(String orderTypecode1) {
		this.orderTypecode1 = orderTypecode1;
	}
	public String getOrderTypecode2() {
		return orderTypecode2;
	}
	public void setOrderTypecode2(String orderTypecode2) {
		this.orderTypecode2 = orderTypecode2;
	}
	public String getAssetIntegrationId() {
		return assetIntegrationId;
	}
	public void setAssetIntegrationId(String assetIntegrationId) {
		this.assetIntegrationId = assetIntegrationId;
	}
	public String getAcdtionCd() {
		return actionCd;
	}
	public void setAcdtionCd(String acdtionCd) {
		this.actionCd = acdtionCd;
	}
	public OrderDetailsBean(String orderNumber, String ctn, String accountNumber, String channel, String shipping,
			String orderTypecode1, String orderTypecode2, String assetIntegrationId, String acdtionCd) {
		super();
		this.orderNumber = orderNumber;
		this.ctn = ctn;
		this.accountNumber = accountNumber;
		this.channel = channel;
		this.shipping = shipping;
		this.orderTypecode1 = orderTypecode1;
		this.orderTypecode2 = orderTypecode2;
		this.assetIntegrationId = assetIntegrationId;
		this.actionCd = acdtionCd;
	}
	
	
	
	//
	
	
	
}
