package com.example.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * SelfFireFoxDriver class contains method for initialisation of the firefox web driver
 * 
 * @author Jaidip Ghosh
 *
 */
public class SelfFireFoxDriver {
	/**
	 * getDriver method creates the the instance of the webdriver and then return it.
	 * 
	 * @return
	 * 			instance of the webdriver
	 */
	public WebDriver getDriver(){
		WebDriver webDriver = new FirefoxDriver();
		return webDriver;
	}
}
