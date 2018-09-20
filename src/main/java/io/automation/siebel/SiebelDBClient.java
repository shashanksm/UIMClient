package io.automation.siebel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.automation.beans.ActiveProductsRecordBean;
import io.automation.beans.BroadbandDetailsBean;
import io.automation.beans.CancellationDataBean;
import io.automation.beans.OrderLineItemsRecordBean;
import io.automation.dbclient.DBClient;

public class SiebelDBClient extends DBClient	{
	private static final Logger logger = LogManager.getLogger(SiebelDBClient.class);
	
	
	public List<ActiveProductsRecordBean> getActiveProducts(String ctn){
		logger.traceEntry();
		List<ActiveProductsRecordBean> activeProducts = new ArrayList<>();
		String sql = "select a.serial_num,a.integration_id,b.serial_num,a.status_cd MSISDNSTATUS,b.status_cd PRODUCTSTATUS,"
				+ "c.name,c.PART_NUM from siebel.s_asset a, siebel.s_asset b, siebel.s_prod_int c "
				+ "where a.row_id=b.root_asset_id and b.prod_id=c.row_id and a.serial_num in ('"
				+ ctn.trim()
				+ "') "
				+ "and     a.status_cd='Active' "
				+ "and     b.status_cd='Active'";
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			
			while(rs.next()) {
				
				
				String integration_id = rs.getString(2);
				String serial_num = rs.getString(3);
				String ctn_status = rs.getString(4);
				String product_status = rs.getString(5);
				String product_name = rs.getString(6);
				String part_num = rs.getString(7);
				
				ActiveProductsRecordBean bean = new ActiveProductsRecordBean(ctn, integration_id, serial_num, ctn_status, product_status, product_name, part_num);
				activeProducts.add(bean);
				
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
		return activeProducts;
	}
	
	
	public BroadbandDetailsBean getBroadBandDetails(String serviceNumber) {
		BroadbandDetailsBean bean = null;
		
		logger.traceEntry();
		
		String sql = "select a.integration_id AID, ac.attrib_68 AccountCategory, sxc.x_vf_tech_prod_name TechProductName, sxc.x_vf_tech_prod_code TechProductCode "
				+ "from siebel.s_asset a, siebel.s_org_ext ext, siebel.s_org_ext_x ac, siebel.s_asset_x sxc "
				+ "where a.owner_accnt_id = ext.row_id "
				+ "and ext.par_row_id = ac.row_id  and sxc.ROW_ID = a.service_point_id and a.status_cd = 'Active' and a. service_point_id is not null "
				+ "and a. root_asset_id in (select root_asset_id from siebel.s_asset where serial_num in ('"
				+ serviceNumber
				+ "'))";
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			while(rs.next()) {
				
				String aid = "dummy";
				String accountCategory = rs.getString(2);
				String techProductName = rs.getString(3);
				String techProductCode = rs.getString(4);
				
				bean = new BroadbandDetailsBean(aid, accountCategory, techProductName, techProductCode);
			}
			
			sql = "select integration_id from siebel.s_asset  where serial_num in ('"+serviceNumber+"')";
			rs.close();
			rs = statement.executeQuery(sql);
			while(rs.next()) {
				
				String aid = rs.getString(1);
				bean.setAid(aid);
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
	
	
	public void logActiveProducts(String ctn, Logger outLog) {
		logger.traceEntry();
		String sql = "select a.serial_num,a.integration_id,b.serial_num,a.status_cd MSISDNSTATUS,b.status_cd PRODUCTSTATUS,"
				+ "c.name,c.PART_NUM from siebel.s_asset a, siebel.s_asset b, siebel.s_prod_int c "
				+ "where a.row_id=b.root_asset_id and b.prod_id=c.row_id and a.serial_num in ('"
				+ ctn.trim()
				+ "')";
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			
			while(rs.next()) {
				
				StringBuilder sb = new StringBuilder();
				for(int i = 1; i<=columnCount; i++) {
					
					sb.append("\"");
					String s = rs.getString(i);
					sb.append(s);
					sb.append("\",");
					
					
				}
				
				if(outLog != null)
					outLog.trace(sb.toString());
				else
					System.out.println(sb.toString());
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
	}
	
	
	public List<OrderLineItemsRecordBean> getOrderLineItems(String order_number){
		List<OrderLineItemsRecordBean> orderLineItemsRecordBeans = new ArrayList<>();
		logger.traceEntry();
		
		String sql = "select Si.Action_Cd, si.service_num,si.asset_integ_id,p.name,s.order_num,s.status_cd,si.serv_accnt_id,ex.x_vf_customer_code, "
				+ "six.attrib_36,sx.attrib_55,to_char(s.created,'DD-MON-YYYY HH24:MM:SS'),si.bill_accnt_id,si.bill_profile_id "
				+ "from siebel.s_order s,siebel.s_order_item si,siebel.s_order_x sx,siebel.s_order_item_x six,siebel.s_org_ext ex,siebel.s_prod_int p "
				+ "Where "
				+ "S.Order_Num In ('"
				+ order_number
				+ "') "
				+ "and si.order_id=s.row_id "
				+ "and sx.par_row_id=s.row_id "
				+ "and six.par_row_id=si.row_id "
				+ "and ex.row_id=si.serv_accnt_id "
				+ "And P.Row_Id=Si.Prod_Id "
				+ "order by si.service_num";
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int columnCount = metaData.getColumnCount();
			
			
			while(rs.next()) {
				
				String action_cd = rs.getString(1);
				String service_num = rs.getString(2);
				String asset_integ_id = rs.getString(3);
				String name = rs.getString(4);
				String status_cd = rs.getString(5);
				String serv_accnt_id = rs.getString(6);
				String x_vf_customer_code = rs.getString(7);
				String attrib_36 = rs.getString(8);
				String attrib_55 = rs.getString(9);
				String created = rs.getString(10);
				
				OrderLineItemsRecordBean bean = new OrderLineItemsRecordBean(action_cd, service_num, asset_integ_id, name, order_number, status_cd, serv_accnt_id, x_vf_customer_code, attrib_36, attrib_55, created);
				orderLineItemsRecordBeans.add(bean);
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
		return orderLineItemsRecordBeans;
	}
	
	public CancellationDataBean getCancellationData(String order_number){
		
		CancellationDataBean cancellationDataBean = null;
		logger.traceEntry();
		
		String sql = "select o.order_num, itm.service_num as \"CTN\", e.X_VF_CUSTOMER_CODE as \"Account number\", ox.ATTRIB_34 as \"Channel\", o.carrier_prio_cd, ordx.attrib_55 as \"Order TypeCode1\", ox.x_vf_is_return_2 as \"Order TypeCode 2\" ,itm.asset_integ_id, pcn.PRODUCT_CLASS_NAME,itm.action_cd "
				+ "six.attrib_36,sx.attrib_55,to_char(s.created,'DD-MON-YYYY HH24:MM:SS'),si.bill_accnt_id,si.bill_profile_id "
				+ "from "
				+ "siebel.s_order o, siebel.s_order_x ox, siebel.s_order_item itm,  siebel.s_prod_int p,PRODUCT_CLASS_NAME pcn, siebel.s_org_ext e, siebel.s_order_x ordx "
				+ "where itm.order_id = o.row_id "
				+ "and pcn.Prod_id = itm.prod_id "
				+ "and ox.par_row_id= o.row_id "
				+ "and itm.prod_id = p.row_id "
				+ "and o.accnt_id = e.row_id "
				+ "and o.row_id = ordx.par_row_id "
				+ "and pcn.PRODUCT_CLASS_NAME in ('Mobile Service','Mobile Broadband Service') "
				+ "and o.order_num in ('"
				+ order_number
				+ "')";
		
		Statement statement = null;
		ResultSet rs = null;
		try {
			
			statement = connection.createStatement();
			
			rs = statement.executeQuery(sql);
			
			while(rs.next()) {
				
				String orderType = "";
				String orderTypeCode1 = rs.getString(6);
				String orderTypeCode2 = rs.getString(7);
				String previousCtn = rs.getString(1);
				String currentCtn = rs.getString(2);
				String previousSim = rs.getString(1);
				String currentSim = rs.getString(1);
				String previousAID = rs.getString(1);
				String currentAID = rs.getString(1);
				
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
		return cancellationDataBean;
	}
}
