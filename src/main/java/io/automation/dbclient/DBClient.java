package io.automation.dbclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBClient {
	
	private String connectionString;
	private String driver;
	private String user;
	private String password;
	
	protected Connection connection;
	private static final Logger logger = LogManager.getLogger(DBClient.class);
	
	public String getConnectionString() {
		return connectionString;
	}
	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public DBClient(String connectionString, String driver, String user, String password) {
		super();
		this.connectionString = connectionString;
		this.driver = driver;
		this.user = user;
		this.password = password;
	}
	public DBClient() {
		super();
	}
	
	public void startup(Properties properties) {
		
		logger.traceEntry();
		
		this.driver = "driver";
		this.connectionString = "connectionString";
		this.password="password";
		this.user="user";
		
		if(properties.containsKey(connectionString)) {
			connectionString = properties.getProperty(connectionString);
		}
		
		if(properties.containsKey(driver)) {
			driver = properties.getProperty(driver);
		}
		
		if(properties.containsKey(user)) {
			user = properties.getProperty(user);
		}
		
		if(properties.containsKey(password)) {
			password = properties.getProperty(password);
		}
		
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(connectionString,user,password);
			connection.setAutoCommit(false);
			
			
		} catch (ClassNotFoundException e) {
			logger.error("invalid driver provided : "+driver+" Exception : "+e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			logger.error("invalid credentials : user="+user);
		}
		
		logger.traceExit();
		
	}
	
	public void shutdown() {
		
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("unable to close connection");
			}
		}
		
	}
	
}
