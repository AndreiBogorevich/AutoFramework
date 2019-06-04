package testcases;

import java.io.IOException;
import org.openqa.selenium.WebDriver;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.TestBase;

public class XYZBank extends TestBase {

	private String customer = "Harry Potter";
	private int amount = 1250;

	@Test (priority=1)
	public void depositTest() throws IOException {

		login(customer);
		deposit(amount);
	}

	@Test (priority=2)
	public void withdrawalTest() throws IOException {

		login(customer);
		withdraw(amount);
	}

	public void login(String customerName) throws IOException {

		Assert.assertTrue(setOR("XYZBank", "login"));
		driver.get(OR.getProperty("url"));

		click("btnCustomerLogin_Xpath");		

		Assert.assertTrue(setOR("XYZBank", "customer"));
		Assert.assertEquals(driver.getCurrentUrl(), OR.getProperty("url"));

		selectByVisibleText("selectYourName_Xpath", customerName);
		
		click("btnLogin_Xpath");
	}

	public void deposit(int amount) {

		Assert.assertTrue(setOR("XYZBank", "account"));

		click("tabDeposit_Xpath");
		type("txtAmount_Xpath", Integer.toString(amount));
		click("btnSubmit_Xpath");

		Assert.assertEquals(read("labelResult_Xpath").toLowerCase(),
				"deposit successful");
	}

	public void withdraw(int amount) {

		Assert.assertTrue(setOR("XYZBank", "account"));

		click("tabWithdrawl_Xpath");
		type("txtAmount_Xpath", Integer.toString(amount));
		click("btnSubmit_Xpath");

		Assert.assertEquals(read("labelResult_Xpath").toLowerCase(),
				"transaction successful");
	}
}
