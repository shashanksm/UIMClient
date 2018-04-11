package io.automation.vfbeans;

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

import io.automation.beans.Characterstics;

public class CustomerService {

	private Characteristics characteristics;
	private String action;
	
	
	public Characteristics getCharacteristics() {
		return characteristics;
	}
	public void setCharacteristics(Characteristics characteristics) {
		this.characteristics = characteristics;
	}
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public CustomerService(Characteristics characteristics, String action) {
		super();
		this.characteristics = characteristics;
		this.action = action;
	}
	
	public CustomerService() {
		super();
	}
	
	public void fromXml(Node importedNode) {
		
		if(this.characteristics == null) {
			this.characteristics = new Characteristics();

		}
		
		Document xml = null;
		String expression;
		NodeList nodes;
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		try {
			
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element root = (Element) xml.importNode(importedNode, true);
			
			//id
			expression = "./*[local-name()=\"characteristics\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				this.characteristics.fromXml(nodes.item(0));
			}else {
				
			}
			
			expression = "./*[local-name()=\"action\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				this.action = ((Element) nodes.item(0)).getTextContent();
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
	
	public Document toXml() {
		Document document = null; 
		try {
			
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = document.createElement("customerService");
			
			
			
			/*
			//id
			Element idElement = document.createElement("id");
			idElement.setTextContent(id);
			root.appendChild(idElement);
			*/
			Element importedNode = this.characteristics.toXml().getDocumentElement();
			
			Node charactersticElement = document.importNode(importedNode, true);
			
			root.appendChild(charactersticElement);
			
			
			Element actionElement = document.createElement("action");
			actionElement.setTextContent(action);
			root.appendChild(actionElement);
			
			document.appendChild(root);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return document;
	}
	
}
