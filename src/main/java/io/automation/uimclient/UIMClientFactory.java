package io.automation.uimclient;

import java.util.Properties;

public class UIMClientFactory {
	
	private static final UIMClientFactory instance = new UIMClientFactory();
	
	
	private UIMClientFactory() {
		
	}
	
	public static UIMClientFactory getInstance() {
		return instance;
	}
	
	public UIMClient getUIMClient() {
		
		UIMClientImpl client = new UIMClientImpl();
		
		
		
		
		return client;
	}
	
}
