package utils;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import base.TestBase;

public class DataProviders {

	@DataProvider(name="dp_Csv")
	public static Object[][] getDataFromCsv(Method m) throws IOException {

		// IMPORTANT: files with test data should have the same name as the test 
		TestBase.log.info("Reading test data from: "
				+ TestBase.getTestConfig().getProperty("general.testData")
				+ m.getName() + ".csv");
		
		return Csv.getData(System.getProperty("user.dir")
				+ TestBase.getTestConfig().getProperty("general.testData")
				+ m.getName() + ".csv");
	}
}
