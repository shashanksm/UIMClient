package io.automation.uimclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import io.automation.utils.FileInputExtractionUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Properties properties = new Properties();
    	try {
			properties.load(new FileInputStream(new File("config\\uimcilentconfig.properties")));
			UIMClient client = UIMClientFactory.getInstance().getUIMClient();
			client.startup(properties);
			
			List<String> unparsedInputStrings = new ArrayList<>();
			
			FileInputExtractionUnit.readLinesFromFile("input.txt", unparsedInputStrings);
			
			for(String unparsedInputString : unparsedInputStrings) {
				
				String[] parsedInputString = unparsedInputString.split("[\t,]+");
				
			
				String serviceId = parsedInputString[0];
				System.out.println(serviceId);
				client.getServiceConfiguration(serviceId);
				
			}
			
			client.shutdown();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
        
    }
}
