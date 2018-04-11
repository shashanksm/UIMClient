package io.automation.beans;

public class ActiveProductsRecordBean extends RecordBean{
	private String ctn;
	private String integration_id;
	private String serial_num;
	private String ctn_status;
	private String product_status;
	private String product_name;
	private String part_num;
	
	
	public String getCtn() {
		return ctn;
	}
	
	public void setCtn(String ctn) {
		this.ctn = ctn;
	}
	
	public String getIntegration_id() {
		return integration_id;
	}
	
	public void setIntegration_id(String row_id) {
		this.integration_id = row_id;
	}
	
	public String getSerial_num() {
		return serial_num;
	}
	
	public void setSerial_num(String serial_num) {
		this.serial_num = serial_num;
	}
	
	public String getCtn_status() {
		return ctn_status;
	}
	
	public void setCtn_status(String ctn_status) {
		this.ctn_status = ctn_status;
	}
	
	public String getProduct_status() {
		return product_status;
	}
	
	public void setProduct_status(String product_status) {
		this.product_status = product_status;
	}
	
	public String getProduct_name() {
		return product_name;
	}
	
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	
	public String getPart_num() {
		return part_num;
	}
	
	
	public void setPart_num(String part_num) {
		this.part_num = part_num;
	}
	
	public ActiveProductsRecordBean(String ctn, String row_id, String serial_num, String ctn_status,
			String product_status, String product_name, String part_num) {
		super();
		this.ctn = ctn;
		this.integration_id = row_id;
		this.serial_num = serial_num;
		this.ctn_status = ctn_status;
		this.product_status = product_status;
		this.product_name = product_name;
		this.part_num = part_num;
	}
	
	public ActiveProductsRecordBean() {
		super();
	}

	@Override
	public String toString() {
		return "ActiveProductsRecordBean [ctn=" + ctn + ", integration_id=" + integration_id + ", serial_num="
				+ serial_num + ", ctn_status=" + ctn_status + ", product_status=" + product_status + ", product_name="
				+ product_name + ", part_num=" + part_num + "]";
	}
	
	
	
}
