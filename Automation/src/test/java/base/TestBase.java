package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {

	/*
	 * TestBase is used to set-up framework for test execution: Properties Logs
	 * Browsers DB connections, etc. Also it implements common keywords like
	 * click, type, etc.
	 */

	protected static WebDriver driver;
	private static Properties config;
	protected static Properties OR;
	// the Logger will dynamically report the name pf the method from which it
	// was called
	protected static Logger log = LogManager.getLogger("log4j");;

	@BeforeSuite
	public void setUp() throws IOException {

		// Loading properties (config.properties file)
		setConfig();

		// initialising browser that is set up in config.properties file
		driver = Browsers.getBrowser(config.getProperty("browser"));

		// navigating to the starting url as per config.properties file
		driver.get(config.getProperty("startUrl"));
		driver.manage().window().maximize();

		// waiting for the page to load
		driver.manage()
				.timeouts()
				.implicitlyWait(
						Long.parseUnsignedLong(config
								.getProperty("implicitWait")), TimeUnit.SECONDS);

	}

	@AfterSuite
	public void tearDown() {

		driver.quit();

	}

	public static Properties getConfig() {
		return config;
	}

	public static boolean setConfig() {

//		Logger log = LogManager.getLogger("setConfig");

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
		return config;
	}

	public static boolean setOR(String site, String page) {

//		Logger log = LogManager.getLogger("setOR");

		try {
			OR = new Properties();
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\resources\\object_reporsitory\\"
							+ site + "\\" + page + ".properties");

			OR.load(fis);

			log.info(site + "\\" + page + ".properties"
					+ " is successfully loaded into OR property.");

			return true;

		} catch (Throwable t) {

			log.error("Error loading " + site + "\\" + page + ".properties");
			log.error(t.getMessage());

			return false;
		}
	}

	public WebElement getElement(String element) {

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
		} else if (element.toLowerCase().contains("_classname")) {
			return driver.findElement(By.className(OR.getProperty(element)));
		} else if (element.toLowerCase().contains("_partiallinktext")) {
		return driver.findElement(By.partialLinkText(OR.getProperty(element)));
		}
		return null;
	}

	// ******************************************************
	// Keywords

	public void click(String element) {

//		Logger log = LogManager.getLogger("click");

		getElement(element).click();

		log.info("Clicked " + element);

		waitForPageToLoad();
	}

	public void type(String element, String text) {

//		Logger log = LogManager.getLogger("type");

		getElement(element).sendKeys(text);

		log.info("Typed " + text + " into " + element);
	}

	public void selectByVisibleText(String element, String visibleText) {

//		Logger log = LogManager.getLogger("select");

		Select sel = new Select(getElement(element));
		sel.selectByVisibleText(visibleText);

		log.info("Selected " + visibleText + " in the " + element);
	}

	public String read(String element) {

		String sText = getElement(element).getText();
		if (sText == null) {
			sText = getElement(element).getAttribute("text");
		}
		return sText;
	}

	public void waitForPageToLoad() {

//		Logger log = LogManager.getLogger("waitForPageToLoad");

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
}