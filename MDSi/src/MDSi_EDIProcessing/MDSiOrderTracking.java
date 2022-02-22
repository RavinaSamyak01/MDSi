package MDSi_EDIProcessing;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class MDSiOrderTracking extends StartUp {

	// static WebDriver driver;
	static StringBuilder msg = new StringBuilder();

	@Test
	public static void mdSiOrderTracking() throws Exception {
		/*
		 * System.setProperty("webdriver.chrome.driver", ".\\chromedriver.exe");
		 * 
		 * ChromeOptions options = new ChromeOptions(); driver = new
		 * ChromeDriver(options); driver.manage().window().maximize();
		 */
		// String baseUrl =
		// "http://10.20.104.82:9077/TestApplicationUtility/MDSITrackOrderClient";
		driver.get("http://10.20.104.82:9077/TestApplicationUtility/MDSITrackOrderClient");

		Thread.sleep(5000);

		// Read data from Excel
		File src = new File(".\\src\\TestFiles\\MDSiTestResult.xlsx");
		FileInputStream fis = new FileInputStream(src);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sh1 = workbook.getSheet("Sheet1");

		for (int i = 1; i < 2; i++) {
			DataFormatter formatter = new DataFormatter();
			String JobID = formatter.formatCellValue(sh1.getRow(i).getCell(1));

			driver.findElement(By.id("MainContent_HyperLinkJobID")).click();
			driver.findElement(By.id("MainContent_txtJobID")).clear();
			driver.findElement(By.id("MainContent_txtJobID")).sendKeys(JobID);

			driver.findElement(By.id("MainContent_txtUserName")).clear();
			driver.findElement(By.id("MainContent_txtUserName")).sendKeys("MDSI_WS");

			driver.findElement(By.id("MainContent_txtPassword")).clear();
			driver.findElement(By.id("MainContent_txtPassword")).sendKeys("MDSI_WS_14");

			driver.findElement(By.id("MainContent_ButtonTrackOrder")).click();
			Thread.sleep(6000);

			Screenshots.takeSnapShot(driver, ".\\src\\TestFiles\\MDSiTracking.jpg");
			Thread.sleep(3000);
			String Job = driver.findElement(By.id("MainContent_lblTrackOrderresult")).getText();
			System.out.println("MDSi Track Order DONE !");
			msg.append("\n\n" + "Response :" + "\n" + Job + "\n\n");

		}
		String subject = "Selenium Automation Script STAGING : MDSi_EDI - Shipment Tracking";
//		/// asharma@samyak.com,pgandhi@samyak.com,kunjan.modi@samyak.com,pdoshi@samyak.com
		try {
			Email.sendMail("ravina.prajapati@samyak.com, asharma@samyak.com,parth.doshi@samyak.com, pgandhi@samyak.com",
					subject, msg.toString(), "");
		} catch (Exception ex) {
			Logger.getLogger(MDSiOrderCreation.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
