package io.automation.application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.SystemUtils;

import io.automation.utils.CommonUtils;
import io.automation.beans.FixedSIProduct;
import io.automation.logic.Workarounds;
import io.automation.utils.FileInputExtractionUnit;
import io.automation.utils.FileOutputPublishingUnit;


public class Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length == 2) {
			
			String path = args[1];
			List<String> unparsedInputStrings = new ArrayList<String>();
			
			FileInputExtractionUnit.readLinesFromFile(path, unparsedInputStrings);
			
			//your processing goes here
			
			Set<String> inputSet = new LinkedHashSet<String>(unparsedInputStrings);
			String operation = args[0];
			
			Workarounds workarounds = new Workarounds();
			
			//
			if(SystemUtils.IS_OS_WINDOWS) {
				workarounds.startup(System.getProperty("user.dir")+"\\config");
			}else {
				workarounds.startup(System.getProperty("user.dir")+"/config");
			}
			
			//workarounds.startup(System.getProperty("user.dir")+"/config");
			switch (operation) {
			case "fluimpatch":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String ctn = parsedInputString[0];
					boolean ret = workarounds.fluimpatch(ctn);
					if(ret)
						status="successful";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+ctn+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "fluimBBpatch":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String ctn = parsedInputString[0];
					boolean ret = workarounds.fluimBBpatch(ctn);
					if(ret)
						status="successful";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+ctn+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "SIMSwap":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String aid = parsedInputString[0];
					String simToDelete = parsedInputString[1];
					String simToAdd = parsedInputString[2];
					boolean ret = workarounds.simSwap(aid, simToDelete, simToAdd);
					if(ret)
						status="successful";
					String element = "\"["
							+ aid
							+ ","
							+ simToDelete
							+ ","
							+ simToAdd
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "changeMSISDN":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String aid = parsedInputString[0];
					String msisdnToDelete = parsedInputString[1];
					String msisdnToAdd = parsedInputString[2];
					boolean ret = workarounds.changeMSISDN(aid, msisdnToDelete, msisdnToAdd);
					if(ret)
						status="successful";
					String element = "\"["
							+ aid
							+ ","
							+ msisdnToDelete
							+ ","
							+ msisdnToAdd
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "cancelConfiguration":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String aid = parsedInputString[0];
				
					boolean ret = workarounds.cancelConfiguration(aid);
					if(ret)
						status="successful";
					String element = "\"["
							+ aid
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "completeConfiguration":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String aid = parsedInputString[0];
				
					boolean ret = workarounds.completeConfiguration(aid);
					if(ret)
						status="successful";
					String element = "\"["
							+ aid
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "changeAssignmentStatus":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String ctn = parsedInputString[0];
					
					boolean ret = workarounds.changeAssignmentStatus(ctn);
					if(ret)
						status="successful";
					String element = "\"["
							+ ctn
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "renameAID":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String oldAid = parsedInputString[0];
					String newAid = parsedInputString[1];
					
					boolean ret = workarounds.renameAID(oldAid,newAid);
					if(ret)
						status="successful";
					String element = "\"["
							+ oldAid
							+ ","
							+ newAid
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "disconnect":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String oldAid = parsedInputString[0];
					
					
					boolean ret = workarounds.disconnect(oldAid);
					if(ret)
						status="successful";
					String element = "\"["
							+ oldAid
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "mapTechnicalProducts":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String aid = parsedInputString[0];
					String productCode = parsedInputString[1];
					boolean ret = workarounds.mapTechnicalProducts(aid,productCode);
					if(ret)
						status="successful";
					String element = "\"["
							+ aid
							+","
							+ productCode
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
				}
				break;
				
			case "testProducts":
				for(String unparsedInputString : inputSet) {
					
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String aid = parsedInputString[0];
					String productCode = parsedInputString[1];
					workarounds.testProducts(aid, productCode);	
				}
				break;
				
			case "getFixedSIProducts":
				for(String unparsedInputString : inputSet) {
					
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String aid = parsedInputString[0];
					List<FixedSIProduct> siProducts = workarounds.getFixedSIProducts(aid);
					
					for(FixedSIProduct siProduct : siProducts) {
						String message = siProduct.toString();
					}
				
					
				}
				break;
				
			case "getICCIDDetails":
				for(String unparsedInputString : inputSet) {
					
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String iccid = parsedInputString[0];
					String message = workarounds.getICCIDDetails(iccid);
					FileOutputPublishingUnit.publishData(message);
				}
				break;
			case "createFixedServices":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String aid = parsedInputString[0];
					boolean ret = workarounds.createFixedService(aid);
					if(ret)
						status="successful";
					String element = "\"["
							+ aid
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
					
				}
				break;
				
			case "createFixedBroadbandServices":
				for(String unparsedInputString : inputSet) {
					String status = "unsuccessful";
					String[] parsedInputString = unparsedInputString.split("[\t,]+");
					String serviceNumber = parsedInputString[0];
					boolean ret = workarounds.createFixedBroadbandService(serviceNumber);
					if(ret)
						status="successful";
					String element = "\"["
							+ serviceNumber
							+ "]\"";
					FileOutputPublishingUnit.publishData(System.getProperty("user.name")+","+element+","+operation+","+CommonUtils.getDate()+","+status);
					
				}
				break;

			default:
				System.out.println("this operation is not yet supported.");
				break;
			}
			
			workarounds.shutdown();
		}else {
			System.out.println("usage : java -jar <jar-file> <OPERATION> \"<input-file>\"");
		}
	}

}
