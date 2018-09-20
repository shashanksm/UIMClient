package io.automation.beans;

public class BroadbandDetailsBean extends RecordBean {
	private String aid;
	private String accountCategory;
	private String techProductName;
	private String techProductCode;

	public BroadbandDetailsBean(String aid, String accountCategory, String techProductName, String techProductCode) {
		super();
		this.aid = aid;
		this.accountCategory = accountCategory;
		this.techProductName = techProductName;
		this.techProductCode = techProductCode;
	}

	public BroadbandDetailsBean() {
		super();
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getAccountCategory() {
		return accountCategory;
	}

	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}

	public String getTechProductName() {
		return techProductName;
	}

	public void setTechProductName(String techProductName) {
		this.techProductName = techProductName;
	}

	public String getTechProductCode() {
		return techProductCode;
	}

	public void setTechProductCode(String techProductCode) {
		this.techProductCode = techProductCode;
	}

}
