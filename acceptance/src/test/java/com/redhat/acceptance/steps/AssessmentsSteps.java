package com.redhat.acceptance.steps;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.acceptance.steps.Helper.Pages;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

public class AssessmentsSteps{
  private static final Logger log=LoggerFactory.getLogger(AssessmentsSteps.class);
  protected static Helper helper;
  private static WebDriver browser;
//  private WebDriver getBrowser(){return browser;}
  
  private static boolean initialised = false;
  @Before public void beforeAll(){
    if(!initialised)helper=new Helper();
    browser=helper.getBrowser();
  }
  
  @Given("^we login with \"(.*?)\":$")
  public void login(String userPass) throws Throwable{
//  	helper.cleanup();
  	if (browser.getCurrentUrl().contains("data:,"))
  		browser.get(Helper.URL);
  	if (!helper.isLoggedIn()){
  		helper.login(userPass.split("/")[0], userPass.split("/")[1]);
  		helper.navigateTo(Pages.CUSTOMERS);
  	}
  }
  
  @Given("^click into the customer \"(.*?)\"$")
  public void clickIntoCustomer(String customerName){
  	helper.clickLink(customerName);
  }
  
  
//  @Given("we login with the following credentials:$")
//  public void login(List<Map<String,String>> table) throws Throwable{
//  	helper.cleanup();
//  	if (!helper.isLoggedIn()){
//  		helper.login(table.get(0).get("Username"), table.get(0).get("Password"));
//  		helper.navigateTo(Pages.ASSESSMENTS);
//  	}
//  }

  
}
