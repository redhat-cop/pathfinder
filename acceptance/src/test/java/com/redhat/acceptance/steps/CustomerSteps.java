/*
* JBoss, Home of Professional Open Source
* Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
* contributors by the @authors tag. See the copyright.txt in the
* distribution for a full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.redhat.acceptance.steps;

import static com.redhat.acceptance.steps.Helper.Pages.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.en_tx.Givenyall;
import cucumber.api.java.en_tx.Whenyall;
import cucumber.api.java.en_tx.Thenyall;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.junit.AfterClass;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
//import org.codehaus.plexus.util.StringUtils;
//import org.jboss.order.domain.Country;
//import org.jboss.order.domain.Order;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.redhat.acceptance.steps.Helper.Pages;
import com.redhat.acceptance.utils.ToHappen;
import com.redhat.acceptance.utils.Utils;
import com.redhat.acceptance.utils.Wait;
import com.redhat.gps.pathfinder.service.util.MapBuilder;

import cucumber.api.java.Before;

public class CustomerSteps{
  private static ResourceBundle rb = ResourceBundle. getBundle("acceptance");
  private static final Logger log=LoggerFactory.getLogger(CustomerSteps.class);
  protected static Helper helper;
  private static WebDriver browser = null;
  private WebDriver getBrowser(){return browser;}
  
//  /** button text to name mapping */
//  private Map<String,String> buttons=new MapBuilder<String,String>()
//  		.put("Add Customer", "btnAddCustomer")
//  		.put("Remove Customer", "btnRemoveCustomer")
//  		.put("Import", "btnImport")
//  		.put("Export", "btnExport")
//  		.build();
  
  private static boolean initialised = false;
  @Before public void beforeAll(){
    if(!initialised)helper=new Helper();
    browser=helper.getBrowser();
  }

  @Given("^we login:$")
  public void login(List<Map<String,String>> table) throws Throwable{
  	helper.cleanup();
  	if (!helper.isLoggedIn()){
  		helper.login(table.get(0).get("Username"), table.get(0).get("Password"));
  		helper.navigateTo(Pages.CUSTOMERS);
  	}
  }

  @Givenyall("^navigate to the \"(.*?)\" page$")
  public void navigate_to_the_page(String pageName) throws Throwable {
  	helper.navigateTo(Pages.valueOf(pageName.toUpperCase()));
  }
  
  @When("^click the \"(.*?)\" button$")
  public void click_the_button(String buttonText) throws Throwable {
  	helper.clickButton(buttonText);
  }

  @Whenyall("^enter the following into the \"(.*?)\" dialog:$")
  public void we_enter_the_following_into_the_dialog(String dialogTitle, List<Map<String,String>> table) throws Throwable {
  	
  	helper.enterDetailsIntoTheDialog(dialogTitle, table);
//  	Wait.For("dialog is not visible: "+dialogTitle, 5, new ToHappen(){@Override public boolean hasHappened(){
//				return getBrowser().findElement(By.className("modal-title")).isDisplayed();
//		}});
//  	
//  	if (!dialogTitle.equals(getBrowser().findElement(By.className("modal-title")).getText())){
//  		throw new RuntimeException("unable to find dialog with name: "+dialogTitle);
//  	}
//  	
//  	final String prefix;
//  	if (dialogTitle.toLowerCase().contains("customer")){
//  		prefix="Customer";
//  	}else
//  		prefix="";
//  	
//  	for(Map<String,String> row:table){
//  		for(Entry<String, String> e:row.entrySet()){
//  			Wait.For(5, new ToHappen(){@Override public boolean hasHappened(){
//  				WebElement element=getBrowser().findElement(By.id(prefix+e.getKey()));
//						return element.isDisplayed() && element.isEnabled();
//				}});
//  			
//  			WebElement element=getBrowser().findElement(By.id(prefix+e.getKey()));
//  			if (element.getTagName().toLowerCase().equals("select")){
//  				new Select(element).selectByValue(e.getValue());
//  			}else{
//  				element.sendKeys(e.getValue());
//  			}
//  			
//  		}
//  	}
  }

  @Then("^customers exist with the following details:$")
  public void customers_exist_with_the_following_details(List<Map<String,String>> table) throws Throwable {
  	
  	// check that the list is exactly what it listed, not too many, not too few
  	
  	// this rows logic doesnt seem to always return the correct number of rows in the table
  	List<WebElement> rows=getBrowser().findElements(By.xpath("//*[@id=\"example\"]/tbody/tr"));
  	int rowSize=table.size(); //resorting to using the expected size since xpath of the table/tr with the selenium driver appears to be inaccurate 
  	
  	try{Thread.sleep(1000);}catch(Exception ignore){}
  	
  	List<Map<String,String>> expectedTable=Lists.newArrayList(table.toArray(new Map[table.size()]));
  	List<Map<String,String>> foundTable=new ArrayList<>();
//  	System.out.println("Datatable.size() = "+rows.size());
		for (int i=0;i<=rowSize;i++){
			try{
//				System.out.println("Datatable("+(i+1)+") ... about to check");
				String name=        getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr["+(i+1)+"]/td[2]/a")).getText();
//				System.out.println("Datatable("+(i+1)+") = "+name);
				
				String description= getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr["+(i+1)+"]/td[3]")).getText();
				String vertical=    getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr["+(i+1)+"]/td[4]")).getText();
				String assessor=    getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr["+(i+1)+"]/td[5]")).getText();
				foundTable.add(new MapBuilder<String,String>()
						.put("Name", name)
						.put("Description", description)
						.put("Vertical", vertical)
						.put("Assessor", assessor)
						.build());
			}catch(WebDriverException ignore){
				break; // assume it's read too many rows
			}
		}
		
		if (expectedTable.size()!=foundTable.size()) {
			System.out.println("gonna fail!");
		}
		Assert.assertEquals("Expected and actual data size is wrong", expectedTable.size(), foundTable.size());
		int protectionMax=expectedTable.size()+1;
		int i=0;
		while (expectedTable.size()>0){
			i+=1;
			if (i>=protectionMax) break;
  		String expectedName=expectedTable.get(0).get("Name");
  		String expectedDescription=expectedTable.get(0).get("Description");
  		String expectedVertical=expectedTable.get(0).get("Vertical");
  		String expectedAssessor=expectedTable.get(0).get("Assessor");
  		String expected=expectedName+expectedDescription+expectedVertical+expectedAssessor;
			
  		for (Map<String, String> e:foundTable){
  			String actual=e.get("Name")+e.get("Description")+e.get("Vertical")+e.get("Assessor");
				if (expected.equals(actual)){
					expectedTable.remove(0);
					break;
				}
			}
		}
		Assert.assertEquals("There are still customers in the expected list that weren't found", 0, expectedTable.size());
  }
  
  @Given("^we delete all customers$")
  public void cleanup() throws Throwable{
  	helper.cleanup();
  }
  
  @Then("^delete the customer:$")
  public void delete_the_customer(List<Map<String,String>> table) throws Throwable {
  	for(Map<String,String> row:table){
  		throw new RuntimeException("NOT IMPLEMENTED YET");
  	}
  }
  
  @Givenyall("^create the following customers:$")
  public void createCustomers(List<Map<String,String>> table) throws Throwable {
  	helper.navigateTo(CUSTOMERS);
  	for(Map<String,String> row:table){
  		helper.clickButton("Add Customer");
  		we_enter_the_following_into_the_dialog("New Customer", Lists.newArrayList(row));
  		helper.clickButton("Create");
//  		helper.waitForPage(CUSTOMERS, 5);
  	}
  }
  
}
