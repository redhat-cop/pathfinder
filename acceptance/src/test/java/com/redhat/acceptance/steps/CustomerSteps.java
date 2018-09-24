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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
//import org.codehaus.plexus.util.StringUtils;
//import org.jboss.order.domain.Country;
//import org.jboss.order.domain.Order;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.acceptance.utils.ToHappen;
import com.redhat.acceptance.utils.Utils;
import com.redhat.acceptance.utils.Wait;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CustomerSteps{
  private static ResourceBundle rb = ResourceBundle. getBundle("acceptance");
  private static final Logger log=LoggerFactory.getLogger(CustomerSteps.class);
  private static final String URL="http://localhost:8082/pathfinder-ui/index.jsp";
//  private List<Order> orders=new ArrayList<Order>();
  WebDriver browser = null;
//  RemoteWebDriver browser = null;
  
  private static boolean initialised = false;
  @Before public void beforeAll(){
    if(!initialised) initialised=Utils.beforeScenarios();
    getBrowser();
  }
  
  private WebDriver getBrowser(){
    if (browser==null){
        System.setProperty("webdriver.chrome.driver", "chromedriver-linux-2.41");
      
//      browser = new ChromeDriver(new ChromeOptions()//.setBinary(new File(path))
//          .setExperimentalOption("useAutomationExtension", false)
//          .addArguments("--headless")
//          .addArguments("--no-sandbox")
//          .addArguments("--disable-dev-shm-usage")
//          .addArguments("--disable-extensions")
//          .addArguments("disable-infobars")
//          );
      
      ChromeOptions o=new ChromeOptions();
      o.setBinary("/usr/bin/google-chrome-stable");
      o.addArguments("--headless");
      o.addArguments("--disable-extensions"); // disabling extensions
      o.addArguments("--disable-gpu"); // applicable to windows os only
      o.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
      o.addArguments("--no-sandbox");
      browser=new ChromeDriver();
      
    }
    return browser;
  }
  
  @Given("we login with the following credentials:$")
  public void login(List<Map<String,String>> table){
    getBrowser().get(URL);
    String title = getBrowser().getTitle();
    System.out.println("TITLE="+title);
    
    WebElement txtUsername = getBrowser().findElement(By.id("username"));
    assertTrue((txtUsername.isDisplayed()));
    for(Map<String,String> row:table){
      
      getBrowser().findElement(By.id("username")).sendKeys(row.get("Username"));
      getBrowser().findElement(By.id("password")).sendKeys(row.get("Password"));
      getBrowser().findElement(By.id("submit")).click();
      break; //only use the first set of credentials in the table
    }
    
    Wait.For("the \"Add Customer\" button to be displayed on the \"CUSTOMERS\" page", 10, new ToHappen(){
      public boolean hasHappened(){
        return getBrowser().findElement(By.name("New")).isDisplayed();
      }
    });
  }
  
  
  @Given("^we navigate to the \"(.*?)\" page$")
  public void we_navigate_to_the_page(String arg1) throws Throwable {
      // Write code here that turns the phrase above into concrete actions
      throw new PendingException();
  }

  @When("^the \"(.*?)\" button is clicked$")
  public void the_button_is_clicked(String arg1) throws Throwable {
      // Write code here that turns the phrase above into concrete actions
      throw new PendingException();
  }

  @When("^we enter the following into the \"(.*?)\" dialog:$")
  public void we_enter_the_following_into_the_dialog(String arg1, List<Map<String,String>> table) throws Throwable {
      // Write code here that turns the phrase above into concrete actions
      // For automatic transformation, change DataTable to one of
      // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
      // E,K,V must be a scalar (String, Integer, Date, enum etc)
      throw new PendingException();
  }

  @Then("^a customer exists in the customers screen with the following details:$")
  public void a_customer_exists_in_the_customers_screen_with_the_following_details(List<Map<String,String>> table) throws Throwable {
      // Write code here that turns the phrase above into concrete actions
      // For automatic transformation, change DataTable to one of
      // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
      // E,K,V must be a scalar (String, Integer, Date, enum etc)
      throw new PendingException();
  }

//  @Given("^the order service is deployed$")
//  public void the_order_service_is_deployed() throws Throwable {
//    assertEquals(200, given().when().get(ORDER_SERVICE_URL+"/version").getStatusCode());
//  }
//
//  @Then("new orders are created with the following details:$")
//  public void a_new_order_is_created_with_the_following_details(List<Map<String,String>> table) throws Throwable {
//    orders.clear();
//    for(Map<String,String> row:table)
//      orders.add(new Order(row.get("ID"), Country.valueOf(row.get("Country")), Double.valueOf(row.get("Amount"))));
//  }
//  
//  @When("^the risk check is performed$")
//  public void the_risk_check_is_performed() throws Throwable {
//    for(Order order:orders)
//      riskCheckOrder(order);
//  }
//  
//  @When("^the orders are processed$")
//  public void the_order_is_submitted() throws Throwable {
//    for(Order order:orders){
//      String payload="{\"id\":\""+order.getId()+"\",\"country\":\""+order.getCountry().name()+"\",\"amount\":"+order.getAmount()+"}";
//      Response response=given().when().body(payload).post(ORDER_SERVICE_URL+"/rest/order/new");
//      String responseString=response.asString();
//      if (response.getStatusCode()!=200)
//        throw new RuntimeException("Response was ["+response.getStatusLine()+"], with content of ["+responseString+"]");
//      assertEquals(200, response.getStatusCode());
//      Order responseOrder=Json.toObject(responseString, Order.class);
////      assertTrue("The response order should have a positive processId, "+responseOrder.getProcessId()+" was returned. Whole response = "+responseString, responseOrder.getProcessId()>0);
//    }
//  }
//  
//  public void riskCheckOrder(Order order) throws JsonParseException, JsonMappingException, IOException{
//    String payload="{\"id\":\""+order.getId()+"\",\"country\":\""+order.getCountry().name()+"\",\"amount\":"+order.getAmount()+"}";
//    Response response=given().when().body(payload).post(ORDER_SERVICE_URL+"/rest/riskcheck");
//    String responseString=response.asString();
//    if (response.getStatusCode()!=200)
//      throw new RuntimeException("Response was ["+response.getStatusLine()+"], with content of ["+responseString+"]");
//    assertEquals(200, response.getStatusCode());
//    Order responseOrder=Json.toObject(responseString, Order.class);
//    order.setRiskStatus(responseOrder.getRiskStatus());
//    order.setRiskReason(responseOrder.getRiskReason());
//  }
//  
//  @Then("^the risk responses should be:$")
//  public void the_risk_responses_should_be(List<Map<String,String>> table) throws Throwable {
//    int ordersCheckedCount=0;
//    assertEquals("You must provide expected responses for all orders", table.size(), orders.size());
//    for(Map<String,String> row:table){
//      Response response=given().when().post(ORDER_SERVICE_URL+"/rest/order/"+row.get("ID"));
//      String responseString=response.asString();
//      Order order=(Order)Json.toObject(responseString, Order.class);
//      if (!row.get("Risk Rating").equals(order.getRiskStatus())){
//        System.err.println("FAILED on row: "+row);
//        assertEquals(row.get("Risk Rating"), order.getRiskStatus());
//      }
//      // allow null or empty
//      assertTrue("Found \""+order.getRiskReason()+"\" risk reason, expecting \""+row.get("Reason")+"\"","".equals(row.get("Reason"))?StringUtils.isEmpty(order.getRiskReason()):order.getRiskReason().equalsIgnoreCase(row.get("Reason").trim()));
//      ordersCheckedCount+=1;
//    }
//    assertEquals(table.size(), ordersCheckedCount);
//  }
  
  
}
