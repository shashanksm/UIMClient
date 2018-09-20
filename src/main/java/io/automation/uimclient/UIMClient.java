package io.automation.uimclient;

import java.util.List;
import java.util.Properties;

import io.automation.vfbeans.Product;


public interface UIMClient {
	
	//public boolean createConfiguration(String assetIntegrationID);
	//public boolean getServiceConfiguration(String assetIntegrationId);
	//public boolean
	
	public void startup(Properties properties);
	public void shutdown();
	public boolean getServiceConfiguration(String serviceid);
	
	public boolean createFixedService(String aid);
	public boolean createFixedBroadbandService(String aid, String accountCategory, String productCode, String productName);
	public boolean changeState_complete_vf(String aid);
	public boolean disconnect(String aid);
	public boolean changeState_create_vf(String aid);
	public boolean completeConfiguration(String id);
	public boolean cancelConfiguration(String id);
	
	public boolean changeServiceConfigurationState(String id, String configurationAction);
	
	public boolean mapTechnicalProduct(Product product);
	public boolean unassignSIM(String serviceId);
	public boolean assignSIM(String serviceId,String resourceId);
	public boolean unassignMSISDN(String serviceId);
	public boolean assignMSISDN(String serviceId, String resourceId);
	public boolean changeAssignmentStatus(String msisdn);
	
}
