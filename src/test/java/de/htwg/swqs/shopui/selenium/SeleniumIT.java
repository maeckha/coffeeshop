package de.htwg.swqs.shopui.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.htwg.swqs.cart.model.ShoppingCart;
import de.htwg.swqs.cart.model.ShoppingCartItem;
import de.htwg.swqs.cart.service.CartService;
import de.htwg.swqs.catalog.repository.CatalogRepository;
import java.net.URL;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * With this test we want to test the whole application with all components included. It is kept
 * simple without a seperate SeleniumConfig and Page Objects.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumIT {


  @LocalServerPort
  private int port;

  private URL base;

  private WebDriver driver;

  @Autowired
  CatalogRepository catalogRepository;

  @Autowired
  CartService cartService;

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

  /**
   * As first step we want to add some products (our cart id is generated when adding the first
   * element).
   */
  @Test
  public void getProductCatalogPage() {
    this.driver.get(this.base.toString() + "/products");
    List<WebElement> elements = this.driver.findElements(By.tagName("article"));
    assertTrue(elements.size() > 0);
    assertEquals(this.catalogRepository.findAll().size(), elements.size());
  }

  @Test
  public void openProductDetailAndAddToCart() {
    long idFromProductWeWantToAdd = this.catalogRepository.findAll().get(0).getId();
    String amountWeWantToAdd = "2";
    this.driver.get(this.base.toString() + "/products/" + idFromProductWeWantToAdd);
    WebElement quantityInput = this.driver.findElement(By.id("quantity"));
    quantityInput.clear();
    quantityInput.sendKeys(amountWeWantToAdd);
    this.driver.findElement(By.id("addButton")).click();

    // verify cookie and corresponding cart exists
    Cookie cookie = this.driver.manage().getCookieNamed("cart-id");
    ShoppingCart cart = this.cartService.getShoppingCart(Long.parseLong(cookie.getValue()));
    assertNotNull(cart);

    // verify product is added to cart
    assertEquals(idFromProductWeWantToAdd,
        cart.getItemsInShoppingCart().get(0).getProduct().getId());
    // in correct quantity
    assertEquals(Integer.parseInt(amountWeWantToAdd),
        cart.getItemsInShoppingCart().get(0).getQuantity());
  }

  @Test
  public void openShoppingCartOverviewAndTriggerOrder() {
    this.driver.get(this.base.toString() + "/show-cart");
    // try to find the table, if not existent exception will be thrown
    WebElement itemTable = this.driver.findElement(By.tagName("table"));
    // optional we can check the table content
    // ...

    this.driver.findElement(By.id("order-button")).click();
    assertEquals(this.base.toString() + "/order", this.driver.getCurrentUrl());
  }

  @Test
  public void fillInOrderForm() {

  }

  @Test
  public void checkAllInfosAreShowedAndSubmitOrder() {

  }

  @Test
  public void checkIfAllDataIsPersistd() {

  }

}
