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

public class Services {
	
	private String id;
	private String name;
	private List<Characterstics> characterstics;
	private Configurations configurations;
	
	
	public Services(String id, String name, List<Characterstics> characterstics, Configurations configurations) {
		super();
		this.id = id;
		this.name = name;
		this.characterstics = characterstics;
		this.configurations = configurations;
	}


	public Services() {
		super();
		this.id = "";
		this.name = "";
		this.characterstics = new ArrayList<>();
		this.configurations = new Configurations();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<Characterstics> getCharacterstics() {
		return characterstics;
	}


	public void setCharacterstics(List<Characterstics> characterstics) {
		this.characterstics = characterstics;
	}


	public Configurations getConfigurations() {
		return configurations;
	}


	public void setConfigurations(Configurations configurations) {
		this.configurations = configurations;
	}
	
	public Document toXml() {
		Document xml = null;
		
		try {
			
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = xml.createElement("services");
			
			//id
			Element idElement = xml.createElement("id");
			idElement.setTextContent(id);
			root.appendChild(idElement);
			
			//name
			Element nameEl = xml.createElement("name");
			nameEl.appendChild(xml.createTextNode(name));
			
			//characterstics
			for(Characterstics characterstic : characterstics) {
				Element importedNode = characterstic.toXml().getDocumentElement();
				
				Node charactersticElement = xml.importNode(importedNode, true);
				
				root.appendChild(charactersticElement);
				
			}
			
			//configuration
			Element importedNode = configurations.toXml().getDocumentElement();
			Element configurationsElement = (Element) xml.importNode(importedNode, true);
			root.appendChild(configurationsElement);
			
			xml.appendChild(root);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return xml;
	}
	
	public void fromXml(Node importedNode) {
		
		if(this.characterstics == null)
			this.characterstics = new ArrayList<>();
		
		
		
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
			
			//name
			expression = "./*[local-name()=\"name\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				Element nameElement = (Element) nodes.item(0);
				this.name = nameElement.getTextContent();
				
				
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
			
			//configurations
			expression = "./*[local-name()=\"configurations\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				Element configurationsElement = (Element) nodes.item(0);
				this.configurations.fromXml(configurationsElement);
				
				
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
	
}
