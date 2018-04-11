package io.automation.dbclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.automation.siebel.SiebelDBClient;
import io.automation.uimdbclient.UIMDBClient;
import io.automation.utils.FileInputExtractionUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	//Logger outLog = LogManager.getLogger("outputLogger");
        UIMDBClient dbClient = new UIMDBClient();
        
        Properties properties = new Properties();
        String configPath = "";
        
        if(System.getProperty("configPath") != null) {
        	configPath = System.getProperty("configPath");
        }
        
        File file = new File(configPath+"dbconfig.properties");
        System.out.println(file.getAbsolutePath());
        
        if(file.exists() && file.isFile()) {
        	
        	try {
				
        		properties.load(new FileInputStream(file));
				
        		dbClient.startup(properties);
        		
        		//System.out.println("if you can read this, the connection is successful.");
        		
        		List<String> unparsedInputStrings = new ArrayList<>();
        		FileInputExtractionUnit.readLinesFromFile("input.txt", unparsedInputStrings);
        		
        		for(String unparsedInputString : unparsedInputStrings) {
        			
        			String[] parsedInputString = unparsedInputString.split("[\t,]+");
        			
        			if(parsedInputString.length == 2) {
        				String oldAid = parsedInputString[0];
            			String newAid = parsedInputString[1];
            			System.out.println("renaming "+oldAid+" to "+newAid);
            			dbClient.renameAID(oldAid, newAid);
        			}else {
        				System.out.println("input is incorrect : "+unparsedInputString);
        			}
        			
        		}
        		
        		dbClient.shutdown();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }else {
        	System.out.println("config file not found");
        }
        
        
        //properties.load();
    }
}
