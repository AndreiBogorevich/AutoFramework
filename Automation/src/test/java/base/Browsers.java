package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Browsers {

	/*
	 * Browsers class implements methods to initialise WebDriver of desired
	 * brand (Chrome, Firefox, IE, etc.) and setting these browsers ready for
	 * test: ssl support, clean profiles, etc.
	 */

	public static WebDriver getBrowser(String browserName) throws IOException {

		switch (browserName.toLowerCase()) {
		case "chrome":
			return getChrome();
		case "ie":
			return getInternetExplorer();
		default:
			return getFirefox();
		}
	}

	@SuppressWarnings("deprecation")
	public static WebDriver getChrome() {

		System.setProperty(
				"webdriver.chrome.driver",
				System.getProperty("user.dir")
						+ "\\src\\test\\resources\\executables\\chromedriver.exe");

		// handling SSL connections
		DesiredCapabilities handlSSL = DesiredCapabilities.chrome();
		handlSSL.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		return new ChromeDriver(handlSSL);
	}

	public static WebDriver getFirefox() throws IOException {

		System.setProperty(
				"webdriver.gecko.driver",
				System.getProperty("user.dir")
						+ "\\src\\test\\resources\\executables\\geckodriver.exe");

		ProfilesIni profile = new ProfilesIni();

		// reading what profile to use from config.properties
		// to create a profile Win+R firefox.exe -p
		// the existin profiles are at %AppData%\Mozilla\Firefox\Profiles

		FirefoxProfile myprofile = profile.getProfile(TestBase.getConfig()
				.getProperty("firefoxProfle"));

		// handling SSL encryption for https sites
		myprofile.setAcceptUntrustedCertificates(true);
		myprofile.setAssumeUntrustedCertificateIssuer(false);

		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(myprofile);

		return new FirefoxDriver(options);
	}

	@SuppressWarnings("deprecation")
	public static WebDriver getInternetExplorer() {

		System.setProperty(
				"webdriver.ie.driver",
				System.getProperty("user.dir")
						+ "\\src\\test\\resources\\executables\\IEDriverServer.exe");

		// setting capabilities to start webdriver regardless of zoom and
		// security settings
		DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
		cap.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
		cap.setCapability(
				InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				true);

		return new InternetExplorerDriver(cap);
	}

}
