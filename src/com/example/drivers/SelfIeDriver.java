package com.example.drivers;

import org.apache.bcel.classfile.Utility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 * SelfIeDriver class contains method for initialisation of the IE Web driver
 * 
 * @author Jaidip Ghosh
 *
 */

public class SelfIeDriver {
	/**
	 * getDriver method creates the the instance of the webdriver and then return it.
	 * 
	 * @return
	 * 			instance of the webdriver
	 */
	public WebDriver getDriver(){
		String IE_DRIVER_PATH = Utility.class.getClassLoader().getResource("/TestingOfDemoProject/resources/IEDriverServer.exe").getFile();
		System.setProperty("webdriver.ie.driver",IE_DRIVER_PATH);
		WebDriver webDriver = new InternetExplorerDriver();
		return webDriver;
	}
}
