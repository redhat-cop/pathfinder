package com.redhat.acceptance.steps;

import com.redhat.acceptance.utils.SafeWait;
import com.redhat.acceptance.utils.Wait;
import cucumber.api.PendingException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.redhat.acceptance.steps.Helper.Pages.CUSTOMERS;
import static org.junit.Assert.assertTrue;

public class Helper {

  private static WebDriver browser;

  private Properties properties = new Properties();

  private static Helper instance;

  private Helper() {

  }

  static Helper get() {

    if (instance == null) {
      instance = new Helper();
      instance.load();
      instance.getBrowser();
    }

    return instance;
  }

  WebDriver getBrowser() {

    if (browser == null) {

      retrieveDriver();

      System.setProperty("webdriver.chrome.driver", getSeleniumWebDriver());
      System.out.println("WEB DRIVER>>>>" + getSeleniumWebDriver());

      ChromeOptions o = new ChromeOptions();
      o.setBinary(getChromeExecutable());
      o.addArguments("--headless");
      o.addArguments("--disable-extensions"); // disabling extensions
      o.addArguments("--disable-gpu"); // applicable to windows os only
      o.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
      o.addArguments("--no-sandbox");
      o.addArguments("--disable-web-security");
      browser = new ChromeDriver(o);
      browser.manage().window().setSize(new Dimension(1600, 1000));
      browser.manage().window().setPosition(new Point(2500, 0));
    }

    return browser;
  }

  private String getUiUrlBase() {
    return properties.getProperty("pathfinder.ui.url");
  }

  String getUiUrl() {
    return getUiUrlBase() + "/index.jsp";
  }

  private void load() {
    try (InputStream input = Helper.class.getClassLoader().getResourceAsStream("acceptance.properties")) {
      properties.load(input);
    } catch (IOException ex) {
      throw new RuntimeException("Error loading properties: ", ex);
    }
  }

  private String getSeleniumWebDriver() {
  	return getSeleniumWebDriverPath().toString();
	}

  private String getSeleniumWebDriverCompressed() {
    return getSeleniumWebDriverPath() + ".zip";
  }

  private Path getSeleniumWebDriverPath() {
    return Paths.get(SystemUtils.getJavaIoTmpDir().getAbsolutePath(), "driver");
  }

	private String getChromeExecutable() {
		if (SystemUtils.IS_OS_LINUX) {
			return "/usr/bin/google-chrome-stable";
		} else if (SystemUtils.IS_OS_MAC_OSX) {
			return "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
		} else {
			throw new IllegalStateException("Unrecognized os: ");
		}
	}

  private void retrieveDriver() {

  	System.out.println("Looking for driver in " + Paths.get(SystemUtils.getJavaIoTmpDir().getAbsolutePath(), "driver"));
    if (Files.exists(Paths.get(SystemUtils.getJavaIoTmpDir().getAbsolutePath(), "driver"))) {
      return;
    }

    String driverLocation = SystemUtils.IS_OS_MAC_OSX ?
				properties.getProperty("selenium.webdriver.chrome.macos.location") :
				properties.getProperty("selenium.webdriver.chrome.linux.location");

    ZipFile driverCompressed = null;
    try {
      FileUtils.copyURLToFile(
          new URL(driverLocation),
          new File(getSeleniumWebDriverCompressed())
      );
      driverCompressed = new ZipFile(getSeleniumWebDriverCompressed());
      ZipEntry entry = driverCompressed.entries().nextElement();
      FileOutputStream dout = new FileOutputStream(getSeleniumWebDriver());
      IOUtils.copy(driverCompressed.getInputStream(entry), dout);
      IOUtils.closeQuietly(driverCompressed.getInputStream(entry));
      dout.close();
      driverCompressed.close();
    } catch (IOException e) {
      throw new RuntimeException("Error retrieving driver: ", e);
    }
    try {
      Files.setPosixFilePermissions(getSeleniumWebDriverPath(), PosixFilePermissions.fromString("rwxr--r--"));
    } catch (IOException ex) {
      throw new RuntimeException("Error setting permissions on driver: ", ex);
    }
  }

  enum Pages {
    CUSTOMERS("Customers"), ASSESSMENTS("Assessments"), APPLICATIONS("Applications"), MEMBERS("Members");
    public String name;

    Pages(String name) {
      this.name = name;
    }
  }

