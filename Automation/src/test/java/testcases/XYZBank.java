package testcases;

import java.io.IOException;
import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import utils.Csv;
import base.TestBase;

public class XYZBank extends TestBase {

	private String customer = "Harry Potter"; // to be replaced with data from a
												// file
	private static String sDepositTestData = System.getProperty("user.dir")
			+ "\\src\\test\\resources\\testData\\XYZBank\\depositTest.csv";

	@Test(priority = 1, dataProvider = "getDepositData")
	public void depositTest(Hashtable<String, String> data) throws IOException {

		login(customer);
		resetTransactions();

		deposit(data.get("amount"));
		Assert.assertTrue(setOR("XYZBank", "account"));

		if (data.get("expectedResult").toLowerCase().equals("success")) {
			Assert.assertEquals(read("lableBalanceValue_Xpath"), // actual
					data.get("amount")); // expected
		} else {
			Assert.assertEquals(read("lableBalanceValue_Xpath"), // actual
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

		return Csv.getData(sDepositTestData);
	}

	public void login(String customerName) throws IOException {

		Assert.assertTrue(setOR("XYZBank", "login"));
		navigateToUrl();

		click("btnCustomerLogin_Xpath");
		// it should open another page - which is verified below

		Assert.assertTrue(setOR("XYZBank", "customer"));
		Assert.assertEquals(driver.getCurrentUrl(), OR.getProperty("url"));

		selectByVisibleText("selectYourName_Xpath", customerName);

		click("btnLogin_Xpath");
	}

	public void deposit(String amount) {

		Assert.assertTrue(setOR("XYZBank", "account"));
		navigateToUrl();

		click("tabDeposit_Xpath");
		type("txtAmount_Xpath", amount);
		click("btnSubmit_Xpath");

		// Assert.assertEquals(read("labelResult_Xpath").toLowerCase(),
		// "deposit successful");
	}

	public void withdraw(int amount) {

		Assert.assertTrue(setOR("XYZBank", "account"));
		navigateToUrl();

		click("tabWithdrawl_Xpath");
		type("txtAmount_Xpath", Integer.toString(amount));
		click("btnSubmit_Xpath");

		Assert.assertEquals(read("labelResult_Xpath").toLowerCase(),
				"transaction successful");
	}

	public void resetTransactions() {

		Assert.assertTrue(setOR("XYZBank", "listTx"));
		navigateToUrl();

		if (getElement("btnReset_Xpath").isDisplayed()) {
			click("btnReset_Xpath");
		}
		driver.navigate().back();
	}
}
