package dev.islam.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import dev.islam.models.Account;
import dev.islam.models.User;
import dev.islam.util.ConnectionUtil;

public class AccountDaos {
	
	private static Logger log = Logger.getRootLogger();
	
	public void newAccount(User cust, double balance) {
		log.info("Attempting to create new account");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String tDate = dtf.format(now);
		String insertQuery = "INSERT INTO ACCOUNT (STATUS,BALANCE,DATE_CREATED,EMAIL) "
				+ "VALUES ('pending'," + balance + "," + "'" + tDate + "','" + cust.getUserName() + "')";
		if (!(cust.getUserName().equals(getAccountE(cust.getUserName()).getEmail()))) {
			Connection connection;
			try {
				connection = ConnectionUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(insertQuery);
				statement.executeQuery();
				Account acc = getAccount(tDate);
				TransactionDaos tr = new TransactionDaos();
				tr.newTransaction(cust.getUserId(), acc.getAccountId(), "deposit", balance, tDate);
				log.info("Account created, deposit added to transaction table");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("There is already an account with that email address.");
			log.info("Account creation failed");
		}
		
		
	}
	
	public Account getAccount(String date) {
		String authQuery = "SELECT * FROM ACCOUNT WHERE DATE_CREATED = " + "'" + date + "'";
		Account def = new Account();
		try {
			Connection connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(authQuery);
			ResultSet rs = statement.executeQuery();
			rs.next();
			double balance = rs.getDouble("BALANCE");
			String status = rs.getString("STATUS");
			String s2 = rs.getString("DATE_CREATED");
			int accountId = rs.getInt("ACCOUNT_ID");
			String email = rs.getString("EMAIL");
			Account acc = new Account();
			acc.setBalance(balance);
			acc.setStatus(status);
			acc.setAccountId(accountId);
			acc.setDate(s2);
			acc.setEmail(email);
			return acc;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return def;
	}

	public Account getAccountE(String email) {
		String authQuery = "SELECT * FROM ACCOUNT WHERE EMAIL = " + "'" + email + "'";
		Account def = new Account();
		try {
			Connection connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(authQuery);
			ResultSet rs = statement.executeQuery();
			rs.next();
			double balance = rs.getDouble("BALANCE");
			String status = rs.getString("STATUS");
			String s2 = rs.getString("DATE_CREATED");
			int accountId = rs.getInt("ACCOUNT_ID");
			String em = rs.getString("EMAIL");
			Account acc = new Account();
			acc.setBalance(balance);
			acc.setStatus(status);
			acc.setAccountId(accountId);
			acc.setDate(s2);
			acc.setEmail(em);
			return acc;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return def;
	}
	
	public void viewAccount(String email) {
		String authQuery = "SELECT * FROM ACCOUNT WHERE EMAIL = " + "'" + email + "'";
		try {
			Connection connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(authQuery);
			ResultSet rs = statement.executeQuery();
			rs.next();
			System.out.println("Balance: $" + rs.getDouble("BALANCE"));
			System.out.println("Status: " + rs.getString("STATUS"));
			System.out.println("Date created: " + rs.getString("DATE_CREATED"));
			System.out.println("Account ID: " + rs.getInt("ACCOUNT_ID"));
			System.out.println("Email: " + rs.getString("EMAIL"));
//			double balance = rs.getDouble("BALANCE");
//			String status = rs.getString("STATUS");
//			String s2 = rs.getString("DATE_CREATED");
//			int accountId = rs.getInt("ACCOUNT_ID");
//			Account acc = new Account();
//			acc.setBalance(balance);
//			acc.setStatus(status);
//			acc.setAccountId(accountId);
//			acc.setDate(s2);
//			return acc;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean checkAccount(String email) {
		String authQuery = "SELECT * FROM ACCOUNT WHERE EMAIL = " + "'" + email + "'";
		try {
			Connection connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(authQuery);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				return true;
			}else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void deposit(Account acc, double deposit, int id) {
		log.info("Attempting to deposit $" + deposit + " to account");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String tDate = dtf.format(now);
		String updateQuery = "UPDATE ACCOUNT a SET a.BALANCE = " + (acc.getBalance() + deposit)
				+ "WHERE a.ACCOUNT_ID = " + acc.getAccountId();
		if (acc.getStatus().equals("approved")) {
			Connection connection;
			try {
				connection = ConnectionUtil.getConnection();
				PreparedStatement statement = connection.prepareStatement(updateQuery);
				statement.executeQuery();
				TransactionDaos tr = new TransactionDaos();
				tr.newTransaction(id, acc.getAccountId(), "deposit", deposit, tDate);
				log.info("$" + deposit + " deposit successful, added to transaction table");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("Your account has not been approved, try again later");
			log.info("Deposit failed");
		}
	}
	
	public void withdraw(Account acc, double withdraw, int id) {
		log.info("Attempting to withdraw $" + withdraw + " to account");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String tDate = dtf.format(now);
		String updateQuery = "UPDATE ACCOUNT a SET a.BALANCE = " + (acc.getBalance() - withdraw)
				+ "WHERE a.ACCOUNT_ID = " + acc.getAccountId();
		if (acc.getStatus().equals("approved")) {
			if (acc.getBalance() > withdraw) {
				Connection connection;
				try {
					connection = ConnectionUtil.getConnection();
					PreparedStatement statement = connection.prepareStatement(updateQuery);
					statement.executeQuery();
					TransactionDaos tr = new TransactionDaos();
					tr.newTransaction(id, acc.getAccountId(), "withdrawal", withdraw, tDate);
					log.info("$" + withdraw + " withdrawal successful, added to transaction table");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				System.out.println("Insufficient funds!");
				log.info("Withdrawal failed");
			}
		}else {
			System.out.println("Your account has not been approved, try again later");
			log.info("Withdrawal failed");
		}
	}
	
	public void approve(String email) {
		String updateQuery = "UPDATE ACCOUNT a SET a.STATUS = 'approved' "
				+ "WHERE a.EMAIL = '" + email + "'";
		Connection connection;
		try {
			connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(updateQuery);
			statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		
	public void deny(String email) {
		String updateQuery = "UPDATE ACCOUNT a SET a.STATUS = 'denied' "
				+ "WHERE a.EMAIL = '" + email + "'";
		Connection connection;
		try {
			connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(updateQuery);
			statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		
	public ArrayList<Account> getPendingAccounts(){
		ArrayList<Account> pendList = new ArrayList<>();
		String selectQuery = "SELECT * FROM ACCOUNT WHERE ACCOUNT.STATUS = 'pending'";
		try {
			Connection connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(selectQuery);
			ResultSet rs = statement.executeQuery();
			Account temp = new Account();
			while (rs.next()) {
				temp.setBalance(rs.getDouble("BALANCE"));
				temp.setStatus(rs.getString("STATUS"));
				temp.setAccountId(rs.getInt("ACCOUNT_ID"));
				temp.setDate(rs.getString("DATE_CREATED"));
				temp.setEmail(rs.getString("EMAIL"));
				pendList.add(temp);
			}
			return pendList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pendList;
	}
	
	public ArrayList<Account> getAllAccounts(){
		ArrayList<Account> pendList = new ArrayList<>();
		String selectQuery = "SELECT * FROM ACCOUNT";
		try {
			Connection connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(selectQuery);
			ResultSet rs = statement.executeQuery();
			Account temp = new Account();
			while (rs.next()) {
				temp.setBalance(rs.getDouble("BALANCE"));
				temp.setStatus(rs.getString("STATUS"));
				temp.setAccountId(rs.getInt("ACCOUNT_ID"));
				temp.setDate(rs.getString("DATE_CREATED"));
				temp.setEmail(rs.getString("EMAIL"));
				pendList.add(temp);
			}
			return pendList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pendList;
	}

}
