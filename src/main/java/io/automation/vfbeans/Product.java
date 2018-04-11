package io.automation.vfbeans;

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

import io.automation.beans.Characterstics;

public class Product {

	private Specification specification;
	private List<Characteristics> characteristics;
	private String action;
	private List<CustomerService> customerServices;

	public Specification getSpecification() {
		return specification;
	}

	public void setSpecification(Specification specification) {
		this.specification = specification;
	}

	public List<Characteristics> getCharacteristics() {
		return characteristics;
	}

	public void setCharacteristics(List<Characteristics> characteristics) {
		this.characteristics = characteristics;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<CustomerService> getCustomerServices() {
		return customerServices;
	}

	public void setCustomerServices(List<CustomerService> customerServices) {
		this.customerServices = customerServices;
	}

	public Product(Specification specification, List<Characteristics> characteristics, String action,
			List<CustomerService> customerServices) {
		super();
		this.specification = specification;
		this.characteristics = characteristics;
		this.action = action;
		this.customerServices = customerServices;
	}

	public Product() {
		super();
	}

	public void fromXml(Node importedNode) {
		// first check if elements are properly set
		// if(this.specification == null)
		// this.specification = new Specification();

		if (this.characteristics == null)
			this.characteristics = new ArrayList<>();

		if (this.specification == null)
			this.specification = new Specification();

		if (this.customerServices == null)
			this.customerServices = new ArrayList<>();

		// next get values from node
		Document xml = null;
		String expression;
		NodeList nodes;

		XPath xPath = XPathFactory.newInstance().newXPath();
		try {

			xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

			Element root = (Element) xml.importNode(importedNode, true);

			// specification
			expression = "./*[local-name()=\"specification\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);

			if (nodes.getLength() > 0) {
				this.specification.fromXml(nodes.item(0));
			}

			// characteristics
			expression = "./*[local-name()=\"characteristics\"]";
			nodes = (NodeList) xPath.evaluate(expression, root, XPathConstants.NODESET);

			if (nodes.getLength() > 0) {

				this.characteristics.clear();
				for (int i = 0; i < nodes.getLength(); i++) {
					Node charactersticElement = nodes.item(i);
					Characteristics c = new Characteristics();
					c.fromXml(charactersticElement);
					this.characteristics.add(c);
				}

			} else {

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
			Element root = document.createElement("product");
			
			//Specification
			
			Element specificationNode = (Element) document.importNode(specification.toXml().getDocumentElement(), true);
			
			root.appendChild(specificationNode);
			//characteristics
			for(Characteristics characterstic : characteristics) {
				Element importedNode = characterstic.toXml().getDocumentElement();
				
				Node charactersticElement = document.importNode(importedNode, true);
				
				root.appendChild(charactersticElement);
				
			}
			
			//action
			Element actionElement = document.createElement("action");
			actionElement.setTextContent(action);
			root.appendChild(actionElement);
			
			//customer
			for(CustomerService customerService : customerServices) {
				Element importedNode = customerService.toXml().getDocumentElement();
				
				Node customerServiceElement = document.importNode(importedNode, true);
				
				root.appendChild(customerServiceElement);
				
			}
			
			document.appendChild(root);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return document;
	}
}
