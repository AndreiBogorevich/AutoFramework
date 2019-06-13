package utils;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.testng.Assert;

import base.TestBase;


public class Csv {
	
	public static Object[][] getData(String sFile){
		
		try (Reader reader = Files.newBufferedReader(Paths.get(sFile));

				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

				) {
					List<CSVRecord> csvRecordsList = csvParser.getRecords();

					int rows = csvRecordsList.size();
					int cols = csvRecordsList.get(0).size();
					CSVRecord csvHeader = csvRecordsList.get(0);

					Object[][] data = new Object[rows - 1][1];
					Hashtable<String, String> table = null;

					for (int i = 1; i < rows; i++) {
						
						table = new Hashtable<String, String>();
						CSVRecord csvRecord = csvRecordsList.get(i);
						
						for (int y = 0; y < cols; y++) {
							
							table.put(csvHeader.get(y), csvRecord.get(y));
							data[i - 1][0] = table;
						}
					}
					return data;
				} catch (IOException e) {					
					TestBase.report("Failed to load test data from " + sFile);			
					TestBase.log.error(e.getMessage());
					e.printStackTrace();
					Assert.fail("Failed to load test data from " + sFile);
					return null;
				}		
	}

}
