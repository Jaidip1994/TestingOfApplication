package com.sap.solman.automationFramework;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.example.drivers.SelfChromeDriver;
import com.example.drivers.SelfFireFoxDriver;
import com.example.drivers.SelfIeDriver;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import com.sap.solman.appModules.Automator;
import com.sap.solman.utility.Constant;
import com.sap.solman.utility.ExcelUtils;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * The TestSuiteMainMethodClass Class contains the main method which runs the test
 * suite. The Local object repository is read from the excel sheet and stored in
 * HashMap LocalObjectRepo.
 * 
 * @author Jaidip Ghosh
 * @version 1.0
 */

public class TestSuiteMain {

	/**
	 * Initialize map to store Local Object Repository
	 */
	public final static Map<String, String> LocalObjectRepo = new HashMap<String, String>();
	//For generating of the extent reports
	public static ExtentReports extent;
	public static ExtentTest test;
	// Initialize webdriver
	public static WebDriver driver = null;
	
	//Initialisation of the logger of the log4j element
	static Logger logger = LogManager.getLogger(TestSuiteMain.class.getName());

	/**
	 * runTest Method calls action that can be performed by calling
	 * processAction, it reads the excel object locator, finds the element and
	 * then passes the element and the action to processAction method
	 * 
	 * @param driver
	 *            Selenium Webdriver instance
	 */
	private static void runTest() throws Exception {
		int i = 1;
		String path="";
		logger.info("openning the website");
		test.log(LogStatus.INFO, "Application up and running"," ");
		while (!ExcelUtils.getCellData(i, 0).isEmpty()) {

			try {
				WebElement element = Automator.findElement(driver,
						ExcelUtils.getCellData(i, 2), LocalObjectRepo);
				System.out.println(i);

				Automator.processAction(driver, element,
						ExcelUtils.getCellData(i, 1),
						ExcelUtils.getCellData(i, 4));

				ExcelUtils.setCellData("Passed", i, 5);
				logger.info(ExcelUtils.getCellData(i, 0)+" - "+ExcelUtils.getCellData(i, 1)+" - Object: "+ExcelUtils.getCellData(i, 2)+" Status:-"+"Passed");
				test.log(LogStatus.PASS,ExcelUtils.getCellData(i, 0)+" - "+ExcelUtils.getCellData(i, 1)+" - Object: "+ExcelUtils.getCellData(i, 2), "Passed");

			} catch (Exception e) {
				path = Automator.captureScreenShot(driver);
				test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(path));
				logger.error(ExcelUtils.getCellData(i, 0)+" - "+ExcelUtils.getCellData(i, 1)+" - Object: "+ExcelUtils.getCellData(i, 2)+" Status:-"+"Failed");
				test.log(LogStatus.FAIL, ExcelUtils.getCellData(i, 0)+" - "+ExcelUtils.getCellData(i, 1)+" - Object: "+ExcelUtils.getCellData(i, 2)+" - Message: "+e.getMessage(), "Failed");
				ExcelUtils.setCellData("Failed", i, 5);
				ExcelUtils.setCellData(e.getMessage(), i, 6);
				System.out.println(e.getMessage());
			}
			i++;
		}
	}

	
	public static void extentReportInitialisation() {
		String date = new Date().toString();
		date = date.replace(' ', '_');
		date = date.replace(':', '_');
	   extent = new ExtentReports(Constant.EXTENT_REPORT_PATH+date+".html", false, DisplayOrder.NEWEST_FIRST, NetworkMode.OFFLINE);
	}
	/**
	 * Setup method is called to set the environment up
	 * 
	 * @return Selenium webdriver
	 * @throws java.lang.Exception
	 *             If excelsheet thorws an exception
	 */
	@BeforeTest
	public static WebDriver setup() throws Exception {

		// This is to open the Excel file. Excel path, file name and the sheet
		// name are parameters to this method
		// Read the local object repository
		ExcelUtils.setExcelFile(
				Constant.PATH_TESTDATA + Constant.FILE_TESTDATA,
				"Local Object Repository");
		Sheet sheet = ExcelUtils.getSelectedSheet();
		try {

			for (Row row : sheet) {
				if (row.getRowNum() == 0) { // if the sheet is empty
					continue;
				}
				LocalObjectRepo.put(row.getCell(0).toString(), row.getCell(1)
						.toString());
			}
		} catch (Exception e) {
			// The worksheet will throw exception when it is read fully
		}
		extentReportInitialisation();
		DOMConfigurator.configure("log4j.xml");
		// Create a new firefox webdriver
		/*SelfFireFoxDriver selfFireFoxDriver = new SelfFireFoxDriver(); 
		driver = selfFireFoxDriver.getDriver();*/
		
		//Creating of the chrome driver
		SelfChromeDriver selfChromeDriver = new SelfChromeDriver(); 
		driver = selfChromeDriver.getDriver();
		
		//Creating of the IE driver
		/*SelfIeDriver ieDriver = new SelfIeDriver(); 
	      driver = ieDriver.getDriver();*/
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // wait
		// 10
		// seconds
		driver.manage().window().maximize();
		return driver;
	}

	/**
	 * method teardown closes the webdriver
	 * 
	 * @param driver
	 *            Selenium WebDriver
	 */
	@AfterTest
	public static void teardown() {
		driver.quit();
		// ending test
		extent.endTest(test);

		// writing everything to document
		extent.flush();
	}

	/**
	 * Main function that runs the test
	 * 
	 * @param args
	 *            Arguments
	 * @throws java.lang.Exception
	 *             if tests raise any exception due to an issue
	 */
	
	@Test(description="This the main testcase")
	public void main() throws Exception {
		// Set the sheet DownloadArena to be used for this test
		test = extent.startTest("Testing Of Demo Project!!!");
		ExcelUtils.setExcelFile(
				Constant.PATH_TESTDATA + Constant.FILE_TESTDATA,
				"TestCase");
		// Run the test
		System.out.println("Testing of the test case start!!!");
		runTest();
		// Save the result to the sheet
		ExcelUtils.saveSheet();
	}
}