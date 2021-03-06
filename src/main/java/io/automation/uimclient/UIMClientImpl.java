package io.automation.uimclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.SystemUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import io.automation.beans.ConfigurationItem;
import io.automation.beans.Services;
import io.automation.utils.CommonUtils;
import io.automation.vfbeans.Product;

public class UIMClientImpl implements UIMClient {

	private String requestFolderPath;
	private String serviceEndPoint;
	private String vfServiceEndPoint;
	private Services rServices;
	
	private static final Logger logger = LogManager.getLogger(UIMClient.class);
	
	private CloseableHttpClient httpClient;
	
	private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder documentBuilder;
	
	public HttpResponse sendXMLRequest(HttpUriRequest request) {
		// TODO Auto-generated method stub
		CloseableHttpResponse ret = null;
		logger.trace("sending xml request");
		try {
			ret = httpClient.execute(request);
			return ret;
		} catch (ClientProtocolException e) {
			logger.error("exception occurred : "+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@Override
	public boolean getServiceConfiguration(String serviceid) {
		// TODO Auto-generated method stub
		boolean ret = false;
		logger.traceEntry();
		logger.trace("getServiceConfiguration called for service-id : "+serviceid);
		Document requestDocument = null;
		Document responseDocument = null;
		File requestFile = null;
		if(SystemUtils.IS_OS_WINDOWS) {
			requestFile = new File(requestFolderPath+"\\GetServiceConfigurationRequest.xml");
		}else {
			requestFile = new File(requestFolderPath+"/GetServiceConfigurationRequest.xml");
		}
		
		FileInputStream fileInputStream = null;
		if(requestFile.exists()) {
			
			try {
				
				fileInputStream = new FileInputStream(requestFile);
				requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
				
				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = ".//*[local-name()=\"serviceId\"]";
				NodeList nodes;
				nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
				
				if(nodes.getLength() == 1) {
					
					Element serviceIdElement = (Element) nodes.item(0);
					
					serviceIdElement.setTextContent(serviceid);
					
					String xml = CommonUtils.stringXML(requestDocument);
					//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
					
					HttpPost request = prepareRequest(serviceEndPoint,xml);
					
					CloseableHttpResponse response = httpClient.execute(request);
					
					
					if(response.getStatusLine().getStatusCode() == 200) {
						//System.out.println("line 104 "+response.getStatusLine());
						
					
						documentBuilder = documentBuilderFactory.newDocumentBuilder();
						responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
						//CommonUtils.printXML(responseDocument.getDocumentElement(), System.out);
						rServices = new Services();
						
						XPath xpath = XPathFactory.newInstance().newXPath();
						expression = ".//*[local-name()=\"service\"]";
						
						nodes = (NodeList) xpath.evaluate(expression, responseDocument.getDocumentElement(), XPathConstants.NODESET);
						
						
						rServices.fromXml(nodes.item(0));
						ret = true;
						
					}else {
						logger.error("errorcode from server : "+response.getStatusLine());
					}
					
				}else {
					logger.error("invalid number of service-id elements in reuest file : "+nodes.getLength());
				}
				
			} catch (FileNotFoundException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (SAXException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (IOException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (XPathExpressionException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (TransformerException e) {
				logger.error("exception occurred : "+e.getMessage());
			}finally {
				if(fileInputStream != null) {

					try {
						fileInputStream.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}else {
			logger.trace("request file not found");
		}
		
		
		
		logger.traceExit();
		return ret;
	}

	private HttpPost prepareRequest(String endpoint, String body) {
		HttpPost ret = new HttpPost(endpoint);
		ret.addHeader("Content-Type","text/xml");
		ByteArrayEntity entity = new ByteArrayEntity(body.getBytes());
		
		ret.setEntity(entity);
		return ret;
	}


	@Override
	public void startup(Properties properties) {
		// TODO Auto-generated method stub
		logger.traceEntry();
		this.requestFolderPath = "requestFolderPath";
		this.serviceEndPoint = "serviceEndPoint";
		this.vfServiceEndPoint = "vfServiceEndPoint";
		
		if(properties.containsKey(requestFolderPath)) {
			this.requestFolderPath = properties.getProperty(requestFolderPath);
		}
		
		if(properties.containsKey(serviceEndPoint)) {
			this.serviceEndPoint = properties.getProperty(serviceEndPoint);
		}
		
		if(properties.containsKey(vfServiceEndPoint)) {
			this.vfServiceEndPoint = properties.getProperty(vfServiceEndPoint);
		}
		
		httpClient = HttpClients.createDefault();
		
		
		
		logger.traceExit();
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		logger.traceEntry();
		try {
			httpClient.close();
		} catch (IOException e) {
			logger.error("exception occurred : "+e.getMessage());
		}
		
		
		logger.traceExit();
	}


	@Override
	public boolean createFixedService(String aid) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("createFixedService called for AID : "+aid);
		
		
		
		
		
		Document requestDocument = null;
		Document responseDocument = null;
		
		File requestFile = null;
		if(SystemUtils.IS_OS_WINDOWS) {
			requestFile = new File(requestFolderPath+"\\CreateFixedServiceRequest.xml");
		}else {
			requestFile = new File(requestFolderPath+"/CreateFixedServiceRequest.xml");
		}
		//File requestFile = new File(requestFolderPath+"\\CreateFixedServiceRequest.xml");
		
		logger.trace(requestFile.getAbsolutePath());
		FileInputStream fileInputStream = null;
		if(requestFile.exists()) {
			
			try {
				
				fileInputStream = new FileInputStream(requestFile);
				requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
				
				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = ".//*[local-name()=\"characteristics\"]"
						+ "/*[local-name()=\"name\" and contains(text(),\"Asset Integration ID\")]/../value";
						
				NodeList nodes;
				nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
				
				if(nodes.getLength() == 1) {
					
					Element aidElement = (Element) nodes.item(0);
					
					aidElement.setTextContent(aid);
					
					String xml = CommonUtils.stringXML(requestDocument);
					//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
					
					HttpPost request = prepareRequest(vfServiceEndPoint,xml);
					
					CloseableHttpResponse response = httpClient.execute(request);
					
					
					if(response.getStatusLine().getStatusCode() == 200) {
						//System.out.println("line 104 "+response.getStatusLine());
						
					
						documentBuilder = documentBuilderFactory.newDocumentBuilder();
						responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
						
						expression = ".//*[local-name()=\"errors\"]";
						nodes = (NodeList) xPath.evaluate(expression, responseDocument, XPathConstants.NODESET);
						
						if(nodes.getLength() > 0) {
							String code = ((Element)nodes.item(0)).getTextContent();
							
							if(code.equals("000")) {
								logger.trace("operation successful");
								ret = true;
							}
						}else {
							logger.error("response incorrect");
						}
						
						
						
					}else {
						logger.error("errorcode from server : "+response.getStatusLine());
					}
					
				}else {
					logger.error("invalid number of service-id elements in reuest file : "+nodes.getLength());
				}
				
			} catch (FileNotFoundException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (SAXException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (IOException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (XPathExpressionException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (TransformerException e) {
				logger.error("exception occurred : "+e.getMessage());
			}finally {
				if(fileInputStream != null) {

					try {
						fileInputStream.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}else {
			logger.trace("request file not found");
		}
		
		logger.traceExit();
		return ret;
	}

	@Override
	public boolean createFixedBroadbandService(String aid, String accountCategory, String productCode, String productName) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("createFixedService called for AID : "+aid);
		
		
		
		
		
		Document requestDocument = null;
		Document responseDocument = null;
		File requestFile = null;
		if(SystemUtils.IS_OS_WINDOWS) {
			requestFile = new File(requestFolderPath+"\\CreateFixedBroadbandServiceRequest.xml");
		}else {
			requestFile = new File(requestFolderPath+"/CreateFixedBroadbandServiceRequest.xml");
		}
		
		
		logger.trace(requestFile.getAbsolutePath());
		FileInputStream fileInputStream = null;
		if(requestFile.exists()) {
			
			try {
				
				fileInputStream = new FileInputStream(requestFile);
				requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
				
				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = ".//*[local-name()=\"characteristics\"]"
						+ "/*[local-name()=\"name\" and contains(text(),\"Asset Integration ID\")]/../value";
						
				NodeList nodes;
				nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
				
				if(nodes.getLength() == 1) {
					
					Element aidElement = (Element) nodes.item(0);
					
					aidElement.setTextContent(aid);
					
					expression = ".//*[local-name()=\"characteristics\"]"
							+ "/*[local-name()=\"name\" and contains(text(),\"ProductName\")]/../value";
					nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
					if(nodes.getLength()==1) {
						
						Element productNameElement = (Element) nodes.item(0);
						
						productNameElement.setTextContent(productName);
						
					}else {
						logger.error("ProductName element does not exist");
					}
					
					expression = ".//*[local-name()=\"characteristics\"]"
							+ "/*[local-name()=\"name\" and contains(text(),\"ProductCode\")]/../value";
					nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
					if(nodes.getLength()==1) {
						
						Element productCodeElement = (Element) nodes.item(0);
						
						productCodeElement.setTextContent(productCode);
						
					}else {
						logger.error("ProductCode element does not exist");
					}
					
					expression = ".//*[local-name()=\"characteristics\"]"
							+ "/*[local-name()=\"name\" and contains(text(),\"accountCategory\")]/../value";
					nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
					if(nodes.getLength()==1) {
						
						Element accountCategoryElement = (Element) nodes.item(0);
						
						accountCategoryElement.setTextContent(accountCategory);
						
					}else {
						logger.error("accountCategory element does not exist");
					}
					
					
					String xml = CommonUtils.stringXML(requestDocument);
					//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
					
					HttpPost request = prepareRequest(vfServiceEndPoint,xml);
					
					CloseableHttpResponse response = httpClient.execute(request);
					
					
					if(response.getStatusLine().getStatusCode() == 200) {
						//System.out.println("line 104 "+response.getStatusLine());
						
					
						documentBuilder = documentBuilderFactory.newDocumentBuilder();
						responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
						
						expression = ".//*[local-name()=\"errors\"]";
						nodes = (NodeList) xPath.evaluate(expression, responseDocument, XPathConstants.NODESET);
						
						if(nodes.getLength() > 0) {
							String code = ((Element)nodes.item(0)).getTextContent();
							
							if(code.equals("000")) {
								logger.trace("operation successful");
								ret = true;
							}
						}else {
							logger.error("response incorrect");
						}
						
						
						
					}else {
						logger.error("errorcode from server : "+response.getStatusLine());
					}
					
				}else {
					logger.error("invalid number of service-id elements in reuest file : "+nodes.getLength());
				}
				
			} catch (FileNotFoundException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (SAXException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (IOException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (XPathExpressionException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (TransformerException e) {
				logger.error("exception occurred : "+e.getMessage());
			}finally {
				if(fileInputStream != null) {

					try {
						fileInputStream.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}else {
			logger.trace("request file not found");
		}
		
		logger.traceExit();
		return ret;
	}

	@Override
	public boolean changeState_complete_vf(String aid) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("changeState_vf called for AID : "+aid);
		Document requestDocument = null;
		Document responseDocument = null;
		File requestFile = null;
		if(SystemUtils.IS_OS_WINDOWS) {
			requestFile = new File(requestFolderPath+"\\ChangeStateRequest_complete_vf.xml");
		}else {
			requestFile = new File(requestFolderPath+"/ChangeStateRequest_complete_vf.xml");
		}
		
		FileInputStream fileInputStream = null;
		if(requestFile.exists()) {
			
			try {
				
				fileInputStream = new FileInputStream(requestFile);
				requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
				
				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = ".//*[local-name()=\"characteristics\"]"
						+ "/*[local-name()=\"name\" and contains(text(),\"Asset Integration ID\")]/../value";
						
				NodeList nodes;
				nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
				
				if(nodes.getLength() == 1) {
					
					Element aidElement = (Element) nodes.item(0);
					
					aidElement.setTextContent(aid);
					
					String xml = CommonUtils.stringXML(requestDocument);
					//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
					
					HttpPost request = prepareRequest(vfServiceEndPoint,xml);
					
					CloseableHttpResponse response = httpClient.execute(request);
					
					
					if(response.getStatusLine().getStatusCode() == 200) {
						//System.out.println("line 104 "+response.getStatusLine());
						
					
						documentBuilder = documentBuilderFactory.newDocumentBuilder();
						responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
						
						expression = ".//*[local-name()=\"errors\"]";
						nodes = (NodeList) xPath.evaluate(expression, responseDocument, XPathConstants.NODESET);
						
						if(nodes.getLength() > 0) {
							String code = ((Element)nodes.item(0)).getTextContent();
							
							if(code.equals("000")) {
								logger.trace("operation successful");
								ret = true;
							}
						}else {
							logger.error("response incorrect");
						}
						
						
						
					}else {
						logger.error("errorcode from server : "+response.getStatusLine());
					}
					
				}else {
					logger.error("invalid number of service-id elements in reuest file : "+nodes.getLength());
				}
				
			} catch (FileNotFoundException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (SAXException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (IOException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (XPathExpressionException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (TransformerException e) {
				logger.error("exception occurred : "+e.getMessage());
			}finally {
				if(fileInputStream != null) {

					try {
						fileInputStream.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}else {
			logger.trace("request file not found");
		}
		
		logger.traceExit();
		return ret;
	}


	@Override
	public boolean disconnect(String aid) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("disconnect called for AID : "+aid);
		Document requestDocument = null;
		Document responseDocument = null;
		File requestFile = new File(requestFolderPath+"\\ChangeState_disconnect_vf.xml");
		if(SystemUtils.IS_OS_WINDOWS) {
			requestFile = new File(requestFolderPath+"\\ChangeState_disconnect_vf.xml");
		}else {
			requestFile = new File(requestFolderPath+"/ChangeState_disconnect_vf.xml");
		}
		FileInputStream fileInputStream = null;
		if(requestFile.exists()) {
			
			try {
				
				fileInputStream = new FileInputStream(requestFile);
				requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
				
				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = ".//*[local-name()=\"characteristics\"]"
						+ "/*[local-name()=\"name\" and contains(text(),\"Asset Integration ID\")]/../value";
						
				NodeList nodes;
				nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
				
				if(nodes.getLength() == 1) {
					
					Element aidElement = (Element) nodes.item(0);
					
					aidElement.setTextContent(aid);
					
					String xml = CommonUtils.stringXML(requestDocument);
					//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
					
					HttpPost request = prepareRequest(vfServiceEndPoint,xml);
					
					CloseableHttpResponse response = httpClient.execute(request);
					
					
					if(response.getStatusLine().getStatusCode() == 200) {
						//System.out.println("line 104 "+response.getStatusLine());
						
					
						documentBuilder = documentBuilderFactory.newDocumentBuilder();
						responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
						
						expression = ".//*[local-name()=\"errors\"]";
						nodes = (NodeList) xPath.evaluate(expression, responseDocument, XPathConstants.NODESET);
						
						if(nodes.getLength() > 0) {
							String code = ((Element)nodes.item(0)).getTextContent();
							
							if(code.equals("000")) {
								logger.trace("operation successful");
								ret = true;
							}
						}else {
							logger.error("response incorrect");
						}
						
						
						
					}else {
						logger.error("errorcode from server : "+response.getStatusLine());
					}
					
				}else {
					logger.error("invalid number of service-id elements in request file : "+nodes.getLength());
				}
				
			} catch (FileNotFoundException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (SAXException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (IOException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (XPathExpressionException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (TransformerException e) {
				logger.error("exception occurred : "+e.getMessage());
			}finally {
				if(fileInputStream != null) {

					try {
						fileInputStream.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}else {
			logger.trace("request file not found");
		}
		
		logger.traceExit();
		return ret;
	}


	@Override
	public boolean changeState_create_vf(String aid) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("changeState_create_vf called for AID : "+aid);
		Document requestDocument = null;
		Document responseDocument = null;
		File requestFile = new File(requestFolderPath+"\\ChangeState_create_vf.xml");
		if(SystemUtils.IS_OS_WINDOWS) {
			requestFile = new File(requestFolderPath+"\\ChangeState_create_vf.xml");
		}else {
			requestFile = new File(requestFolderPath+"/ChangeState_create_vf.xml");
		}
		FileInputStream fileInputStream = null;
		if(requestFile.exists()) {
			
			try {
				
				fileInputStream = new FileInputStream(requestFile);
				requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
				
				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = ".//*[local-name()=\"characteristics\"]"
						+ "/*[local-name()=\"name\" and contains(text(),\"Asset Integration ID\")]/../value";
						
				NodeList nodes;
				nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
				
				if(nodes.getLength() == 1) {
					
					Element aidElement = (Element) nodes.item(0);
					
					aidElement.setTextContent(aid);
					
					String xml = CommonUtils.stringXML(requestDocument);
					//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
					
					HttpPost request = prepareRequest(vfServiceEndPoint,xml);
					
					CloseableHttpResponse response = httpClient.execute(request);
					
					
					if(response.getStatusLine().getStatusCode() == 200) {
						//System.out.println("line 104 "+response.getStatusLine());
						
					
						documentBuilder = documentBuilderFactory.newDocumentBuilder();
						responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
						
						expression = ".//*[local-name()=\"errors\"]";
						nodes = (NodeList) xPath.evaluate(expression, responseDocument, XPathConstants.NODESET);
						
						if(nodes.getLength() > 0) {
							String code = ((Element)nodes.item(0)).getTextContent();
							
							if(code.equals("000")) {
								logger.trace("operation successful");
								ret = true;
							}
						}else {
							logger.error("response incorrect");
						}
						
						
						
					}else {
						logger.error("errorcode from server : "+response.getStatusLine());
					}
					
				}else {
					logger.error("invalid number of service-id elements in reuest file : "+nodes.getLength());
				}
				
			} catch (FileNotFoundException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (SAXException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (IOException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (XPathExpressionException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (TransformerException e) {
				logger.error("exception occurred : "+e.getMessage());
			}finally {
				if(fileInputStream != null) {

					try {
						fileInputStream.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}else {
			logger.trace("request file not found");
		}
		
		logger.traceExit();
		return ret;
	}


	@Override
	public boolean mapTechnicalProduct(Product product) {
		
		logger.trace("mapTechnicalProduct called for product : "+product);
		boolean ret = false;
		Document requestDocument = null;
		Document responseDocument = null;
		File requestFile = new File(requestFolderPath+"\\MapTechnicalProductRequest.xml");
		if(SystemUtils.IS_OS_WINDOWS) {
			requestFile = new File(requestFolderPath+"\\MapTechnicalProductRequest.xml");
		}else {
			requestFile = new File(requestFolderPath+"/MapTechnicalProductRequest.xml");
		}
		FileInputStream fileInputStream = null;
		
		try {
			fileInputStream = new FileInputStream(requestFile);
			requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
			
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = ".//*[local-name()=\"mapTechnicalProductRequest\"]";
					
					
			NodeList nodes;
			nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
			
			if(nodes.getLength() == 1) {
				
				
				Element mapTechnicalProductsElement = (Element) nodes.item(0);
				
				Element productElement = product.toXml().getDocumentElement();
				
				Element importedNode = (Element) requestDocument.importNode(productElement, true);
				
				mapTechnicalProductsElement.appendChild(importedNode);
				
				String xml = CommonUtils.stringXML(requestDocument);
				FileOutputStream fos = new FileOutputStream(new File("mapTechnicalProducts.xml"));
				fos.write(xml.getBytes());
				fos.close();
				
				HttpPost request = prepareRequest(vfServiceEndPoint,xml);
				
				/**/
				CloseableHttpResponse response = httpClient.execute(request);
				
				
				if(response.getStatusLine().getStatusCode() == 200) {
					//System.out.println("line 104 "+response.getStatusLine());
					
				
					documentBuilder = documentBuilderFactory.newDocumentBuilder();
					responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
					
					
					
					expression = ".//*[local-name()=\"errors\"]";
					nodes = (NodeList) xPath.evaluate(expression, responseDocument, XPathConstants.NODESET);
					
					if(nodes.getLength() > 0) {
						String code = ((Element)nodes.item(0)).getTextContent();
						
						if(code.equals("000")) {
							logger.trace("operation successful");
							ret = true;
						}
					}else {
						logger.error("response incorrect");
					}
					
					
					
				}else {
					logger.error("errorcode from server : "+response.getStatusLine());
				}
				
			}else {
				logger.error("invalid number of service-id elements in reuest file : "+nodes.getLength());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.traceExit();
		return ret;
	}


	@Override
	public boolean completeConfiguration(String id) {
		logger.trace("completeConfiguration called for service with id : "+id);
		boolean ret = false;
		
		getServiceConfiguration(id);
		
		String state = rServices.getConfigurations().getState();
		
		switch (state) {
		case "in progress":
			ret = changeServiceConfigurationState(id, "approve");
			if(ret) {
				ret = changeServiceConfigurationState(id, "issue");
				if(ret) {
					ret = changeServiceConfigurationState(id, "complete");
				}
			}
			break;
			
		case "designed":
			ret = changeServiceConfigurationState(id, "issue");
			if(ret) {
				ret = changeServiceConfigurationState(id, "complete");
			}
			break;
			
		case "issued":
			ret = changeServiceConfigurationState(id, "complete");
			break;
			
		case "completed":
			ret = true;
			logger.trace("configuration is already in completed state");
			break;
			
		case "cancelled":
			ret = true;
			logger.trace("configuration is already in cancelled state");
			break;
			
		case "pending cancel":
			ret = changeServiceConfigurationState(id, "complete");
			break;
		default:
			logger.trace("unknown configuration status");
			break;
		}
		
		return ret;
	}


	@Override
	public boolean changeServiceConfigurationState(String id, String configurationAction) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("changeServiceConfigurationState called for service-id : "+id+" to : "+configurationAction);
		Document requestDocument = null;
		Document responseDocument = null;
		File requestFile = new File(requestFolderPath+"\\ChangeServiceConfigurationStateRequest.xml");
		if(SystemUtils.IS_OS_WINDOWS) {
			requestFile = new File(requestFolderPath+"\\ChangeServiceConfigurationStateRequest.xml");
		}else {
			requestFile = new File(requestFolderPath+"/ChangeServiceConfigurationStateRequest.xml");
		}
		FileInputStream fileInputStream = null;
		if(requestFile.exists()) {
			
			try {
				
				fileInputStream = new FileInputStream(requestFile);
				requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
				
				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = ".//*[local-name()=\"serviceId\"]";
				NodeList nodes;
				nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
				
				if(nodes.getLength() == 1) {
					
					Element serviceIdElement = (Element) nodes.item(0);
					
					serviceIdElement.setTextContent(id);
					
					expression = ".//*[local-name()=\"configurationAction\"]";
				
					nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
					
					Element configurationActionElement = (Element) nodes.item(0);
					
					configurationActionElement.setTextContent(configurationAction);
					
					String xml = CommonUtils.stringXML(requestDocument);
					//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
					
					HttpPost request = prepareRequest(serviceEndPoint,xml);
					
					CloseableHttpResponse response = httpClient.execute(request);
					
					
					if(response.getStatusLine().getStatusCode() == 200) {
						//System.out.println("line 104 "+response.getStatusLine());
						
					
						documentBuilder = documentBuilderFactory.newDocumentBuilder();
						responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
						//CommonUtils.printXML(responseDocument.getDocumentElement(), System.out);
						rServices = new Services();
						
						XPath xpath = XPathFactory.newInstance().newXPath();
						expression = ".//*[local-name()=\"service\"]";
						
						nodes = (NodeList) xpath.evaluate(expression, responseDocument.getDocumentElement(), XPathConstants.NODESET);
						rServices.fromXml(nodes.item(0));	
						ret = true;
						
					}else {
						logger.error("errorcode from server : "+response.getStatusLine());
					}
					
				}else {
					logger.error("invalid number of service-id elements in reuest file : "+nodes.getLength());
				}
				
			} catch (FileNotFoundException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (SAXException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (IOException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (XPathExpressionException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (TransformerException e) {
				logger.error("exception occurred : "+e.getMessage());
			}finally {
				if(fileInputStream != null) {

					try {
						fileInputStream.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}else {
			logger.trace("request file not found");
		}
		
		
		
		logger.traceExit();
		return ret;
	}


	@Override
	public boolean cancelConfiguration(String id) {
		logger.trace("cancelConfiguration called for service with id : "+id);
		boolean ret = false;
		
		getServiceConfiguration(id);
		
		String state = rServices.getConfigurations().getState();
		
		switch (state) {
		case "in progress":
			ret = changeServiceConfigurationState(id, "cancel");
			break;
			
		case "designed":
			ret = changeServiceConfigurationState(id, "cancel");
			break;
			
		case "issued":
			ret = changeServiceConfigurationState(id, "cancel");
			if(ret) {
				ret = changeServiceConfigurationState(id, "complete");
			}
			break;
			
		case "completed":
			ret=true;
			logger.trace("configuration is already in completed state");
			break;
			
		case "cancelled":
			ret = true;
			logger.trace("configuration is already in cancelled state");
			break;
			
		case "pending cancel":
			ret = changeServiceConfigurationState(id, "complete");
			break;
		default:
			logger.trace("unknown configuration status");
			break;
		}
		
		return ret;
	}


	@Override
	public boolean unassignSIM(String serviceId) {
		
		boolean ret = false;
		logger.traceEntry();
		logger.trace("unassignSIM called for service-id : "+serviceId);
		ret = getServiceConfiguration(serviceId);
		
		if(ret && (rServices.getConfigurations().getState().equals("in progress") || rServices.getConfigurations().getState().equals("issued") || rServices.getConfigurations().getState().equals("designed"))) {
			
			Document requestDocument = null;
			Document responseDocument = null;
			File requestFile = new File(requestFolderPath+"\\UnassignSIMRequest.xml");
			if(SystemUtils.IS_OS_WINDOWS) {
				requestFile = new File(requestFolderPath+"\\UnassignSIMRequest.xml");
			}else {
				requestFile = new File(requestFolderPath+"/UnassignSIMRequest.xml");
			}
			
			FileInputStream fileInputStream = null;
			if(requestFile.exists()) {
				
				try {
					
					fileInputStream = new FileInputStream(requestFile);
					requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
					
					XPath xPath = XPathFactory.newInstance().newXPath();
					String expression = ".//*[local-name()=\"services\"]"
							+ "/*[local-name()=\"id\"]";
					NodeList nodes;
					nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
					
					if(nodes.getLength() == 1) {
						
						Element serviceIdElement = (Element) nodes.item(0);
						
						serviceIdElement.setTextContent(serviceId);
						
						//version
						expression = ".//*[local-name()=\"services\"]"
								+ "/*[local-name()=\"configurations\"]"
								+ "/*[local-name()=\"version\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element versionElement = (Element) nodes.item(0);
						versionElement.setTextContent(rServices.getConfigurations().getVersion());
						
						//configid
						expression = ".//*[local-name()=\"services\"]"
								+ "/*[local-name()=\"configurations\"]"
								+ "/*[local-name()=\"id\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element configIdElement = (Element) nodes.item(0);
						configIdElement.setTextContent("Se_"+serviceId+"_"+rServices.getConfigurations().getVersion());
						
						//SIM id
						expression = ".//*[local-name()=\"configurationItems\"]"
								+ "/*[local-name()=\"path\" and contains(text(),\"SIM\")]"
								+ "/.."
								+ "/*[local-name()=\"resourceAssignment\"]"
								+ "/*[local-name()=\"resource\"]"
								+ "/*[local-name()=\"id\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element simIdElement = (Element) nodes.item(0);
						String simId = "";
						
						for(ConfigurationItem configurationItem : rServices.getConfigurations().getConfigurationItems()) {
							if(configurationItem.getName().equals("SIM")) {
								simId = configurationItem.getResourceAssignments().get(0).getResources().get(0).getId();
							}
						}
						
						simIdElement.setTextContent(simId);
						
						String xml = CommonUtils.stringXML(requestDocument);
						
						
						FileOutputStream out = new FileOutputStream(new File("request_unassign_sim.xml"));
						CommonUtils.printXML(requestDocument.getDocumentElement(), out);
						out.close();
						HttpPost request = prepareRequest(serviceEndPoint,xml);
						
						CloseableHttpResponse response = httpClient.execute(request);
						
						
						if(response.getStatusLine().getStatusCode() == 200) {
							//System.out.println("line 104 "+response.getStatusLine());
							
						
							documentBuilder = documentBuilderFactory.newDocumentBuilder();
							responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
							//CommonUtils.printXML(responseDocument.getDocumentElement(), System.out);
							rServices = new Services();
							
							XPath xpath = XPathFactory.newInstance().newXPath();
							expression = ".//*[local-name()=\"service\"]";
							
							nodes = (NodeList) xpath.evaluate(expression, responseDocument.getDocumentElement(), XPathConstants.NODESET);
							
							
							rServices.fromXml(nodes.item(0));
							ret = true;
							
						}else {
							logger.error("errorcode from server : "+response.getStatusLine());
						}
						
					}else {
						logger.error("invalid number of service-id elements in reuest file : "+nodes.getLength());
					}
					
				} catch (FileNotFoundException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (SAXException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (IOException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (ParserConfigurationException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (XPathExpressionException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (TransformerException e) {
					logger.error("exception occurred : "+e.getMessage());
				}finally {
					if(fileInputStream != null) {

						try {
							fileInputStream.close();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				
			}else {
				logger.trace("request file not found");
			}
			
		}else {
			logger.error("getServiceConfiguration "+serviceId+" failed");
			if(ret) {
				logger.error("configuration is not in valid state");
			}
		}
		
		
		logger.traceExit();
		return ret;
		
	}


	@Override
	public boolean assignSIM(String serviceId, String resourceId) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("unassignSIM called for service-id : "+serviceId);
		ret = getServiceConfiguration(serviceId);
		
		if(ret && (rServices.getConfigurations().getState().equals("in progress") || rServices.getConfigurations().getState().equals("issued") || rServices.getConfigurations().getState().equals("designed"))) {
			
			Document requestDocument = null;
			Document responseDocument = null;
			File requestFile = new File(requestFolderPath+"\\AssignSIMRequest.xml");
			if(SystemUtils.IS_OS_WINDOWS) {
				requestFile = new File(requestFolderPath+"\\AssignSIMRequest.xml");
			}else {
				requestFile = new File(requestFolderPath+"/AssignSIMRequest.xml");
			}
			FileInputStream fileInputStream = null;
			if(requestFile.exists()) {
				
				try {
					
					fileInputStream = new FileInputStream(requestFile);
					requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
					
					XPath xPath = XPathFactory.newInstance().newXPath();
					String expression = ".//*[local-name()=\"services\"]"
							+ "/*[local-name()=\"id\"]";
					NodeList nodes;
					nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
					
					if(nodes.getLength() == 1) {
						
						Element serviceIdElement = (Element) nodes.item(0);
						
						serviceIdElement.setTextContent(serviceId);
						
						//version
						expression = ".//*[local-name()=\"services\"]"
								+ "/*[local-name()=\"configurations\"]"
								+ "/*[local-name()=\"version\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element versionElement = (Element) nodes.item(0);
						versionElement.setTextContent(rServices.getConfigurations().getVersion());
						
						//configid
						expression = ".//*[local-name()=\"services\"]"
								+ "/*[local-name()=\"configurations\"]"
								+ "/*[local-name()=\"id\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element configIdElement = (Element) nodes.item(0);
						configIdElement.setTextContent("Se_"+serviceId+"_"+rServices.getConfigurations().getVersion());
						
						//SIM id
						expression = ".//*[local-name()=\"configurationItems\"]"
								+ "/*[local-name()=\"path\" and contains(text(),\"SIM\")]"
								+ "/.."
								+ "/*[local-name()=\"resourceAssignment\"]"
								+ "/*[local-name()=\"resource\"]"
								+ "/*[local-name()=\"id\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element simIdElement = (Element) nodes.item(0);
						String simId = "";
						
						simId = resourceId;
						
						simIdElement.setTextContent(simId);
						
						String xml = CommonUtils.stringXML(requestDocument);
						//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
						
						HttpPost request = prepareRequest(serviceEndPoint,xml);
						
						CloseableHttpResponse response = httpClient.execute(request);
						
						
						if(response.getStatusLine().getStatusCode() == 200) {
							//System.out.println("line 104 "+response.getStatusLine());
							
						
							documentBuilder = documentBuilderFactory.newDocumentBuilder();
							responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
							//CommonUtils.printXML(responseDocument.getDocumentElement(), System.out);
							rServices = new Services();
							
							XPath xpath = XPathFactory.newInstance().newXPath();
							expression = ".//*[local-name()=\"service\"]";
							
							nodes = (NodeList) xpath.evaluate(expression, responseDocument.getDocumentElement(), XPathConstants.NODESET);
							
							
							rServices.fromXml(nodes.item(0));
							ret = true;
							
						}else {
							logger.error("errorcode from server : "+response.getStatusLine());
						}
						
					}else {
						logger.error("invalid number of service-id elements in request file : "+nodes.getLength());
					}
					
				} catch (FileNotFoundException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (SAXException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (IOException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (ParserConfigurationException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (XPathExpressionException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (TransformerException e) {
					logger.error("exception occurred : "+e.getMessage());
				}finally {
					if(fileInputStream != null) {

						try {
							fileInputStream.close();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				
			}else {
				logger.trace("request file not found");
			}
			
		}else {
			logger.error("getServiceConfiguration "+serviceId+" failed");
			if(ret) {
				logger.error("configuration is not in valid state");
			}
		}
		
		
		logger.traceExit();
		return ret;
	}


	@Override
	public boolean unassignMSISDN(String serviceId) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("unassignMSISDN called for service-id : "+serviceId);
		ret = getServiceConfiguration(serviceId);
		
		if(ret && (rServices.getConfigurations().getState().equals("in progress") || rServices.getConfigurations().getState().equals("issued") || rServices.getConfigurations().getState().equals("designed"))) {
			
			Document requestDocument = null;
			Document responseDocument = null;
			File requestFile = new File(requestFolderPath+"\\UnassignMSISDNRequest.xml");
			if(SystemUtils.IS_OS_WINDOWS) {
				requestFile = new File(requestFolderPath+"\\UnassignMSISDNRequest.xml");
			}else {
				requestFile = new File(requestFolderPath+"/UnassignMSISDNRequest.xml");
			}
			FileInputStream fileInputStream = null;
			if(requestFile.exists()) {
				System.out.println("here line 1250");
				try {
					
					fileInputStream = new FileInputStream(requestFile);
					requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
					
					XPath xPath = XPathFactory.newInstance().newXPath();
					String expression = ".//*[local-name()=\"services\"]"
							+ "/*[local-name()=\"id\"]";
					NodeList nodes;
					nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
					System.out.println("here line 1409");
					if(nodes.getLength() == 1) {
						
						Element serviceIdElement = (Element) nodes.item(0);
						
						serviceIdElement.setTextContent(serviceId);
						
						//version
						expression = ".//*[local-name()=\"services\"]"
								+ "/*[local-name()=\"configurations\"]"
								+ "/*[local-name()=\"version\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element versionElement = (Element) nodes.item(0);
						versionElement.setTextContent(rServices.getConfigurations().getVersion());
						
						//configid
						expression = ".//*[local-name()=\"services\"]"
								+ "/*[local-name()=\"configurations\"]"
								+ "/*[local-name()=\"id\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element configIdElement = (Element) nodes.item(0);
						configIdElement.setTextContent("Se_"+serviceId+"_"+rServices.getConfigurations().getVersion());
						
						//MSISDN id
						expression = ".//*[local-name()=\"configurationItems\"]"
								+ "/*[local-name()=\"path\" and contains(text(),\"MSISDN\")]"
								+ "/.."
								+ "/*[local-name()=\"resourceAssignment\"]"
								+ "/*[local-name()=\"resource\"]"
								+ "/*[local-name()=\"id\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element simIdElement = (Element) nodes.item(0);
						String simId = "";
						
						for(ConfigurationItem configurationItem : rServices.getConfigurations().getConfigurationItems()) {
							if(configurationItem.getName().equals("MSISDN")) {
								simId = configurationItem.getResourceAssignments().get(0).getResources().get(0).getId();
							}
						}
						
						System.out.println(simId);
						
						simIdElement.setTextContent(simId);
						
						String xml = CommonUtils.stringXML(requestDocument);
						
//						FileOutputStream out = new FileOutputStream(new File("request_unassign_msisdn"));
//						CommonUtils.printXML(requestDocument.getDocumentElement(), out);
//						out.close();
						HttpPost request = prepareRequest(serviceEndPoint,xml);
						
						CloseableHttpResponse response = httpClient.execute(request);
						
						
						if(response.getStatusLine().getStatusCode() == 200) {
							//System.out.println("line 104 "+response.getStatusLine());
							
						
							documentBuilder = documentBuilderFactory.newDocumentBuilder();
							responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
							//CommonUtils.printXML(responseDocument.getDocumentElement(), System.out);
							rServices = new Services();
							
							XPath xpath = XPathFactory.newInstance().newXPath();
							expression = ".//*[local-name()=\"service\"]";
							
							nodes = (NodeList) xpath.evaluate(expression, responseDocument.getDocumentElement(), XPathConstants.NODESET);
							
							
							rServices.fromXml(nodes.item(0));
							ret = true;
							
						}else {
							logger.error("errorcode from server : "+response.getStatusLine());
						}
						
					}else {
						logger.error("invalid number of service-id elements in request file : "+nodes.getLength());
					}
					
				} catch (FileNotFoundException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (SAXException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (IOException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (ParserConfigurationException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (XPathExpressionException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (TransformerException e) {
					logger.error("exception occurred : "+e.getMessage());
				}finally {
					if(fileInputStream != null) {

						try {
							fileInputStream.close();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				
			}else {
				logger.trace("request file not found");
			}
			
		}else {
			logger.error("getServiceConfiguration "+serviceId+" failed");
			if(ret) {
				logger.error("configuration is not in valid state");
			}
		}
		
		
		logger.traceExit();
		return ret;	
	}


	@Override
	public boolean assignMSISDN(String serviceId, String resourceId) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("assignMSISDN called for service-id : "+serviceId);
		ret = getServiceConfiguration(serviceId);
		
		if(ret && (rServices.getConfigurations().getState().equals("in progress") || rServices.getConfigurations().getState().equals("issued") || rServices.getConfigurations().getState().equals("designed"))) {
			
			Document requestDocument = null;
			Document responseDocument = null;
			File requestFile = new File(requestFolderPath+"\\AssignMSISDNRequest.xml");
			if(SystemUtils.IS_OS_WINDOWS) {
				requestFile = new File(requestFolderPath+"\\AssignMSISDNRequest.xml");
			}else {
				requestFile = new File(requestFolderPath+"/AssignMSISDNRequest.xml");
			}
			FileInputStream fileInputStream = null;
			if(requestFile.exists()) {
				
				try {
					
					fileInputStream = new FileInputStream(requestFile);
					requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
					
					XPath xPath = XPathFactory.newInstance().newXPath();
					String expression = ".//*[local-name()=\"services\"]"
							+ "/*[local-name()=\"id\"]";
					NodeList nodes;
					nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
					
					if(nodes.getLength() == 1) {
						
						Element serviceIdElement = (Element) nodes.item(0);
						
						serviceIdElement.setTextContent(serviceId);
						
						//version
						expression = ".//*[local-name()=\"services\"]"
								+ "/*[local-name()=\"configurations\"]"
								+ "/*[local-name()=\"version\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element versionElement = (Element) nodes.item(0);
						versionElement.setTextContent(rServices.getConfigurations().getVersion());
						
						//configid
						expression = ".//*[local-name()=\"services\"]"
								+ "/*[local-name()=\"configurations\"]"
								+ "/*[local-name()=\"id\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element configIdElement = (Element) nodes.item(0);
						configIdElement.setTextContent("Se_"+serviceId+"_"+rServices.getConfigurations().getVersion());
						
						//MSISDN id
						expression = ".//*[local-name()=\"configurationItems\"]"
								+ "/*[local-name()=\"path\" and contains(text(),\"MSISDN\")]"
								+ "/.."
								+ "/*[local-name()=\"resourceAssignment\"]"
								+ "/*[local-name()=\"resource\"]"
								+ "/*[local-name()=\"id\"]";
						nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
						Element simIdElement = (Element) nodes.item(0);
						String simId = "";
						
						simId = resourceId;
						
						simIdElement.setTextContent(simId);
						
						String xml = CommonUtils.stringXML(requestDocument);
						//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
						
						HttpPost request = prepareRequest(serviceEndPoint,xml);
						
						CloseableHttpResponse response = httpClient.execute(request);
						
						
						if(response.getStatusLine().getStatusCode() == 200) {
							//System.out.println("line 104 "+response.getStatusLine());
							
						
							documentBuilder = documentBuilderFactory.newDocumentBuilder();
							responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
							//CommonUtils.printXML(responseDocument.getDocumentElement(), System.out);
							rServices = new Services();
							
							XPath xpath = XPathFactory.newInstance().newXPath();
							expression = ".//*[local-name()=\"service\"]";
							
							nodes = (NodeList) xpath.evaluate(expression, responseDocument.getDocumentElement(), XPathConstants.NODESET);
							
							
							rServices.fromXml(nodes.item(0));
							ret = true;
							
						}else {
							logger.error("errorcode from server : "+response.getStatusLine());
						}
						
					}else {
						logger.error("invalid number of service-id elements in request file : "+nodes.getLength());
					}
					
				} catch (FileNotFoundException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (SAXException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (IOException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (ParserConfigurationException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (XPathExpressionException e) {
					logger.error("exception occurred : "+e.getMessage());
				} catch (TransformerException e) {
					logger.error("exception occurred : "+e.getMessage());
				}finally {
					if(fileInputStream != null) {

						try {
							fileInputStream.close();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				
			}else {
				logger.trace("request file not found");
			}
			
		}else {
			logger.error("getServiceConfiguration "+serviceId+" failed");
			if(ret) {
				logger.error("configuration is not in valid state");
			}
		}
		
		
		logger.traceExit();
		return ret;
	}


	@Override
	public boolean changeAssignmentStatus(String msisdn) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("changeAssignmentStatus called for MSISDN : "+msisdn);
		Document requestDocument = null;
		Document responseDocument = null;
		File requestFile = new File(requestFolderPath+"\\ChangeAssignmentStatusRequest.xml");
		if(SystemUtils.IS_OS_WINDOWS) {
			requestFile = new File(requestFolderPath+"\\ChangeAssignmentStatusRequest.xml");
		}else {
			requestFile = new File(requestFolderPath+"/ChangeAssignmentStatusRequest.xml");
		}
		FileInputStream fileInputStream = null;
		if(requestFile.exists()) {
			
			try {
				
				fileInputStream = new FileInputStream(requestFile);
				requestDocument = documentBuilderFactory.newDocumentBuilder().parse(new InputSource(fileInputStream));
				
				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = ".//*[local-name()=\"id\"]";
						
						
				NodeList nodes;
				nodes = (NodeList) xPath.evaluate(expression, requestDocument.getDocumentElement(), XPathConstants.NODESET);
				
				if(nodes.getLength() == 1) {
					
					Element idElement = (Element) nodes.item(0);
					
					idElement.setTextContent(msisdn);
					
					String xml = CommonUtils.stringXML(requestDocument);
					//CommonUtils.printXML(requestDocument.getDocumentElement(), System.out);
					
					HttpPost request = prepareRequest(vfServiceEndPoint,xml);
					
					CloseableHttpResponse response = httpClient.execute(request);
					
					
					if(response.getStatusLine().getStatusCode() == 200) {
						//System.out.println("line 104 "+response.getStatusLine());
						
					
						documentBuilder = documentBuilderFactory.newDocumentBuilder();
						responseDocument = documentBuilder.parse(new InputSource(response.getEntity().getContent()));
						
						expression = ".//*[local-name()=\"errors\"]";
						nodes = (NodeList) xPath.evaluate(expression, responseDocument, XPathConstants.NODESET);
						
						if(nodes.getLength() > 0) {
							String code = ((Element)nodes.item(0)).getTextContent();
							
							if(code.equals("000")) {
								logger.trace("operation successful");
								ret = true;
							}
						}else {
							logger.error("response incorrect");
						}
						
						
						
					}else {
						logger.error("errorcode from server : "+response.getStatusLine());
					}
					
				}else {
					logger.error("invalid number of service-id elements in request file : "+nodes.getLength());
				}
				
			} catch (FileNotFoundException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (SAXException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (IOException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (ParserConfigurationException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (XPathExpressionException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (TransformerException e) {
				logger.error("exception occurred : "+e.getMessage());
			}finally {
				if(fileInputStream != null) {

					try {
						fileInputStream.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}else {
			logger.trace("request file not found");
		}
		
		logger.traceExit();
		return ret;
	}
}
