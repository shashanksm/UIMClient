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

public class Configurations {

	private String version;
	private String state;
	private List<ConfigurationItem> configurationItems;
	
	public Configurations(String version, List<ConfigurationItem> configurationItems) {
		super();
		this.version = version;
		this.configurationItems = configurationItems;
	}
	
	public Configurations() {
		super();
		this.configurationItems = new ArrayList<>();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<ConfigurationItem> getConfigurationItems() {
		return configurationItems;
	}

	public void setConfigurationItems(List<ConfigurationItem> configurationItems) {
		this.configurationItems = configurationItems;
	}
	
	public void fromXml(Node importedNode) {
		
		//first check the attributes
		if(this.configurationItems == null)
			this.configurationItems = new ArrayList<>();
		
		Document xml = null;
		String expression;
		NodeList nodes;
		
		XPath xPath = XPathFactory.newInstance().newXPath();
		
		try {
			
			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element root = (Element) xml.importNode(importedNode, true);
			
			//version
			expression = "./*[local-name()=\"version\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
			
			if(nodes.getLength() > 0) {
				
				Element versionElement = (Element) nodes.item(0);
				this.version = versionElement.getTextContent();
				
				expression = "./*[local-name()=\"state\"]";
				nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
				if(nodes.getLength() > 0) {
					Element stateElement = (Element) nodes.item(0);
					this.state = stateElement.getTextContent();
				}
				
				//configurationItems
				expression = "./*[local-name()=\"configurationItems\"]";
				nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);
				
				if(nodes.getLength() > 0) {
					
					
					this.configurationItems.clear();
					for(int i = 0; i<nodes.getLength(); i++) {
						Node configurationItemsElement = nodes.item(i);
						ConfigurationItem c = new ConfigurationItem();
						c.fromXml(configurationItemsElement);
						this.configurationItems.add(c);
					}
					
				}else {
					
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
			
			Element root = xml.createElement("configurations");
			
			//version
			Element versionElement = xml.createElement("version");
			versionElement.setTextContent(this.version);
			root.appendChild(versionElement);
			
			//configurationItems
			for(ConfigurationItem configurationItem : configurationItems) {
				Element importedNode = configurationItem.toXml().getDocumentElement();
				
				Node configurationItemElement = xml.importNode(importedNode, true);
				
				root.appendChild(configurationItemElement);
				
			}
			
			xml.appendChild(root);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return xml;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
