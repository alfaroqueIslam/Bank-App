package dev.islam.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.islam.LoginDriver;
import dev.islam.models.Customer;
import dev.islam.models.Employee;
import dev.islam.models.Manager;
import dev.islam.models.User;
import dev.islam.util.ConnectionUtil;

public class AuthenticationDaos {

	public User Authenticate(LoginDriver login) {
		
		String authQuery = "SELECT * FROM APP_USER WHERE EMAIL = " + "'" + login.getEmail() + "'";
//		System.out.println(authQuery);
		User def = new Customer();
		try {
			Connection connection = ConnectionUtil.getConnection();
			PreparedStatement statement = connection.prepareStatement(authQuery);
			ResultSet rs = statement.executeQuery();
			rs.next();
			System.out.println(rs.getString("EMAIL"));
			System.out.println(rs.getString("PASSWORD"));
			String s1 = rs.getString("EMAIL");
			String s2 = rs.getString("PASSWORD");
			String s3 = rs.getString("USER_TYPE");
			int n = rs.getInt("USER_ID");
			if (login.getEmail().equals(s1) & login.getPassword().equals(s2)) {
				System.out.println("Login Successful!");
				switch(s3) {
				case "CUSTOMER":
					User cust = new Customer();
					cust.setUserName(s1);
					cust.setPassword(s2);
					cust.setUserType(s3);
					cust.setUserId(n);
					return cust;
				case "EMPLOYEE":
					User emp = new Employee();
					emp.setUserName(s1);
					emp.setPassword(s2);
					emp.setUserType(s3);
					emp.setUserId(n);
					return emp;
				case "MANAGER":
					User manag = new Manager();
					manag.setUserName(s1);
					manag.setPassword(s2);
					manag.setUserType(s3);
					manag.setUserId(n);
					return manag;
				}
			} else {
				System.out.println("Login Invalid");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return def;
	}

}
