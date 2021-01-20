package dev.islam;

import java.sql.Connection;
import java.sql.SQLException;


import dev.islam.daos.AccountDaos;
import dev.islam.daos.AuthenticationDaos;
import dev.islam.daos.TransactionDaos;
import dev.islam.models.User;
import dev.islam.services.LoginService;
import dev.islam.util.ConnectionUtil;

public class ActionsDriver {


	public static void main(String[] args) {
//		TransactionDaos t = new TransactionDaos();
//		t.viewTransactions();
//		AccountDaos x = new AccountDaos();
		LoginService login = new LoginService();
		LoginDriver loginInfo = new LoginDriver();
		
		loginInfo.setEmail(login.takeEmail());
		loginInfo.setPassword(login.takePassword());
		
		AuthenticationDaos auth = new AuthenticationDaos();
		User u = auth.Authenticate(loginInfo);
		MenuDriver md = new MenuDriver();
		md.menu(u);
//		x.newAccount(xy, 1000);
//		x.viewAccount(xy.getUserName());
//		System.out.println(loginInfo.getEmail());
//		System.out.println(loginInfo.getPassword());
		
		

	}

}
