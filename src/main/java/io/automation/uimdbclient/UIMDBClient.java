package io.automation.uimdbclient;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDateTime;

import io.automation.beans.AIDStatusRecordBean;
import io.automation.beans.FixedSIProduct;
import io.automation.beans.ICCIDBean;
import io.automation.beans.LogicalDeviceRecordBean;
import io.automation.beans.MSISDNStatusBean;
import io.automation.dbclient.DBClient;

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
	
	public LogicalDeviceRecordBean getLogicalDevice(String iccid) {
		LogicalDeviceRecordBean logicalDeviceRecordBean = null;
		
		logger.traceEntry();
		
		String sql = "select name, id, adminstate,objectstate "
				+ "From Logicaldevice "
				+ "where "
				+ "name  in ('"
				+ iccid
				+ "') "
				;
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			
			while(rs.next()) {
				
				String name = rs.getString(1);
				String id = rs.getString(2);
				String adminstate = rs.getString(3);
				String objectstate = rs.getString(4);
				
				logicalDeviceRecordBean = new LogicalDeviceRecordBean(name, id, adminstate, objectstate);
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
		
		
		return logicalDeviceRecordBean;
	}
	
	public MSISDNStatusBean getMSISDNStatus(String msisdn) {
		MSISDNStatusBean msisdnStatusBean = null;
		
		logger.traceEntry();
		
		String sql = "select tn.name,tn.adminstate,tc.adminstate,tc.entityid from telephonenumber tn, tnconsumer tc  "
				+ "where "
				+ "tc.telephonenumber = tn.entityid and "
				+ "tn.CURRENTASSIGNMENT = tc.ENTITYID and "
				+ "tn.name in ('"
				+ msisdn
				+ "')";
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			
			while(rs.next()) {
				
				String name = rs.getString(1);
				
				String tnAdminstate = rs.getString(2);
				String tcAdminstate = rs.getString(3);
				
				msisdnStatusBean = new MSISDNStatusBean(name, tnAdminstate, tcAdminstate);
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
		
		
		if(msisdnStatusBean == null) {
			logger.warn("MSISDN "+msisdn+" returned null query");
		}
		
		logger.traceExit();
		
		return msisdnStatusBean;
	}
	
	public String getServiceEntityId(String id) {
		String ret = null;
		
		logger.traceEntry();
				
				String sql = "select entityid from service where id in('"
						+ id
						+ "')";
				
				Statement statement = null;
				ResultSet rs = null;
				try {
					
					statement = connection.createStatement();
					
					rs = statement.executeQuery(sql);
					
					ResultSetMetaData metaData = rs.getMetaData();
					
					int columnCount = metaData.getColumnCount();
					
					
					while(rs.next()) {
						
						ret = rs.getString(1);
						
						
						
						
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
				
				return ret;
	}
	
	public String getServiceConfigurationVersionEntityId(String entityId) {
		String ret = null;
		
		logger.traceEntry();
				
				String sql = "select entityid from serviceconfigurationversion where service in ('"
						+ entityId
						+ "') order by SERVICE, VERSIONNUMBER desc";
				
				Statement statement = null;
				ResultSet rs = null;
				try {
					
					statement = connection.createStatement();
					
					rs = statement.executeQuery(sql);
					
					ResultSetMetaData metaData = rs.getMetaData();
					
					int columnCount = metaData.getColumnCount();
					
					
					while(rs.next()) {
						
						ret = rs.getString(1);
		
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
				
				return ret;
	}
	
	public List<String> getServiceConfigurationItems(String entityId) {
		List<String> ret = new ArrayList<>();
		
		logger.traceEntry();
				
				String sql = "select entityid from serviceconfigurationversion where service in ('"
						+ entityId
						+ "') order by SERVICE, VERSIONNUMBER desc";
				
				Statement statement = null;
				ResultSet rs = null;
				try {
					
					statement = connection.createStatement();
					
					rs = statement.executeQuery(sql);
					
					ResultSetMetaData metaData = rs.getMetaData();
					
					int columnCount = metaData.getColumnCount();
					
					
					while(rs.next()) {
						
						ret.add(rs.getString(1));
		
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
				
				return ret;
	}
	
	public FixedSIProduct getServiceConfigItemChar(String charowner) {
		FixedSIProduct ret = null;
		
		logger.traceEntry();
				
				String sql = "select name, value from serviceconfigitem_char where charowner in ('"
						+ charowner
						+ "')";
				
				Statement statement = null;
				ResultSet rs = null;
				try {
					
					statement = connection.createStatement();
					
					rs = statement.executeQuery(sql);
					
					ResultSetMetaData metaData = rs.getMetaData();
					
					int columnCount = metaData.getColumnCount();
					
					
					while(rs.next()) {
						
						ret = new FixedSIProduct(rs.getString(1),rs.getString(1),rs.getString(2));
		
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
				
				return ret;
	}
	
	public ICCIDBean getICCIDDetails(String iccid) {
		ICCIDBean bean = null;
		
		
		logger.traceEntry();
		
		String sql = "select ld.name, ldc.adminstate, tn.name, tc.adminstate, ldc.LASTMODIFIEDDATE, ldc.LASTMODIFIEDUSER "
				+ "from telephonenumber tn, tnconsumer tc, logicaldevice ld, logicaldeviceconsumer ldc, logicaldeviceaccount lda, tnassignment ta, logicaldeviceassignment la "
				+ "where "
				+ "tn.entityid = tc.telephonenumber and "
				+ "tc.entityid = ta.entityid and "
				+ "ta.serviceconsumer = la.serviceconsumer and "
				+ "la.entityid =ldc.entityid and "
				+ "ldc.logicaldevice = ld.entityid and "
				+ "lda.logicaldevice = ld.entityid and "
				+ "ld.name in ('"
				+ iccid
				+ "') "
				+ "order by ldc.entityid desc"
				;
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			
			while(rs.next()) {
				
				String name = rs.getString(1);
				String adminstate = rs.getString(2);
				String msisdn = rs.getString(3);
				String msisdnstatus = rs.getString(4);
				LocalDateTime lastModifiedDate = LocalDateTime.parse(rs.getDate(5).toString());
				String lastModifiedUser = rs.getString(6);
				
				bean = new ICCIDBean(name, adminstate, msisdn, msisdnstatus, lastModifiedDate, lastModifiedUser);
				
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
		
		return bean;
	}
}
