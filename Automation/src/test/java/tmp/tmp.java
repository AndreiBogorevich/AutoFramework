package tmp;

import java.util.ArrayList;
import java.util.Hashtable;

import utils.Csv;

public class tmp {

	public static void main(String[] args) {

		ArrayList<Hashtable<String, String>> data = Csv
				.getData("C:\\Users\\bogorevich_a\\git\\AutoFramework\\Automation\\src\\test\\resources\\testData\\XYZBank\\depositTest.csv");
		
		ArrayList<Hashtable<String, String>> table = new ArrayList<Hashtable<String, String>>();

		if (data != null) {


			System.out.println(data.size());
			if (data.get(0).containsKey("runmode")) {

				for (Hashtable<String, String> row : data) {

					if (row.get("runmode").equalsIgnoreCase("y")) {
						table.add(row);
					}

				}
				int iSize = table.size();

				
				Object[][] dataObj = new Object[iSize][1];
				for (int i=0; i <iSize; i++)
				{
					dataObj[i][0]=table.get(i);
					System.out.println(dataObj[i][0]);
				}
				
				
			} else {
				System.out.println("the file doesn't have runmodes");
			}
		}
		else{
			System.out.println("empty file");
		}

	}
}
