package com.resources;

//http://relevantcodes.com/Tools/ExtentReports2/javadoc/index.html?com/relevantcodes/extentreports/ExtentReports.html


import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null) {
			Date d = new Date();
			String fileName = d.toString().replaceAll(" EDT 2018", "").replace(":", "_").replace(" ", "_") +".html";
//			extent = new ExtentReports("C:\\report\\"+fileName, true, DisplayOrder.NEWEST_FIRST);
//			extent = new ExtentReports(System.getProperty("user.dir") + "//Reports//" + fileName);
			extent = new ExtentReports(System.getProperty("user.dir") + "//Reports//" + fileName, true, DisplayOrder.NEWEST_FIRST);
			
			extent.loadConfig(new File(System.getProperty("user.dir") + "//ReportsConfig.xml"));
			// optional
			extent.addSystemInfo("Selenium Version", "3.7.1").addSystemInfo("Environment", "QA");
		}
		return extent;
	}
}
