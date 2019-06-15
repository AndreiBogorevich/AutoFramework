package listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;

import utils.Csv;
import utils.Messages;
import utils.Screen;
import base.TestBase;

import com.aventstack.extentreports.ExtentTest;

public class CustomListeners extends TestBase implements ITestListener,
		IInvokedMethodListener {

	@Override
	public void onTestStart(ITestResult result) {

		// initiating extent report test
		ExtentTest extentTest = extent.createTest(result.getMethod()
				.getMethodName(), result.getMethod().getDescription());
		test.set(extentTest);

	}

	@Override
	public void onTestSuccess(ITestResult result) {

		// updating extent report
		test.get().pass(Messages.msgTestPassed);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		// Capturing a screenshot and adding it to the report
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		Screen.captureScreenshot();
		Reporter.log("<a target=\"_blank\" href=" + Screen.screenshotFile
				+ ">Screenshot</a>");
		Reporter.log("<br>");
		Reporter.log("<br>");
		Reporter.log("<a target=\"_blank\" href=" + Screen.screenshotFile
				+ "><img src=" + Screen.screenshotFile
				+ " height=200 width=200></img></a>");

		// updating extent report
		test.get().fail(Messages.msgTestFailed);
		test.get().fail(result.getThrowable());
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
		// test.get().skip(result.getThrowable());
		test.get().skip(Messages.msgTestSkipped);
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

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

		// only if the invoking method is not configuration
		if (!method.isConfigurationMethod()) {
			// checking if the use runnable file flag is set in propterties
			if (TestBase.getTestConfig().getProperty("general.flagUseRunnable")
					.equalsIgnoreCase("y")) {

				// reading data from the runnable file
				ArrayList<Hashtable<String, String>> table = Csv.getData(System
						.getProperty("user.dir")
						+ TestBase.getTestConfig().getProperty(
								"general.runnable"));

				// looking for the current test name in the runnable test file
				for (Hashtable<String, String> tableRow : table) {

					// if the current test exists in the runnable file
					if (tableRow.get("testCaseName").equalsIgnoreCase(
							testResult.getName())) {

						// and the flag is NOT set to "y" - skip the test
						if (!tableRow.get("runnable").equalsIgnoreCase("y")) {

							throw new SkipException(
									Messages.msgSkipTestAsNotRunnable);
						}
					}
				}
			}
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub

	}

}
