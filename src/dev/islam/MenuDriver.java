package dev.islam;

import java.util.ArrayList;
import java.util.Scanner;

import dev.islam.daos.AccountDaos;
import dev.islam.daos.TransactionDaos;
import dev.islam.models.Account;
import dev.islam.models.User;

public class MenuDriver {
	
	public void menu(User u) {
		if (u.getUserType() == null) {
			System.out.println("Login unsuccessful");
		}else {
			if (u.getUserType().equals("CUSTOMER")) {
				customerMenu(u);
			}else if (u.getUserType().equals("EMPLOYEE")) {
				employeeMenu(u);
			}
		}
	}
	
	public void customerMenu(User cust) {
		Scanner scanner = new Scanner(System.in);
		AccountDaos acc = new AccountDaos();
		int num = 1;
		while (num == 1) {
			System.out.println("User Menu: ");
			System.out.println("1. View account");
			System.out.println("2. Withdraw funds");
			System.out.println("3. Deposit funds");
			System.out.println("4. Create account");
			System.out.println("5. Logout");
			System.out.println("Enter an option number: ");
			int n = scanner.nextInt();
			switch (n) {
				case 1:
					if (!(acc.checkAccount(cust.getUserName()))) {
						System.out.println("You don't have an account, go back and create one.");
						customerMenu(cust);
						num = 2;
						break;
					}
					acc.viewAccount(cust.getUserName());
					System.out.println("Return to menu? y/n: ");
					String s = scanner.next();
					if (s.equals("y")) {
						customerMenu(cust);
						num = 2;
						break;
					}else {
						System.out.println("Logged out successfully");
						num = 2;
						break;
					}
				case 2:
					if (!(acc.checkAccount(cust.getUserName()))) {
						System.out.println("You don't have an account, go back and create one.");
						num = 2;
						break;
					}
					System.out.println("How much would you like to withdraw?");
					double d = scanner.nextDouble();
					acc.withdraw(acc.getAccountE(cust.getUserName()), d, cust.getUserId());
					System.out.println("Return to menu? y/n: ");
					s = scanner.next();
					if (s.equals("y")) {
						customerMenu(cust);
						num = 2;
						break;
					}else {
						System.out.println("Logged out successfully");
						num = 2;
						break;
					}
				case 3:
					if (!(acc.checkAccount(cust.getUserName()))) {
						System.out.println("You don't have an account, go back and create one.");
						num = 2;
						break;
					}
					System.out.println("How much would you like to deposit?");
					d = scanner.nextDouble();
					acc.deposit(acc.getAccountE(cust.getUserName()), d, cust.getUserId());
					System.out.println("Return to menu? y/n: ");
					s = scanner.next();
					if (s.equals("y")) {
						customerMenu(cust);
						num = 2;
						break;
					}else {
						System.out.println("Logged out successfully");
						num = 2;
						break;
					}
				case 4:
					System.out.println("How much would you like to start your account with?");
					d = scanner.nextDouble();
					acc.newAccount(cust, d);
					System.out.println("Return to menu? y/n: ");
					s = scanner.next();
					if (s.equals("y")) {
						customerMenu(cust);
						num = 2;
						break;
					}else {
						System.out.println("Logged out successfully");
						num = 2;
						break;
					}
				case 5:
					System.out.println("Logged out successfully");
					num = 2;
					break;
					
			}
			break;
		
		}
	}
	
	public void employeeMenu(User emp) {
		Scanner scanner = new Scanner(System.in);
		AccountDaos acc = new AccountDaos();
		int num = 1;
		while (num == 1) {
			System.out.println("User Menu: ");
			System.out.println("1. View customer account");
			System.out.println("2. Approve or deny pending accounts");
			System.out.println("3. View all transactions");
			System.out.println("4. Logout");
			System.out.println("Enter an option number: ");
			int n = scanner.nextInt();
			switch (n) {
				case 1:
					System.out.println("Enter customer email: ");
					String s = scanner.next();
					if (!(acc.checkAccount(s))) {
						System.out.println("No account exists with that email");
						employeeMenu(emp);
						num = 2;
						break;
					}
					acc.viewAccount(s);
					System.out.println("Return to menu? y/n: ");
					s = scanner.next();
					if (s.equals("y")) {
						employeeMenu(emp);
					}else {
						System.out.println("Logged out successfully");
						num = 2;
						break;
					}
				case 2:
					ArrayList<Account> a = acc.getPendingAccounts();
					if (a.isEmpty()) {
						System.out.println("No pending accounts");
						employeeMenu(emp);
						num = 2;
						break;
					}
					for (Account act:a) {
						acc.viewAccount(act.getEmail());
						System.out.println("Would you like to approve this account? y/n:");
						s = scanner.next();
						if (s.equals("y")) {
							acc.approve(act.getEmail());
							System.out.println("Account approved");
						}else {
							acc.deny(act.getEmail());
							System.out.println("Account denied");
						}
					}
					employeeMenu(emp); 
					num = 2;
					break;
				case 3:
					TransactionDaos t = new TransactionDaos();
					t.viewTransactions();
					System.out.println("Return to menu? y/n: ");
					s = scanner.next();
					if (s.equals("y")) {
						employeeMenu(emp);
					}else {
						System.out.println("Logged out successfully");
						num = 2;
						break;
					}
				case 4:
					System.out.println("Logged out successfully");
					num = 2;
					break;
					
			}
			break;
		
		}
	}

}
