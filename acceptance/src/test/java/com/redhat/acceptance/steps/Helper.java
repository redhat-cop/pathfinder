package com.redhat.acceptance.steps;

import static com.redhat.acceptance.steps.Helper.Pages.CUSTOMERS;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import com.redhat.acceptance.steps.Helper.Pages;
import com.redhat.acceptance.utils.SafeWait;
import com.redhat.acceptance.utils.ToHappen;
import com.redhat.acceptance.utils.Wait;
import com.redhat.gps.pathfinder.service.util.MapBuilder;

import cucumber.api.PendingException;

public class Helper{
	public static final String URL_BASE="http://localhost:8082/pathfinder-ui";
	public static final String URL=URL_BASE+"/index.jsp";
	private static WebDriver browser;
//	private String baseUrl;
	
	private static Helper instance;
	public static Helper get(){
		if (instance==null) instance=new Helper();
		return instance;
	}
	
  protected WebDriver getBrowser(){
    if (browser==null){
      System.setProperty("webdriver.chrome.driver", "chromedriver-linux-2.41");
      ChromeOptions o=new ChromeOptions();
      o.setBinary("/usr/bin/google-chrome-stable");
      o.addArguments("--headless");
      o.addArguments("--disable-extensions"); // disabling extensions
      o.addArguments("--disable-gpu"); // applicable to windows os only
      o.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
      o.addArguments("--no-sandbox");
      browser=new ChromeDriver();
      browser.manage().window().setSize(new Dimension(1600,1000));
      browser.manage().window().setPosition(new Point(2500, 0));
    }
    return browser;
  }
	
	enum Pages{
		CUSTOMERS("Customers"),ASSESSMENTS("Assessments"),APPLICATIONS("Applications"),MEMBERS("Members");
		public String name;
		Pages(String name){
			this.name=name;
		}
	}
	
