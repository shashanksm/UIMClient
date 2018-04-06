package io.automation.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class Resource {
	
	private String id;
	private String type;
	private String inventoryGroupName;
	//private Specification specification;
	private List<Characterstics> characterstics;
	
	public Resource(String id, String type, String inventoryGroupName, Specification specification,
			List<Characterstics> characterstics) {
		super();
		this.setId(id);
		this.setType(type);
		this.setInventoryGroupName(inventoryGroupName);
		this.characterstics = characterstics;
	}

	public Resource() {
		super();
		this.setId("");
		this.setType("");
		this.setInventoryGroupName("");
		//this.specification = new Specification();
		this.characterstics = new ArrayList<>();
	}
	
	public void fromXml(Node importedNode) {
		
		//first check if elements are properly set
		//if(this.specification == null)
			//this.specification = new Specification();
		
		if(this.characterstics == null)
			this.characterstics = new ArrayList<>();
		
		//next get values from node
		Document xml = null;
		String expression;
		NodeList nodes;
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element root = (Element) xml.importNode(importedNode, true);
			
			//id
			expression = "./*[local-name()=\"id\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				Element idElement = (Element) nodes.item(0);
				this.setId(idElement.getTextContent());
				
				
			}else {
				
			}
			
			//type
			expression = "./*[local-name()=\"type\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				Element typeElement = (Element) nodes.item(0);
				this.setType(typeElement.getTextContent());
				
				
			}else {
				
			}
			
			//inventoryGroupName
			expression = "./*[local-name()=\"inventoryGroupName\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				Element inventoryGroupNameElement = (Element) nodes.item(0);
				this.setInventoryGroupName(inventoryGroupNameElement.getTextContent());
				
				
			}else {
				
			}
			
			//characteristics
			expression = "./*[local-name()=\"characteristics\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				
				this.characterstics.clear();
				for(int i = 0; i<nodes.getLength(); i++) {
					Node charactersticElement = nodes.item(i);
					Characterstics c = new Characterstics();
					c.fromXml(charactersticElement);
					this.characterstics.add(c);
				}
				
			}else {
				
			}
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInventoryGroupName() {
		return inventoryGroupName;
	}

	public void setInventoryGroupName(String inventoryGroupName) {
		this.inventoryGroupName = inventoryGroupName;
	}

	/*
	public Specification getSpecification() {
		return specification;
	}

	
	public void setSpecification(Specification specification) {
		this.specification = specification;
	}
*/
	public List<Characterstics> getCharacterstics() {
		return characterstics;
	}

	public void setCharacterstics(List<Characterstics> characterstics) {
		this.characterstics = characterstics;
	}
	
	public Document toXml() {
		Document document = null; 
		
		try {
			
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = document.createElement("resource");
			
			
			
			//id
			Element idElement = document.createElement("id");
			idElement.setTextContent(id);
			root.appendChild(idElement);
			
			
			//type
			Element typeElement = document.createElement("type");
			typeElement.setTextContent(type);
			root.appendChild(typeElement);
			
			//inventoryGroupName
			Element inventoryGroupNameElement = document.createElement("inventoryGroupName");
			inventoryGroupNameElement.setTextContent(inventoryGroupName);
			root.appendChild(inventoryGroupNameElement);
			
			//characteristics
			for(Characterstics characterstic : characterstics) {
				Element importedNode = characterstic.toXml().getDocumentElement();
				
				Node charactersticElement = document.importNode(importedNode, true);
				
				root.appendChild(charactersticElement);
				
			}
			
			document.appendChild(root);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return document;
	}
}
