package de.htwg.swqs.shopui.selenium;

import static org.junit.Assert.assertEquals;

import de.htwg.swqs.shopui.controller.HomeController;
import java.net.URL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Selenium based tests for {@link HomeController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumSimple {

  @LocalServerPort
  private int port;

  private URL base;

  private WebDriver driver;

  @Before
  public void setUp() throws Exception {
    this.base = new URL("http://localhost:" + this.port);

    FirefoxBinary firefoxBinary = new FirefoxBinary();
    firefoxBinary.addCommandLineOptions("--headless");
    System.setProperty("webdriver.gecko.driver", "geckodriver");

    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.setBinary(firefoxBinary);
    this.driver = new FirefoxDriver(firefoxOptions);
  }

  @Test
  public void getWelcomePage() {
    this.driver.get(this.base.toString());
    WebElement element = this.driver.findElement(By.tagName("h1"));
    assertEquals("Web shop demo application", element.getText());
  }

  @After
  public void tearDown() {
    this.driver.quit();
  }

}