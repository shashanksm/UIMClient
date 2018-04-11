package io.automation.uimclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import io.automation.beans.AIDStatusRecordBean;
import io.automation.beans.ActiveProductsRecordBean;
import io.automation.beans.OrderLineItemsRecordBean;
import io.automation.siebel.SiebelDBClient;
import io.automation.uimdbclient.UIMDBClient;
import io.automation.utils.CommonUtils;
import io.automation.utils.FileInputExtractionUnit;
import io.automation.vfbeans.Characteristics;
import io.automation.vfbeans.CustomerService;
import io.automation.vfbeans.Product;
import io.automation.vfbeans.Specification;

public class TestV1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File input = new File("input.txt");
		
		
		Properties properties = null;
		Properties uimdbproperties = null;
		Properties uimproperties = null;
		SiebelDBClient client = new SiebelDBClient();
		UIMDBClient uimdbClient = new UIMDBClient();
		UIMClient uimclient = UIMClientFactory.getInstance().getUIMClient();
		properties = new Properties();
		uimdbproperties = new Properties();
		uimproperties = new Properties();
		try {
			properties.load(new FileInputStream(new File("config\\siebeldbconfig.properties")));
			uimdbproperties.load(new FileInputStream(new File("config\\uimdbconfig.properties")));
			uimproperties.load(new FileInputStream(new File("config\\uimclientconfig.properties")));
			client.startup(properties);
			uimdbClient.startup(uimdbproperties);
			uimclient.startup(uimproperties);
			
			if(input.exists()) {
				
				List<String> unparsedInputStrings = new ArrayList<>();
				
				FileInputExtractionUnit.readLinesFromFile("input.txt", unparsedInputStrings);
					
				for(String unparsedInputString : unparsedInputStrings) {
					
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					
				
					String ctn = parsedInputString[0];
					
					List<ActiveProductsRecordBean> list = client.getActiveProducts(ctn);
					
					String aid = list.get(0).getIntegration_id();
					
					AIDStatusRecordBean aidStatusRecordBean = uimdbClient.getAIDStatus(aid);
					
					if(aidStatusRecordBean != null) {
						
						if(aidStatusRecordBean.getAdminstate().equals("IN_SERVICE") || aidStatusRecordBean.getAdminstate().equals("PENDING")) {
							
							String id = aidStatusRecordBean.getId();
							
							uimclient.completeConfiguration(id);
							
							uimclient.disconnect(aid);
							
							uimdbClient.renameAID(aid, aid+"_OLD");
							
							uimclient.createFixedService(aid);
							
							aidStatusRecordBean = uimdbClient.getAIDStatus(aid);
							
							id = aidStatusRecordBean.getId();
							
							uimclient.changeState_complete_vf(aid);
							
							Specification specification = new Specification("FixedLineProduct", "Service");
							Characteristics c1 = new Characteristics("Asset Integration ID", aid);
							Characteristics c2 = new Characteristics("mappingName", "ProductCode");
							Characteristics c3 = new Characteristics("mappingName", "PSTN_Single_Line");
							Characteristics c4 = new Characteristics("accountCategory", "INDIVIDUAL");
							
							List<Characteristics> characteristics = new ArrayList<>();
							characteristics.add(c1);
							characteristics.add(c2);
							characteristics.add(c3);
							characteristics.add(c4);
							
							List<CustomerService> customerServices = new ArrayList<>();
							
							List<ActiveProductsRecordBean> activeProductsRecordBeans = client.getActiveProducts(ctn);
							
							for(ActiveProductsRecordBean activeProductsRecordBean : activeProductsRecordBeans) {
								
								String newcoId = activeProductsRecordBean.getPart_num();
								Characteristics c = new Characteristics("newCoId", newcoId);
								CustomerService cs = new CustomerService(c, "ADD");
								customerServices.add(cs);
							}
							
							
							String action = "ADD";
							
							Product product = new Product(specification, characteristics, action, customerServices);
							
							uimclient.mapTechnicalProduct(product);
							
							uimclient.changeState_complete_vf(aid);
							
							
						}else if(aidStatusRecordBean.getAdminstate().equals("DISCONNECTED")){
							
						}else {
							System.out.println("adminstate not in valid state for workaround : "+aidStatusRecordBean.getAdminstate());
						}
						
					}else {
						System.out.println("service does not exist in uim");
						
						
						boolean ret = uimclient.createFixedService(aid);
						System.out.println("line 143 "+ret);
						aidStatusRecordBean = uimdbClient.getAIDStatus(aid);
						System.out.println("line 145");
						String id = aidStatusRecordBean.getId();
						System.out.println("line 147");
						uimclient.changeState_complete_vf(aid);
						System.out.println("line 149");
						Specification specification = new Specification("FixedLineProduct", "Service");
						Characteristics c1 = new Characteristics("Asset Integration ID", aid);
						Characteristics c2 = new Characteristics("mappingName", "ProductCode");
						Characteristics c3 = new Characteristics("mappingName", "PSTN_Single_Line");
						Characteristics c4 = new Characteristics("accountCategory", "INDIVIDUAL");
						System.out.println("line 155");
						List<Characteristics> characteristics = new ArrayList<>();
						characteristics.add(c1);
						characteristics.add(c2);
						characteristics.add(c3);
						characteristics.add(c4);
						
						List<CustomerService> customerServices = new ArrayList<>();
						
						List<ActiveProductsRecordBean> activeProductsRecordBeans = client.getActiveProducts(ctn);
						
						for(ActiveProductsRecordBean activeProductsRecordBean : activeProductsRecordBeans) {
							
							String newcoId = activeProductsRecordBean.getPart_num();
							Characteristics c = new Characteristics("newCoId", newcoId);
							CustomerService cs = new CustomerService(c, "ADD");
							customerServices.add(cs);
						}
						
						
						String action = "ADD";
						
						Product product = new Product(specification, characteristics, action, customerServices);
						
						uimclient.mapTechnicalProduct(product);
						
						uimclient.changeState_complete_vf(aid);
						
					}
					
					
				}
				
				
				
				
			}else {
				System.out.println("input file not found");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			client.shutdown();
			uimdbClient.shutdown();
			uimclient.shutdown();
		}
		
		
		
		
		
	}

}
