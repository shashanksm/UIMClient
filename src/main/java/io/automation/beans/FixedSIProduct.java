package io.automation.beans;




public class FixedSIProduct {
	String name;
	String charactersticName;
	String charactersticValue;
	public FixedSIProduct(String name, String charactersticName, String charactersticValue) {
		super();
		this.name = name;
		this.charactersticName = charactersticName;
		this.charactersticValue = charactersticValue;
	}
	
	public FixedSIProduct() {
		super();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCharactersticName() {
		return charactersticName;
	}
	
	public void setCharactersticName(String charactersticName) {
		this.charactersticName = charactersticName;
	}
	
	public String getCharactersticValue() {
		return charactersticValue;
	}
	
	public void setCharactersticValue(String charactersticValue) {
		this.charactersticValue = charactersticValue;
	}
	
	@Override
	public String toString() {
		String ret = "";
		
		ret = "[\""
				+ name
				+ "\","
				+ "\""
				+ charactersticName
				+ "\","
				+ charactersticValue
				+ "\""
				+ "]";
		
		
		return ret;
	}
}
