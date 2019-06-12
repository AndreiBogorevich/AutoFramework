package utils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import base.TestBase;

public class Screen extends TestBase {
	
	private static String screenshotPath;
	public static String screenshotFile;
	
	public static void captureScreenshot(){
		
		File fFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		
		screenshotPath = (System.getProperty("user.dir") + config.getProperty("screenshotAndReportFolder"));
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Timestamp(System.currentTimeMillis()));
		
		screenshotFile = "Screenshot_" + timeStamp + ".jpg";
		
		log.info("Taking screensot and saving it at :" + screenshotPath + screenshotFile);
		
		try {
			FileUtils.copyFile(fFile, new File(screenshotPath + screenshotFile));
		} catch (IOException e) {
			log.error("Failed to take a screenshot");
		}
	}
}
