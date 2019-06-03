package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

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

		try {
			config = new Properties();
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\resources\\properties\\config.properties");
			config.load(fis);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	public static Properties getOR() {
		return config;
	}

	public static boolean setOR(String site, String page) {
		try {
			OR = new Properties();
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\resources\\object_reporsitory\\"
							+ site + "\\" + page + ".properties");

			OR.load(fis);

			return true;
		} catch (Throwable t) {
			System.out.println(t.getMessage());
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
		} else if (element.toLowerCase().contains("_partiallinktext")) {
			return driver.findElement(By.partialLinkText(OR
					.getProperty(element)));
		} else if (element.toLowerCase().contains("_classname")) {
			return driver.findElement(By.className(OR.getProperty(element)));
		}
		return null;
	}

	// ******************************************************
	// Keywords

	public void click(String element) {

		getElement(element).click();
		waitForPageToLoad();
	}

	public void type(String element, String text) {

		getElement(element).sendKeys(text);
	}

	public void selectByVisibleText(String element, String visibleText) {
		Select sel = new Select(getElement(element));
		sel.selectByVisibleText(visibleText);
	}

	public String read(String element) {

		String sText = getElement(element).getText();
		if (sText == null) {
			sText = getElement(element).getAttribute("text");
		}
		return sText;
	}

	public void waitForPageToLoad() {

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
			// TO DO: add propeper handling here
			System.out
					.println("Timeout waiting for Page Load Request to complete.");
			Assert.fail();
		}
	}
}