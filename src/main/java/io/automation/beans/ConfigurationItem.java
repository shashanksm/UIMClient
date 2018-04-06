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

public class ConfigurationItem {
	
	private String name;
	private List<ResourceAssignment> resourceAssignments;
	private List<Characterstics> characterstics;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ResourceAssignment> getResourceAssignments() {
		return resourceAssignments;
	}
	public void setResourceAssignments(List<ResourceAssignment> resourceAssignments) {
		this.resourceAssignments = resourceAssignments;
	}
	public List<Characterstics> getCharacterstics() {
		return characterstics;
	}
	public void setCharacterstics(List<Characterstics> characterstics) {
		this.characterstics = characterstics;
	}
	
	public ConfigurationItem(String name, List<ResourceAssignment> resourceAssignments,
			List<Characterstics> characterstics) {
		super();
		this.name = name;
		this.resourceAssignments = resourceAssignments;
		this.characterstics = characterstics;
	}
	public ConfigurationItem() {
		super();
		this.name = "";
		this.characterstics = new ArrayList<>();
		this.resourceAssignments = new ArrayList<>();
	}
	
	public void fromXml(Node importedNode) {
		
		Document xml = null;
		XPath xPath = XPathFactory.newInstance().newXPath();
		String expression = "";
		NodeList nodes;
		
		try {
			
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element root = (Element) xml.importNode(importedNode, true);
			
			//name
			expression = "./*[local-name()=\"name\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET); 
			
			if(nodes.getLength() > 0) {
				Element  nameElement = (Element) nodes.item(0);
				
				this.name = nameElement.getTextContent();
			}else {
				
			}
			
			//configurationItems
			expression = "./*[local-name()=\"resourceAssignment\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET); 
			
			if(nodes.getLength() > 0) {
				this.resourceAssignments.clear();
				for(int i = 0; i<nodes.getLength(); i++) {
					
					Element  resourceAssignmentsElement = (Element) nodes.item(i);
					ResourceAssignment resourceAssignment = new ResourceAssignment();
					resourceAssignment.fromXml(resourceAssignmentsElement);
					
					this.resourceAssignments.add(resourceAssignment);
	
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
	
	public Document toXml() {
		Document xml = null;
		
		try {
			
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element root = xml.createElement("configurationItems");
			
			//name
			Element nameElement = xml.createElement("name");
			nameElement.setTextContent(this.name);
			
			root.appendChild(nameElement);
			
			//resourceAssignment
			for(ResourceAssignment resourceAssignment : resourceAssignments) {
				Node importedNode = resourceAssignment.toXml().getDocumentElement();
				Element resourceAssignmentElement = (Element) xml.importNode(importedNode, true);
				root.appendChild(resourceAssignmentElement);
			}
			
			
			//characteristics
			for(Characterstics characterstic : characterstics) {
				Node importedNode = characterstic.toXml().getDocumentElement();
				Element charactersticElement = (Element) xml.importNode(importedNode, true);
				root.appendChild(charactersticElement);
			}
			
			xml.appendChild(root);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return xml;
	}
	
	
}
