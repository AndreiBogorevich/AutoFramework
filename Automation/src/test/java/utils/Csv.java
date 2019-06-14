package utils;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.testng.Assert;

import base.TestBase;

public class Csv {

	public static ArrayList<Hashtable<String, String>> getData(String sFile) {

		// reading CSV file
		try (Reader reader = Files.newBufferedReader(Paths.get(sFile));

		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

		) {
			// getting all records from csv
			List<CSVRecord> csvRecordsList = csvParser.getRecords();

			// if the file is contains data:
			if (csvRecordsList.size() > 0) {
				int rows = csvRecordsList.size(); // number of rows
				int cols = csvRecordsList.get(0).size(); // number of columns
				CSVRecord csvHeader = csvRecordsList.get(0); // column headers

				// Object[][] data = new Object[rows - 1][1];
				Hashtable<String, String> tableRow = new Hashtable<String, String>();
				ArrayList<Hashtable<String, String>> table = new ArrayList<Hashtable<String, String>>();

				for (int i = 1; i < rows; i++) {

					// for every csv row we initiate a hashtable
					tableRow = new Hashtable<String, String>();
					// and retrieve the current csv record
					CSVRecord csvRecord = csvRecordsList.get(i);

					for (int y = 0; y < cols; y++) {

						// filling the hashtable using csv header
						// and column values of the current csv row
						tableRow.put(csvHeader.get(y), csvRecord.get(y));

					}
					// adding the hashtable to the returnable array
					table.add(tableRow);

				}
				return table;
			}
			else {
				return null;
			}
		} catch (IOException e) {
			TestBase.report("Failed to load test data from " + sFile);
			TestBase.log.error(e.getMessage());
			e.printStackTrace();
			Assert.fail("Failed to load test data from " + sFile);
			return null;
		}

	}
}
