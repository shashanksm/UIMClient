package io.automation.vfbeans;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Characteristics {
	private String name;
	private String value;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Characteristics(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	public Characteristics() {
		super();
		this.name = "";
		this.value = "";
	} 
	
	public void fromXml(Node importedNode) {
		Document xml = null;
		String expression;
		NodeList nodes;
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		try {
			
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element root = (Element) xml.importNode(importedNode, true);
			
			//name
			expression = "./*[local-name()=\"name\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				Element nameElement = (Element) nodes.item(0);
				this.name = nameElement.getTextContent();
				
				
			}else {
				
			}
			
			//value
			expression = "./*[local-name()=\"value\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				Element valueElement = (Element) nodes.item(0);
				this.value = valueElement.getTextContent();
				
				
			}else {
				
			}
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			
		}finally {
			
		}
	}
	
	public Document toXml() {
		Document document = null; 
		
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = document.createElement("characteristics");
			document.appendChild(root);
			
			Element nameEl = document.createElement("name");
			nameEl.appendChild(document.createTextNode(name));
			
			Element valueEl = document.createElement("value");
			valueEl.appendChild(document.createTextNode(value));
			
			root.appendChild((Node)nameEl);
			root.appendChild((Node)valueEl);
			
			return document;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}
}

