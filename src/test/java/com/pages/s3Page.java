package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import com.base.BaseTest;
import com.relevantcodes.extentreports.LogStatus;
import com.resources.Constants;

public class s3Page extends BaseTest {

	public void loginToAws() {	
			System.out.println(convertedTimestamp()+ " ****************** loginToAws");
			test = rep.startTest("login To " +qa_s3);
			test.log(LogStatus.INFO, "Loging to S3");
			navigate(qa_s3);
			visible("sign_in_button");
//			select("aws", "AWS");
			click("sign_in_button");
			click("sign_in_button2");
			invisible("sign_in_button2");
	}
	
	public void loginToAws2() {	
		test = rep.startTest("loginToAws2");
		test.log(LogStatus.INFO, "loginToAws2");
		ifAlertExistAccept();
		navigate(qa_s3);
		click("sign_in_button2");
		radio("match_eng_qa");
		click("sign_in_button3");
}
	
	public void deleteFiles(String delete) {	
		if(!delete.equals("")){
			System.out.println(convertedTimestamp()+ " ****************** deleteFiles");
			test = rep.startTest("deleteFiles");
			test.log(LogStatus.INFO, "deleteFiles");
			loginToAws2();
			click("s3_link");
			click("ingest_uat_ascap");
			visible("search_for_buckets_field");
			type("search_for_buckets_field", "upload");
			pressEnter("search_for_buckets_field");
			if(count("no_keys_found_message") > 0)
				verifyText("no_keys_found_message", "No keys were found for prefix search.");
			else if(count("select_all_checkbox") > 0) {
				check("select_all_checkbox");
				click("actions_button");
				click("delete_button");
				click("delete_button");				
			}
		}
	}
	
	public void uploadFile(String upload) {	
		if(!upload.equals("")){
			System.out.println(convertedTimestamp()+ " ****************** uploadFile");
			test = rep.startTest("uploadFile");
			test.log(LogStatus.INFO, "uploadFile");
			loginToAws2();
			click("s3_link");
			click("ingest_uat_ascap");
			click("upload_button");
			send("add_files_button", System.getProperty("user.dir")+ "\\src\\test\\java\\com\\data\\upload.csv");
			click("upload_button2");			
			clickAndWait("refresh_icon", "file_name");
			click("sort_by_last_modified");			
		}
	}
	
	public void publishToTopic(String message) {	
		if(!message.equals("")){
			System.out.println(convertedTimestamp()+ " ****************** publishToTopic");
			test = rep.startTest("publishToTopic");
			test.log(LogStatus.INFO, "publishToTopic");	
			loginToAws2();
			click("simple_notification_service");
			click("topicks_link");
			click("inbound_client");
			click("publish_to_topicks_button");
			type("message_area", message);
			click("publish_message_button");
			wait(3);
			if(count("please_sign_in_again_message") > 0)
				click("reload_button");
			visible("message_label");
//			verifyText("message_label", prop.getProperty("message_published"));
		}
	}
	
	public void verifyResults(String key) {	
		if(!key.equals("")){
			String[] data = key.split("~"); 
			System.out.println(convertedTimestamp()+ " ****************** verifyResults");
			test = rep.startTest("verifyResults");
			test.log(LogStatus.INFO, "verifyResults");
			loginToAws2();
			click("s3_link");			
			click("ingest_uat_ascap");
			visible("search_for_buckets_field");
			
			clickAndWait("refresh_icon", "file_name_results");
			click("sort_by_last_modified");	
			
//			type("search_for_buckets_field", "upload.csv-results.csv");
//			pressEnter("search_for_buckets_field");			
//			clickAndWait("refresh_icon", "file_name_results");
			click("file_name_results");			
			click("select_from_tab");
			scrollAllWayDown();
			click("show_file_preview_tab");
			wait(3);
//			System.out.println(driver.findElement(By.xpath("//pre[@class='s3-select-result-display ng-binding']")).getText());
			if (!data[0].equals("null"))
				verifyText("results_preview", data[0]);
			if (!data[1].equals("null"))
				verifyText("results_preview", data[1]);
			if (!data[2].equals("null"))
				verifyText("results_preview", data[2]);
			if (!data[3].equals("null"))
				verifyText("results_preview", data[3]);
			if (!data[4].equals("null"))
				verifyText("results_preview", data[4]);
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
/*			WebElement element1 = driver.findElement(By.xpath("//*[contains(.,'" +data[0]+ "')]"));
			js.executeScript("arguments[0].style.border='3px solid red'", element1);
			WebElement element2 = driver.findElement(By.xpath("//*[contains(.,'" +data[1]+ "')]"));
			js.executeScript("arguments[0].style.border='3px solid red'", element2);*/
			
			
			WebElement element2 = getElement("results_preview");
			js.executeScript("arguments[0].style.border='3px solid red'", element2);
			
/*			highlightElement("part_one" +data[0]+ "part_two");
			highlightElement("part_one" +data[1]+ "part_two");*/

		}
	}
}
