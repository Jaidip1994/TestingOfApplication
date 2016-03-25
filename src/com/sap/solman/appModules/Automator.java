package com.sap.solman.appModules;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.bcel.classfile.Utility;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sap.solman.automationFramework.TestSuiteMain;
import com.sap.solman.utility.Constant;

/**
 * Automator class is used for automation in the UI, It has method for closing
 * the popup, Method to find element from the UI and process action on the
 * elements
 * 
 * @author Jaidip Ghosh
 * @version 1.0
 */
public class Automator {

	/**
	 * The method closePopup() is used to close the popups of the browser. This
	 * test involves launching Operating system popups which need to be closed
	 * manually and do not close by this method
	 * 
	 * @param driver
	 *            Webdriver reference
	 */
	private static void closePopup(WebDriver driver) {
		// get window handles
		String mainWindow = driver.getWindowHandle();
		// Wait for more than one window to be created
		new WebDriverWait(driver, 120).until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return driver.getWindowHandles().size() == 2;
			}
		});

		// close the window
		for (String handle : driver.getWindowHandles()) {
			if (!handle.equals(mainWindow)) {
				driver.switchTo().window(handle);
				driver.close();
				break;
			}
		}

		// switch to main window
		driver.switchTo().window(mainWindow);
	}
	/**
	 * highlightElement method is basically used to highlight the element during 
	 * the current execution with a yellow background and a border of 2px red 
	 * and after five second that highlight things toggles back to its original
	 * state.
	 * 
	 * @param driver
	 *            Selenium Webdriver reference
	 * 
	 * @param element
	 *            UI element reference
	 */
	public static void highlightElement(WebDriver driver ,WebElement element){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].setAttribute('style' ,'background: yellow;border:2px solid red')", element);
		try{
			Thread.sleep(5000);
		}catch(Exception e){
		}
		js.executeScript("arguments[0].setAttribute('style' ,'border:2px solid white')", element);
	}
	
	/**
	 * Method processAction is used to process the actions on the UI, which is
	 * read from the excel
	 * 
	 * @param driver
	 *            Selenium Webdriver reference
	 * 
	 * @param element
	 *            UI element reference
	 * 
	 * @param action
	 *            The action like click, Enter etc.
	 * 
	 * @param value
	 *            Value to be passed for navigate or enter in a input field
	 *  @throws java.lang.Exception
	 *  		  If an action leads to an exception
	 */
	public static void processAction(WebDriver driver, WebElement element,
			String action, String value) throws Exception {
		
		if ("Enter".equals(action)) {
			highlightElement(driver,element);
			if (value.equals("ENTER")) {
				element.sendKeys(Keys.ENTER);
				return;
			}
			element.sendKeys(value);
		} else if("login".equals(action)) {
			driver.get(value);
			String PATH_AUTOIT = Utility.class.getClassLoader().getResource("authentication.exe").getFile();
			Runtime.getRuntime().exec(PATH_AUTOIT);		
		}  else if ("click".equals(action)) {
			highlightElement(driver,element);
			element.click();
		} else if("clear".equals(action)) {
			highlightElement(driver,element);
			element.clear();
		} else if ("close".equals(action)) {
			closePopup(driver);
		} else if ("goback".equals(action)) {
			driver.navigate().back();
		} else if ("navigate".equals(action)) {
			driver.navigate().to(Constant.BASEURL + value);
		} else if ("select".equals(action)) {
			highlightElement(driver,element);
			new Select(element).selectByVisibleText(value);
		} else if ("get".equals(action)) {
			driver.get(value);
		} else if ("wait".equals(action)) {
			try {
				
				Thread.sleep((long)Double.parseDouble(value));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("verify".equals(action)) {
			highlightElement(driver,element);
			if(!value.equals(element.getText())) {
				throw new Exception("Does not match");
			}
		} else if ("move".equals(action)) {
			highlightElement(driver,element);
			Actions builder = new Actions(driver);
			builder.moveToElement(element);
		} else if ("exists".equals(action)) {
			highlightElement(driver,element);
			Assert.assertEquals(true, element.isDisplayed());
		} else if ("isEnabled".equals(action)) {
			highlightElement(driver,element);
			Assert.assertEquals(Boolean.parseBoolean(value), element.isEnabled());
		} else if ("hover".equals(action)) {
			highlightElement(driver,element);
			Actions builder = new Actions(driver);
			builder.moveToElement(element).build().perform();
		} else if ("contains".equals(action)) {
			highlightElement(driver,element);
			Assert.assertTrue(element.getText().toLowerCase()
					.contains(value.toLowerCase()));
		} else if ("refresh".equals(action)) {
			driver.navigate().refresh();
		} else if("moveAndClick".equals(action)) {
			String data[] = value.split(",");
			Actions builder = new Actions(driver);
			builder.moveByOffset(Integer.parseInt(data[0]), Integer.parseInt(data[1]))
			       .click()
			       .perform();
		} else if ("verifyTitle".equals(action)) {
			highlightElement(driver,element);
			if(!value.equals(element.getAttribute("title"))){
				throw  new Exception("Pass/Fail icon not visible");
				
			};
		} else if ("verifyPageTitle".equals(action)) {
			if(!value.equals(driver.getTitle())) {
				throw new Exception("Page Title does not match");
			}
		} else if("openNewTab".equals(action)) {
			Actions builder = new Actions(driver);
			builder.keyDown(Keys.CONTROL)
				   .sendKeys("t")
				   .keyUp(Keys.CONTROL)
		    	   .perform();
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
		    driver.switchTo().window(tabs2.get(1));
		} else if("closeTab".equals(action)) {
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
			if(tabs2.size()==2) {
				driver.close();
			}
		    driver.switchTo().window(tabs2.get(0));
		} else if("switchTab".equals(action)) {
			ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
			if(tabs2.size()==2) {
				driver.switchTo().window(tabs2.get(1));
			}
		} else if ("verifyDownload".equals(action)){
 			String url = element.getAttribute("href");
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if(responseCode != 200){
				throw  new Exception("Download file failed");	
			}
		} else if ("verifyValue".equals(action)) {// Just for Shell Features as otherwise it was giving a stale element reference for this element
			highlightElement(driver,element);
			element = driver.findElement(By.id("WD54"));
			Assert.assertEquals(value, element.getAttribute("value"));
		}else if ("checkDataCenter".equals(action)){
			List<WebElement> ele = findElements(driver, value, TestSuiteMain.LocalObjectRepo);
			if(!(ele.size()>1)){
				throw new Exception("No scope selection defined");
			}
		}
	}

	/**
	 * findElement method is used to find the element from the UI using
	 * locators,first the element is searched in the local object repository and
	 * then locator is parsed and the the ui element is searched
	 * 
	 * @param driver
	 *            Is the Selenium Webdriver instance
	 * 
	 * @param value
	 *            The locator provided in the excel sheet
	 * 
	 * @param localObjectRepo
	 *            Local object repository read fronm the excel sheet
	 * 
	 * @return element WebElement
	 */
	public static WebElement findElement(WebDriver driver, String value,
			Map<String, String> localObjectRepo) {
		if (!localObjectRepo.containsKey(value)) {
			return null;
		}
		String[] values = localObjectRepo.get(value).split("==");
		if ("id".equals(values[0])) {
			try {
				return driver.findElement(By.id(values[1]));
			}
			catch(Exception e){
				return null;
			}
		}
		if ("linkText".equals(values[0])) {
			return driver.findElement(By.linkText(values[1]));
		}
		if("partialLinkText".equals(values[0])){
			return driver.findElement(By.partialLinkText(values[1]));
		}
		if ("css".equals(values[0])) {
			return driver.findElement(By.cssSelector(values[1]));
		}
		if ("xpath".equals(values[0])) {
			WebElement ele = new WebDriverWait(driver, 60) // 1 minute until
															// present
					.until(ExpectedConditions.presenceOfElementLocated(By
							.xpath(values[1])));
			return ele;
		}

		return null;
	}
	
	/**
	 * findElements method is used to find the multiple elements from the UI using
	 * locators,first the composite element path is  searched in the local object repository and
	 * then locator is parsed and the the ui element is searched
	 * 
	 * @param driver
	 *            Is the Selenium Webdriver instance
	 * 
	 * @param value
	 *            The locator provided in the excel sheet
	 * 
	 * @param localObjectRepo
	 *            Local object repository read fronm the excel sheet
	 * 
	 * @return element WebElement
	 */
	
	public static List<WebElement> findElements(WebDriver driver, String value,
			Map<String, String> localObjectRepo) {
		if (!localObjectRepo.containsKey(value)) {
			return null;
		}
		String[] values = localObjectRepo.get(value).split("==");
		if ("id".equals(values[0])) {
			try {
				return driver.findElements(By.id(values[1]));
			}
			catch(Exception e){
				return null;
			}
		}
		if ("linkText".equals(values[0])) {
			return driver.findElements(By.linkText(values[1]));
		}
		if("partialLinkText".equals(values[0])){
			return driver.findElements(By.partialLinkText(values[1]));
		}
		if ("css".equals(values[0])) {
			return driver.findElements(By.cssSelector(values[1]));
		}
		if ("xpath".equals(values[0])) {
			List<WebElement> ele = new WebDriverWait(driver, 60) //  1 minute until
															// present
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By
							.xpath(values[1])));
			return ele;
		}

		return null;
	}
	
	/*
	 * captureScreenShot is used to capture a screenshot of the current screen
	 * 
	 * @param driver
	 *            Is the Selenium Webdriver instance
	 *            
	 */
	public static String captureScreenShot(WebDriver driver) {
		// Take screenshot and store as a file format             
		 File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); 
		 String path="";
		try {
		// now copy the  screenshot to desired location using copyFile method
			
			path = Constant.PATH_SCREENSHOT+System.currentTimeMillis()+".png";
		 
		FileUtils.copyFile(src, new File(path));                              
		}
		catch (IOException e) {
		  System.out.println(e.getMessage()) ;
		}
		return path;
	}

}
