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
	public boolean changeState_complete_vf(String aid);
	public boolean disconnect(String aid);
	public boolean changeState_create_vf(String aid);
	public boolean completeConfiguration(String id);
	public boolean cancelConfiguration(String id);
	
	public boolean changeServiceConfigurationState(String id, String configurationAction);
	public boolean mapTechnicalProduct(Product product);
	
}
