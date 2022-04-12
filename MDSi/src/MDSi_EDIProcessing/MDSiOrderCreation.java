package MDSi_EDIProcessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;

import org.testng.annotations.Test;

public class MDSiOrderCreation extends StartUp {
	static StringBuilder msg = new StringBuilder();
	static String jobid, jobNum1, jobNum;

	@Test
	public void mdSiOrderCreation() throws Exception {
		String baseUrl = "http://10.20.104.82:9077/TestApplicationUtility/MDSIOrderCreation";
		driver.get(baseUrl);

		Thread.sleep(5000);

		// Read data from Excel
		File src = new File(".\\src\\TestFiles\\MDSiTestResult.xlsx");
		FileInputStream fis = new FileInputStream(src);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sh1 = workbook.getSheet("Sheet1");

		for (int i = 1; i < 4; i++) {
			DataFormatter formatter = new DataFormatter();
			String file = formatter.formatCellValue(sh1.getRow(i).getCell(0));
			System.out.println("file Name==" + file + ".xml");
			driver.findElement(By.id("MainContent_ctrlfileupload"))
					.sendKeys("C:\\Users\\rprajapati\\git\\MDSi\\MDSi\\src\\TestFiles\\" + file + ".xml");
			Thread.sleep(1000);
			driver.findElement(By.id("MainContent_btnProcess")).click();
			Thread.sleep(5000);

			if (i == 1) {
				fedEXCarrier();
			}

			if (i == 2) {
				upsCarrier();
			}

			if (i == 3) {
				cpuService();
			}

			String Job = driver.findElement(By.id("MainContent_lblresult")).getText();
			System.out.println("Response=" + Job);

			if (Job.contains("<a:ErrorCode i:nil=\"true\" />")) {
				jobNum = Job.replaceAll("[^0-9]", "");
				String[] list = jobNum.split("");
				jobid = (list[27] + list[28] + list[29] + list[30] + list[31] + list[32] + list[33] + list[34]);
				System.out.println("JOB# " + jobid);

				File src1 = new File(".\\src\\TestFiles\\MDSiTestResult.xlsx");
				FileOutputStream fis1 = new FileOutputStream(src1);
				Sheet sh2 = workbook.getSheet("Sheet1");
				sh2.getRow(i).createCell(1).setCellValue(jobid);
				workbook.write(fis1);
				fis1.close();

				msg.append("JOB # " + jobid + "\n");
			}

			else {
				System.out.println(Job);
				msg.append("\n\n\n" + "Order Did Not Created and Reason :: " + Job + "\n\n\n");
			}

		}
		String subject = "Selenium Automation Script: STAGING MDSi_EDI - Shipment Creation";
		// asharma@samyak.com,pgandhi@samyak.com,kunjan.modi@samyak.com,pdoshi@samyak.com
		try {

			Email.sendMail("ravina.prajapati@samyak.com,asharma@samyak.com,parth.doshi@samyak.com", subject,
					msg.toString(), "");

		} catch (Exception ex) {
			Logger.getLogger(MDSiOrderCreation.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void fedEXCarrier() throws Exception {
		Thread.sleep(5000);
		Screenshots.takeSnapShot(driver, ".\\H3P_WithFedExCarrier.jpg");
	}

	public static void upsCarrier() throws Exception {
		Thread.sleep(5000);
		Screenshots.takeSnapShot(driver, ".\\H3P_WithUPSCarrier.jpg");
	}

	public static void cpuService() throws Exception {
		Thread.sleep(5000);
		Screenshots.takeSnapShot(driver, ".\\CPUService.jpg");
	}

}
