package io.automation.uimclient;

import java.util.Properties;

public class UIMClientFactory {
	
	private static final UIMClientFactory instance = new UIMClientFactory();
	
	
	private UIMClientFactory() {
		
	}
	
	public UIMClientFactory getInstance() {
		return instance;
	}
	
	public UIMClient getUIMClient(Properties properties) {
		
		UIMClientImpl client = new UIMClientImpl();
		
		
		
		
		return client;
	}
	
}
