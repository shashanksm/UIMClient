package io.automation.beans;

public class OrderLineItemsRecordBean extends RecordBean {
	
	private String action_cd;
	private String service_num;
	private String asset_integ_id;
	private String name;
	private String order_number;
	private String status_cd;
	private String serv_accnt_id;
	private String x_vf_customer_code;
	private String attrib_36;
	private String attrib_55;
	private String created;
	public String getAction_cd() {
		return action_cd;
	}
	public void setAction_cd(String action_cd) {
		this.action_cd = action_cd;
	}
	public String getService_num() {
		return service_num;
	}
	public void setService_num(String service_num) {
		this.service_num = service_num;
	}
	public String getAsset_integ_id() {
		return asset_integ_id;
	}
	public void setAsset_integ_id(String asset_integ_id) {
		this.asset_integ_id = asset_integ_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrder_number() {
		return order_number;
	}
	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	public String getStatus_cd() {
		return status_cd;
	}
	public void setStatus_cd(String status_cd) {
		this.status_cd = status_cd;
	}
	public String getServ_accnt_id() {
		return serv_accnt_id;
	}
	public void setServ_accnt_id(String serv_accnt_id) {
		this.serv_accnt_id = serv_accnt_id;
	}
	public String getX_vf_customer_code() {
		return x_vf_customer_code;
	}
	public void setX_vf_customer_code(String x_vf_customer_code) {
		this.x_vf_customer_code = x_vf_customer_code;
	}
	public String getAttrib_36() {
		return attrib_36;
	}
	public void setAttrib_36(String attrib_36) {
		this.attrib_36 = attrib_36;
	}
	public String getAttrib_55() {
		return attrib_55;
	}
	public void setAttrib_55(String attrib_55) {
		this.attrib_55 = attrib_55;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public OrderLineItemsRecordBean(String action_cd, String service_num, String asset_integ_id, String name,
			String order_number, String status_cd, String serv_accnt_id, String x_vf_customer_code, String attrib_36,
			String attrib_55, String created) {
		super();
		this.action_cd = action_cd;
		this.service_num = service_num;
		this.asset_integ_id = asset_integ_id;
		this.name = name;
		this.order_number = order_number;
		this.status_cd = status_cd;
		this.serv_accnt_id = serv_accnt_id;
		this.x_vf_customer_code = x_vf_customer_code;
		this.attrib_36 = attrib_36;
		this.attrib_55 = attrib_55;
		this.created = created;
	}
	public OrderLineItemsRecordBean() {
		super();
	}
	@Override
	public String toString() {
		return "OrderLineItemsRecordBean [action_cd=" + action_cd + ", service_num=" + service_num + ", asset_integ_id="
				+ asset_integ_id + ", name=" + name + ", order_number=" + order_number + ", status_cd=" + status_cd
				+ ", serv_accnt_id=" + serv_accnt_id + ", x_vf_customer_code=" + x_vf_customer_code + ", attrib_36="
				+ attrib_36 + ", attrib_55=" + attrib_55 + ", created=" + created + "]";
	}
	
	
	
}