  public void cleanup() throws Throwable {
    if (!isLoggedIn()) {
      System.out.println("Loading page: " + getUiUrl());
      getBrowser().get(getUiUrl());
      login("admin", "admin");
    }

    navigateTo(CUSTOMERS);
    if (!waitForPage(CUSTOMERS, 10)) {
      getBrowser().findElement(By.id(CUSTOMERS.name.toLowerCase())).click();
      if (!waitForPage(CUSTOMERS)) Assert.fail("Failed to find Customers page");
    }

    List<WebElement> rows = getBrowser().findElements(By.xpath("//*[@id=\"example\"]/tbody/tr"));

    if (1 == rows.size() && "No data available in table".equals(getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr/td[1]")).getText())) {
      return;
    }

    // select all customers
    for (int i = 0; i < rows.size(); i++) {
      String xpathToCheckbox = "//*[@id=\"example\"]/tbody/tr[" + (i + 1) + "]/td[1]/input";
      if (getBrowser().findElement(By.xpath(xpathToCheckbox)).isDisplayed())
        getBrowser().findElement(By.xpath(xpathToCheckbox)).click();
    }

    // delete all customers
    clickButton("Remove Customer");
    getBrowser().switchTo().alert().accept();
  }

  void clickButton(String withText) {
    String xpath = "//button[contains(text(),'" + withText + "')]";
    Wait.For("Unable to find button with text: " + withText, 5, () -> {
      WebElement btn = browser.findElement(By.xpath(xpath));
      return btn.isDisplayed() && btn.isEnabled();
    });
    // don't know why, but a wait is required here for reliable execution
//  	try{Thread.sleep(500);}catch(Exception e){};

    // keep trying to click the button and sinking exceptions - it seems to need a small delay after the isDisplayed check before you can click the button
    Wait.For("Unable to click button with text: " + withText, 5, () -> {
      WebElement btn = browser.findElement(By.xpath(xpath));
      try {
        btn.click();
        return true;
      } catch (WebDriverException e) {
        return false;
      }
    });
  }

  private boolean waitForPage(Pages page) {
    return waitForPage(page, 10);
  }

  private boolean waitForPage(Pages page, int timeout) {
    return SafeWait.For("\"" + page.name + "\" page is not being displayed", timeout,
        () -> page.name.equalsIgnoreCase(browser.findElement(By.id("title")).getText())
    );
  }

  boolean isLoggedIn() {
    try {
      System.out.println("Checking Login in");
      return browser.findElement(By.id("logged-status")).getText().contains("Logged in as");
    } catch (Exception e) {
      return false;
    }
  }

  void navigateTo(Pages page) {
    if (!isOnPage(page)) {
      if (Pages.CUSTOMERS.name().equalsIgnoreCase(page.name())) {
        browser.get(getUiUrlBase() + "/manageCustomers.jsp");
        //			getBrowser().findElement(By.id(pageName.toLowerCase())).click();
      } else if (Pages.ASSESSMENTS.name().equalsIgnoreCase(page.name())) {


        browser.findElement(By.id("breadcrumb-" + page.name().toLowerCase())).click();
      } else if (Pages.APPLICATIONS.name().equalsIgnoreCase(page.name())) {
        browser.findElement(By.id("breadcrumb-" + page.name().toLowerCase())).click();
      } else if (Pages.MEMBERS.name().equalsIgnoreCase(page.name())) {
        browser.findElement(By.id("breadcrumb-" + page.name().toLowerCase())).click();
      } else if ("put other pages here...".equalsIgnoreCase(page.name())) {
        throw new PendingException();
      }
      if (!waitForPage(page)) Assert.fail("Failed to find " + page + " page");
    }
  }

  private boolean isOnPage(Pages page) {
    return waitForPage(page, 1);
  }

  void login(String username, String password) {
//		browser.get(CustomerSteps.URL);
    WebElement txtUsername = browser.findElement(By.id("username"));
    assertTrue((txtUsername.isDisplayed()));
    browser.findElement(By.id("username")).sendKeys(username);
    browser.findElement(By.id("password")).sendKeys(password);
    browser.findElement(By.id("submit")).click();

    if (!waitForPage(Pages.CUSTOMERS)) Assert.fail("Failed to find Customers page");
  }

  void clickLink(String withText) {
    SafeWait.For("link with text " + withText + " is not being displayed", 10, () -> {
      try {
        WebElement e = browser.findElement(By.xpath("//*//a[contains(text(), '" + withText + "')]"));
        return e.isDisplayed() && e.isEnabled();
      } catch (Exception e) {
        return false;
      }
    });
    browser.findElement(By.xpath("//a[contains(text(), '" + withText + "')]")).click();
  }

  List<Map<String, String>> getDataTable(By id) {
    List<Map<String, String>> result = new ArrayList<>();

    Map<Integer, String> headers = new HashMap<>();
    for (int i = 1; i <= 15; i++) {
      try {
        String headerName = getBrowser().findElement(By.xpath("//*[@id=\"example\"]/thead/tr/th[" + (i + 1) + "]")).getText();
        if (!StringUtils.isEmpty(headerName))
          headers.put(i, headerName);
      } catch (WebDriverException ignore) {
        break; // assume it's read too many rows
      }
    }

    int rowSize = getBrowser().findElements(By.xpath("//*[@id=\"example\"]/tbody/tr")).size();
    for (int i = 0; i <= rowSize; i++) {
      try {
        Map<String, String> rowData = new HashMap<>();
        rowData.put("row", String.valueOf(i + 1));
        rowData.put("id", getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr[" + (i + 1) + "]/td[" + (1) + "]/input")).getAttribute("value"));
        for (Entry<Integer, String> e : headers.entrySet()) {
          rowData.put(e.getValue(), getBrowser().findElement(By.xpath("//*[@id=\"example\"]/tbody/tr[" + (i + 1) + "]/td[" + (e.getKey() + 1) + "]")).getText());
        }
        result.add(rowData);
      } catch (WebDriverException ignore) {
        break; // assume it's read too many rows
      }
    }
    return result;
  }


  void enterDetailsIntoTheDialog(String dialogTitle, List<Map<String, String>> table) {

    Wait.For("dialog is not visible: " + dialogTitle, 5,
        () -> getBrowser().findElement(By.className("modal-title")).isDisplayed()
    );

    if (!dialogTitle.equals(getBrowser().findElement(By.className("modal-title")).getText())) {
      throw new RuntimeException("unable to find dialog with name: " + dialogTitle);
    }

    final String prefix;
    if (dialogTitle.toLowerCase().contains("customer")) {
      prefix = "Customer";
    } else
      prefix = "";

    for (Map<String, String> row : table) {
      for (Entry<String, String> e : row.entrySet()) {
        Wait.For(5, () -> {
          WebElement element = getBrowser().findElement(By.id(prefix + e.getKey()));
          return element.isDisplayed() && element.isEnabled();
        });

        WebElement element = getBrowser().findElement(By.id(prefix + e.getKey()));
        if (element.getTagName().toLowerCase().equals("select")) {
          new Select(element).selectByValue(e.getValue());
        } else {
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
