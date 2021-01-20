package dev.islam.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import dev.islam.util.ConnectionUtil;

public class TransactionDaos {
	
	public void newTransaction(int user_id, int account_id, String type, double amount, String tDate) {
		String insertQuery = "INSERT INTO ACCOUNT_TRANSACTION (USER_ID,ACCOUNT_ID,TRANSACTION_TYPE,TRANSACTION_AMOUNT,TRANSACTION_DATE) "
				+ "VALUES ("+ user_id + "," + account_id + "," + "'" + type + "'" + "," + amount + ",'" + tDate + "')";
		Connection connection;
		try {
			connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(insertQuery);
			statement.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void viewTransactions() {
		try {
			Connection connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM ACCOUNT_TRANSACTION");
			ResultSet rs = statement.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			
			System.out.println(rsmd.getColumnLabel(1) + "    |" + rsmd.getColumnLabel(2) + "            |" 
					+ rsmd.getColumnLabel(3) + "         |" + rsmd.getColumnLabel(4) + "         |" + rsmd.getColumnLabel(5) + "    |" + 
					rsmd.getColumnLabel(6));
			while (rs.next()) {
			    for(int i = 1; i <= columnsNumber; i++)
			        System.out.print(rs.getString(i) + "                 | ");
			    System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