  public void cleanup() throws Throwable{
  	if (!isLoggedIn()){
  		getBrowser().get(URL);
  		login("admin", "admin");
  	}
  	
  	navigateTo(CUSTOMERS);
  	if (!waitForPage(CUSTOMERS, 10)) {
  		getBrowser().findElement(By.id(CUSTOMERS.name.toLowerCase())).click();
  		if (!waitForPage(CUSTOMERS)) Assert.fail("Failed to find Customers page");
  	}
  	
  	List<WebElement> rows=getBrowser().findElements(By.xpath("//*[@id=\"example\"]/tbody/tr"));
  	
  	if (1==rows.size() && "No data available in table".equals(getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr/td[1]")).getText())){
  		return;
  	}
  	
  	// select all customers
  	for (int i=0;i<rows.size();i++){
			String xpathToCheckbox="//*[@id=\"example\"]/tbody/tr["+(i+1)+"]/td[1]/input";
  		if (getBrowser().findElement(By.xpath(xpathToCheckbox)).isDisplayed())
				getBrowser().findElement(By.xpath(xpathToCheckbox)).click();
		}
  	
  	// delete all customers
  	clickButton("Remove Customer");
  	getBrowser().switchTo().alert().accept();
  }	
	
	public void clickButton(String withText){
		String xpath="//button[contains(text(),'"+withText+"')]";
  	Wait.For("Unable to find button with text: "+withText, 5, new ToHappen(){
  		@Override public boolean hasHappened(){
  			WebElement btn=browser.findElement(By.xpath(xpath));
				return btn.isDisplayed() && btn.isEnabled();
			}
		});
  	// don't know why, but a wait is required here for reliable execution
//  	try{Thread.sleep(500);}catch(Exception e){};
  	
  	// keep trying to click the button and sinking exceptions - it seems to need a small delay after the isDisplayed check before you can click the button
  	Wait.For("Unable to click button with text: "+withText, 5, new ToHappen(){
  		@Override public boolean hasHappened(){
  			WebElement btn=browser.findElement(By.xpath(xpath));
  			try{
  				btn.click();
  				return true;
  			}catch(WebDriverException e){}
  			return false;
			}
		});
	}
	
	public boolean waitForPage(Pages page){
		return waitForPage(page, 10);
	}
	
	public boolean waitForPage(Pages page, int timeout){
		return SafeWait.For("\""+page.name+"\" page is not being displayed", timeout, new ToHappen(){
      public boolean hasHappened(){
        return page.name.equalsIgnoreCase(browser.findElement(By.id("title")).getText());
      }
    });
	}
	
	public boolean isLoggedIn(){
		try{
			return browser.findElement(By.id("logged-status")).getText().contains("Logged in as");
		}catch(Exception e){}
		return false;
	}
	
	public void navigateTo(Pages page){
		if (!isOnPage(page)){
	  	if (Pages.CUSTOMERS.name().equalsIgnoreCase(page.name())){
	  		browser.get(URL_BASE+"/manageCustomers.jsp");
	//			getBrowser().findElement(By.id(pageName.toLowerCase())).click();
			}else if (Pages.ASSESSMENTS.name().equalsIgnoreCase(page.name())){
				
				
				browser.findElement(By.id("breadcrumb-"+page.name().toLowerCase())).click();
			}else if (Pages.APPLICATIONS.name().equalsIgnoreCase(page.name())){
				browser.findElement(By.id("breadcrumb-"+page.name().toLowerCase())).click();
			}else if (Pages.MEMBERS.name().equalsIgnoreCase(page.name())){
				browser.findElement(By.id("breadcrumb-"+page.name().toLowerCase())).click();
			}else if ("put other pages here...".equalsIgnoreCase(page.name())){
				throw new PendingException();
			}
	  	if (!waitForPage(page)) Assert.fail("Failed to find "+page+" page");
		}
	}
	
	public boolean isOnPage(Pages page){
		return waitForPage(page, 1);
	}
	
	public void login(String username, String password){
//		browser.get(CustomerSteps.URL);
    WebElement txtUsername = browser.findElement(By.id("username"));
    assertTrue((txtUsername.isDisplayed()));
    browser.findElement(By.id("username")).sendKeys(username);
    browser.findElement(By.id("password")).sendKeys(password);
    browser.findElement(By.id("submit")).click();
    
    if (!waitForPage(Pages.CUSTOMERS)) Assert.fail("Failed to find Customers page");		
	}

	public void clickLink(String withText){
		SafeWait.For("link with text "+withText+" is not being displayed", 10, new ToHappen(){public boolean hasHappened(){
			try {
				WebElement e = browser.findElement(By.xpath("//*//a[contains(text(), '"+withText+"')]"));
				return e.isDisplayed() && e.isEnabled();
			}catch(Exception e) {
				return false;
			}
    }});
		browser.findElement(By.xpath("//a[contains(text(), '"+withText+"')]")).click();
	}

	public List<Map<String, String>> getDataTable(By id){
		List<Map<String, String>> result=new ArrayList<>();
		
		Map<Integer,String> headers=new HashMap<>();
		for (int i=1;i<=15;i++){
			try {
				String headerName=getBrowser().findElement(By.xpath("//*[@id=\"example\"]/thead/tr/th["+(i+1)+"]")).getText();
				if (!StringUtils.isEmpty(headerName))
					headers.put(i, headerName);
			}catch (WebDriverException ignore){
				break; // assume it's read too many rows
			}
		}
		
		int rowSize=getBrowser().findElements(By.xpath("//*[@id=\"example\"]/tbody/tr")).size();
		for (int i=0;i<=rowSize;i++){
			try{
				Map<String,String> rowData=new HashMap<>();
				rowData.put("row", String.valueOf(i+1));
				rowData.put("id", getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr[" + (i + 1) + "]/td["+(1)+"]/input")).getAttribute("value"));
				for(Entry<Integer, String> e:headers.entrySet()){
					rowData.put(e.getValue(), getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr[" + (i + 1) + "]/td["+(e.getKey()+1)+"]")).getText());
				}
				result.add(rowData);
			}catch (WebDriverException ignore){
				break; // assume it's read too many rows
			}
		}
		return result;
	}
	
	
	public void enterDetailsIntoTheDialog(String dialogTitle, List<Map<String,String>> table) throws Throwable {

  	Wait.For("dialog is not visible: "+dialogTitle, 5, new ToHappen(){@Override public boolean hasHappened(){
				return getBrowser().findElement(By.className("modal-title")).isDisplayed();
		}});
  	
  	if (!dialogTitle.equals(getBrowser().findElement(By.className("modal-title")).getText())){
  		throw new RuntimeException("unable to find dialog with name: "+dialogTitle);
  	}
  	
  	final String prefix;
  	if (dialogTitle.toLowerCase().contains("customer")){
  		prefix="Customer";
  	}else
  		prefix="";
  	
  	for(Map<String,String> row:table){
  		for(Entry<String, String> e:row.entrySet()){
  			Wait.For(5, new ToHappen(){@Override public boolean hasHappened(){
  				WebElement element=getBrowser().findElement(By.id(prefix+e.getKey()));
						return element.isDisplayed() && element.isEnabled();
				}});
  			
  			WebElement element=getBrowser().findElement(By.id(prefix+e.getKey()));
  			if (element.getTagName().toLowerCase().equals("select")){
  				new Select(element).selectByValue(e.getValue());
  			}else{
  				element.sendKeys(e.getValue());
  			}
  			
  		}
  	}
  }

	
//	public WebElement findByIdOrName(String idOrName){
//		Wait.For("unable to find button by id or name: "+idOrName, 10, new ToHappen(){
//      public boolean hasHappened(){
//      	return browser.findElement(By.id(idOrName)).isDisplayed() ||
//      				 browser.findElement(By.name(idOrName)).isDisplayed();
//      }
//    });
//		return browser.findElement(By.id(idOrName)).isDisplayed()?browser.findElement(By.id(idOrName)):browser.findElement(By.name(idOrName));
//	}
}
