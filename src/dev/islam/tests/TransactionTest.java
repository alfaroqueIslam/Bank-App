package dev.islam.tests;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import dev.islam.daos.AccountDaos;
import dev.islam.models.Account;

public class TransactionTest {
	
	AccountDaos acc = new AccountDaos();
	
	
	@Test
	public void testWithdraw() {
//		Account a = acc.getAccountE("test@gmail.com");
		acc.withdraw(acc.getAccountE("test@gmail.com"), 500, 7);
		double expected = 500;
		double actual = acc.getAccountE("test@gmail.com").getBalance();
		assertEquals(expected, actual, 500);
	}
	
	@Test
	public void testDeposit() {
//		Account a = acc.getAccountE("test@gmail.com");
		acc.deposit(acc.getAccountE("test@gmail.com"), 500, 7);
		double expected = 1000;
		double actual = acc.getAccountE("test@gmail.com").getBalance();
		assertEquals(expected, actual, 1000);
	}

}
