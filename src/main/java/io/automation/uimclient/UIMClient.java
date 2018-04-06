package io.automation.uimclient;

import java.util.Properties;

public interface UIMClient {
	
	//public boolean createConfiguration(String assetIntegrationID);
	//public boolean getServiceConfiguration(String assetIntegrationId);
	//public boolean
	
	public void startup(Properties properties);
	public void shutdown();
	public boolean getServiceConfiguration(String serviceid);
	//public boolean createFixedService(String aid, List);
	
	
}
