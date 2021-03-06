package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import utils.ExtentManager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class TestBase {

	/*
	 * TestBase is used to set-up framework for test execution: Properties Logs
	 * Browsers DB connections, etc. Also it implements common keywords like
	 * click, type, etc.
	 */

	protected static WebDriver driver;

	// properties declarations
	protected static Properties config;
	protected static Properties OR;
	protected static Properties TestConfig;

	// log4j2 declarations
	public static final Logger log = LogManager.getLogger(TestBase.class);

	// Extent Report Declarations
	protected static ExtentReports extent = ExtentManager.createInstance();
	protected static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	@BeforeSuite
	public void setUp() throws IOException {

		// Loading properties (config.properties file)
		setConfig();

		// if the environmental property for browser is set
		// recording it into the config file
		if (System.getenv("browser") != null
				&& !System.getenv("browser").isEmpty()) {
			config.setProperty("browser", System.getenv("browser"));
		}

		// Initializing browser that is set up in config.properties file
		driver = Browsers.getBrowser(config.getProperty("browser"));

		// navigating to the starting url as per config.properties file
		navigateToUrl(config.getProperty("startUrl"));

	}

	@AfterSuite
	public void tearDown() {

		driver.quit();

	}

	public static Properties getConfig() {
		return config;
	}

	public static boolean setConfig() {

		try {
			config = new Properties();
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\resources\\properties\\config.properties");
			config.load(fis);

			log.info("Config.properties successfully loaded");

			return true;

		} catch (Throwable t) {

			log.error("Error loading config.properties");
			log.error(t.getMessage());

			return false;
		}
	}

	public static Properties getOR() {
		return OR;
	}

	public static boolean setOR(String sOrFileName) {

		String sFile = System.getProperty("user.dir")
				+ "\\src\\test\\resources\\object_reporsitory\\" + sOrFileName
				+ ".properties";

		try {
			OR = new Properties();
			FileInputStream fis = new FileInputStream(sFile);

			OR.load(fis);

			// adding hard-coded wait for the properties to load
			Thread.sleep(1000L);

			log.info(sFile + " is successfully loaded into OR property.");

			return true;

		} catch (Throwable t) {

			log.error("Error loading " + sFile);
			log.error(t.getMessage());

			return false;
		}
	}

	public static boolean setTestConfig(String sTestConfigFileName) {

		String sFile = System.getProperty("user.dir")
				+ "\\src\\test\\resources\\properties\\" + sTestConfigFileName
				+ ".properties";

		try {
			TestConfig = new Properties();
			FileInputStream fis = new FileInputStream(sFile);
			TestConfig.load(fis);

			// adding hard-coded wait for the properties to load
			Thread.sleep(1000L);

			log.info(sFile
					+ " is successfully loaded into TestConfig property.");

			return true;

		} catch (Throwable t) {

			log.error("Error loading " + sFile);
			log.error(t.getMessage());

			return false;
		}
	}

	public static Properties getTestConfig() {
		return TestConfig;
	}

	public static WebElement getElement(String element) {

		if (element.toLowerCase().contains("_xpath")) {
			return driver.findElement(By.xpath(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_id")) {
			return driver.findElement(By.id(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_name")) {
			return driver.findElement(By.name(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_css")) {
			return driver.findElement(By.cssSelector(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_tagname")) {
			return driver.findElement(By.tagName(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_linktext")) {
			return driver.findElement(By.linkText(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_partiallinktext")) {
			return driver.findElement(By.partialLinkText(OR
					.getProperty(element)));
		} else if (element.toLowerCase().contains("_classname")) {
			return driver.findElement(By.className(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_partiallinktext")) {
			return driver.findElement(By.partialLinkText(OR
					.getProperty(element)));
		}
		return null;
	}

	public static List<WebElement> getElements(String element) {

		if (element.toLowerCase().contains("_xpath")) {
			return driver.findElements(By.xpath(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_id")) {
			return driver.findElements(By.id(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_name")) {
			return driver.findElements(By.name(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_css")) {
			return driver.findElements(By.cssSelector(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_tagname")) {
			return driver.findElements(By.tagName(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_linktext")) {
			return driver.findElements(By.linkText(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_partiallinktext")) {
			return driver.findElements(By.partialLinkText(OR
					.getProperty(element)));
		} else if (element.toLowerCase().contains("_classname")) {
			return driver.findElements(By.className(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_partiallinktext")) {
			return driver.findElements(By.partialLinkText(OR
					.getProperty(element)));
		}
		return null;
	}

	public boolean isElementPresent(String element) {
		if (getElement(element) != null) {
			return true;
		}
		return false;
	}

	// ******************************************************
	// Keywords

	public void click(String element) {
		getElement(element).click();

		log.info("Clicked " + element);

		waitForPageToLoad();
	}

	public void type(String element, String text) {

		getElement(element).sendKeys(text);

		log.info("Typed " + text + " into " + element);
	}

	public void selectByVisibleText(String element, String visibleText) {

		Select sel = new Select(getElement(element));
		sel.selectByVisibleText(visibleText);

		log.info("Selected " + visibleText + " in the " + element);
	}

	public static String read(String element) {

		String sText = getElement(element).getText();
		if (sText == null) {
			sText = getElement(element).getAttribute("text");
		}
		return sText;
	}

	public static void waitForPageToLoad() {

		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver)
						.executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		try {
			Thread.sleep(1000L);
			WebDriverWait wait = new WebDriverWait(driver,
					Integer.parseInt(config.getProperty("explicitWait")));
			wait.until(expectation);
		} catch (Throwable error) {
			log.error("Timed out loading a page.");

			Assert.fail();
		}
	}

	public static void navigateToUrl(String sUrl) {

		log.info("Navigating to " + sUrl);
		driver.get(sUrl);
		driver.manage().window().maximize();

		// waiting for the page to load
		driver.manage()
				.timeouts()
				.implicitlyWait(
						Long.parseUnsignedLong(config
								.getProperty("implicitWait")), TimeUnit.SECONDS);

	}

	public static void navigateToUrl() {
		navigateToUrl(OR.getProperty("url"));
	}

	public static int getNumberOfRowsInTable(String elementTable) {
		String sXpath = elementTable + "/tbody/tr";
		return driver.findElements(By.xpath(sXpath)).size();
	}

	public static void highlightElement(String element) {

		((JavascriptExecutor) driver).executeScript(
				"arguments[0].style.border='3px solid red'",
				getElement(element));

	}

	public static void checkResults(String sActual, String sExpected) {

		report("Comparing test values.   " + "ACTUAL: " + sActual
				+ " | EXPECTED: " + sExpected);

		Assert.assertEquals(sActual, sExpected);
	}

	public static void checkResults(String sActual, String sExpected,
			String elementToHighlight) {

		highlightElement(elementToHighlight);

		checkResults(sActual, sExpected);

	}

	public static void report(String sText) {

		// reporting using ReportNG
		Reporter.log(sText);
		Reporter.log("<br>");

		// reporting using log4j2
		log.info(sText);

		// reporting using Extent reports
		test.get().info(sText);
	}
}
