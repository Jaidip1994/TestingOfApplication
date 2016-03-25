package com.example.drivers;

import org.apache.bcel.classfile.Utility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * SelfChromedriver class contains method for initialisation of the chrome web driver
 * 
 * @author Jaidip Ghosh
 *
 */
public class SelfChromeDriver {
	/**
	 * getDriver method creates the the instance of the webdriver and then return it.
	 * 
	 * @return
	 * 			instance of the webdriver
	 */
	public WebDriver getDriver(){
		String CHROME_DRIVER_PATH = Utility.class.getClassLoader().getResource("chromedriver.exe").getFile();
		System.setProperty("webdriver.chrome.driver",CHROME_DRIVER_PATH);
		WebDriver webDriver = new ChromeDriver();
		return webDriver;
	}

}
