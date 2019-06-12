package listeners;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import base.TestBase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import utils.ExtentManager;
import utils.Screen;

public class CustomListeners extends TestBase implements ITestListener {
	
	@Override
	public void onTestStart(ITestResult result) {

		// initiating extent report test
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),result.getMethod().getDescription());
        test.set(extentTest);
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		// updating extent report
        test.get().pass("Test passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// Capturing a screenshot and adding it to the report		
		System.setProperty("org.uncommons.reportng.escape-output","false");
		Screen.captureScreenshot();
		Reporter.log("<a target=\"_blank\" href=" + Screen.screenshotFile + ">Screenshot</a>");
		Reporter.log("<br>");
		Reporter.log("<br>");
		Reporter.log("<a target=\"_blank\" href=" + Screen.screenshotFile + "><img src=" + Screen.screenshotFile + " height=200 width=200></img></a>");
		
		// updating extent report
        test.get().fail("Test failed");
        try {
			test.get().addScreenCaptureFromPath(Screen.screenshotFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {

		// updating extent report
		test.get().skip(result.getThrowable());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {

		extent.flush();		
	}

}
