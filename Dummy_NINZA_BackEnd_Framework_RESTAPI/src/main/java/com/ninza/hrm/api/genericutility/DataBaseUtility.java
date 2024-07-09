package com.ninza.hrm.api.genericutility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.Driver;

public class DataBaseUtility {
	Connection con;
	public void getDbconnection(String url,String username,String password) throws Throwable
	{   
//Rule:When we writing the JDBC program,makesure you are keeping in try-catch block.
		try{                                   
			Driver driver =new Driver();
			DriverManager.registerDriver(driver);
			con = DriverManager.getConnection(url,username,password);
		}
		catch (Exception e) {
		}
	}

	public void getDbconnection() throws Throwable
	{   
		try{                                   
			Driver driver =new Driver();
			DriverManager.registerDriver(driver);
			con = DriverManager.getConnection("jdbc:mysql://49.249.28.218:3333/ninza_hrm","root@%","root");
		}
		catch (Exception e) {
		}
	}

	public void closeDbconnection() throws Throwable
	{
		try {
		con.close();
		}
		catch (Exception e) {
		}
	}

	public ResultSet executeSelectQuery(String query) throws Throwable
	{  ResultSet result=null;
	try {
		Statement stat = con.createStatement();     
		result = stat.executeQuery(query);
	}
	catch (Exception e) {
	}
	return result;
	}
	
	public int executeNonselectQuery(String query)
	{
		int result=0;
		try {
			Statement stat = con.createStatement();     
			result=stat.executeUpdate(query);
		}
		catch (Exception e) {
		}
		return result;
	}
	
}
