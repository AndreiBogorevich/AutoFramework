package testcases;

import java.io.IOException;
import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import utils.Csv;
import base.TestBase;

public class XYZBank extends TestBase {
	
	@BeforeMethod
	public void startTest(){
		log.info("*************************");
	}
	
	@BeforeSuite
	public void startSuite(){
		Assert.assertTrue(setTestConfig("XYZBankTests"));
		Assert.assertTrue(setOR("XYZBankOR"));		
	}
	

	@Test(priority = 1, dataProvider = "getDepositData")
	public void depositTest(Hashtable<String, String> data) throws IOException {

		login(TestConfig.getProperty("deposit.customer"));
		// resetting transaction before each deposit test to have 0 balance
		resetTransactions();

		deposit(data.get("amount"));

		if (data.get("expectedResult").toLowerCase().equals("success")) {
			Assert.assertEquals(read("account.lableBalanceValue_Xpath"), // actual
					data.get("amount")); // expected
		} else {
			Assert.assertEquals(read("account.lableBalanceValue_Xpath"), // actual
					"0"); // expected
		}
	}

	// @Test(priority = 2)
	// public void withdrawalTest() throws IOException {
	//
	// login(customer);
	// withdraw(amount);
	// }

	@DataProvider
	public static Object[][] getDepositData() throws IOException {

		log.info("Reading test data from: " + TestConfig.getProperty("deposit.testData"));
		return Csv.getData(System.getProperty("user.dir")+
				TestConfig.getProperty("deposit.testData"));
	}

	public void login(String customerName) throws IOException {

		navigateToUrl( OR.getProperty("login.url") );

		click("login.btnCustomerLogin_Xpath");
		// it should open another page - which is verified below
		Assert.assertEquals(driver.getCurrentUrl(), OR.getProperty("customer.url"));

		selectByVisibleText("customer.selectYourName_Xpath", customerName);

		click("customer.btnLogin_Xpath");
	}

	public void deposit(String amount) {

		navigateToUrl( OR.getProperty("account.url"));

		click("account.tabDeposit_Xpath");
		type("account.txtAmount_Xpath", amount);
		click("account.btnSubmit_Xpath");
	}

	public void withdraw(int amount) {

		navigateToUrl( OR.getProperty("account.url"));

		click("account.tabWithdrawl_Xpath");
		type("account.txtAmount_Xpath", Integer.toString(amount));
		click("account.btnSubmit_Xpath");
	}

	public void resetTransactions() {

		navigateToUrl( OR.getProperty("listTx.url"));

		if (getElement("listTx.btnReset_Xpath").isDisplayed()) {
			click("listTx.btnReset_Xpath");
		}
		driver.navigate().back();
	}
}
