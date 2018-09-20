package io.automation.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.automation.beans.AIDStatusRecordBean;
import io.automation.beans.ActiveProductsRecordBean;
import io.automation.beans.BroadbandDetailsBean;
import io.automation.beans.CancellationDataBean;
import io.automation.beans.FixedSIProduct;
import io.automation.beans.ICCIDBean;
import io.automation.beans.LogicalDeviceRecordBean;
import io.automation.beans.MSISDNStatusBean;
import io.automation.siebel.SiebelDBClient;
import io.automation.uimclient.UIMClient;
import io.automation.uimclient.UIMClientFactory;
import io.automation.uimdbclient.UIMDBClient;
import io.automation.vfbeans.Characteristics;
import io.automation.vfbeans.CustomerService;
import io.automation.vfbeans.Product;
import io.automation.vfbeans.Specification;

public class Workarounds {
	
	private UIMClient uimclient;
	private UIMDBClient uimdbclient;
	private SiebelDBClient siebeldbclient;
	
	private static final Logger logger = LogManager.getLogger(Workarounds.class);
	
	public void startup(String configFolderPath) {
		
		uimclient = UIMClientFactory.getInstance().getUIMClient();
		uimdbclient = new UIMDBClient();
		siebeldbclient = new SiebelDBClient();
		
		Properties uimclientproperties = new Properties();
		Properties uimdbclientproperties = new Properties();
		Properties siebeldbclientproperties = new Properties();
		
		FileInputStream fs1 = null;
		FileInputStream fs2 = null;
		FileInputStream fs3 = null;
		
		File file = null;
		
		if(SystemUtils.IS_OS_WINDOWS) {
			file = new File(configFolderPath+"\\uimclientconfig.properties");
		}else {
			file = new File(configFolderPath+"/uimclientconfig.properties");
		}
		
		if(file.exists() && file.isFile()) {
			
			try {
				
				fs1 = new FileInputStream(file);
				uimclientproperties.load(fs1);
				
				uimclient.startup(uimclientproperties);
				
				if(SystemUtils.IS_OS_WINDOWS) {
					file = new File(configFolderPath+"\\uimdbclientconfig.properties");
				}else {
					file = new File(configFolderPath+"/uimdbclientconfig.properties");
				}
				
				
				if(file.exists() && file.isFile()) {
					
					fs2 = new FileInputStream(file);
					
					uimdbclientproperties.load(fs2);
					uimdbclient.startup(uimdbclientproperties);
					
					
					if(SystemUtils.IS_OS_WINDOWS) {
						file = new File(configFolderPath+"\\siebeldbclientconfig.properties");
					}else {
						file = new File(configFolderPath+"/siebeldbclientconfig.properties");
					}
					
					
					if(file.exists() && file.isFile()) {
						
						fs3 = new FileInputStream(file);
						
						siebeldbclientproperties.load(fs3);
						siebeldbclient.startup(siebeldbclientproperties);
						
					}else {
						logger.error("siebeldbclientconfig.properties file not found in "+configFolderPath);
					}
					
				}else {
					logger.error("uimdbclientconfig.properties file not found in "+configFolderPath);
				}
				
			} catch (FileNotFoundException e) {
				logger.error("exception occurred : "+e.getMessage());
			} catch (IOException e) {
				logger.error("exception occurred : "+e.getMessage());
			}finally {
				if(fs1 != null) {
					try {
						fs1.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(fs2 != null) {
					try {
						fs2.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(fs3 != null) {
					try {
						fs3.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}else {
			logger.error("uimclientconfig.properties file not found in "+configFolderPath);
		}
				
				
	}
	
	public void shutdown() {
		uimclient.shutdown();
		uimdbclient.shutdown();
		siebeldbclient.shutdown();
	}
	
	public boolean fluimpatch(String ctn) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("fluimpatch called for CTN : "+ctn);
		
		List<ActiveProductsRecordBean> list = siebeldbclient.getActiveProducts(ctn);
		
		
		
		
		if(list!= null && list.size()>0) {
			
			String aid = list.get(0).getIntegration_id();
			AIDStatusRecordBean aidStatusRecordBean = uimdbclient.getAIDStatus(aid);
			
			if(aidStatusRecordBean == null) {
				logger.error("aid does not exist");
				uimclient.createFixedService(aid);
				uimclient.changeState_create_vf(aid);
				uimclient.changeState_complete_vf(aid);
			}
			
			String id = aidStatusRecordBean.getId();
			
			ret = uimclient.completeConfiguration(id);
			
			if(ret) {
				
				ret = uimclient.disconnect(aid);
				
				if(ret) {
					
					ret = uimclient.changeState_complete_vf(aid);
					
					if(ret) {
						
						ret = uimdbclient.renameAID(aid, aid+"_OLD");
						
						if(ret) {
							
							
							ret = uimclient.createFixedService(aid);
							
							if(ret) {
								aidStatusRecordBean = uimdbclient.getAIDStatus(aid);
								id = aidStatusRecordBean.getId();
								ret = uimclient.changeState_complete_vf(aid);
								if(ret) {
									Specification specification = new Specification("FixedLineProduct", "Service");
									Characteristics c1 = new Characteristics("Asset Integration ID", aid);
									Characteristics c2 = new Characteristics("mappingName", "ProductCode");
									Characteristics c3 = new Characteristics("mappingValue", "PSTN_Single_Line");
									Characteristics c4 = new Characteristics("accountCategory", "INDIVIDUAL");
									
									List<Characteristics> characteristics = new ArrayList<>();
									characteristics.add(c1);
									characteristics.add(c2);
									characteristics.add(c3);
									characteristics.add(c4);
									
									List<CustomerService> customerServices = new ArrayList<>();
									
									List<ActiveProductsRecordBean> activeProductsRecordBeans = siebeldbclient.getActiveProducts(ctn);
									
									for(ActiveProductsRecordBean activeProductsRecordBean : activeProductsRecordBeans) {
										
										String newcoId = activeProductsRecordBean.getPart_num();
										Characteristics c = new Characteristics("newCoId", newcoId);
										CustomerService cs = new CustomerService(c, "ADD");
										customerServices.add(cs);
									}
									
									
									String action = "ADD";
									
									Product product = new Product(specification, characteristics, action, customerServices);
									
									ret = uimclient.mapTechnicalProduct(product);
									
									if(ret) {
										
										ret = uimclient.changeState_complete_vf(aid);
										
									}else {
										logger.trace("mapTechnicalProduct failed");
									}
								}else {
									logger.trace("changeState_complete_vf failed");
								}
							}else {
								logger.trace("createFixedService failed");
							}
							
						}else {
							logger.error("renameAID failed");
						}
						
					}else {
						logger.error("changeState_complete_vf configuration failed");
					}
					
				}else {
					logger.error("disconnect aid failed");
				}
				
				
			}else {
				logger.error("complete configuration failed");
			}
			
		}else {
			logger.trace("aid does not exist");
			
			if(list == null || list.size()==0) {
				logger.error("no ative products for the CTN : "+ctn);
			}
		}
		
	
		logger.trace("completing configuration in UIM");
		
		logger.traceExit();
		return ret;
	}
	
	
	
	public boolean fluimBBpatch(String ctn) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("fluimBBpatch called for CTN : "+ctn);
		
		List<ActiveProductsRecordBean> list = siebeldbclient.getActiveProducts(ctn);
		
		
		
		
		if(list!= null && list.size()>0) {
			
			String aid = list.get(0).getIntegration_id();
			AIDStatusRecordBean aidStatusRecordBean = uimdbclient.getAIDStatus(aid);
			
			if(aidStatusRecordBean == null) {
				logger.error("aid does not exist");
				uimclient.createFixedService(aid);
				uimclient.changeState_create_vf(aid);
				uimclient.changeState_complete_vf(aid);
			}
			
			String id = uimdbclient.getAIDStatus(aid).getId();
			
			ret = uimclient.completeConfiguration(id);
			
			if(ret) {
				
				ret = uimclient.disconnect(aid);
				
				if(ret) {
					
					ret = uimclient.changeState_complete_vf(aid);
					
					if(ret) {
						
						ret = uimdbclient.renameAID(aid, aid+"_OLD");
						
						if(ret) {
							
							BroadbandDetailsBean bbbean = siebeldbclient.getBroadBandDetails(ctn);
							
							ret = uimclient.createFixedBroadbandService(aid, bbbean.getAccountCategory(), bbbean.getTechProductCode(), bbbean.getTechProductName());
							
							if(ret) {
								aidStatusRecordBean = uimdbclient.getAIDStatus(aid);
								id = aidStatusRecordBean.getId();
								ret = uimclient.changeState_complete_vf(aid);
								if(ret) {
									Specification specification = new Specification("FixedBroadbandProduct", "Service");
									Characteristics c1 = new Characteristics("Asset Integration ID", aid);
									Characteristics c2 = new Characteristics("mappingName", bbbean.getTechProductCode());
									Characteristics c3 = new Characteristics("mappingValue", bbbean.getTechProductName());
									Characteristics c4 = new Characteristics("accountCategory", bbbean.getAccountCategory());
									
									List<Characteristics> characteristics = new ArrayList<>();
									characteristics.add(c1);
									characteristics.add(c2);
									characteristics.add(c3);
									characteristics.add(c4);
									
									List<CustomerService> customerServices = new ArrayList<>();
									
									List<ActiveProductsRecordBean> activeProductsRecordBeans = siebeldbclient.getActiveProducts(ctn);
									
									for(ActiveProductsRecordBean activeProductsRecordBean : activeProductsRecordBeans) {
										
										String newcoId = activeProductsRecordBean.getPart_num();
										Characteristics c = new Characteristics("newCoId", newcoId);
										CustomerService cs = new CustomerService(c, "ADD");
										customerServices.add(cs);
									}
									
									
									String action = "ADD";
									
									Product product = new Product(specification, characteristics, action, customerServices);
									
									ret = uimclient.mapTechnicalProduct(product);
									
									if(ret) {
										
										ret = uimclient.changeState_complete_vf(aid);
										
									}else {
										logger.trace("mapTechnicalProduct failed");
									}
								}else {
									logger.trace("changeState_complete_vf failed");
								}
							}else {
								logger.trace("createFixedService failed");
							}
							
						}else {
							logger.error("renameAID failed");
						}
						
					}else {
						logger.error("changeState_complete_vf configuration failed");
					}
					
				}else {
					logger.error("disconnect aid failed");
				}
				
				
			}else {
				logger.error("complete configuration failed");
			}
			
		}else {
			logger.trace("aid does not exist");
			
			if(list == null || list.size()==0) {
				logger.error("no ative products for the CTN : "+ctn);
			}
		}
		
	
		logger.trace("completing configuration in UIM");
		
		logger.traceExit();
		return ret;
	}
	
	
	public boolean cancelConfiguration(String aid) {
		boolean ret = false;
		logger.traceEntry();
		AIDStatusRecordBean aidStatusRecordBean = uimdbclient.getAIDStatus(aid);
		
		if(aidStatusRecordBean != null) {
			
			String id = aidStatusRecordBean.getId();
			ret = uimclient.cancelConfiguration(id);
			
		}else {
			logger.error("aid does not exist");
		}
		
		logger.traceExit();
		return ret;
	}
	
	public boolean completeConfiguration(String aid) {
		boolean ret = false;
		logger.traceEntry();
		AIDStatusRecordBean aidStatusRecordBean = uimdbclient.getAIDStatus(aid);
		
		if(aidStatusRecordBean != null) {
			
			String id = aidStatusRecordBean.getId();
			ret = uimclient.completeConfiguration(id);
			
		}else {
			logger.error("aid does not exist");
		}
		
		logger.traceExit();
		return ret;
	}
	
	public boolean cancelOrder(CancellationDataBean cancellationDataBean) {
		boolean ret = false;
		
		return ret;
	}
	
	public boolean simSwap(String aid, String simToDelete, String simToAdd) {
		boolean ret = false;
		
		AIDStatusRecordBean aidStatusRecordBean = uimdbclient.getAIDStatus(aid);
		
		if(aidStatusRecordBean != null) {
			
			ret = cancelConfiguration(aid);
			String serviceId = aidStatusRecordBean.getId();
			if(ret) {
				
				ret = uimclient.changeState_create_vf(aid);
				
				if(ret) {
					
					ret = uimclient.unassignSIM(serviceId);
					
					if(ret) {
						
						LogicalDeviceRecordBean ld = uimdbclient.getLogicalDevice(simToAdd);
						
						if(ld != null) {
							
							String resourceId = ld.getId();
							ret = uimclient.assignSIM(serviceId, resourceId);
							if(ret) {
								
								ret = completeConfiguration(aid);
								
							}else {
								logger.error("assignSIM failed");
							}
							
						}else {
							logger.error("ICCID does not exist");
						}
						
					}else {
						logger.error("unable to unassign sim");
					}
					
				}else {
					logger.error("could not create a configuration");
				}
				
			}else {
				logger.error("unable to complete configuration");
			}
			
		}else {
			logger.error("service "+aid+" does not exist in uim");
		}
		
		return ret;
	}
	
	
	public boolean changeMSISDN(String aid,String msisdnToDelete, String msisdnToAdd) {
		boolean ret = false;
		
		AIDStatusRecordBean aidStatusRecordBean = uimdbclient.getAIDStatus(aid);
		if(aidStatusRecordBean != null) {
			
			String id = aidStatusRecordBean.getId();
			
			MSISDNStatusBean msisdnStatusBean = uimdbclient.getMSISDNStatus(msisdnToDelete);
			if(msisdnStatusBean != null && msisdnStatusBean.getTcAdminstate().equals("ASSIGNED")) {
				
				msisdnStatusBean = uimdbclient.getMSISDNStatus(msisdnToAdd);
				
				//if(msisdnStatusBean != null && (msisdnStatusBean.getTcAdminstate().equals("DISCONNECTED") || msisdnStatusBean.getTcAdminstate().equals("UNASSIGNED"))) {
					
//					String msisdnAssignmentStatus = msisdnStatusBean.getTcAdminstate();
//					
//					if(msisdnAssignmentStatus.equals("DISCONNECTED")) {
//						
//						ret = uimclient.changeAssignmentStatus(msisdnToAdd);
//					}
					
					ret = uimclient.cancelConfiguration(id);
					
					if(ret) {
						
						ret = uimclient.changeState_create_vf(aid);
						if(ret) {
							
							ret = uimclient.unassignMSISDN(id);
							if(ret) {
								ret = uimclient.assignMSISDN(id,msisdnToAdd);
								if(ret) {
									ret = completeConfiguration(aid);
								}else {
									logger.error("assign MSISDN failed");
								}
							}else {
								logger.trace("unassign MSISDN failed");
							}
							
						}else {
							logger.error("creating configuration failed");
						}
						
					}else {
						logger.error("cancel configuration failed");
					}
					
//				}else {
//					logger.error("MSISDN to add "+msisdnToAdd+" is not in required state : "+msisdnStatusBean.getTcAdminstate());
//				}
				
			}else {
				if(msisdnStatusBean == null)
					logger.error("MSISDN "+msisdnToDelete+" does not exist");
				else {
					logger.error("MSISDN to delete " +msisdnToDelete+" is not in assigned state : "+msisdnStatusBean.getTcAdminstate() );
				}
				
			}
			
		}else {
			logger.error("aid does not exist");
		}
		
		return ret;
	}

	public boolean renameAID(String oldAid, String newAid) {
		logger.traceEntry();
		logger.trace("rename AID called for "+oldAid+" to "+newAid);
		boolean ret = false;
		
		AIDStatusRecordBean aidStatusRecordBean = uimdbclient.getAIDStatus(oldAid);
		
		if(aidStatusRecordBean != null) {
			
			ret = uimdbclient.renameAID(oldAid, newAid);
			
		}else {
			logger.error("aid does not exist in uim");
		}
		
		return ret;
	}

	public boolean changeAssignmentStatus(String ctn) {
		boolean ret = false;
		logger.traceEntry();
		logger.trace("changeAssignmentStatus called for ctn : "+ctn);
		
		MSISDNStatusBean msisdnStatusBean = uimdbclient.getMSISDNStatus(ctn);
		
		if(msisdnStatusBean != null) {
			
			String status = msisdnStatusBean.getTcAdminstate();
			boolean condition = status.equals("UNASSIGNED") || status.equals("DISCONNECTED") || status.equals("TRANSITIONAL"); 
			if(condition) {
				
				ret = uimclient.changeAssignmentStatus(ctn);
				
			}else {
				logger.trace("ctn is in "+status+" state");
			}
			
		}else {
			logger.trace("ctn "+ctn+" does not exist");
		}
		
		return ret;
	}

	public boolean disconnect(String aid) {
		boolean ret = false;
		
		logger.traceEntry();
		logger.trace("disconnect called for "+aid);
		
		AIDStatusRecordBean aidStatusRecordBean = uimdbclient.getAIDStatus(aid);
		
		if(aidStatusRecordBean != null) {
			
			
			ret = completeConfiguration(aid);
			
			ret = uimclient.disconnect(aid);
			
			ret = completeConfiguration(aid);
			
			String status = aidStatusRecordBean.getAdminstate();
			
			if(status.equals("DISCONNECTED")) {
				ret = true;
			}
			
		}else {
			logger.error("aid "+aid+" does not exist");
		}
		logger.traceExit();
		return ret;
	}

	public boolean mapTechnicalProducts(String aid, String productCode) {
		boolean ret = false;
		
		List<Characteristics> characteristics = new ArrayList<>();
		
		String action = "ADD";
		
		List<CustomerService> customerServices = new ArrayList<>();
		
		Characteristics c = new Characteristics("newCoId", productCode);
		
		CustomerService customerService = new CustomerService(c, "ADD");
		
		customerServices.add(customerService);
		
		Specification specification = new Specification("FixedLineProduct", "Service");
		Characteristics c1 = new Characteristics("Asset Integration ID", aid);
		Characteristics c2 = new Characteristics("mappingName", "ProductCode");
		Characteristics c3 = new Characteristics("mappingValue", "PSTN_Single_Line");
		Characteristics c4 = new Characteristics("accountCategory", "INDIVIDUAL");
		
		characteristics.add(c1);
		characteristics.add(c2);
		characteristics.add(c3);
		characteristics.add(c4);
		
		Product product = new Product(specification, characteristics, action, customerServices);
		
		uimclient.mapTechnicalProduct(product);
		
		return ret;
	}
	
	public void testProducts(String aid, String productCode) {
		uimclient.createFixedService(aid);
		uimclient.changeState_complete_vf(aid);
List<Characteristics> characteristics = new ArrayList<>();
		
		String action = "ADD";
		
		List<CustomerService> customerServices = new ArrayList<>();
		
		Characteristics c = new Characteristics("newCoId", productCode);
		
		CustomerService customerService = new CustomerService(c, "ADD");
		
		customerServices.add(customerService);
		
		Specification specification = new Specification("FixedLineProduct", "Service");
		Characteristics c1 = new Characteristics("Asset Integration ID", aid);
		Characteristics c2 = new Characteristics("mappingName", "ProductCode");
		Characteristics c3 = new Characteristics("mappingValue", "PSTN_Single_Line");
		Characteristics c4 = new Characteristics("accountCategory", "INDIVIDUAL");
		
		characteristics.add(c1);
		characteristics.add(c2);
		characteristics.add(c3);
		characteristics.add(c4);
		
		Product product = new Product(specification, characteristics, action, customerServices);
		
		uimclient.mapTechnicalProduct(product);
		
		completeConfiguration(aid);
	}
	
	public List<FixedSIProduct> getFixedSIProducts(String aid){
		
		List<FixedSIProduct> ret = new ArrayList<>();
		logger.traceEntry();
		
		String id = uimdbclient.getAIDStatus(aid).getId();
		
		if(id != null) {
			
			String serviceEntityId = uimdbclient.getServiceEntityId(id);
			
			if(serviceEntityId != null) {
				
				String serviceConfigurationVersionEntityId = uimdbclient.getServiceConfigurationVersionEntityId(serviceEntityId);
				
				if(serviceConfigurationVersionEntityId != null) {
					
					List<String> serviceconfigitem_chars = uimdbclient.getServiceConfigurationItems(serviceConfigurationVersionEntityId);
					
					if(serviceconfigitem_chars.size()!= 0) {
						
						for(String serviceconfigitem_char : serviceconfigitem_chars) {
							
							ret.add(uimdbclient.getServiceConfigItemChar(serviceconfigitem_char));
						}
						
						
					}else {
						logger.error("serviceconfigitem_chars "+serviceConfigurationVersionEntityId+" does not have any items");
					}
					
				}else {
					logger.error("serviceConfigurationVersionEntityId "+serviceEntityId+" does not exist");
				}
				
			}else {
				logger.error("serviceEntityId "+serviceEntityId+" does not exist");
			}
			
			
		}else {
			logger.error("aid "+aid+" does not exist");
		}
		
		
		logger.traceExit();
		return ret;
		
	}
	
	public boolean createFixedService(String aid) {
		boolean ret = false;
		
		ret = uimclient.createFixedService(aid);
		
		return ret;
	}
	
	public boolean createFixedBroadbandService(String serviceNumber) {
		boolean ret = false;
		
		BroadbandDetailsBean bean = siebeldbclient.getBroadBandDetails(serviceNumber);
		
		if(bean != null) {
			
			ret = uimclient.createFixedBroadbandService(bean.getAid(), bean.getAccountCategory(), bean.getTechProductCode(), bean.getTechProductName());
			
			if(ret) {
				
				String aid = bean.getAid();
				ret = completeConfiguration(aid);
				
				 
			}else {
				logger.error("could not create service.");
			}
			
		}else {
			logger.error("service-number "+serviceNumber+" does not exist");
		}
		
		return ret;
	}
	
	public String getICCIDDetails(String iccid) {
		StringBuilder sb = new StringBuilder("");
		
		ICCIDBean bean = uimdbclient.getICCIDDetails(iccid);
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		sb.append(fmt.print(DateTime.now()));
		sb.append(",");
		sb.append(iccid);
		if(bean != null) {
			
			sb.append(",");
			sb.append(bean.getAdminstate());
			sb.append(",");
			sb.append(bean.getLastModifiedDate().toString());
			sb.append(",");
			sb.append(bean.getLastModifiedUser());
		}else {
			sb.append(",");
			sb.append("ICCID does not exist");
		}
		
		
		return sb.toString();
	}
}
