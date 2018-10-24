package com.revature.KevBank;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionUtil 
{
	
	public static Connection getConnection() 
	{
		
		try {
			String url = "jdbc:postgresql://createdb.cjpst2uz3vak.us-east-2.rds.amazonaws.com:5432/postgres";
			String username = "****";
			String password = "****";
			return DriverManager.getConnection(url, username, password);
			}catch (SQLException e) 
		{
			e.printStackTrace();
		}

		return null;
	}
}
