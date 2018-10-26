package com.testcases;

import java.util.Hashtable;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.base.BaseTest;
import com.resources.Constants;
import com.resources.TestUtil;
import com.resources.Xls_Reader;
import com.pages.s3Page;
import com.relevantcodes.extentreports.LogStatus;

public class MatchingEnt extends BaseTest{
	String testname = this.getClass().getSimpleName();
 	Xls_Reader xlsx = new Xls_Reader(Constants.testCases);
	
	@BeforeSuite
	public void BeforeSuite() {
		initConfigurations();
	}

	@BeforeMethod
	public void init() {
		initDriver();
		getEnvironmentDetails();
	}

	@AfterMethod
	public void quit() {
		quitDriver();
	}

	@AfterSuite
	public void killDrivers() {
		killDriver();
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(testname, xlsx);
	}
	s3Page s3 = new s3Page();
 	
 	@Test(priority = 1, dataProvider="getTestData", invocationCount = 1)
 	public void Matching(Hashtable<String,String> data) {
 		if(!TestUtil.isExecutable(testname, xlsx) || data.get("Runmode").equals("N"))
 			throw new SkipException("Skipping the test");
 		System.out.println(convertedTimestamp()+ " ************* " +data.get("description")); // + " " +CONFIG.getProperty("env")
		test = rep.startTest("TEST CASE: '" +data.get("description")+ "'");
		test.log(LogStatus.INFO, data.get("description"));
		test = rep.startTest("Test Case Data");
		test.log(LogStatus.INFO, data.toString());
		s3.loginToAws();
		s3.deleteFiles(data.get("delete_files")); 
 		s3.uploadFile(data.get("upload")); 		
 		s3.publishToTopic(data.get("message"));
 		s3.verifyResults(data.get("key"));
 		
 	}	

}
