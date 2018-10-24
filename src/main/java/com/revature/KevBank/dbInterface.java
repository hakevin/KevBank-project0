package com.revature.KevBank;

import java.util.Map;

public interface dbInterface 
{	
	public boolean insertUser(User user);

	public User getUser(String name);

	public boolean updateUser(User user);

	 public abstract Map<String, User> getAllUsers();
}
