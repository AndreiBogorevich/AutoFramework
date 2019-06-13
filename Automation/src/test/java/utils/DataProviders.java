package utils;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import base.TestBase;

public class DataProviders {

	@DataProvider(name = "dp_Csv")
	public static Object[][] getDataFromCsv(Method m) {

		// IMPORTANT: files with test data should have the same name as the test
		String sTestDataFile = System.getProperty("user.dir")
				+ TestBase.getTestConfig().getProperty("general.testData")
				+ m.getName() + ".csv";

		TestBase.log.info("Reading test data from: " + sTestDataFile);

		return Csv.getData(sTestDataFile);
	}
}
