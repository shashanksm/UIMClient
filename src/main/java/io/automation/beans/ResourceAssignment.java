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

public class ResourceAssignment {

	private List<Resource> resources;

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	
	public void addResource(Resource resource) {
		this.resources.add(resource);
	}

	public ResourceAssignment(List<Resource> resources) {
		super();
		this.resources = resources;
	}

	public ResourceAssignment() {
		super();
		this.resources = new ArrayList<>();
	}
	
	public void fromXml(Node importedNode) {
		
		Document xml = null;
		String expression;
		NodeList nodes;
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		try {
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element root = (Element) xml.importNode(importedNode, true);
			
			//resources
			expression = "./*[local-name()=\"resource\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				
				this.resources.clear();
				for(int i = 0; i<nodes.getLength(); i++) {
					Node resourceElement = nodes.item(i);
					Resource r = new Resource();
					r.fromXml(resourceElement);
					this.resources.add(r);
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
		Document document = null;
		
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element root = document.createElement("resourceAssignment");
			
			for(Resource resource : resources) {
				Element importedNode = resource.toXml().getDocumentElement();
				
				Node resourceElement = document.importNode(importedNode, true);
				
				root.appendChild(resourceElement);
				
			}
			
			document.appendChild(root);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//resources
	
		return document;
	}
}
