package utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

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

		ArrayList<Hashtable<String, String>> arrTestsAll = Csv
				.getData(sTestDataFile);

		// checking if the file contains data at all
		if (arrTestsAll != null) {

			ArrayList<Hashtable<String, String>> arrTestsRunnable = new ArrayList<Hashtable<String, String>>();

			// checking if the csv file has runmode column
			if (arrTestsAll.get(0).containsKey("runmode")) {

				// if yes, creating a multidimentional collecton
				// to store the runnable tests only
				for (Hashtable<String, String> row : arrTestsAll) {

					if (row.get("runmode").equalsIgnoreCase("y")) {
						arrTestsRunnable.add(row);
					} // end if runmode = y

				} // end for
					// return only runnable tests after
					// converting the arraylist to Object[][]
					// as only this type is allowed for DataProviders

				TestBase.log
						.info("Returning only records where runmode = y from: "
								+ sTestDataFile);

				return convertArrayListToObjectArray(arrTestsRunnable);

			} // end if: does the first row has 'runmode' key?
			else { // no 'runmode' key in the first row

				// return all tests after
				// converting the arraylist to Object[][]
				// as only this type is allowed for DataProviders
				TestBase.log.info("Returning all records from: "
						+ sTestDataFile);

				return convertArrayListToObjectArray(arrTestsAll);

			} // end else: no 'runmode' key in the first row

		} else { // no data in the file

			// no data found in file, logging it and returning null
			TestBase.log.error("No data found in the test data file: "
					+ sTestDataFile);

			return new Object[0][0];

		} // end else: no data in the file
	}

	private static Object[][] convertArrayListToObjectArray(
			ArrayList<Hashtable<String, String>> array) {
		
		int iSize = array.size();

		Object[][] data = new Object[iSize][1];
		
		for (int i = 0; i < iSize; i++) {
			data[i][0] = array.get(i);
		}

		return data;
	}

}