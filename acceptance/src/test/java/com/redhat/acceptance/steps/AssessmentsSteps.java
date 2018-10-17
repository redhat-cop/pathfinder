package com.redhat.acceptance.steps;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.redhat.acceptance.steps.Helper.Pages;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en_tx.Givenyall;

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
  	helper.cleanup();
  	if (browser.getCurrentUrl().contains("data:,"))
  		browser.get(Helper.URL);
  	if (!helper.isLoggedIn()){
  		helper.login(userPass.split("/")[0], userPass.split("/")[1]);
  		helper.navigateTo(Pages.CUSTOMERS);
  	}
  }
  
  @Given("^create and open customer:$")
  public void createAndEnterCustomer(List<Map<String,String>> table) throws Throwable {
  	new CustomerSteps().createCustomers(table);
  	helper.clickLink(table.get(0).get("Name"));
  }
  
  @Given("^click into the customer \"(.*?)\"$")
  public void clickIntoCustomer(String customerName){
  	helper.clickLink(customerName);
  }
  
  @Then("^the following application assessments exist:$")
  public void appAssessmentsExist(List<Map<String,String>> table) throws Throwable {
  	
  	List<Map<String, String>> data=helper.getDataTable(By.id("example"));
  	System.out.println("data.size() = "+data.size());
  	System.out.println("data = "+data);
  	
  	List<Map<String,String>> expectedTable=Lists.newArrayList(table.toArray(new Map[table.size()]));
  	int protectionMax=expectedTable.size()+1;
		int i=0;
		while (expectedTable.size()>0){
			i+=1;
			if (i>=protectionMax) break;
  		String expected1=expectedTable.get(0).get("Name");
  		String expected2=expectedTable.get(0).get("Assessed");
  		String expected3=expectedTable.get(0).get("Reviewed");
  		String expected4=expectedTable.get(0).get("Criticality");
  		String expected5=expectedTable.get(0).get("Decision");
  		String expected6=expectedTable.get(0).get("Effort");
  		String expected7=expectedTable.get(0).get("Review Date");
  		String expected=expected1+expected2+expected3+expected4+expected5+expected6+expected7;
			
  		for (Map<String, String> e:data){
  			String actual=e.get("Name")+e.get("Assessed")+e.get("Reviewed")+e.get("Criticality")+e.get("Decision")+e.get("Effort")+e.get("Review Date");
				if (expected.equals(actual)){
					expectedTable.remove(0);
					break;
				}
			}
		}
		Assert.assertEquals("Expected and actual assessments/apps differs", 0, expectedTable.size());
  }

  @Then("^delete all applications$")
  public void deleteAllApps() throws Throwable {
  	List<Map<String, String>> data=helper.getDataTable(By.id("example"));
  	for(int i=0;i<data.size();i++){
  		browser.findElement(By.xpath("//input[@value='"+data.get(i).get("id")+"']")).click();
  	}
  	helper.clickButton("Remove Application(s)");
  	browser.switchTo().alert().accept();
  }
  	
  @Then("^delete the applications:$")
  public void deleteApps(List<Map<String,String>> table) throws Throwable {
  	List<Map<String, String>> data=helper.getDataTable(By.id("example"));
  	
  	for(int i=0;i<data.size();i++){
  		if (data.get(i).get("Name").equals(table.get(i).get("Name"))){
  			String appGuid=data.get(i).get("id");
  			
  			WebElement btn=browser.findElement(By.xpath("//input[@value='"+appGuid+"']"));
  			try{
  				btn.click();
  			}catch(WebDriverException e){}
  		}
  	}
  	helper.clickButton("Remove Application(s)");
  	browser.switchTo().alert().accept();
  }
  
  
  @Then("^create the following applications:$")
  public void createApps(List<Map<String,String>> table) throws Throwable{
  	
  	for(Map<String, String> row:table){
  		helper.clickButton("Add Application");
  		helper.enterDetailsIntoTheDialog("New Application", Lists.newArrayList(row));
  		helper.clickButton("Create");
  	}
  	//appAssessmentsExist(table);
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
