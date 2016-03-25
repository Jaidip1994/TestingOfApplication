package com.sap.solman.utility;

import org.apache.bcel.classfile.Utility;

/**
 * Constant class to take care of constants used in the Project
 * @author Jaidip Ghosh
 * @version 1.0
 */
public class Constant {

	// provide the path for the spreadsheet
	public static final String PATH_TESTDATA = "src/testData/";
	// provide the name of the spreadsheet
	public static final String FILE_TESTDATA = "PIMONTestSuite.xlsx";

	public static final String BASEURL = "http://www.calculator.net";
	// path for screenshots
	public static final String PATH_SCREENSHOT = "C:\\ExtentReportsOutput\\TestOfDemoProject\\Screenshots\\Screenshot";
	// path for the path_AUTO_IT
	public static final String PATH_AUTOIT = Utility.class.getClassLoader().getResource("handleAuthentication.exe").getFile();
	// path for the extent report
	public static final String EXTENT_REPORT_PATH = "C:\\ExtentReportsOutput\\TestOfDemoProject\\TestReport";
	
}


