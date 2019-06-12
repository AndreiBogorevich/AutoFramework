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
	public void startTest() {
		log.info("*************************");
	}

	@BeforeSuite
	public void startSuite() {
		Assert.assertTrue(setTestConfig("XYZBankTests"));
		Assert.assertTrue(setOR("XYZBankOR"));
	}

	// ***************** TESTS *********************

	/*
	 * This test 1) logs into the XYz Bank site 2) Selects a customer (test
	 * properties file) 3) Cleans the Balance before each deposit 4) Deposits an
	 * amount as per test data file 5) Evaluates if the deposit was successful,
	 * i.e. balance = deposit amount or 6) if it failed and balance remained at
	 * 0. Expected result is taken from the test data file
	 */
	@Test(priority = 1, dataProvider = "getDepositData", enabled = true)
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

	/*
	 * This test 1) logs into the XYZ Bank site 2) Selects a customer (test
	 * properties file) 3) Cleans the Balance before each deposit 4) Deposits an
	 * amount as per test data file 5) Withdraws an amount as per test data file
	 * 6) Evaluates if the withdrawal was successful, i.e. balance = deposit -
	 * withdrawal or 7) if it failed and balance remained as deposit. Expected
	 * result is taken from the test data file
	 */
	@Test(priority = 2, dataProvider = "getWithdrawalData", enabled = true)
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

	// ***************** DATA PROVIDERS *********************

	@DataProvider
	public static Object[][] getDepositData() throws IOException {

		log.info("Reading test data from: "
				+ TestConfig.getProperty("deposit.testData"));
		
		return Csv.getData(System.getProperty("user.dir")
				+ TestConfig.getProperty("deposit.testData"));
	}

	@DataProvider
	public static Object[][] getWithdrawalData() throws IOException {

		log.info("Reading test data from: "
				+ TestConfig.getProperty("withdrawal.testData"));
		return Csv.getData(System.getProperty("user.dir")
				+ TestConfig.getProperty("withdrawal.testData"));
	}

	// ***************** KEYWORDS *********************

	public void login(String customerName) throws IOException {

		navigateToUrl(OR.getProperty("login.url"));

		click("login.btnCustomerLogin_Xpath");
		// it should open another page - which is verified below
		Assert.assertEquals(driver.getCurrentUrl(),
				OR.getProperty("customer.url"));

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
		}
		else
		{
			report("Trying to delete transactions - no transactions found.");
		}
		driver.navigate().back();
	}
}
