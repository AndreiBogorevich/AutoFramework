package tmp;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import base.Browsers;


public class tmp {

	public static void main(String[] args) throws IOException {

		WebDriver driver = Browsers.getFirefox();
		driver.get("https://www.google.com");
		driver.quit();
	}
}
