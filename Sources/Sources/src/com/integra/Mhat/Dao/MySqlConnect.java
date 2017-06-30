package com.integra.Mhat.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnect {

	private Connection con;
	private Statement statement;

	public MySqlConnect(){
		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","admin123");    
			con.setAutoCommit(false);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void commit(){
		try{
			if(con != null)
				con.commit();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void rollback(){
		try{
			if(con != null)
				con.rollback();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public Statement getSqlStatement(){
		try {
			if(con != null)
				return con.createStatement();
			
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void stopConnection(){
		try {
			if(con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
