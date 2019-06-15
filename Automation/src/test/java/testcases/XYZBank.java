package testcases;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.io.IOException;
import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import utils.DataProviders;
import base.TestBase;

public class XYZBank extends TestBase {

	@BeforeMethod
	public void startTest() {
		log.info("*************************");
	}

	@BeforeSuite
	public void startSuite() {
		AssertJUnit.assertTrue(setTestConfig("XYZBankTests"));
		AssertJUnit.assertTrue(setOR("XYZBankOR"));
	}

	// ***************** TESTS *********************

	
	// depositTest
	// 1) logs into the XYz Bank site
	// 2) Selects a customer (test properties file)
	// 3) Cleans the Balance before each deposit
	// 4) Deposits an amount as per test data file
	// 5) Evaluates if the deposit was successful, i.e. balance = deposit amount
	// 6) if it failed and balance remained at 0.
	// Expected result is taken from the test data file
	
	@Test(priority = 1, dataProviderClass = DataProviders.class, dataProvider = "dp_Csv", enabled = true)
	public void depositTest(Hashtable<String, String> data) throws IOException {

		// reading test customer name from test config and logging in
		login(TestConfig.getProperty("deposit.customer"));

		// resetting transaction before each deposit test to have 0 balance
		resetTransactions();

		deposit(data.get("deposit"));

		// expected balance should be equal to deposit
		// for negative tests: expected balance is 0
		checkResults(read("account.lableBalanceValue_Xpath"), // actual
				data.get("expectedResult"), // expected
				"account.lableBalanceValue_Xpath"); // highlighting balance

	}

	
	// withdrawalTest
	// 1) logs into the XYz Bank site
	// 2) Selects a customer (test properties file)
	// 3) Cleans the Balance before each deposit
	// 4) Deposits an amount as per test data file
	// 5) Withdraws an amount as per test data file
	// 5) Evaluates if the deposit was successful, i.e. balance = deposit - withdrawal
	// 6) if it failed and balance remained as deposit.
	// Expected result is taken from the test data file	
	
	@Test(priority = 2, dataProviderClass = DataProviders.class, dataProvider = "dp_Csv", enabled = true)
	public void withdrawalTest(Hashtable<String, String> data)
			throws IOException {

		// reading test customer name from test config and logging in
		login(TestConfig.getProperty("withdrawal.customer"));

		// resetting transaction before each deposit test to have 0 balance
		resetTransactions();

		deposit(data.get("deposit"));
		withdraw(data.get("withdrawal"));

		// if expected balance should be equal to deposit - withdrawal
		// for negative tests: expected balance should be equal to deposit
		checkResults(read("account.lableBalanceValue_Xpath"), // actual
				data.get("expectedResult"), // expected
				"account.lableBalanceValue_Xpath"); // highlighting balance

	}
	

	// ***************** KEYWORDS *********************

	public void login(String customerName) throws IOException {

		navigateToUrl(OR.getProperty("login.url"));

		click("login.btnCustomerLogin_Xpath");
		// it should open another page - which is verified below
		checkResults(driver.getCurrentUrl(), // actual
				OR.getProperty("customer.url")); // expected
		
		selectByVisibleText("customer.selectYourName_Xpath", customerName);
		click("customer.btnLogin_Xpath");

		report("Logging in as " + customerName);
	}

	public void deposit(String amount) {

		navigateToUrl(OR.getProperty("account.url"));

		click("account.tabDeposit_Xpath");
		type("account.txtAmount_Xpath", amount);
		click("account.btnSubmit_Xpath");

		report("Depositing " + amount);
	}

	public void withdraw(String amount) {

		navigateToUrl(OR.getProperty("account.url"));

		click("account.tabWithdrawl_Xpath");
		type("account.txtAmount_Xpath", amount);
		click("account.btnSubmit_Xpath");

		report("Withdrawing " + amount);
	}

	public void resetTransactions() {

		navigateToUrl(OR.getProperty("listTx.url"));

		if (getElement("listTx.btnReset_Xpath").isDisplayed()) {
			click("listTx.btnReset_Xpath");
			report("Previous transactions are deleted. Balance is set to 0.");
		} else {
			report("Trying to delete transactions - no transactions found.");
		}
		driver.navigate().back();
	}
}
