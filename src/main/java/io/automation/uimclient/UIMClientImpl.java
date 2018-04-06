package io.automation.uimclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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

import io.automation.beans.Services;
import io.automation.utils.CommonUtils;

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
		File requestFile = new File(requestFolderPath+"\\GetServiceConfigurationRequest.xml");
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
						
						System.out.println("configuration is in : "+rServices.getConfigurations().getState());
						CommonUtils.printXML(rServices.toXml().getDocumentElement(), System.out);
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
	
	

	

}
