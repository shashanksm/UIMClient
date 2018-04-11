package io.automation.uimdbclient;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.automation.beans.AIDStatusRecordBean;
import io.automation.beans.ActiveProductsRecordBean;
import io.automation.dbclient.DBClient;
import io.automation.siebel.SiebelDBClient;

public class UIMDBClient extends DBClient {
	
	private static final Logger logger = LogManager.getLogger(UIMDBClient.class);
	
	public boolean renameAID(String oldAid, String newAid){
		
		boolean ret = false;
		Statement statement = null;
		ResultSet rs = null;
		String charowner = null;
		
	
		
		String sql = "select sc.value,  sc.charowner "
				+ "from service s, service_char sc "
				+ "where sc.CHAROWNER = s.ENTITYID and "
				+ "sc.name = 'Asset_Integration_ID' "
				+ "and sc.value in ('"
				+ oldAid
				+ "')";
		
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			
			
			if(rs.next()) {
				charowner = rs.getString(2);
				
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(charowner != null) {
			
			
			try {
				
				statement = connection.createStatement();
				sql = "update service_char set value ='"
						+ newAid
						+ "' where CHAROWNER ='"
						+ charowner
						+ "' and name = 'Asset_Integration_ID'";
				
				
				
				statement.executeUpdate(sql);
				connection.commit();
				ret = true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(statement != null) {
					try {
						statement.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}
		
		return ret;
		
	}
	
	public AIDStatusRecordBean getAIDStatus(String aid) {
		AIDStatusRecordBean aidStatusRecordBean = null;
		logger.traceEntry();
		
		String sql = "select sc.value, s.adminstate, s.id, s.lastmodifieduser,sc.lastmodifieddate "
				+ "from service s, service_char sc "
				+ "where sc.charowner = s.entityid and "
				+ "sc.name = 'Asset_Integration_ID' "
				+ "and sc.value in ('"
				+ aid
				+ "')";
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			
			while(rs.next()) {
				
				String value = rs.getString(1);
				String adminstate = rs.getString(2);
				String id = rs.getString(3);
				String lastmodifieduser = rs.getString(4);
				
				aidStatusRecordBean = new AIDStatusRecordBean(value, adminstate, id, lastmodifieduser);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		}
		
		logger.traceExit();
		
		
		logger.traceExit();
		return aidStatusRecordBean;
	}
	
}
