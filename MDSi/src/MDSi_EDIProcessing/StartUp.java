package MDSi_EDIProcessing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class StartUp {
	static WebDriver driver;

	@BeforeSuite
	public static void startUp() {
		System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");

		ChromeOptions options = new ChromeOptions();
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
	}

	@AfterSuite
	public void Complete() throws Exception {
		driver.close();
	}
}
