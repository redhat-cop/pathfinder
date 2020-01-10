package com.redhat.acceptance;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.assertTrue;

public class BasicTest{
//  public static final String FIREFOX_DRIVER="/home/mallen/Work/pathfinder-ng/acceptance/geckodriver-linux-0.20.1";
//  public static final String CHROME_DRIVER="/home/mallen/Work/pathfinder-ng/acceptance/chromedriver-linux-2.41";
  
  @Test
  public void site_header_is_on_home_page(){
    WebDriver browser;
    
    System.setProperty("webdriver.chrome.driver", "chromedriver-linux-2.41");
    ChromeOptions o=new ChromeOptions();
    o.setBinary("/usr/bin/google-chrome-stable");
    o.addArguments("--headless");
    o.addArguments("--disable-extensions"); // disabling extensions
    o.addArguments("--disable-gpu"); // applicable to windows os only
    o.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
    o.addArguments("--no-sandbox");
    browser=new ChromeDriver();

    //Firefox's proxy driver executable is in a folder already
    //  on the host system's PATH environment variable.
    browser.get("http://saucelabs.com");
    WebElement header=browser.findElement(By.id("page-header"));
    assertTrue((header.isDisplayed()));

    browser.close();
  }
}
