package com.revature.KevBank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DAO implements dbInterface 
{
	private static DAO instance;

	public static DAO getInstance() 
	{
		if (instance == null) 
		{
			instance = new DAO();
		}
		return instance;
	}

	public User getUser(String name) 
	{
		try (Connection con = ConnectionUtil.getConnection()) 
		{
			PreparedStatement ps = con
					.prepareStatement("SELECT name, password, balance, admin, approved " + "FROM user_table WHERE name = ?");
			ps.setString(1, name);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) 
			{
				return new User(rs.getString("name"), rs.getString("password"), rs.getFloat("balance"), rs.getBoolean("admin"),
						 rs.getBoolean("approved"));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.err.println("SQL State: " + e.getSQLState());
			System.err.println("Error code: " + e.getErrorCode());
		}
		return null;
	}

	public boolean insertUser(User u) 
	{
		try (Connection con = ConnectionUtil.getConnection()) 
		{
			PreparedStatement ps = con.prepareStatement("INSERT INTO user_table "
					+ "VALUES (NEXTVAL('user_id_seq'), ?, ?, ?, ?, ?)");
		
			ps.setString(1, u.name);
			ps.setString(2, u.password);
			ps.setFloat(3, u.balance);
			ps.setBoolean(4, u.admin);
			ps.setBoolean(5, u.approved);


			return ps.executeUpdate() > 0;
		} catch (SQLException e) 
		{
			System.err.print(e.getMessage());
			System.err.println("SQL State: " + e.getSQLState());
			System.err.println("Error code: " + e.getErrorCode());
		}
		return false;
	}
	public boolean updateUser(User u) 
	{
		 
		
		try (Connection con = ConnectionUtil.getConnection()) 
		{
			PreparedStatement ps = con.prepareStatement("UPDATE user_table SET "
					+ "balance = ?, approved = ?, admin = ? WHERE name = ?");
			ps.setFloat  (1, u.balance);
			ps.setBoolean(2, u.approved);
			ps.setBoolean(3, u.admin);
			ps.setString (4 , u.name);


			return ps.executeUpdate() > 0;
		} catch (SQLException e) 
		{
			System.err.println(e.getMessage());
			System.err.println("SQL State: " + e.getSQLState());
			System.err.println("Error code: " + e.getErrorCode());
		} 
		return false;
	}
		


	@Override
	public Map<String, User> getAllUsers() 
	{
		Map<String, User> um = new HashMap<>();
		try (Connection con = ConnectionUtil.getConnection()) 
		{
			PreparedStatement ps = con
					.prepareStatement("SELECT name, password, balance, admin, approved "
							+ "FROM user_table");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) 
			{
				User u = new User(rs.getString("name"), 
							rs.getString("password"), 
							rs.getFloat("balance"),
							rs.getBoolean("admin"),
							rs.getBoolean("approved"));
				um.put(u.name, u);
}
		} catch (SQLException e) 
		{
			System.err.println(e.getMessage());
			System.err.println("SQL State: " + e.getSQLState());
			System.err.println("Error code: " + e.getErrorCode());
		}


		return um;
	}
	
}