package com.resources;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import com.base.BaseTest;
import com.resources.Xls_Reader;

public class TestUtil {
	// true- test has to be executed
	// false- test has to be skipped
	public static boolean isExecutable(String testName, Xls_Reader xls){
		
		for(int rowNum=2;rowNum<=xls.getRowCount("Test Cases");rowNum++){
			
			if(xls.getCellData("Test Cases", "TCID", rowNum).equals(testName)){
			
				if(xls.getCellData("Test Cases", "Runmode", rowNum).equals("Y"))
					return true;
				else 
					return false;
			}
			// print the test cases with RUnmode Y
		}
		
		return false;
	}
	
	public static void takeScreenShot(String fileName) {
		File srcFile = ((TakesScreenshot)(BaseTest.driver)).getScreenshotAs(OutputType.FILE);
	    try {
//	    	FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir")+"\\src\\test\\java\\screenshots\\"+fileName+".jpg"));
	    	FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir")+"\\screenshots\\"+fileName+".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// make zip of reports
	public static void zip(String filepath){
	 	try
	 	{
	 		File inFolder=new File(filepath);
	 		File outFolder=new File("Reports.zip");
	 		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
	 		BufferedInputStream in = null;
	 		byte[] data  = new byte[1000];
	 		String files[] = inFolder.list();
	 		for (int i=0; i<files.length; i++)
	 		{
	 			in = new BufferedInputStream(new FileInputStream
	 			(inFolder.getPath() + "/" + files[i]), 1000);  
	 			out.putNextEntry(new ZipEntry(files[i])); 
	 			int count;
	 			while((count = in.read(data,0,1000)) != -1)
	 			{
	 				out.write(data, 0, count);
	 			}
	 			out.closeEntry();
  }
  out.flush();
  out.close();
	 	
}
  catch(Exception e)
  {
	  e.printStackTrace();
  } 
 }
//	public void getData(String testName, Xls_Reader xls){
		// returning complete data for the test
//	}
	
	public static Object[][] getData(String testName, Xls_Reader xls)
	{
		int testStartRowNum = 0;
		for(int rNum=1; rNum<=xls.getRowCount("Test Data"); rNum++)
		{
			if(xls.getCellData("Test Data", 0, rNum).equals(testName))
			{
				testStartRowNum = rNum;
				break;
			}
				
		}
//		System.out.println( "Test " + testName +  " starts from " + testStartRowNum);
		
		int colStartRowNum = testStartRowNum + 1;
		int totalCols = 0;
		while(!xls.getCellData("Test Data", totalCols, colStartRowNum).equals(""))
		{
			totalCols++;
		}
//		System.out.println( "Total cols in the Test " + testName +  " are " + totalCols);
		
		int dataStartRowNumber = testStartRowNum + 2;
		int totalRows = 0;
		while (!xls.getCellData("Test Data", 0, dataStartRowNumber + totalRows).equals(""))
		{
			totalRows++;
		}
//		System.out.println( "+++++++++++++++++++++++++++++++++++++++++++++");
		// extract data
		Object[][] data = new Object[totalRows][1];
		int index = 0;
		Hashtable<String,String> table = null;
		for(int rNum = dataStartRowNumber; rNum <(dataStartRowNumber + totalRows); rNum++)
		{
			table = new Hashtable<String,String>();
			for(int cNum = 0; cNum < totalCols; cNum++)
			{
				table.put(xls.getCellData("Test Data", cNum, colStartRowNum), xls.getCellData("Test Data", cNum, rNum));
				// Print Test Data single line
//				System.out.print(xls.getCellData("Test Data", cNum, rNum) + " -- ");
			}
			data[index][0] = table;
			index++;
//			System.out.println();
		}
//		System.out.println("done");
		return data;
	}
}
