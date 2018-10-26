package com.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.testng.asserts.SoftAssert;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
//import org.openqa.selenium.IOUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
//import com.sun.java_cup.internal.runtime.Scanner;
//import com.sun.javafx.scene.paint.GradientUtils.Point;
//import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;
import com.resources.Constants;
import com.resources.ErrorUtil;
import com.resources.TestUtil;
//import com.resources.Xls_Reader;
import com.resources.Xls_Reader;
import com.resources.ExtentManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class BaseTest {

	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	public static Logger APPLICATION_LOGS = null;
	public static Properties CONFIG=null;
	public static WebDriver driver=null;
//	public static boolean isLoggedIn=false;
	public static Properties prop=null;
	public static Properties OR_PROPERTIES=null;
	public static Properties TEXT_PROPERTIES=null;
	public static Properties text=null;
	public static final String browsername = null;
//	public static String timestamp;
	public static String baseUrl;
	public static String qa_s3;
//	public static boolean isBrowserOpened=false;
	public static String xpath;
	public static String actualText;
	public static WebElement e;

	Xls_Reader xlsx = new Xls_Reader(Constants.testCases);
//	SoftAssert softAssert= new SoftAssert();
	public SoftAssert softAssertion= new SoftAssert();
	
	
	public void initConfigurations() {
		if(CONFIG==null) {
		 CONFIG = new Properties();
		try {
			FileInputStream fs = new FileInputStream(Constants.CONFIG_FILE_PATH);
			CONFIG.load(fs);
		} catch (Exception e) {	e.printStackTrace();}
		}
		if(prop==null) {
		prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(Constants.PROPS);
			prop.load(fs);
		} catch (Exception e) {	e.printStackTrace();}
		}
/*		if(TEXT_PROPERTIES==null) {
		TEXT_PROPERTIES = new Properties();
		try {
			FileInputStream fs = new FileInputStream(Constants.TEXT_PROPERTIES);
			TEXT_PROPERTIES.load(fs);
		} catch (Exception e) {	e.printStackTrace();}
		}*/
		if(prop==null) {
		prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(Constants.OR_PROPERTIES);
			prop.load(fs);
		} catch (Exception e) {	e.printStackTrace();}
		}
	}
	
	public void getEnvironmentDetails() {
		if(CONFIG.getProperty("env").equals("qa")) {
			baseUrl = prop.getProperty("qa_url");
			qa_s3 = prop.getProperty("qa_s3");
		}
	}
	
	public void initDriver() {
		if (driver == null) {
			if (CONFIG.getProperty("browser").equals("Chrome")) {
				System.setProperty("webdriver.chrome.driver", Constants.chromePath);
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--start-maximized");
				options.addArguments("--disable-extensions");
				options.addArguments("--disable-web-security");
				options.addArguments("--no-proxy-server");
				options.addArguments("--disable-default-apps");
//				options.addArguments("disable-infobars");
				options.addArguments("test-type");
				options.addArguments("no-sandbox");
				options.addArguments("--allow-running-insecure-content");
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);
//		    options.setExperimentalOptions("prefs", prefs);
				driver = new ChromeDriver(options);
			} else if (CONFIG.getProperty("browser").equals("Firefox")) {
				System.setProperty("webdriver.gecko.driver", Constants.geckoPath);
				FirefoxOptions options = new FirefoxOptions();
				options.setBinary(Constants.firefoxPath);
				driver = new FirefoxDriver(options);
				driver.manage().window().maximize();
			}
			else if (CONFIG.getProperty("browser").equals("IE")) {
				DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				System.setProperty("webdriver.ie.driver", Constants.iePath);
				driver = new InternetExplorerDriver();
				driver.manage().window().maximize();
			}
		}
	}	
	
	public void navigate(String urlKey) {	
		driver.manage().deleteAllCookies();
	    driver.get(urlKey);
 		test = rep.startTest("Test URL");
 		test.log(LogStatus.INFO, "Navigate to  " + urlKey);
	}	

	public void waitForPageToLoad() {
		wait(1);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		String state = (String)js.executeScript("return document.readyState");
		while(!state.equals("complete")){
			wait(2);
			state = (String)js.executeScript("return document.readyState");
		}
	}
	
	public boolean isElementPresent(String locatorKey) {
		List<WebElement> elementList=null;
		elementList=driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		if(elementList.size() > 0) {
//			System.out.println("true");
			return true;			
		}			
		else {
//			System.out.println("false");
			return false;
		}
			
	}
	
	public void visible(String locatorKey) {
		for (int i = 1; i < 30; i++) {
			rep.flush();
			xpath = prop.getProperty(locatorKey);
			wait(1);
			if (driver.findElements(By.xpath(xpath)).size() > 0)
				break;
		}
	}

	public void invisible(String locatorKey) {
		for (int i = 1; i <= 30; i++) {
			xpath = prop.getProperty(locatorKey);
			wait(1);
			if (driver.findElements(By.xpath(xpath)).size() == 0)
				break;
		}
	}
	
	public WebElement getElement(String locatorKey) {
//		waitForPageToLoad();
		visible(locatorKey);		
//		xpath = prop.getProperty(locatorKey);
		for (int i = 0; i < 30; i++) {
//			int count = driver.findElements(By.xpath(xpath)).size();
			wait(1);
			int count = driver.findElements(By.xpath(prop.getProperty(locatorKey))).size();
			if (count > 0)
				break;
		}
		try {
			e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
		} catch (Exception ex) {
			test = rep.startTest("HARD ASSERT unable to locate: " +locatorKey);
			test.log(LogStatus.FAIL, "Unable to locate element: '" +locatorKey+ "'");
			takeScreenShot();
			Assert.fail("Failed the test - " + ex.getMessage());
		}
		return e;
	}	

	public void click(String locatorKey){
		test.log(LogStatus.INFO, "Clicking on "+locatorKey);
		getElement(locatorKey).click();
	}
	public void doubleclick(WebElement locatorKey) {
		test.log(LogStatus.INFO, "Doubleclicking on " + locatorKey);
		waitForPageToLoad();
		new Actions(driver).doubleClick(locatorKey).perform();
	}

	public void check(String locatorKey) {
		test.log(LogStatus.INFO, "Checking box " + locatorKey);
		visible(locatorKey);
		if (!getElement(locatorKey).isSelected())
			getElement(locatorKey).click();
	}
	public String text(String locatorKey){
		test.log(LogStatus.INFO, "Getting Text of "+locatorKey);
		String text = getElement(locatorKey).getText();
		return text;
	}
	public void type(String locatorKey,String text){
		test.log(LogStatus.INFO, "Typing in "+locatorKey+" :: "+text);
		getElement(locatorKey).sendKeys(text, Keys.TAB);
//		getElement(locatorKey).sendKeys(Keys.TAB);
	}

	public void select(String locatorKey, String text) {
		test.log(LogStatus.INFO, "Selecting by text '" + text + "'");
		Select select = new Select(getElement(locatorKey));
		select.selectByVisibleText(text);
	}
	
	public void radio(String locatorKey) {
		test.log(LogStatus.INFO, "Radiobutton " + locatorKey);
		WebElement e = null;
		try {
			e = getElement(locatorKey);
			if(!e.isSelected())
			e.click();
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex);
			takeScreenShot();
			ex.printStackTrace();
			Assert.fail("Failed the test - " + ex.getMessage());
		}
	}
	
	public void send(String locatorKey, String key) {
		test.log(LogStatus.INFO, "Sending Key " + key);
		getElement(locatorKey).sendKeys(key);
	}
	
	public void clickAndWait(String locator_clicked, String locator_present) {
		for (int i = 0; i < 60; i++) {
			getElement(locator_clicked).click();
			wait(2);
			if (isElementPresent(locator_present))
				break;
		}
	}
	
	public int count(String locatorKey) {
		test.log(LogStatus.INFO, "Number of elements of " +locatorKey);
		int num_items = driver.findElements(By.xpath(prop.getProperty(locatorKey))).size();
		test.log(LogStatus.INFO, "Number of elements: " + num_items);
		return num_items;
	}
	
	public boolean verifyText(String locatorKey, String expectedText){
		actualText = getElement(locatorKey).getText().trim();
		test = rep.startTest("Verify Text " +expectedText);
		if(actualText.contains(expectedText)) {
//			test = rep.startTest("Verify Text " +expectedText);
			test.log(LogStatus.PASS, actualText+ " CONTAINS " +expectedText);
			return true;
		}
		else {
//			test = rep.startTest("Verify Text " +expectedText);
			test.log(LogStatus.FAIL, actualText+ " != " +expectedText);
			takeScreenShot();
	 		return false;
		}		
	}
	
	public void highlightElement(String locatorKey) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement element = getElement(locatorKey);
		if (count(locatorKey) > 0) {
			try {
//				js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
				js.executeScript("arguments[0].style.border='3px solid red'", element);
				js.executeScript("arguments[0].style.border='3px solid yellow'", element);
//				js.executeScript("arguments[0].style.border='3px solid green'", element);
			} catch (Throwable t) {
			}
		}
	}
	
	public void ifAlertExistAccept() {
		try {
			Alert alert = new WebDriverWait(driver, 5).until(ExpectedConditions.alertIsPresent());
			if (alert != null) {
				wait(1);
				driver.switchTo().alert().accept();
			} else {
				throw new Throwable();
			}
		} catch (Throwable e) {
			// System.err.println("Alert isn't present!!");
		}
	}
	
	public void quitDriver() {
 		if(driver != null) {
 			driver.close();
 			driver=null;
 		}
 		if(rep != null) {
 			rep.endTest(test);
 			rep.flush();
 		}
		softAssertion.assertAll();
	}
	
	public void takeScreenShot(){
		Date d=new Date();
		String screenshotFile = d.toString().replaceAll(" EDT 2018", "").replace(":", "_").replace(" ", "_") +".png";
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		test.log(LogStatus.INFO,"FAILURE "+ test.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
	}
	
	public void pressEnter(String locatorKey) {
		getElement(locatorKey).sendKeys(Keys.ENTER);
	}
	
	
	public void click1or2(String locatorKey) {
		test.log(LogStatus.INFO, "Clicking on " + locatorKey);
		if(count(locatorKey) > 0) {
			try {
				click("(" +locatorKey+ ")[last()]");
			} catch (Exception ex) {
				click("(" +locatorKey+ ")[1]");
			}				
		}
	}
	
	public void scrollAllWayDown() {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(2000, 2000)"); // if the element is on bottom
		jse.executeScript("scroll(2000, 2000)");
	}
	public void scrollTo(String locatorKey) {
		// test.log(LogStatus.INFO, "Scrolling to "+locatorKey);
		WebElement element = driver.findElement(By.xpath(locatorKey));
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0," + element.getLocation().y + ")");
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element); // NEW
	}
	
