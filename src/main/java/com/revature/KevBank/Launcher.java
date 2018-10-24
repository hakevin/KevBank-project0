package com.revature.KevBank;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;


public class Launcher 
{
	private static Scanner scan = new Scanner(System.in);
	private static User u = null;
	private static User v = null;
	final static DAO dao = DAO.getInstance();

	public static void main(String[] args) throws Exception 
	{

			while (true) {
			System.out.println("Welcome to KevBank!");
			System.out.println("====================");
			System.out.println("Select:");
			System.out.println("Enter 1 to log into an account");
			System.out.println("Enter 2 to create a customer account");
			System.out.println("Enter 3 to create an admin account");
			System.out.println("Enter 0 to exit system");
			System.out.println("Enter selection: ");
			int option = scan.nextInt();
			scan.nextLine();

			switch (option) {
			case 1:
				login();
				break;
			case 2:
				createCustomer();
				break;
			case 3:
				createAdmin();
				break;
			case 0:
				break;
			}

			if (u != null) 
			{
				loggedIn();
			}
		}

	}

	
	public static void login() throws IOException 
	{
		System.out.println("Log in...");

		boolean authenticated = false;
		while (!authenticated) {
			System.out.println("Enter your user name: ");
			String name = scan.nextLine();
			u = dao.getUser(name);
			if (u == null) {
				System.out.println("Invalid user name: " + name);
				continue;
			}

			System.out.println("Enter your password: ");
			String password = scan.nextLine();

			if (!u.password.equals(password)) {
				System.out.println("Invalid password for : " + name);

			} else
				authenticated = true;
		}
	
		System.out.println("Welcome " + u.name + " to KevBank!");
		System.out.println(u.name + " your current balance is : $" + u.balance);


		loggedIn();
	}
	
	public static void createCustomer() 
	{
		System.out.println("Create a customer account");
		createUser(false);
		System.out.println("Customer account created, username: " + u.name);
	}

	public static void createAdmin() 
	{
		System.out.println("Create an admin account");
		createUser(true);
		System.out.println("Admin account created, username: " + u.name);
	}
	
	public static void createUser(boolean admin) 
	{
		String name = null;
		while (true) {
			System.out.println("Enter a new user name: ");
			name = scan.nextLine();
			if (dao.getUser(name) == null)
				break;
			System.out.println("User name already exists!");
		}
		
		System.out.println("Enter a password: ");
		String password = scan.nextLine();

		u = new User(name, password, 0, admin, false); 
		boolean inserted = dao.insertUser(u);
		if (inserted) {

		} else {
			System.out.println("User could not be created. Please Retry.");
		}
	}
	
	public static void loggedIn() 
	{
		if (!u.approved) 
		{
			System.out.println(u.name + " is not approved for deposits and withdrawals by an admin. Logging out...\n");
			u = null;
			return;
		}

		while (true) 
		{
			System.out.println("Options:");
			System.out.println("Enter 1 to make deposit");
			System.out.println("Enter 2 to make withdraw");
			System.out.println("Enter 3 to make transfer");
			System.out.println("Enter 4 for joint options");
			if (u.admin) 
			{
				System.out.println("Enter 5 to approve users");
				System.out.println("Enter 6 to view all accounts");
				
			}
			System.out.println("Enter 0 to log out of: " + u.name);
			System.out.println("Enter option: ");

			int option = scan.nextInt();
			scan.nextLine();
			if (!u.admin && option > 4) 
			{
				System.out.println("Invalid option for a customer. Retry...");
				continue;
			}
			switch (option) {
			case 0:
				System.out.println(u.name + " logging out...");
				u = null;
				return;
			case 1:
				deposit();
				break;
			case 2:
				withdraw();
				break;
			case 3:
				transfer();
				break;
			case 4:
				joint();
				break;
			case 5:
				approveUsers();
			case 6:
				viewAllAccounts();

		
			}
		}
	}
					

	public static void deposit() 
	{
		System.out.println(u.name + " your current balance is : $" + u.balance);
		System.out.println("Enter the amount to deposit: ");
		System.out.print("$");
		float deposit = scan.nextFloat();

		u.deposit(deposit);
		dao.updateUser(u);

		System.out.println(u.name + " your new balance is : $" + u.balance);
	}
	
	public static void withdraw() 
	{
		System.out.println(u.name + " your current balance is : $" + u.balance);
		System.out.println("Enter the amount to withdraw: ");
		System.out.print("$");
		float withdrawal = scan.nextFloat();

		u.withdraw(withdrawal);
		dao.updateUser(u);

		System.out.println(u.name + " your new balance is : $" + u.balance);
	}

	private static void transfer() 
	{
		System.out.println(u.name + " your current balance is : $" + u.balance);
		System.out.println("Enter the amount to transfer: ");
		System.out.print("$");
		float withdrawal = scan.nextFloat();

		u.withdraw(withdrawal);
		dao.updateUser(u);
		System.out.println("Which user would you like to transfer money to:");
		String name = scan.next();
		v = dao.getUser(name);
		v.deposit(withdrawal);
		dao.updateUser(v);
		
		System.out.println(u.name + ", your after transfer balance is : $" + u.balance);
		System.out.println(v.name + " has a new balance of : $" + v.balance );
		
	}
	
	private static void approveUsers() 
	{
		System.out.println("Approve users");

		Map<String, User> map = dao.getAllUsers();
		if (map.isEmpty()) {
			System.out.println("No users in the system.");
			return;
		}
		System.out.println("Users in the system:");
		for (String name : map.keySet()) 
		{
			User acct = map.get(name);
			System.out.println(name + " " + acct.approved);
		}

		System.out.println("Enter the name of the user to approve: ");
		String name = scan.nextLine();

		User enteredUser = map.get(name); 
		enteredUser.approved = true;
		dao.updateUser(enteredUser);
		System.out.println(enteredUser.name + " approved.");

	}
	
	private static void viewAllAccounts() 
	{
		Map<String, User> map = dao.getAllUsers();
		if (map.isEmpty()) {
			System.out.println("No users in the system.");
			return;
		}
		System.out.println("Users in the system:");
		for (String name : map.keySet()) 
		{
			User acct = map.get(name);
			System.out.println
			(name + " pw: " + acct.password + " balance: " + acct.balance + 
					" admin: " + acct.admin + " approved: " + acct.approved);
		}
		
	}

	//-----------------------------------------------------------------------------------------------
	
	// needs to be implemented in DAO
	
	
	private static void joint() 
		
	{
		System.out.println(u.name + " your current balance is : $" + u.balance);
		System.out.println("Do you want to create a joint (y/n)?");
		String joint = scan.next();
		if (joint.equals("y"))
		{
		System.out.println("Enter the username to joint with: ");
		String name = scan.next();
		v = dao.getUser(name);	
		float jointBalance = (u.balance + v.balance);
		
		if (u.approved) 
			{
			System.out.println(u.name + " is viewing a joint account info with "
			
				 + v.name + " containing $" + jointBalance + ".");
			}
		}
		
		else return;
	}
	


	 @Override
	protected void finalize() throws Throwable {
		super.finalize();

	}
}
