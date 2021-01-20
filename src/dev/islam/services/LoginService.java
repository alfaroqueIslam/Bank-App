package dev.islam.services;

import java.util.Scanner;

public class LoginService {
	
	private Scanner scanner = new Scanner(System.in);
	
	public LoginService() {
		// TODO Auto-generated constructor stub
	}
	
	public String takeEmail() {
		System.out.println("Email: ");
		String email = scanner.next();
		return email;
	}

	public String takePassword() {
		System.out.println("Password: ");
		String password = scanner.next();
		return password;
	}
}
