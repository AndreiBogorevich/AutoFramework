package utils;

import base.TestBase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager extends TestBase {

	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null)
			createInstance();
		return extent;
	}

	// Create an extent report instance
	public static ExtentReports createInstance() {

		// loading config.properties to read reports folder location
		setConfig();
		
		String fileName = (System.getProperty("user.dir")
				+ config.getProperty("screenshotAndReportFolder") 
				+ "extent.html");
		
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		
		htmlReporter.config().enableTimeline(false);	
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle(fileName);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName(fileName);
		
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		return extent;
	}

}