/*	public void reportPass(String msg){
		test.log(LogStatus.PASS, msg);
	}
	public void reportFail(String msg){
		test.log(LogStatus.FAIL, msg);
		takeScreenShot();
		Assert.fail(msg);
	}*/
	
/*	private static Map<ITestResult,List> verificationFailuresMap = new HashMap<ITestResult,List>();
	
	public static void addVerificationFailure(Throwable e) {
		System.out.println("*addVerificationFailure*");
		List verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
	}

	public static List getVerificationFailures() {
		System.out.println("*getVerificationFailures*");
		List verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList() : verificationFailures;
	}*/
	
	public void wait(int timeToWaitInMiliSec){
		try {
			Thread.sleep(timeToWaitInMiliSec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String convertedTimestamp() {
		DateFormat dateFormat = new SimpleDateFormat("M-d HH:mm:ss"); // 07/31/2014 16:31
		String timestamp = dateFormat.format(Calendar.getInstance().getTime());
		return timestamp;
	}
	
/*	public void softAssert(String msg1, String msg2) {
		test = rep.startTest("softAssert");
		reportFail(msg1 + msg2);
 		takeScreenShot();
	}*/
		
	public void setConfigBrowser(String browser) {
		try {
			System.out.println(convertedTimestamp() + " **************** Set Config Browser to " +browser);
			FileInputStream fileName = new FileInputStream(Constants.CONFIG_FILE_PATH);
			Properties props = new Properties();
			props.load(fileName);
			props.setProperty("browser", browser);
			fileName.close();
			FileOutputStream outFileName = new FileOutputStream(Constants.CONFIG_FILE_PATH);
			props.store(outFileName, "# BROWSERS :: IE  Chrome Firefox  ENVIRONMENTS ::  dev qa uat prod");
			outFileName.close();
			// props.load(fileName);
			FileInputStream fs = new FileInputStream(Constants.CONFIG_FILE_PATH);
			CONFIG.load(fs);
			test.log(LogStatus.PASS, "Driver set to " +browser);
		} catch (IOException io) {
			io.printStackTrace();
		}
	}
	
	public void killDriver() {
		quitDriver();
		try {
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
			Runtime.getRuntime().exec("taskkill /F /IM firefox.exe /T");
			Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe /T");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
