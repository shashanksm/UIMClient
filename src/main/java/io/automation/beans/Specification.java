package io.automation.beans;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Specification {
	
	private String name;
	private String entityType;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	public Document toXml() {
		
		Document document = null;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = document.createElement("specification");
			document.appendChild(root);
			
			Element nameEl = document.createElement("name");
			nameEl.appendChild(document.createTextNode(name));
			
			Element entityTypeEl = document.createElement("entityType");
			entityTypeEl.appendChild(document.createTextNode(entityType));
			
			root.appendChild((Node)nameEl);
			root.appendChild((Node)entityTypeEl);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return document;
	}
	
	public Specification(String name, String entityType) {
		super();
		this.name = name;
		this.entityType = entityType;
	}
	
	public Specification() {
		super();
		this.name = "";
		this.entityType = "";
	}
	
	public void fromXml(Node importedNode) {
		
		
		Document xml = null;
		
		try {
			
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element root = (Element) xml.importNode(importedNode, true);
			
			Element nameElement = (Element) root.getElementsByTagName("name").item(0);
			
			if(nameElement == null)
				throw new NullPointerException("no name");
			
			this.name = nameElement.getTextContent();
			
			Element entityTypeElement = (Element) root.getElementsByTagName("entityType").item(0);
			
			if(entityTypeElement == null)
				throw new NullPointerException("no entityType");
			
			this.entityType = entityTypeElement.getTextContent();
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			
		}finally {
			
		}		
		
	}
	
	@Override
	public String toString() {
		return "Specification [name=" + name + ", entityType=" + entityType + "]";
	}
	
	
}
