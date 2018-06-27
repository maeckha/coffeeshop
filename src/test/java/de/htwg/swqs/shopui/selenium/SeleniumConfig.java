package de.htwg.swqs.shopui.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumConfig {

  private WebDriver driver;
  private URL base;


  public SeleniumConfig(int port) throws MalformedURLException {

    FirefoxBinary firefoxBinary = new FirefoxBinary();
    // must be headless so it can be easily run in an container
    firefoxBinary.addCommandLineOptions("--headless");

    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.setBinary(firefoxBinary);
    firefoxOptions.addPreference("--log", "error");

    this.driver = new FirefoxDriver(firefoxOptions);

    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    this.base = new URL("http://localhost:" + port);

  }

  static {
    // geckodriver must be placed in the root directory of the project (see README.md)
    String operatingSystem = System.getProperty("os.name", "generic").toLowerCase();
    System.out.println(operatingSystem);
    if (operatingSystem.contains("win")) {
      System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
    } else {
      System.setProperty("webdriver.gecko.driver", "geckodriver");
    }
  }


  public void close() {
    try {
      driver.quit();
    } catch (UnhandledAlertException f) {
      try {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        System.out.println("Alert data: " + alertText);
        alert.accept();
      } catch (NoAlertPresentException e) {
        e.printStackTrace();
      }
    }

  }

  public void navigateTo(String url) {
    driver.navigate().to(this.base.toString() + url);
  }

  public void clickElement(WebElement element) {
    element.click();
  }

  public WebDriver getDriver() {
    return driver;
  }
}
