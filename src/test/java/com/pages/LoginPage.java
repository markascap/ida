package com.pages;

import com.base.BaseTest;
import com.relevantcodes.extentreports.LogStatus;
import com.resources.Constants;

public class LoginPage extends BaseTest {

	public void loginToIda(String username, String password) {	
		if(!username.equals("")){
//			System.out.println(convertedTimestamp()+ " ****************** loginToSite");
			navigate(baseUrl);
			verifyText("ida_label", "International Distribution Application");
			test = rep.startTest("login To " +baseUrl);
			test.log(LogStatus.INFO, "Loging to IDA");
			type("username_field", username);
			type("password_field", password);
//			verifyText("password_field", "");
			click("login_button");
		}
	}
	

		
}
