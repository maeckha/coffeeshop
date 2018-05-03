package de.htwg.swqs.shopui.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class SeleniumConfig {

  private WebDriver driver;
  private URL base;


  public SeleniumConfig(int port) throws MalformedURLException{

    FirefoxBinary firefoxBinary = new FirefoxBinary();
    //firefoxBinary.addCommandLineOptions("--headless");

    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.setBinary(firefoxBinary);
    this.driver = new FirefoxDriver(firefoxOptions);

    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    this.base = new URL("http://localhost:" + port);

  }

  static {
    // geckodriver must be placed in the root directory of the project (see README.md)
    System.setProperty("webdriver.gecko.driver", "geckodriver");
  }


  public void close() {
    driver.close();
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
