package utils;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class Csv {
	
	public static Object[][] getData(String sFile) throws IOException{
		
		try (Reader reader = Files.newBufferedReader(Paths.get(sFile));

				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

				) {
					Map<String, String> map = new HashMap();

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
				}
		
	}

}
