package testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.TestBase;

public class XYZBank extends TestBase {

	private String customer = "Harry Potter";
	private int amount = 1250;

	@Test
	public void depositTest() throws IOException{
		
		doLogin(customer);
		
		deposit(amount);
		
	}
	
	
	public void doLogin(String customerName) throws IOException {

		Assert.assertTrue(setOR("XYZBank", "login"));

		click("btnCustomerLogin_Xpath");
		
		Assert.assertTrue(setOR("XYZBank", "customer"));
		Assert.assertEquals(driver.getCurrentUrl(), OR.getProperty("url"));

		selectByVisibleText("selectYourName_Xpath", customerName);
		click("btnLogin_Xpath");
	}
	
	public void deposit(int amount){
		
		Assert.assertTrue(setOR("XYZBank", "account"));
		
		click("tabDeposit_Xpath");
		type("txtAmount_Xpath", Integer.toString(amount));
		click("btnSubmit_Xpath");
		
		Assert.assertEquals(read("labelSuccessfulDeposit_Xpath"), "Deposit Successful");
	}
}
